package sh.calaba.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author ddary
 *
 */
public class PropertyReader {
	private Properties properties = new Properties();

	public PropertyReader(String propertyFileName) {
		if (propertyFileName == null || propertyFileName.isEmpty()) {
			throw new IllegalArgumentException(
					"String 'propertyFileName' must not be null!");
		}
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(propertyFileName);
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key){
		if(key==null||key.isEmpty()){
			throw new IllegalArgumentException(
					"String 'key' must not be null!");
		}
		return properties.getProperty(key);
	}
}
