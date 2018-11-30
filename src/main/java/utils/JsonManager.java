package utils;

import hubspotComponents.Contact;
import hubspotComponents.Opportunity;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonManager {
    @Deprecated
    public static String getCreateContactJSON(Contact contact) {

        return "{\n" +
                "    \"properties\": [\n" +
                "        {\n" +
                "            \"property\": \"firstname\",\n" +
                "           \"value\": \" " + contact.getFirstName() + "\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"property\": \"lastname\",\n" +
                "            \"value\": \"" + contact.getLastName() + "\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"property\": \"phone\",\n" +
                "            \"value\": \"" + contact.getPhone() + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public static String getCreateContactJson(Contact contact) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");

        putPropertyToJson(stringBuilder, "firstname", contact.getFirstName());
        putPropertyToJson(stringBuilder, "lastname", contact.getLastName());
        putPropertyToJson(stringBuilder, "phone", contact.getPhone());
        putPropertyToJson(stringBuilder, "verified", contact.isVerified() ? "true" : "false");  // need to be created

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }


    // append property WITHT ','
    private static void putPropertyToJson(StringBuilder stringBuilder, String propertyName, String propertyValue) {
        stringBuilder.append("{");
        stringBuilder.append("\"property\": \"" + propertyName + "\",");
        stringBuilder.append("\"value\": \"");
        stringBuilder.append(propertyValue);
        stringBuilder.append("\"}");
        stringBuilder.append(",");
    }


    public void createNewContactProperty(String newPropertyName){

    }

    public void createNewContactPropertyGroup(){}

    public static String getUpdateContactJson(Contact contact) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");

        putPropertyToJson(stringBuilder, "firstname", contact.getFirstName());
        putPropertyToJson(stringBuilder, "lastname", contact.getLastName());
        putPropertyToJson(stringBuilder, "phone", contact.getPhone());
        putPropertyToJson(stringBuilder, "verified", contact.isVerified() ? "true" : "false");  // need to be created

        if (contact.getNumberOfBuyedOpp() > 0 && contact.getNumberOfCreatedOpp() <=0 ){
            putPropertyToJson(stringBuilder, "profile", "business supplier");

        }
        else if (contact.getNumberOfCreatedOpp() > 0 && contact.getNumberOfBuyedOpp() <= 0 ){
            putPropertyToJson(stringBuilder, "profile", "Buyer");
        }else {
            putPropertyToJson(stringBuilder, "profile", "Undefined");
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public static String getCreateNewOpportunityJson(Opportunity opportunity){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
            // assotioations todo no assisiations now
            stringBuilder.append("\"associations\": {");
            stringBuilder.append("}");
            stringBuilder.append(",");
            // prop
            stringBuilder.append("\"properties\":[");

            putDealPropertyToJson(stringBuilder, "dealname" , opportunity.getTitle());
          //  putDealPropertyToJson(stringBuilder, "dealstage" , opportunity.getStatus());
            putDealPropertyToJson(stringBuilder, "dealstage" , "appointmentscheduled");
            putDealPropertyToJson(stringBuilder, "pipeline", "default");
            putDealPropertyToJson(stringBuilder, "dealtype", "f2tdealtype");
            putDealPropertyToJson(stringBuilder, "description", opportunity.getDescription());


            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private static void putDealPropertyToJson(StringBuilder stringBuilder, String propertyName, String propertyValue) {
        stringBuilder.append("{");
        stringBuilder.append("\"value\": \"" + propertyValue.replaceAll("\"", "\'") + "\",");
        stringBuilder.append("\"name\": \"");
        stringBuilder.append(propertyName);
        stringBuilder.append("\"}");
        stringBuilder.append(",");
    }

    public static String getUpdateNewOpportunityJson(Opportunity opportunity) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");

        putDealPropertyToJson(stringBuilder, "dealname" , opportunity.getTitle());
        putDealPropertyToJson(stringBuilder, "dealstage" , "appointmentscheduled");
        putDealPropertyToJson(stringBuilder, "pipeline", "default");
        putDealPropertyToJson(stringBuilder, "dealtype", "f2tdealtype");
        putDealPropertyToJson(stringBuilder, "description", opportunity.getDescription());

        stringBuilder.append("]");
        stringBuilder.append("}");
        return null;
    }

    public static String getCreateContactJsonRs(List<Pair<String, String>> row) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");

        for (Pair pair : row){
            if (pair.getValue() != null) {
                putPropertyToJson(stringBuilder, pair.getKey().toString(), pair.getValue().toString());
            }

        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();

    }

    public static String getContactFromMap(Map contact, List<String> propertiesList) {

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");


        Iterator<Entry<String, String>> it = contact.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            if (propertiesList.contains(pair.getKey())){
                putPropertyToJson(stringBuilder, pair.getKey(), pair.getValue());
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();

    }


    public static String getOpportunityFromMap(Map opportunity, List<String> opportunity_properties_insert) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        stringBuilder.append("\"associations\": {");
        stringBuilder.append("}");
        stringBuilder.append(",");
        // prop
        stringBuilder.append("\"properties\":[");

        Iterator<Entry<String, String>> it = opportunity.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            if (opportunity_properties_insert.contains(pair.getKey())){
                putDealPropertyToJson(stringBuilder, pair.getKey(), pair.getValue());
            }
        }
        putDealPropertyToJson(stringBuilder, "dealtype", "f2tdealtype");
        putDealPropertyToJson(stringBuilder, "pipeline", "default");
        putDealPropertyToJson(stringBuilder, "dealstage" , "appointmentscheduled");

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        stringBuilder.append("}");

        String returnString = stringBuilder.toString();

        return returnString.replaceAll("\r\n","");
    }

    public static String getUpdateOpportunityJsonFromMap(Map opportunity , List<String> columnnames) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"properties\":[");

        Iterator<Entry<String, String>> it = opportunity.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            if (columnnames.contains(pair.getKey())){
                putDealPropertyToJson(stringBuilder, pair.getKey(), pair.getValue());
            }
        }

        putDealPropertyToJson(stringBuilder, "dealtype", "f2tdealtype");
        putDealPropertyToJson(stringBuilder, "pipeline", "default");
        putDealPropertyToJson(stringBuilder, "dealstage" , "appointmentscheduled");

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");
        stringBuilder.append("}");

        String returnString = stringBuilder.toString();

        return returnString.replaceAll("\r\n","");
    }
}