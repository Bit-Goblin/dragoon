package tech.bitgoblin.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.Process;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.concurrent.Executors;

import tech.bitgoblin.config.Config;
import tech.bitgoblin.io.IOUtils;

public class Transcoder {

  private String repo_dir;
  private Config config;
  private String ffmpeg_path = "/usr/bin/ffmpeg";

  // only define the working directory; use default FFMPEG path
  public Transcoder(Config config) {
    this.config = config;
    this.repo_dir = IOUtils.resolveTilda(config.getString("transcoder.repo_path"));
    if (this.config.contains("transcoder.ffmpeg_path")) {
      this.ffmpeg_path = config.getString("transcoder.ffmpeg_path");
    }
    this.initDirectory();
  }

  // transcode files in the working directory
  public void transcode() {
    // search for files
    System.out.println("Searching for files to transcode in " + this.repo_dir);
    File repo = new File(Paths.get(this.repo_dir, "ingest").toString());
    File[] sourceFiles = repo.listFiles();

    // transcode
    System.out.println("Transcoding files in " + this.repo_dir + "/ingest...");
    for (File f : sourceFiles) {
      String filePath = f.toString().substring(0, f.toString().lastIndexOf("."));
      String filename = Paths.get(filePath).getFileName().toString();
      String outputPath = Paths.get(this.repo_dir, "output", String.format("%s.mov", filename)).toString();

      String cmd = String.format("%s -i %s -y -f %s -c:v %s -vf %s -profile:v %s -c:a %s %s",
        this.ffmpeg_path, // FFMPEG binary path
        f.toString(), // input file
        "mov",
        "dnxhd", // video codec
        "scale=1920x1080,fps=60,format=yuv422p", // video format
        "dnxhr_hq", // video profile
        "pcm_s16le", // audio codec
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
