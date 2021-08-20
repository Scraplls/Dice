package net.scraplls.nvutibot.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * An util file to work with program init.
 * @author  Scraplls
 * @version 1.0-a
 */
public class Preferences {
    private final static String fileName = "prefs.properties";
    private Properties props;
    private boolean customDriver;
    private String driverPath;
    private String website;

    public Preferences() throws IOException {
        InputStream inputStream = null;
        try {
            props = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                props.load(inputStream);
                customDriver = Boolean.parseBoolean(props.getProperty("customDriver"));
                driverPath = props.getProperty("driverPath");
                website = props.getProperty("website");
            } else {
                throw new FileNotFoundException(String.format("Could not load the file %s", fileName));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public Properties getProps() {
        return props;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public boolean isCustomDriver() {
        return customDriver;
    }

    public String getWebsite() {
        return website;
    }
}
