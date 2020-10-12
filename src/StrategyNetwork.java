import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class StrategyNetwork {
  public abstract void init();
  public abstract int predict(LabeledImage labeledImage);
  public abstract LabeledImage predict(LabeledImage labeledImage, int dummy);
  public abstract void train(Integer trainDataSize, Integer testDataSize) throws IOException;
}
