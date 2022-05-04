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

import tech.bitgoblin.config.Config;
import tech.bitgoblin.io.IOUtils;

public class Transcoder {

  private String repo_dir;
  private Config config;
  private String ffmpeg_path = "/usr/bin/ffmpeg";

  // only define the working directory; use default FFMPEG path
  public Transcoder(Config config) {
    this.config = config;
    this.repo_dir = IOUtils.resolveTilda(this.config.getString("transcoder.repo_path"));
    if (this.config.contains("transcoder.ffmpeg_path")) {
      this.ffmpeg_path = this.config.getString("transcoder.ffmpeg_path");
    }
    this.initDirectory();
  }

  // create a periodic timer task and start it
  public void start() {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new RunTranscoderTask(this), 10000, this.config.getInt("transcoder.interval") * 60 * 1000);
  }

  // transcode files in the working directory
  public void transcode() {
    // search for files
    System.out.println("Searching for files to transcode in " + this.repo_dir);
    File repo = new File(Paths.get(this.repo_dir, "ingest").toString());
    File[] sourceFiles = repo.listFiles();

    if (sourceFiles.length == 0) {
      System.out.println("There is nothing to transcode in " + this.repo_dir + "/ingest.");
      return;
    }

    // transcode
    System.out.println("Transcoding files in " + this.repo_dir + "/ingest...");
    for (File f : sourceFiles) {
      String filePath = f.toString().substring(0, f.toString().lastIndexOf("."));
      String filename = Paths.get(filePath).getFileName().toString();
      String outputPath = Paths.get(this.repo_dir, "output", String.format("%s.mov", filename)).toString();

      String cmd = String.format("%s -i %s -y -f %s -c:v %s -vf %s -profile:v %s -c:a %s %s",
        this.ffmpeg_path, // FFMPEG binary path
        f.toString(), // input file
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
        System.out.printf("Program exited with code: %d\n", ret);
        System.out.println(String.join(" ", pb.command()));
        System.out.println();
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    // end output
    System.out.println("------------ End of transcoding ------------");
    System.out.println();
  }

  // copies sources files to the archive directory
  public void archive() {
    File repo = new File(Paths.get(this.repo_dir, "ingest").toString());
    File[] sourceFiles = repo.listFiles();

    for (File f : sourceFiles) {
      Path filePath = Path.of(f.toString());
      String filename = filePath.getFileName().toString();
      String archivePath = Paths.get(this.repo_dir, "archive", filename).toString();

      try {
        Files.copy(filePath, Paths.get(archivePath), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  // clean up the ingest directory once we're done
  public void cleanup() {
    File repo = new File(Paths.get(this.repo_dir, "ingest").toString());
    File[] sourceFiles = repo.listFiles();

    for (File f : sourceFiles) {
      f.delete();
    }
  }

  // ensures the transcoder's working directory is available
  private void initDirectory() {
    // create transcoder directory
    IOUtils.createDirectory(this.repo_dir);
    // create the sub-directories
    IOUtils.createDirectory(Paths.get(this.repo_dir, "ingest").toString());
    IOUtils.createDirectory(Paths.get(this.repo_dir, "archive").toString());
    IOUtils.createDirectory(Paths.get(this.repo_dir, "output").toString());
  }

}
