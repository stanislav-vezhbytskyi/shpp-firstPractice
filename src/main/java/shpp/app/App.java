package shpp.app;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String DEFAULT_EXTERNAL_PROPERTY_PATH = "app.properties";
    private static final String DEFAULT_USERNAME_NAME = "defaultname";
    private static final String DEFAULT_PHRASE = "Привіт %s!";

    public static void main(String[] args) {
        Properties properties = new Properties();

        InputStream is = ClassLoader.getSystemResourceAsStream(DEFAULT_EXTERNAL_PROPERTY_PATH);
        String username = DEFAULT_USERNAME_NAME;

        LOGGER.info("Trying to read an external properties file");
        if (is != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(is,StandardCharsets.UTF_8);

            LOGGER.info("external properties file exist");
            try {
                properties.load(inputStreamReader);
                username = properties.getProperty("username");
                LOGGER.info("external properties file has been read");
                LOGGER.debug("Username from properties file: " + username);
            } catch (IOException ex) {
                LOGGER.error(ex.toString(),ex);
            }
        } else {
            LOGGER.warn("properties not found");
            username = DEFAULT_USERNAME_NAME;
        }

        Message message = new Message(String.format((DEFAULT_PHRASE), username));
        ObjectMapper objectMapper;

        LOGGER.info("Trying to read printFormat in system parameters");

        if (System.getProperties().containsKey("printFormat") &&
                System.getProperty("printFormat").equals("xml")) {
            objectMapper = new XmlMapper();
        } else {
            objectMapper = new ObjectMapper();
            LOGGER.warn("no system parameter printFormat");
        }

        try {
            LOGGER.info(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            LOGGER.error("incorrect message value",e);
        }
    }
}
