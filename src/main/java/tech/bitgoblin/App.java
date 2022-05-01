package tech.bitgoblin;

import tech.bitgoblin.video.Transcoder;

/**
 * The Bit Goblin video transcoder service.
 *
 */
public class App {

  public static void main(String[] args) {
    // create new Transcoder object and start the service
    Transcoder t = new Transcoder("~/dragoon");
    t.transcode();
  }

}
