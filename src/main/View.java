package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

public class View implements ViewPlan {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(View.class);
	
    private StrategyContext strategyContext = new StrategyContext();
    private DrawArea drawArea;
    private JFrame mainFrame;
    public JFrame getMainFrame() {
		return mainFrame;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private JPanel mainPanel;
    private JPanel drawAndDigitPredictionPanel;
    private JPanel resultPanel;
    private final Font sansSerifBold = new Font(consts.fontType, Font.BOLD, 18);
    @SuppressWarnings("rawtypes")
	private JComboBox algoList;
    
    public View() throws Exception {
    	init();
        createPanels();
    }
    
    public void init() throws Exception {
    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put(consts.buttonFont, new FontUIResource(new Font(consts.dialog, Font.BOLD, 18)));
        UIManager.put(consts.comboxFont, new FontUIResource(new Font(consts.dialog, Font.BOLD, 18)));
    }
    
    private void createPanels() {
    	mainFrame = createMainFrame();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        drawAndDigitPredictionPanel = new JPanel(new GridLayout());
    }
    
    @Override
    public void addDrawAreaAndPredictionArea() {

        drawArea = new DrawArea();

        drawAndDigitPredictionPanel.add(drawArea);
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        resultPanel.setBackground(Color.lightGray);
        resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Result Panel",
                TitledBorder.CENTER,
                TitledBorder.TOP, sansSerifBold, Color.BLUE));
        drawAndDigitPredictionPanel.add(resultPanel);
        mainPanel.add(drawAndDigitPredictionPanel, BorderLayout.CENTER);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addTopPanel() {
    	JPanel topPanel = new JPanel(new FlowLayout());
        
        algoList = new JComboBox(consts.algorithms);
        
        algoList.addActionListener(new ActionListener() { 
    	    public void actionPerformed(ActionEvent e) {
    	    	resultPanel.removeAll();
                drawArea.repaint();
                drawAndDigitPredictionPanel.updateUI();
    	    }
        });
        
        JButton runAlgo = new JButton("Run");
        
        runAlgo.addActionListener(e -> {
        	consts.selectedAlgo = (String) (algoList).getSelectedItem();
        	strategyContext.decideStrategy(consts.selectedAlgo);
        	putResult();
			/*
			 * if (selectedAlgo.equals(cnnAlgo)) {
			 * strategyContext.setStrategy(convolutionalNeuralNetwork); putResult(); } else
			 * if (selectedAlgo.equals(nnAlgo)) {
			 * strategyContext.setStrategy(neuralNetwork); putResult(); }
			 */
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

    
    private void putResult() {
    	Image drawImage = drawArea.getImage();
        BufferedImage sbi = toBufferedImage(drawImage);
        Image scaled = scale(sbi);
        BufferedImage scaledBuffered = toBufferedImage(scaled);
        double[] scaledPixels = transformImageToOneDimensionalVector(scaledBuffered);
        LabeledImage labeledImage = new LabeledImage(0, scaledPixels);
        LabeledImage predict = strategyContext.predictInStrategy(labeledImage);
        JLabel predictNumber = new JLabel("" + (int) predict.getLabel());
        predictNumber.setForeground(Color.RED);
        predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
        resultPanel.removeAll();
        resultPanel.add(predictNumber);
        resultPanel.updateUI();
    	
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
    
    @Override
    public JFrame createMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Digit Recognizer");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(consts.FRAME_WIDTH, consts.FRAME_HEIGHT);
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
