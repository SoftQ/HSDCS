package utils;

import hubspotComponents.Opportunity;
import javafx.util.Pair;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonManagerTest {
    @Test
    public void getCreateContactJSON() throws Exception {
    }

    @Test
    public void getCreateContactJson() throws Exception {
    }

    @Test
    public void createNewContactProperty() throws Exception {
    }

    @Test
    public void createNewContactPropertyGroup() throws Exception {
    }

    @Test
    public void getUpdateContactJson() throws Exception {
    }

    @Test
    public void getCreateNewOpportunityJson() throws Exception {

        Opportunity testOpportunity = new Opportunity(1,
                "published",
                101,
                -1,
                "2018-05-16 15:54:10",
                "Ceci est un test",
                "Première opportunité",
                231);

        String createNewOpportunityJson = JsonManager.getCreateNewOpportunityJson(testOpportunity);

        String expectedCreateNewOppJson = "{\"associations\": {},\"properties\":[{\"value\": \"Première opportunité\",\"name\": \"dealname\"},{\"value\": \"appointmentscheduled\",\"name\": \"dealstage\"},{\"value\": \"default\",\"name\": \"pipeline\"},{\"value\": \"f2tdealtype\",\"name\": \"dealtype\"}]}";
        assertEquals( expectedCreateNewOppJson ,createNewOpportunityJson);

    }

    @Test
    public void getCreateContactJsonRs() throws Exception{

        List<Pair<String, String>> row = new LinkedList<Pair<String, String>>();
        row.add(new Pair<String, String>("firstname","Xiox"));
        row.add(new Pair<String, String>("lastname","Trololo"));
        row.add(new Pair<String, String>("email","email@email.com"));
        row.add(new Pair<String, String>("phone","121200"));
        row.add(new Pair<String, String>("createdate","2017"));
        row.add(new Pair<String, String>("verified","1"));

        String createContactJsonRs = JsonManager.getCreateContactJsonRs(row);
        String expectedValue = "{\n" +
                "  \"properties\": [\n" +
                "    {\n" +
                "      \"property\": \"firstname\",\n" +
                "      \"value\": \"Xiox\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"property\": \"lastname\",\n" +
                "      \"value\": \"Trololo\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"property\": \"email\",\n" +
                "      \"value\": \"email@email.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"property\": \"phone\",\n" +
                "      \"value\": \"121200\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"property\": \"createdate\",\n" +
                "      \"value\": \"2017\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"property\": \"verified\",\n" +
                "      \"value\": \"1\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedValue.replaceAll("\\s+",""), createContactJsonRs.replaceAll("\\s+",""));

    }

}