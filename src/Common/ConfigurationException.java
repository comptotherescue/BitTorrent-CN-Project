/**
 * 
 */
package Common;

/**
 * @author sonal
 *
 */
public class ConfigurationException extends Exception {
	  private String configFileName;
	    public ConfigurationException(String configFileName) {
	        this.configFileName = configFileName;
	    }

	    public String getConfigFileName() {
	        return configFileName;
	    }

	    @Override
	    public String toString() {
	        return "Unknown configuration option in config file " + configFileName;
	    }
}
