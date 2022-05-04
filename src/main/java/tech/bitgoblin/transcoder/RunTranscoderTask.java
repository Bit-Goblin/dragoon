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
    transcoder.archive();
    // run the transcoder
    transcoder.transcode();
    // clean up ingest
    transcoder.cleanup();
  }

}
