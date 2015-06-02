package mlsp.cs.cmu.edu.filters;

import mlsp.cs.cmu.edu.audio.AudioConstants;
import mlsp.cs.cmu.edu.features.MFCCFeatureVectorContainer;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class DiscreteFourierTransform extends FrameFilter {

  FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

  /**
   * returns the FFT power spectrum
   */
  @Override
  protected double[] doFilterImplementation(double[] frame) {
    double[] signal = getPowerOfTwoArray(frame);
    Complex[] complex = fft.transform(signal, TransformType.FORWARD);
    frame = new double[complex.length/2+1];
    for (int i = 0; i < frame.length; i++) {
      // compute power spectrum
      frame[i] = complex[i].getReal() * complex[i].getReal() + complex[i].getImaginary()
              * complex[i].getImaginary();
    }
    return frame;
  }
  
  /*
  private double[] getPowerOfTwoArray(double[] frame) {
    int padding = 2;
    while (padding < frame.length) {
      padding = padding * 2;
    }
    double[] signal = new double[padding];
    System.arraycopy(frame, 0, signal, 0, frame.length);
    return signal;
  }*/

  private double[] getPowerOfTwoArray(double[] frame) {
    double[] signal = new double[AudioConstants.FFT_BINS.getValue()];
    System.arraycopy(frame, 0, signal, 0, frame.length);
    return signal;
  }



  @Override
  public String getName() {
    return "Discrete Fourier Transform";
  }

  @Override
  public void visit(MFCCFeatureVectorContainer container) {
    // do nothing...
  }

}
