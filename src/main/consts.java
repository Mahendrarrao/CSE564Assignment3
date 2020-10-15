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

  private consts(){
    //this prevents even the native class from
    //calling this ctor as well :
    throw new AssertionError();
  }
}
