package mlsp.cs.cmu.edu.hmm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HMMDriver {

  public static void main(String[] args) throws IOException {

    Files.deleteIfExists(Paths.get("./hw7-data/cleaned-hmm-train-japanese.txt"));
    Files.deleteIfExists(Paths.get("./hw7-data/cleaned-hmm-test-japanese.txt"));
    Files.deleteIfExists(Paths.get("./hw7-data/cleaned-hmm-train.txt"));
    Files.deleteIfExists(Paths.get("./hw7-data/cleaned-hmm-train.txt"));
    Files.deleteIfExists(Paths.get("./hw7-data/cleaned-hmm-decode.txt"));
    Files.deleteIfExists(Paths.get("./hw7-data/hmm-emit.txt"));

    DataCleaner cleaner = new DataCleaner();
    cleaner.cleanData("./hw7-data/hmm-train.txt", false);
    cleaner.cleanData("./hw7-data/hmm-test.txt", false);
    cleaner.cleanData("./hw7-data/hmm-decode.txt", false);
    cleaner.cleanData("./hw7-data/hmm-train-japanese.txt", true);
    cleaner.cleanData("./hw7-data/hmm-test-japanese.txt", false);
    HMMFactory<String, Character> hmmFactory = CharacterHMMFactory.getInstance();
    HiddenMarkovModel<String, Character> HMM = hmmFactory.initializeHMM();

    HMM.A.getObservationProbability(HMM.Pi, HMM.B, "./hw7-data/cleaned-hmm-test.txt");
    HMM.B.getObservationProbability(HMM.Pi, HMM.A, "./hw7-data/cleaned-hmm-test.txt");
    // HMM.A.getObservationProbability(HMM.Pi, HMM.B, "./hw7-data/toy-hmm-decode.txt");
    // HMM.B.getObservationProbability(HMM.Pi, HMM.A, "./hw7-data/toy-hmm-decode.txt");

    HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-decode.txt");
    // HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/ex-hmm-decode.txt");
    HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-train-japanese.txt");
    HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-train.txt");
    // HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-test.txt");
    HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-test-japanese.txt");
    //
    // HMM.trainHMMFromFile("./hw7-data/hmm-train-cleaned.txt");
    HMM.trainHMMFromFile("./hw7-data/cleaned-hmm-train-japanese.txt");
    // HMM.trainHMMFromFile("./hw7-data/cleaned-hmm-train.txt");

    HMM.A.setDisplayOutput(true);
    HMM.B.setDisplayOutput(true);
    HMM.A.getObservationProbability(HMM.Pi, HMM.B, "./hw7-data/cleaned-hmm-train-japanese.txt");
    HMM.B.getObservationProbability(HMM.Pi, HMM.A, "./hw7-data/cleaned-hmm-train-japanese.txt");

    HMM.A.getObservationProbability(HMM.Pi, HMM.B, "./hw7-data/cleaned-hmm-test-japanese.txt");
    HMM.B.getObservationProbability(HMM.Pi, HMM.A, "./hw7-data/cleaned-hmm-test-japanese.txt");

    // HMM.trainHMMFromFile("./hw7-data/toy-hmm-decode.txt");
    // HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/toy-hmm-decode.txt");
    // HMM.trainHMMFromFile("./hw7-data/ex-hmm-decode-cleaned.txt");
    HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-decode.txt");
    // HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/ex-hmm-decode.txt");
     HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-train-japanese.txt");
     HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-train.txt");
     HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-test.txt");
     HMM.Viterbi.getViterbiBestPathFromFile("./hw7-data/cleaned-hmm-test-japanese.txt");
  }

}
