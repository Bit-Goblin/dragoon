package tech.bitgoblin.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import tech.bitgoblin.Logger;
import tech.bitgoblin.io.IOUtils;

public class Config {

  private final String configPath;
  private TomlParseResult result;

  public Config(String path) {
    this.configPath = IOUtils.resolveTilda(path);
    // parse config file
    try {
      this.parseConfig();
    } catch (IOException e) {
      Logger.logger.info("Unable to read config file; please check that " + this.configPath + " is available.");
      System.exit(1);
    }
  }

  public String getString(String key) {
    return this.result.getString(key);
  }

  public int getInt(String key) {
    return Objects.requireNonNull(this.result.getLong(key)).intValue();
  }

  public boolean contains(String key) {
    return this.result.contains(key);
  }

  private void parseConfig() throws IOException {
    // parse config file
    Path source = Paths.get(this.configPath);
    this.result = Toml.parse(source);
    this.result.errors().forEach(error -> System.err.println(error.toString()));
  }

}
