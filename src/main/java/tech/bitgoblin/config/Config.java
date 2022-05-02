package tech.bitgoblin.config;

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
      String value = result.getString("repo");
      System.out.println(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void parseConfig() throws IOException {
    // parse config file
    Path source = Paths.get(this.configPath);
    this.result = Toml.parse(source);
    this.result.errors().forEach(error -> System.err.println(error.toString()));
  }

}
