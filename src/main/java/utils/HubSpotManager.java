package utils;

import hubspotComponents.Contact;
import hubspotComponents.Opportunity;
import javafx.util.Pair;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;


/**
 * Klasa ralizująca polączenia z HubSpotem.
 * Metody o tej samej nazwie realizuja te same zadania w inny sposób. Część z nich używa obiektów zmapowanych
 * z tabel bazy danych, a część omija kolejną warstwę kodu, i z ResultSet'a od razu tworzy REST'a.
 */
public class HubSpotManager {
    public static PropertiesManager propertiesManager;

    /**
     * Tworzy kontatk używając klasy Contact.java
     * @param contact
     * @return
     * @throws IOException
     */
    public static Pair<Integer,Integer> createContact(Contact contact) throws IOException {

        URL url = new URL(propertiesManager.CREATE_CONTACT_URL + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeChars(JsonManager.getCreateContactJson(contact));
        outputStream.flush();
        outputStream.close();

        String response = "";
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
        }else{
            response = getErrorAsString(connection);
        }
        int vid = getVidFromCreatedContact(response);
     //   System.out.println(contact.getId() + " response " + response.substring(0,20) + " created vid " + vid);
        //System.out.println(response);

        connection.getInputStream().close();
        //  connection.connect();
        connection.disconnect();
        System.out.println("Contact with id " + contact.getId() + " succesfuly created");
        return new Pair<Integer, Integer>(contact.getId(), vid);
    }

    /**
     * Aktualizuje kontakt przy uzyciu klasy Contact.java
     * @param contact
     * @param vid
     * @throws IOException
     */
    public static void updateContract(Contact contact, int vid) throws IOException {
        String urlAsString = propertiesManager.UPDATE_CONTACT_URL.replace("???", String.valueOf(vid));
        URL url = new URL(urlAsString + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
       // outputStream.writeChars(getUpdateContactJSON(contact));
        String createContactJSON = JsonManager.getUpdateContactJson(contact);
        outputStream.writeChars(createContactJSON);
        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
        }else{
            response = getErrorAsString(connection);
        }
        System.out.println("Contact with id " + contact.getId() + " succesfuly updated");
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Tworzy deal przy pomocy klasy Opportunity.java
     * @param opportunity
     * @return
     * @throws IOException
     */
    public static Pair<Integer,Integer> createOpportunity(Opportunity opportunity) throws IOException {
        URL url = new URL(propertiesManager.CREATE_NEW_OPPORTUNITY + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        String createNewOpportunityJson = JsonManager.getCreateNewOpportunityJson(opportunity);
        outputStream.writeChars(createNewOpportunityJson);

        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
        }else{
            response = getErrorAsString(connection);
        }

        outputStream.flush();
        outputStream.close();

        int vid = getVidFromCreatedContact(response); // todo get vid is ok?
        //   System.out.println(contact.getId() + " response " + response.substring(0,20) + " created vid " + vid);
        //System.out.println(response);

        connection.getInputStream().close();
        //  connection.connect();
        connection.disconnect();
        System.out.println("Opportunity with id " + opportunity.getId() + " succesfuly created");
        return new Pair<Integer, Integer>(opportunity.getId(), vid);
    }

    /**
     * Aktualizuje deal przy uzyciu klasy Opportunity.java
     * @param opportunity
     * @throws IOException
     */
    public static void updateOpportunity(Opportunity opportunity) throws IOException {
        if (opportunity.getVid() > 0) {
            URL url = new URL(propertiesManager.UPDATE_OPPORTUNITY.replaceAll("dealID_", String.valueOf(opportunity.getVid())) + propertiesManager.API_KEY);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            String createUpdatedOpportunityJson = JsonManager.getUpdateNewOpportunityJson(opportunity);
            outputStream.writeChars(createUpdatedOpportunityJson);

            String response;
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
                response = getResponseAsString(connection);
            }else{
                response = getErrorAsString(connection);
            }

            outputStream.flush();
            outputStream.close();

            connection.getInputStream().close();
            //  connection.connect();
            connection.disconnect();
            System.out.println("Opportunity with id " + opportunity.getId() + " succesfuly updated");
        }
    }

    /**
     * Bardziej generyczna metoda do tworzenia kontaktów
     * @param contact
     * @return
     * @throws IOException
     */
    public static Pair<Integer, Integer> createContact(Map contact) throws IOException {
        URL url = new URL(propertiesManager.CREATE_CONTACT_URL + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);


        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeChars(JsonManager.getContactFromMap(contact, propertiesManager.CONTACT_PROPERTIES_INSERT));
        outputStream.flush();
        outputStream.close();

        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
            int vid = getVidFromCreatedContact(response);
            System.out.println("Contact with id " + contact.get("id") + " succesfuly created");
            connection.getInputStream().close();
            connection.disconnect();
            return new Pair<>(Integer.valueOf((String) contact.get("id")), vid);
        }else{
            response = getErrorAsString(connection);
            JSONObject jsResponse = new JSONObject(response);

            System.out.println("contactId" + contact.get("id") + " " + jsResponse.get("status") + " : " + jsResponse.get("message"));
        }
        //   System.out.println(contact.getId() + " response " + response.substring(0,20) + " created vid " + vid);
        //System.out.println(response);
        connection.getInputStream().close();
        //  connection.connect();
        connection.disconnect();

        throw new IOException("Cannot add contact to hubspot " + response );
    }

    /**
     * Bardziej generyczna metoda do aktualizacji kontaktów
     * @param contact
     * @throws IOException
     */
    public static void updateContact (Map contact) throws IOException {
        String urlAsString = propertiesManager.UPDATE_CONTACT_URL.replace("???", (CharSequence) contact.get("userVid"));
        URL url = new URL(urlAsString + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        String createContactJSON = JsonManager.getContactFromMap(contact, propertiesManager.CONTACT_PROPERTIES_UPDATE);
        outputStream.writeChars(createContactJSON);
        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
        }else{
            response = getErrorAsString(connection);
            JSONObject jsResponse = new JSONObject(response);
            System.out.println("contactId" + contact.get("id") + " " + jsResponse.get("status") + " : " + jsResponse.get("message"));
        }
        System.out.println("Contact with id " + contact.get("id") + " succesfuly updated");
        outputStream.flush();
        outputStream.close();

        connection.disconnect();

    }

    private static int getVidFromCreatedContact(String response) {
        JSONObject jsResponse = new JSONObject(response);
        int vid = jsResponse.getInt("vid");
        return vid;
    }
    private static int getVidFromCreatedOpportunity(String response) {
        JSONObject jsResponse = new JSONObject(response);
        int vid = jsResponse.getInt("dealId");
        return vid;
    }



    private static String getResponseAsString(HttpsURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();
        BufferedReader br = null;
       // if (statusCode >= 200 && statusCode < 400){
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
     //   }else {
      //      br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
    //    }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    private static String getErrorAsString(HttpsURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();
        BufferedReader br = null;
     //   if (statusCode >= 200 && statusCode < 400){
     //       br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
       // }else {
        InputStream errorStream = connection.getErrorStream();
        br = new BufferedReader(new InputStreamReader(errorStream));
     //   }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    public static Pair<Integer, Integer> createContactRs(List<Pair<String, String>> row) throws IOException {
        URL url = new URL(propertiesManager.CREATE_CONTACT_URL + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        int id = Integer.parseInt(row.remove(0).getValue());
        outputStream.writeChars(JsonManager.getCreateContactJsonRs(row));
        outputStream.flush();
        outputStream.close();

        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
        }else{
            response = getErrorAsString(connection);
        }
        int vid = getVidFromCreatedContact(response);
        //   System.out.println(contact.getId() + " response " + response.substring(0,20) + " created vid " + vid);
        //System.out.println(response);

        connection.getInputStream().close();
        //  connection.connect();
        connection.disconnect();
        System.out.println("Contact with id " + id + " succesfuly created");
        return new Pair<Integer, Integer>(id , vid);
    }

    public static Pair<Integer, Integer> createOpportunity(Map opportunity) throws IOException {
        URL url = new URL(propertiesManager.CREATE_NEW_OPPORTUNITY + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeChars(JsonManager.getOpportunityFromMap(opportunity, propertiesManager.OPPORTUNITY_PROPERTIES_INSERT));
        outputStream.flush();
        outputStream.close();

        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
            int vid = getVidFromCreatedOpportunity(response);
            System.out.println("Opportunity with id " + opportunity.get("id") + " succesfuly created");
            connection.getInputStream().close();
            connection.disconnect();
            return new Pair<>(Integer.valueOf((String) opportunity.get("id")), vid);
        }else{
            response = getErrorAsString(connection);
            JSONObject jsResponse = new JSONObject(response);

            System.out.println("contactId" + opportunity.get("id") + " " + jsResponse.get("status") + " : " + jsResponse.get("message"));
        }
        connection.getInputStream().close();
        connection.disconnect();

        throw new IOException("Cannot add opportunity to hubspot " + response );

    }

    public static Pair<Integer, Integer> updateOpportunity(Map opportunity) throws IOException {
        URL url = new URL(propertiesManager.UPDATE_OPPORTUNITY.replaceAll("dealID_", String.valueOf(opportunity.get("opportunityVid"))) + propertiesManager.API_KEY);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        String createUpdatedOpportunityJson = JsonManager.getUpdateOpportunityJsonFromMap(opportunity, propertiesManager.OPPORTUNITY_PROPERTIES_UPDATE);
        outputStream.writeChars(createUpdatedOpportunityJson);



        String response;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
            response = getResponseAsString(connection);
            int vid = getVidFromCreatedOpportunity(response);
            System.out.println("Opportunity with id " + opportunity.get("id") + " succesfuly updated");
            connection.getInputStream().close();
            connection.disconnect();
            return new Pair<>(Integer.valueOf((String) opportunity.get("id")), vid);
        }else{
            response = getErrorAsString(connection);
         //   response = getResponseAsString(connection);
            JSONObject jsResponse = new JSONObject(response);

            System.out.println("contactId" + opportunity.get("id") + " " + jsResponse.get("status") + " : " + jsResponse.get("message"));
        }

        outputStream.flush();
        outputStream.close();
        connection.getInputStream().close();
        connection.disconnect();

        throw new IOException("Cannot add opportunity to hubspot " + response );


    }
}
