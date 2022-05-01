package tech.bitgoblin.io;

import java.io.File;

public class IOUtils {

  public static void createDirectory(String path) {
    File f = new File(path);
    boolean res = f.mkdir();

    if (res) {
      System.out.println(path + " was created.");
    }
  }

  public static String resolveTilda(String path) {
    if (path.startsWith("~" + File.separator)) {
      path = System.getProperty("user.home") + path.substring(1);
    } else if (path.startsWith("~")) {
      // here you can implement reading homedir of other users if you care
      throw new UnsupportedOperationException("Home dir expansion not implemented for explicit usernames");
    }

    return path;
  }

}
