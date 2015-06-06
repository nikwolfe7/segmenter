package mlsp.cs.cmu.edu.audio;

import mlsp.cs.cmu.edu.sampling.Sampler;
import mlsp.cs.cmu.edu.segmentation.Segmenter;

public class RecordContext {

  private static Sampler sampler = null;

  private static Segmenter segmenter = null;

  public static void registerSampler(Sampler samp) {
    sampler = samp;
  }

  public static void registerSegmenter(Segmenter seg) {
    segmenter = seg;
  }

  public static void startAll() {
    if (sampler != null)
      sampler.start(); // extends Thread
    if (segmenter != null)
      segmenter.start(); // extends Thread
  }

  public static void stopAll() {
    if (sampler != null)
      sampler.stopSampling();
    if (segmenter != null)
      segmenter.stopSegmenting();
  }

}
