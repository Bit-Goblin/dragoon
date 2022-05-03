package tech.bitgoblin.io;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;

public class IOUtilsTest {

  private static String testDir = "test-temp";

  @Test
  public void shouldCreateDirectory() {
    IOUtils.createDirectory(testDir);
    assertTrue(new File(testDir).exists());
  }

  @Test
  public void shouldExpandTilda() {
    String homeExpanded = IOUtils.resolveTilda("~");
    assertTrue(!homeExpanded.equals("~"));
  }

  @Test(expected=UnsupportedOperationException.class)
  public void shouldFailExpandExplicitTilda() {
    IOUtils.resolveTilda("~test");
  }

  @AfterClass
  public static void cleanup() {
    new File(testDir).delete();
  }

}
