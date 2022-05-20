package tech.bitgoblin.transcoder;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.Process;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Timer;

import tech.bitgoblin.Logger;
import tech.bitgoblin.config.Config;
import tech.bitgoblin.io.IOUtils;

public class Transcoder {

  private Repository repo;
  private Config config;
  private String ffmpeg_path = "/usr/bin/ffmpeg";

  // only define the working directory; use default FFMPEG path
  public Transcoder(Config config) {
    this.config = config;
    this.repo = new Repository(this.config.getString("transcoder.repo_path"));
    if (this.config.contains("transcoder.ffmpeg_path")) {
      this.ffmpeg_path = this.config.getString("transcoder.ffmpeg_path");
    }
    this.repo.init();
  }

  // create a periodic timer task and start it
  public void run() throws IOException {
    // pull list of files to transcode
    File[] sourceFiles = this.repo.searchIngest();

    // check if we have anything to process
    if (sourceFiles.length == 0) {
      Logger.logger.info("There is nothing in ingest, skipping transcode run.");
    }

    // loop through each file
    for (File sf : sourceFiles) {
      // check if the file is open before attempting to transcode it
      if (IOUtils.isFileLocked(sf)) {
        Logger.logger.info(String.format("Skipping %s because it is open in another program.", sf.toString()));
        continue;
      }

      // archive files
      this.repo.archiveFile(sf);
      // transcode files
      this.transcodeFile(sf);
      // cleanup old files
      this.repo.cleanupFile(sf);
    }

    // end output
    Logger.logger.info("------------ End of transcoding ------------");
    Logger.logger.info("");
  }

  // transcode files in the working directory
  public void transcodeFile(File sourceFile) throws IOException {
    String filePath = sourceFile.toString().substring(0, sourceFile.toString().lastIndexOf("."));
    String filename = Paths.get(filePath).getFileName().toString();
    String outputPath = Paths.get(this.repo.getOutputPath(), String.format("%s.mov", filename)).toString();

    String cmd = String.format("%s -i %s -y -f %s -c:v %s -vf %s -profile:v %s -c:a %s %s",
      this.ffmpeg_path, // FFMPEG binary path
      sourceFile.toString(), // input file
      this.config.getString("transcoder.video_format"), // video container format
      this.config.getString("transcoder.video_codec"), // video codec
      this.config.getString("transcoder.video_parameters"), // video format
      this.config.getString("transcoder.video_profile"), // video profile
      this.config.getString("transcoder.audio_codec"), // audio codec
      outputPath // output file path
    );

    ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s+"));
    pb.inheritIO(); // use the java processes' input and output streams
    try {
      Process process = pb.start();
      int ret = process.waitFor();
      Logger.logger.info(String.format("Program exited with code: %d", ret));
      Logger.logger.info("");
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
