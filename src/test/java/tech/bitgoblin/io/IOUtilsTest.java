package tech.bitgoblin.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtilsTest {

  private static String testDir = "test-temp";
  private static String testFile = "test.txt";

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

  @Test
  public void fileShouldBeLocked() throws IOException {
    RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
    assertTrue(IOUtils.isFileLocked(new File(testFile)));
  }

  @Test
  public void fileShouldNotBeLocked() throws IOException {
    RandomAccessFile raf = new RandomAccessFile("test.txt", "rw");
    raf.close();
    assertFalse(IOUtils.isFileLocked(new File(testFile)));
  }

  @BeforeClass
  // ensure that test files are created before running tests
  public static void setup() throws IOException {
    new File(testFile).createNewFile();
  }

  @AfterClass
  // ensure test files are cleaned up from the environment
  public static void cleanup() {
    new File(testDir).delete();
    new File(testFile).delete();
  }

}
