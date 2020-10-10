import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.JComboBox;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class UI {

    private final static Logger LOGGER = LoggerFactory.getLogger(UI.class);

    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 628;
    private final NeuralNetwork neuralNetwork = new NeuralNetwork();
    private final ConvolutionalNeuralNetwork convolutionalNeuralNetwork = new ConvolutionalNeuralNetwork();

    private DrawArea drawArea;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel drawAndDigitPredictionPanel;
    private JPanel resultPanel;
    @SuppressWarnings("rawtypes")
	private JComboBox algoList;

    private String[] algorithms = {"Convolutional Neural Network", 
    		"Neural Network"};
    
    private static String cnnAlgo = "Convolutional Neural Network";
    private static String nnAlgo = "Neural Network";
    private static String selectedAlgo = "";
    
    public UI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
        UIManager.put("ComboBox.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
        neuralNetwork.init();
        convolutionalNeuralNetwork.init();
    }

    public void initUI() {
        mainFrame = createMainFrame();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        addTopPanel();

        drawAndDigitPredictionPanel = new JPanel(new GridLayout());
        
        addDrawAreaAndPredictionArea();
        mainPanel.add(drawAndDigitPredictionPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);

    }

    private void addDrawAreaAndPredictionArea() {

        drawArea = new DrawArea();

        drawAndDigitPredictionPanel.add(drawArea);
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        resultPanel.setBackground(Color.lightGray);
        drawAndDigitPredictionPanel.add(resultPanel);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void addTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        
        algoList = new JComboBox(algorithms);
        
        algoList.addActionListener(new ActionListener() { 
    	    public void actionPerformed(ActionEvent e) {
    	    	resultPanel.removeAll();
                drawArea.repaint();
                drawAndDigitPredictionPanel.updateUI();
    	    }
        });
        
        JButton runAlgo = new JButton("Run");
        
        runAlgo.addActionListener(e -> {
        	selectedAlgo = (String) (algoList).getSelectedItem();
        	if (selectedAlgo.equals(cnnAlgo)) {
        		Image drawImage = drawArea.getImage();
                BufferedImage sbi = toBufferedImage(drawImage);
                Image scaled = scale(sbi);
                BufferedImage scaledBuffered = toBufferedImage(scaled);
                double[] scaledPixels = transformImageToOneDimensionalVector(scaledBuffered);
                LabeledImage labeledImage = new LabeledImage(0, scaledPixels);
                LabeledImage predict = neuralNetwork.predict(labeledImage);
                JLabel predictNumber = new JLabel("" + (int) predict.getLabel());
                predictNumber.setForeground(Color.RED);
                predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
                resultPanel.removeAll();
                resultPanel.add(predictNumber);
                resultPanel.updateUI();
        	} else if  (selectedAlgo.equals(nnAlgo)) {
        		Image drawImage = drawArea.getImage();
                BufferedImage sbi = toBufferedImage(drawImage);
                Image scaled = scale(sbi);
                BufferedImage scaledBuffered = toBufferedImage(scaled);
                double[] scaledPixels = transformImageToOneDimensionalVector(scaledBuffered);
                LabeledImage labeledImage = new LabeledImage(0, scaledPixels);
                int predict = convolutionalNeuralNetwork.predict(labeledImage);
                JLabel predictNumber = new JLabel("" + predict);
                predictNumber.setForeground(Color.RED);
                predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
                resultPanel.removeAll();
                resultPanel.add(predictNumber);
                resultPanel.updateUI();
        	}
        });
        
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> {
        	resultPanel.removeAll();
            drawArea.setImage(null);
            drawArea.repaint();
            drawAndDigitPredictionPanel.updateUI();
        });

        topPanel.add(algoList);
        topPanel.add(runAlgo);
        topPanel.add(clear);

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private static BufferedImage scale(BufferedImage imageToScale) {
        ResampleOp resizeOp = new ResampleOp(28, 28);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        BufferedImage filter = resizeOp.filter(imageToScale, null);
        return filter;
    }

    private static BufferedImage toBufferedImage(Image img) {
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private static double[] transformImageToOneDimensionalVector(BufferedImage img) {

        double[] imageGray = new double[28 * 28];
        int w = img.getWidth();
        int h = img.getHeight();
        int index = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(img.getRGB(j, i), true);
                int red = (color.getRed());
                int green = (color.getGreen());
                int blue = (color.getBlue());
                double v = 255 - (red + green + blue) / 3d;
                imageGray[index] = v;
                index++;
            }
        }
        return imageGray;
    }

    private JFrame createMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Digit Recognizer");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        ImageIcon imageIcon = new ImageIcon("icon.png");
        mainFrame.setIconImage(imageIcon.getImage());

        return mainFrame;
    }

}