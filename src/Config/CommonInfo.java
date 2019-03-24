
package Config;

import java.io.*;
import java.io.File;
import java.util.Properties;

import Common.*;
public class CommonInfo {
	  private static int numberOfPreferredNeighbors;
	  private static float unchokingInterval;
	  private static float optimisticUnchokingInterval;
	  private static String sharedFileName;
	  private static long sharedFileSize;
	  private static int sharedFilePieceSize;

	    public static int getNumberOfPreferredNeighbors() {
	        return numberOfPreferredNeighbors;
	    }

	    public static void setNumberOfPreferredNeighbors(int a_numberOfPreferredNeighbors) {
	        numberOfPreferredNeighbors = a_numberOfPreferredNeighbors;
	    }

	    public static float getUnchokingInterval() {
	        return unchokingInterval;
	    }

	    public static void setUnchokingInterval(float a_unchokingInterval) {
	        unchokingInterval = a_unchokingInterval;
	    }

	    public static float getOptimisticUnchokingInterval() {
	        return optimisticUnchokingInterval;
	    }

	    public static void setOptimisticUnchokingInterval(float a_optimisticUnchokingInterval) {
	        optimisticUnchokingInterval = a_optimisticUnchokingInterval;
	    }

	    public static String getSharedFileName() {
	        return sharedFileName;
	    }

	    public static void setSharedFileName(String a_sharedFileName) {
	         sharedFileName = a_sharedFileName;
	    }

	    public static long getSharedFileSize() {
	        return sharedFileSize;
	    }

	    public static void setSharedFileSize(long a_sharedFileSize) {
	        sharedFileSize = a_sharedFileSize;
	    }

	    public static int getSharedFilePieceSize() {
	        return sharedFilePieceSize;
	    }

	    public static void setSharedFilePieceSize(int a_sharedFilePieceSize) {
	        sharedFilePieceSize = a_sharedFilePieceSize;
	    }

	   
	    public static void loadCommonConfig() { //TODO: Handle this
	    	Properties properties = new Properties();
	    	try {
				FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + File.separatorChar+ "src/Resources/Common.cfg");
				properties.load(fin);
				setSharedFileName(properties.getProperty(Constants.FileName));
				setSharedFileSize(Integer.parseInt(properties.getProperty(Constants.FileSize)));
				setSharedFilePieceSize(Integer.parseInt(properties.getProperty(Constants.PieceSize)));
				setOptimisticUnchokingInterval(Float.parseFloat(properties.getProperty(Constants.OptimUnchokingInterval)));
				setUnchokingInterval(Float.parseFloat(properties.getProperty(Constants.UnchokingInterval)));
				setNumberOfPreferredNeighbors(Integer.parseInt(properties.get(Constants.NumofPreferredNeighbors).toString()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
