package tech.bitgoblin.io;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;

public class IOUtilsTest {

  @Test
  public void shouldCreateDirectory() {
    IOUtils.createDirectory("test-temp.txt");
    assertTrue(new File("test-temp.txt").exists());
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

}
