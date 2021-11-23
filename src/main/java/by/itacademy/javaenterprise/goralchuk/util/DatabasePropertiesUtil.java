package by.itacademy.javaenterprise.goralchuk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabasePropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabasePropertiesUtil.class);
    public static final String URL = getProperties("db.url");
    public static final String USER = getProperties("db.username");
    public static final String USERPASS = getProperties("db.password");
    public static final String LOCATIONMIGRATION = getProperties("db.locations");

    private static String getProperties(String str) {
        Properties props = new Properties();
        try {
            String propFileName = "database/database.properties";
            InputStream inputStream = DatabasePropertiesUtil.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        return props.getProperty(str);
    }
}
