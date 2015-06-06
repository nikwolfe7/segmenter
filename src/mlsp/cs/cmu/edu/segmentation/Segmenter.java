package mlsp.cs.cmu.edu.segmentation;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import mlsp.cs.cmu.edu.audio.AudioFormatFactory;
import mlsp.cs.cmu.edu.audio.AudioStrings;
import mlsp.cs.cmu.edu.audio.RecordContext;
import mlsp.cs.cmu.edu.sampling.FrameSequence;
import mlsp.cs.cmu.edu.wavutils.LabelMaker;

/**
 * This class uses the energy waveform to allow different strategies to do endpointing
 * 
 * @author nwolfe
 *
 */
public abstract class Segmenter extends Thread {

  private static Integer classSegID = 0;
  
  private FrameSequence frameSequence;
  
  protected final int sampleRate;

  protected final int frameSize;

  private int sampleIndex;

  private int frameIndex;

  protected AudioFormat audioFormat;

  protected SegmentStrategy segmentStrategy;

  protected ArrayList<Short> waveform;

  protected ArrayList<Double> decibelWaveform;

  protected ArrayList<double[]> waveframes;

  protected ArrayList<Segment> segments;

  /* We use 10 ms frames */
  public Segmenter(FrameSequence fs, AudioFormatFactory formatFactory, SegmentStrategy strategy) {
    RecordContext.registerSegmenter(this);
    this.frameSequence = fs;
    this.audioFormat = formatFactory.getAudioFormat();
    this.sampleRate = (int) audioFormat.getSampleRate();
    this.frameSize = sampleRate / 100; /* This is 10ms */
    this.sampleIndex = 0;
    this.frameIndex = 0;
    this.waveform = new ArrayList<Short>();
    this.waveframes = new ArrayList<double[]>();
    this.decibelWaveform = new ArrayList<Double>();
    this.segments = new ArrayList<Segment>();
    this.segmentStrategy = strategy;
  }

  /* Expects a continuous stream in general... */
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        double[] frame = new double[frameSize];
        for (int i = 0; i < frameSize; i++) {
          short val = frameSequence.getFrame();
          waveform.add(val);
          frame[i] = (double) val;
          sampleIndex++;
        }
        /* get energy, do classification */
        frameIndex++;
        double energy = getFrameDecibelLevel(frame);
        decibelWaveform.add(new Double(energy));
        classifyAndSegmentFrame(energy, segmentStrategy.isSpeech(energy));

        /* run the attached filters */
        waveframes.add(frame);
        /*
         * Not sure what else to do here...
         */
        printEnergies(energy);
      } catch (InterruptedException e) {
        System.out.println("Segmenter interrupted! Terminating...");
        Thread.currentThread().interrupt();
      }
    }
    System.out.println("Writing label file...");
    writeLabelFile();
    System.out.println("Done writing label file!");
  }

  private void writeLabelFile() {
    LabelMaker lm = new LabelMaker();
    lm.writeLabelFile(segments);
  }

  private void printEnergies(double energy) {
    StringBuilder sb = new StringBuilder(frameIndex + ": " + Math.round(energy) + "  \t| ");
    while (energy > 25) { // hack for display purposes...
      energy += -2;
      sb.append("|||");
    }
    System.out.println(sb);
  }

  public void stopSegmenting() {
    this.interrupt();
  }

  protected double getFrameDecibelLevel(double[] frame) {
    double sigma = 0.0;
    for (double s : frame) {
      sigma += (s * s);
    }
    return 10 * Math.log10(sigma);
  }

  protected int getSampleIndex() {
    return sampleIndex;
  }

  protected int getFrameIndex() {
    return frameIndex;
  }

  protected void registerSegment(int startFrame, int endFrame) {
    Segment seg = getNewSegment();
    seg.setEndpoints(startFrame, endFrame);
    segments.add(seg);
  }

  private Segment getNewSegment() {
      return new Segment(AudioStrings.SEGMENT.getValue() + "_" + classSegID++, 
              this.audioFormat, 
              this.waveform, 
              this.decibelWaveform, 
              this.waveframes);
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<Segment> getSegments() {
    return (ArrayList<Segment>) this.segments.clone();
  }

  /**
   * Template method for the algorithm that defines how to do segmentation Takes the energy of a
   * given frame and the result of the SegmentStrategy
   * 
   * @param energy
   * @param isSpeech
   */
  protected abstract void classifyAndSegmentFrame(double energy, boolean isSpeech);
  
  /**
   * Defines how to run feature extraction...
   */
  protected abstract void runFeatureExtraction();

}
