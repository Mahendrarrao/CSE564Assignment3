package main;

public final class consts {
  public static final int FRAME_WIDTH = 1200;
  public static final int FRAME_HEIGHT = 628;
  public String[] algorithms = {"Convolutional Neural Network","Neural Network"};
  public static String cnnAlgo = "Convolutional Neural Network";
  public static String nnAlgo = "Neural Network";
  public static String selectedAlgo = "";

  private Consts(){
    //this prevents even the native class from
    //calling this ctor as well :
    throw new AssertionError();
  }
}
