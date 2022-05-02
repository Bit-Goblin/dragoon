package tech.bitgoblin;

import tech.bitgoblin.config.Config;
import tech.bitgoblin.video.Transcoder;

/**
 * The Bit Goblin video transcoder service.
 *
 */
public class App {

  public static void main(String[] args) {
    // read our config file
    Config c = new Config("~/dragoon/config.toml");
    // create new Transcoder object and start the service
    //Transcoder t = new Transcoder("~/dragoon");
    //t.transcode();
  }

}
