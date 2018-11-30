package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesManager {



    public String API_KEY;
    public String DB_URL;
    public String DBUSER;
    public String DB_PASWD;

    public String CREATE_CONTACT_URL;
    public String UPDATE_CONTACT_URL;

    public String CREATE_NEW_OPPORTUNITY;
    public String UPDATE_OPPORTUNITY;

    /**
     * Lista zawierająca wszystkie propeercje jakie są używane w mapowaniu danych z bazy danych do hubspota.
     * Zawiera elementy które po obu stronach nazywają się tak samo.
     */
    public List<String> CONTACT_PROPERTIES_INSERT;
    public List<String> CONTACT_PROPERTIES_UPDATE;

    public List<String> OPPORTUNITY_PROPERTIES_INSERT;
    public List<String> OPPORTUNITY_PROPERTIES_UPDATE;


    public PropertiesManager() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        API_KEY = (String) prop.get("apikey");
        DB_URL = (String) prop.get("dburl");
        DBUSER = prop.getProperty("dbuser");
        DB_PASWD = prop.getProperty("dbpaswd");

        CREATE_CONTACT_URL = prop.getProperty("addContactUrl");
        UPDATE_CONTACT_URL = prop.getProperty("updateContactUrl");

        CREATE_NEW_OPPORTUNITY = prop.getProperty("addNewOpportunityUrl");
        UPDATE_OPPORTUNITY = prop.getProperty("updateNewOpportunityUrl");

        CONTACT_PROPERTIES_INSERT =  Arrays.asList(prop.get("contactPropertiesColumnNames").toString().split("\\s*,\\s*"));
        CONTACT_PROPERTIES_UPDATE =  Arrays.asList(prop.get("contactPropertiesColumnNamesUpdate").toString().split("\\s*,\\s*"));

        OPPORTUNITY_PROPERTIES_INSERT =  Arrays.asList(prop.get("opportunityPropertiesColumnNames").toString().split("\\s*,\\s*"));
        OPPORTUNITY_PROPERTIES_UPDATE =  Arrays.asList(prop.get("opportunityPropertiesColumnNamesUpdate").toString().split("\\s*,\\s*"));
    }


}
