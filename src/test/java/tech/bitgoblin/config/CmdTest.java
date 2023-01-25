package tech.bitgoblin.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.commons.cli.ParseException;

public class CmdTest {

  @Test
  public void shouldDefaultToHome() throws ParseException {
    Cmd cmd = new Cmd(new String[]{});
    assertTrue(cmd.getConfigPath().equals("~/.config/dragoon.toml"));
  }

}
