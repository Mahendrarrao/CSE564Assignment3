package main;

public final class consts {
  public static final int FRAME_WIDTH = 1200;
  public static final int FRAME_HEIGHT = 628;
  public static String[] algorithms = {"Convolutional Neural Network","Neural Network"};
  public static String cnnAlgo = "Convolutional Neural Network";
  public static String nnAlgo = "Neural Network";
  public static String selectedAlgo = "";
  public static String buttonFont = "Button.font";
  public static String comboxFont = "ComboBox.font";
  public static String fontType = "SansSerif";
  public static String dialog = "Dialog";
  public static String filePath = "resources/winutils-master/hadoop-2.8.1";
  public static String hadoopHome = "HADOOP_HOME";
  public static String javaPath = "java.lang.ProcessEnvironment";
  public static String consts.unmodifiableMap = "java.util.Collections$UnmodifiableMap";
  public static final String OUT_DIR = "resources/cnnCurrentTrainingModels";
  public static final String TRAINED_MODEL_FILE = "resources/cnnTrainedModels/bestModel.bin";

  private consts(){
    //this prevents even the native class from
    //calling this ctor as well :
    throw new AssertionError();
  }
}
