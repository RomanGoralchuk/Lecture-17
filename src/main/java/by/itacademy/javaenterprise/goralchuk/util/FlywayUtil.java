package by.itacademy.javaenterprise.goralchuk.util;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FlywayUtil {
    private static final Logger logger = LoggerFactory.getLogger(FlywayUtil.class);
    private static final Flyway flyway = Flyway.configure()
            .dataSource(
                    DatabasePropertiesUtil.URL,
                    DatabasePropertiesUtil.USER,
                    DatabasePropertiesUtil.USERPASS)
            .locations(
                    DatabasePropertiesUtil.LOCATIONMIGRATION)
            .load();

    public static void updateMigration() {
        try {
            flyway.migrate();
            logger.info("Migrations was up to date successfully");
        } catch (FlywayException e) {
            logger.error(e.toString(), e);
            logger.info("Test links:" +
                    "\n   " + DatabasePropertiesUtil.URL +
                    "\n   " + DatabasePropertiesUtil.USER +
                    "\n   " + DatabasePropertiesUtil.USERPASS +
                    "\n   " + DatabasePropertiesUtil.LOCATIONMIGRATION
            );
        }
    }

    public static void cleanMigration() {
        try {
            flyway.clean();
            logger.info("Migrations was up to date successfully");
        } catch (FlywayException e) {
            logger.error(e.toString(), e);
        }
    }
}
