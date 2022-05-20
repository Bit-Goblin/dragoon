package tech.bitgoblin.transcoder;

import java.io.IOException;
import java.util.TimerTask;

public class RunTranscoderTask extends TimerTask {

  private Transcoder transcoder;

  public RunTranscoderTask(Transcoder t) {
    this.transcoder = t;
  }

  @Override
  public void run() {
    // archive the files
    try {
      transcoder.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
