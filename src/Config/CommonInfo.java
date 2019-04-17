
package Config;

import java.io.*;
import java.util.Properties;

import Common.*;
public class CommonInfo {
	  private static int numberOfPreferredNeighbors;
	  private static float unchokingInterval;
	  private static float optimisticUnchokingInterval;
	  private static String targetFileName;
	  private static long sharedFileSize;
	  private static int sharedFilePieceSize;
	  private static int numberofFilePieces;

	  public CommonInfo() {
		  loadCommonConfig();
	  }
	    public static int getNumberOfPreferredNeighbors() {
	        return numberOfPreferredNeighbors;
	    }
	    
	    public static void loadCommonConfig() { //TODO: Handle this
	    	Properties properties = new Properties();
	    	try {
				FileInputStream fin = new FileInputStream(Constants.ConfigPath);
				properties.load(fin);
				settargetFileName(properties.getProperty(Constants.FileName));
				setSharedFileSize(Integer.parseInt(properties.getProperty(Constants.FileSize)));
				setSharedFilePieceSize(Integer.parseInt(properties.getProperty(Constants.PieceSize)));
				setOptimisticUnchokingInterval(Float.parseFloat(properties.getProperty(Constants.OptimUnchokingInterval)));
				setUnchokingInterval(Float.parseFloat(properties.getProperty(Constants.UnchokingInterval)));
				setNumberOfPreferredNeighbors(Integer.parseInt(properties.get(Constants.NumofPreferredNeighbors).toString()));
				calculateNumberOfPieces();
	    	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	    public static void setSharedFilePieceSize(int a_sharedFilePieceSize) {
	        sharedFilePieceSize = a_sharedFilePieceSize;
	    }

	    public static int getNumberOfPieces() {
			return numberofFilePieces;
		}
	    
	    public static String gettargetFileName() {
	        return targetFileName;
	    }

	    public static void settargetFileName(String a_sharedFileName) {
	    	targetFileName = a_sharedFileName;
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
	    
	    public static void calculateNumberOfPieces() {
	    	numberofFilePieces = (int) (sharedFileSize % sharedFilePieceSize) == 0 ? (int) (sharedFileSize / sharedFilePieceSize)
					: (int) (sharedFileSize / sharedFilePieceSize) + 1;
			System.out.println("CommonProperties.calculateNumberOfPieces - Number of pieces: " + numberofFilePieces);
		}
}
