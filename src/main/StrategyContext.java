package main;

import java.io.IOException;

public class StrategyContext {
	
	private StrategyNetwork strategy;
	public final NeuralNetwork neuralNetwork = new NeuralNetwork();
    public final ConvolutionalNeuralNetwork convolutionalNeuralNetwork = new ConvolutionalNeuralNetwork();
    
    public void decideStrategy(String algo) {
    	if (algo.equals(consts.cnnAlgo)) {
    		setStrategy(convolutionalNeuralNetwork);
    	}
    	else {
    		setStrategy(neuralNetwork);
    	}
    }
    
	public StrategyNetwork getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyNetwork strategy) {
		this.strategy = strategy;
	}

	public LabeledImage predictInStrategy(LabeledImage labeledImage) {
		return strategy.predict(labeledImage);
	}

	public void trainInStrategy(Integer trainDataSize, Integer testDataSize) throws IOException{
		strategy.train(trainDataSize,testDataSize);
	}
	
	
}
