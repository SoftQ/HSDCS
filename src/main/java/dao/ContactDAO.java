package dao;

import hubspotComponents.Contact;
import javafx.util.Pair;
import utils.PropertiesManager;
import java.util.Date;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContactDAO {
    /**
     *
     * @return all contact in first2trade.getAllContacts view
     */
    public List<Contact> getAllContacts(String querry){
        Connection connection = DBConnection.getConnection();
        List<Contact> contactList = new LinkedList<Contact>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);
            
            while (resultSet.next()){
                Contact contact = extractContactFromResultSet(resultSet);
                contactList.add(contact);
            }
            return contactList;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }
        return null;
    }

    public List<Map<String,String>> getAllContactsAsMaps(String querry){
        List<Map<String, String>> contacts = new LinkedList<>();
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (resultSet.next()){
                Map<String, String> contact = new HashMap<>();
                for (int i = 1; i <= columnCount; i++ ) {
                    String name = rsmd.getColumnName(i);
                    Object object = resultSet.getObject(i);
                    if (object != null) {
                        Class<?> aClass = object.getClass();
                        if (aClass.equals(Integer.class)){
                            contact.put(name, String.valueOf( (Integer) object));
                            }else
                        if (aClass.equals(Timestamp.class)){

                            contact.put(name , String.valueOf(new Date(((Timestamp)resultSet.getObject(i)).getTime())));
                        }else
                        if (aClass.equals(Boolean.class)){
                            contact.put(name,  String.valueOf(object));
                        }
                        else{
                            contact.put(name, object != null ? (String) object : "");
                        }
                    }else {
                        contact.put(name,  "");
                    }
                }
                contacts.add(contact);
            }

            return contacts;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }
        return null;
    }

    /**
     * Inserts all new users to joinning table with userId from database and vid, obtained from hubspot
     * @param newUsers
     */
    public boolean addCreatedContactsToJoiningTable(List<Pair<Integer, Integer>> newUsers){
        String inserQuerry = createQuerryStringWithNewContacts(newUsers);
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(inserQuerry);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection,statement);
        }
        return false;
    }

    public Map<Integer,Integer> getContactsWithVid (){

        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Querries.GET_CONTACTS_WITH_VID);

            return extractContactsVid(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }

        return null;
    }

    public int getAllBuyedOpportunitiesOfGivenUser(String userID){
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Querries.GET_ALL_BUYED_OPPORTUNITES.replace("???", userID));
            return resultSet.next() ? getNumberOfBuyedOpp(resultSet) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }
        return -21;
    }

    public int getAllCreatedOpportuntiesOfGivenUser(String userId){
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Querries.GET_ALL_CREATED_OPPORTUNITES.replace("???", userId));
            return resultSet.next() ? getNumberOFCreatedOpp(resultSet) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }
        return -1;
    }

    private void closeConnectionsAndStatements(Connection connection, Statement statement) {
        try {
            if (statement != null){
                statement.close();
            }
            if (connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Contact extractContactFromResultSet(ResultSet resultSet) throws SQLException {
        return new Contact(
                resultSet.getInt("id"),
                resultSet.getString("business_owner_firstname"),
                resultSet.getString("business_owner_lastname"),
                resultSet.getString("phone"),
                resultSet.getInt("email_verified")
                //   resultSet.getString("mobile"),
                //   resultSet.getString("creationDate")

        );
    }

    private  String createQuerryStringWithNewContacts(List<Pair<Integer, Integer>> newUsers){

        StringBuilder insertQuerry = new StringBuilder(Querries.SET_NEW_CONTACTS);
        //  insertQuerry.append("(");
        for (Pair<Integer,Integer> pair : newUsers){
            insertQuerry.append("(").append(pair.getKey()).append(" ,").append(pair.getValue()).append("),");
        }
        insertQuerry.deleteCharAt(insertQuerry.length() - 1);
        insertQuerry.append(";");
        return insertQuerry.toString();
    }

    private int getNumberOfBuyedOpp(ResultSet resultSet) {

        try {
            return resultSet.getInt("noOfBuyOpp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getNumberOFCreatedOpp(ResultSet resultSet) {
        try {
            return resultSet.getInt("noOfOpp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Map<Integer, Integer> extractContactsVid(ResultSet resultSet) throws SQLException {
        Map<Integer, Integer> idWithsVids = new HashMap<Integer, Integer>();

        while(resultSet.next()){
            idWithsVids.put(resultSet.getInt("userId"), resultSet.getInt("userVid"));
        }
        return  idWithsVids;
    }

    public int getContactVidByGivenId(int id) {
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Querries.GET_CONTACTVID_BY_GIVEN_ID.replace("???", String.valueOf(id)));
            int userVid = -1;
            try {
                userVid = resultSet.getInt("userVid");
            } catch (SQLException e) {
                System.out.println("Contact with id " + id + " does not exist in contactId_Vid table");
                //e.printStackTrace();
            }
            return userVid;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection,statement);
        }
        return -1;
    }
}
