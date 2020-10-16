package main;

import java.io.IOException;

public abstract class StrategyNetwork {
  public abstract LabeledImage predict(LabeledImage labeledImage);
  public abstract void train(Integer trainDataSize, Integer testDataSize) throws IOException;
}
