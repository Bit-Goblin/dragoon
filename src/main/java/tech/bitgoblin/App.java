package tech.bitgoblin;

import tech.bitgoblin.config.Config;
import tech.bitgoblin.transcoder.RunTranscoderTask;
import tech.bitgoblin.transcoder.Transcoder;

import java.util.Timer;

/**
 * The Bit Goblin video transcoder service.
 *
 */
public class App {

  private static final String configFile = "~/.config/dragoon.toml";

  private static final int msToMinutes = 60 * 1000;

  public static void main(String[] args) {
    // read our config file
    Config c = new Config(configFile);
    // create new Transcoder object and start the service
    Transcoder t = new Transcoder(c);
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new RunTranscoderTask(t), 2500, ((long) c.getInt("transcoder.interval") * msToMinutes));
    Logger.logger.info(String.format("Starting transcoder, running in %d minute intervals.", c.getInt("transcoder.interval")));
  }

}
