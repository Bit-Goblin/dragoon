package tech.bitgoblin.transcoder;

import tech.bitgoblin.Logger;
import tech.bitgoblin.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Repository {

  private String repoPath;
  private String ingestPath;
  private String outputPath;
  private String archivePath;

  public Repository(String repoPath) {
    this.repoPath = IOUtils.resolveTilda(repoPath);
    this.ingestPath = Paths.get(this.repoPath, "ingest").toString();
    this.outputPath = Paths.get(this.repoPath, "output").toString();
    this.archivePath = Paths.get(this.repoPath, "archive").toString();
  }

  // initializes the video repository
  public void init() {
    String[] dirs = {this.repoPath, this.ingestPath, this.outputPath, this.archivePath};
    for (String p : dirs) {
      IOUtils.createDirectory(p);
    }
  }

  // searches this ingest directory for video files
  public File[] searchIngest() {
    Logger.logger.info("Searching for files to transcode in " + this.ingestPath);
    File repo = new File(this.ingestPath);
    return repo.listFiles();
  }

  // archives files in the ingest directory
  public void archiveIngest(File[] sourceFiles) {
    for (File f : sourceFiles) {
      Path filePath = Path.of(f.toString());
      String filename = filePath.getFileName().toString();
      String archivePath = Paths.get(this.archivePath, filename).toString();

      try {
        Files.copy(filePath, Paths.get(archivePath), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  // clean up the ingest directory once we're done
  public void cleanupIngest(File[] sourceFiles) {
    for (File f : sourceFiles) {
      f.delete();
    }
  }

  // returns the repository's path
  public String getPath() {
    return this.repoPath;
  }

  // return the repository's output path
  public String getOutputPath() {
    return this.outputPath;
  }

}
