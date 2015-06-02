package mlsp.cs.cmu.edu.features;

import java.util.ArrayList;

import mlsp.cs.cmu.edu.filters.Filterable;
import mlsp.cs.cmu.edu.filters.FrameFilter;
import mlsp.cs.cmu.edu.segmentation.Segment;

public abstract class FeatureExtractor extends Thread implements Filterable {

  private ArrayList<FrameFilter> featureExtractors;

  private ArrayList<Segment> segments;

  private ArrayList<MFCCFeatureVectorContainer> processedSegments;

  public FeatureExtractor() {
    this.segments = new ArrayList<Segment>();
    this.featureExtractors = new ArrayList<FrameFilter>();
    this.processedSegments = new ArrayList<MFCCFeatureVectorContainer>();
  }

  public void registerSegment(Segment seg) {
    segments.add(seg);
  }

  @Override
  public void attachFilter(FrameFilter frameFilter) {
    featureExtractors.add(frameFilter);
    System.out.println("Attached " + frameFilter.getName());
  }

  @Override
  public void clearFilters() {
    featureExtractors.clear();
    System.out.println("Filters cleared!");
  }

  public void run() {
    System.out.println("Now running feature extraction...");
    while (!Thread.currentThread().isInterrupted()) {
      for (Segment seg : segments) {
        MFCCFeatureVectorContainer segFeatureVectors = new MFCCFeatureVectorContainer(seg);
        while (seg.hasNext()) {
          short[] frame = seg.next();
          double[] dFrame = new double[frame.length];
          for (int i = 0; i < frame.length; i++) {
            dFrame[i] = (double) frame[i];
          }
          for (FrameFilter filter : featureExtractors) {
            // System.out.println("Running " + filter.getName() + "...");
            dFrame = filter.doFilter(dFrame);
            filter.visit(segFeatureVectors);
          }
        }
        System.out.println("Segment feature extraction completed! Expanding MFCCs...");
        segFeatureVectors.printMatlabScripts();
        segFeatureVectors.expand();
        processedSegments.add(segFeatureVectors);
      }
      System.out.println("Done!");
      Thread.currentThread().interrupt();
    }
  }
}
