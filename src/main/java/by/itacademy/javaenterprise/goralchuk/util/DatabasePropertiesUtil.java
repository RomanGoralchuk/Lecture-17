package by.itacademy.javaenterprise.goralchuk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
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
            props.load(new FileInputStream("src/main/resources/database/database.properties"));
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        return props.getProperty(str);
    }
}
