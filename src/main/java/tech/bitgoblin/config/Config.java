package tech.bitgoblin.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import tech.bitgoblin.io.IOUtils;

public class Config {

  private String configPath;
  private TomlParseResult result;

  public Config(String path) {
    this.configPath = IOUtils.resolveTilda(path);
    // parse config file
    try {
      this.parseConfig();
      String value = this.result.getString("repo");
      System.out.println(value);
    } catch (IOException e) {
      System.out.println("Unable to read config file; please check that " + this.configPath + " is available.");
      System.exit(1);
    }
  }

  public String getString(String key) {
    return this.result.getString(key);
  }

  private void parseConfig() throws IOException {
    // parse config file
    Path source = Paths.get(this.configPath);
    this.result = Toml.parse(source);
    this.result.errors().forEach(error -> System.err.println(error.toString()));
  }

}
