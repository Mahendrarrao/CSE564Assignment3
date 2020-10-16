package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Run {

    private final static Logger LOGGER = LoggerFactory.getLogger(Run.class);
    private static JFrame mainFrame = new JFrame();
    
    public static void main(String[] args) throws Exception{
    	LOGGER.info("Application is starting ... ");
    	setHadoopHomeEnvironmentVariable();
    	UIBuilder builder = new NewUIBuilder();
    	UIDirector director = new UIDirector(builder);
    	Executors.newCachedThreadPool().submit(()->{ try {    		
    		director.makeUI();
    	} 
    	finally { mainFrame.dispose();
    	} });
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setHadoopHomeEnvironmentVariable() throws Exception {
    	HashMap<String, String> hadoopEnvSetUp = new HashMap<>();
    	hadoopEnvSetUp.put(consts.hadoopHome, new File(consts.filePath).getAbsolutePath());
    	try {
    		Class<?> processEnvironmentClass = Class.forName(consts.javaPath);
    		Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
    		theEnvironmentField.setAccessible(true);
    		Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
    		env.clear();
    		env.putAll(hadoopEnvSetUp);
    		Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
    				.getDeclaredField("theCaseInsensitiveEnvironment");
    		theCaseInsensitiveEnvironmentField.setAccessible(true);
    		Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
    		cienv.clear();
    		cienv.putAll(hadoopEnvSetUp);
    	} catch (java.lang.NoSuchFieldException ne) {
    		Class[] classes = Collections.class.getDeclaredClasses();
    		Map<String, String> env = System.getenv();
    		for (Class cl : classes) {
    			if (consts.unmodifiableMap.equals(cl.getName())) {
    				Field field = cl.getDeclaredField("m");
    				field.setAccessible(true);
    				Object obj = field.get(env);
    				Map<String, String> map = (Map<String, String>) obj;
    				map.clear();
    				map.putAll(hadoopEnvSetUp);
    			}
    		}
    	}
    }
}
