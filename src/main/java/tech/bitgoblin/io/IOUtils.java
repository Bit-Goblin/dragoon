package tech.bitgoblin.io;

import tech.bitgoblin.Logger;

import java.io.*;

public class IOUtils {

  public static void createDirectory(String path) {
    File f = new File(path);
    boolean res = f.mkdir();

    if (res) {
      System.out.println(path + " was created.");
    }
  }

  public static String resolveTilda(String path) {
    if (path.startsWith("~" + File.separator) || path.equals("~")) {
      path = System.getProperty("user.home") + path.substring(1);
    } else if ((!path.equals("~")) && path.startsWith("~")) {
      // here you can implement reading homedir of other users if you care
      throw new UnsupportedOperationException("Home dir expansion not implemented for explicit usernames");
    }

    return path;
  }

  // checks to see if a file is currently locked/being written to.
  public static boolean isFileLocked(File file) throws IOException {
    String[] cmd = {"lsof", file.toString()};
    Process process = Runtime.getRuntime().exec(cmd);
    BufferedReader reader = new BufferedReader(new InputStreamReader(
        process.getInputStream()));

    boolean isOpen = false; // we'll change this if lsof returns that the file is open

    String s;
    while ((s = reader.readLine()) != null) {
      if (s.endsWith(file.toString())) {
        isOpen = true;
      }
    }

    return isOpen;
  }

}
