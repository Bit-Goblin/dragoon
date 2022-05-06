package tech.bitgoblin.transcoder;

import java.util.TimerTask;

public class RunTranscoderTask extends TimerTask {

  private Transcoder transcoder;

  public RunTranscoderTask(Transcoder t) {
    this.transcoder = t;
  }

  @Override
  public void run() {
    // archive the files
    transcoder.run();
  }

}
