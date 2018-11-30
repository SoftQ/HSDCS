package dao;

import hubspotComponents.Opportunity;
import javafx.util.Pair;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OpportunityDAO {

    public List<Opportunity> getAllOpportunities(String querry){
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        LinkedList<Opportunity> opportunityList = new LinkedList<Opportunity>();
        ContactDAO contactDAO = new ContactDAO();

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);

            while (resultSet.next()){
                Opportunity opportunity = extractOpportunityFromResultSet(resultSet,contactDAO);
                opportunityList.add(opportunity);
            }
            return opportunityList;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Map<String,String>> getAllOpportunitiesAsMap(String querry){
        List<Map<String, String>> opportunities = new LinkedList<>();
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> opportunity = new HashMap<>();
                for (int i = 1; i <= columnCount; i++ ) {
                    String name = rsmd.getColumnName(i);
                    Object object = resultSet.getObject(i);
                    if (object != null){
                        Class<?> aClass = object.getClass();
                        // wsadz wartosc do mapy
                        if (aClass.equals(Integer.class)){
                            opportunity.put(name, String.valueOf( (Integer) object));
                        }else
                        if (aClass.equals(Timestamp.class)){
                            opportunity.put(name , String.valueOf(new Date(((Timestamp)resultSet.getObject(i)).getTime())));
                        }else
                        if (aClass.equals(String.class)){
                            opportunity.put(name, object != null ? (String) object : "");
                        }else
                        if (aClass.equals(Boolean.class)){
                            opportunity.put(name,  String.valueOf(object));
                        }


                    }else {
                        opportunity.put(name,  "");
                    }
                }
                opportunities.add(opportunity);
            }
            return opportunities;
        }
         catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }

        return null;
    }

    private Opportunity extractOpportunityFromResultSet(ResultSet resultSet, ContactDAO contactDAO) throws SQLException {
        int vid = -1;
        try {
            if (resultSet.getInt("opportunityVid") > 0){
                vid = resultSet.getInt("opportunityVid");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            vid = 0;
        }

        return new Opportunity(
                resultSet.getInt("id"),
                resultSet.getString("status"),
                resultSet.getInt("provider_id"),
                contactDAO.getContactVidByGivenId(resultSet.getInt("id")),
                resultSet.getString("deleteDate"),
                resultSet.getString("description"),
                resultSet.getString("dealname"),
                vid
        );
    }


    public void updateOpportunity(String opportunityID){

    }

    public boolean addCreatedOpportunitiesToJoiningTable(List<Pair<Integer, Integer>> addedOpportunitiesWithVid) {
        String inserQuerry = createQuerryStringWithNewOpportunities(addedOpportunitiesWithVid);
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

    private String createQuerryStringWithNewOpportunities(List<Pair<Integer, Integer>> addedOpportunitiesWithVid) {
        StringBuilder insertQuerry = new StringBuilder(Querries.SET_NEW_OPPORTUNITIES);

        for (Pair pair : addedOpportunitiesWithVid){
            insertQuerry.append("(").append(pair.getKey()).append(" ,").append(pair.getValue()).append("),");
        }
        insertQuerry.deleteCharAt(insertQuerry.length() - 1);
        insertQuerry.append(";");
        return insertQuerry.toString();
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

    public Map<Integer, Integer> getOpportunitiesWithVid() {
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Querries.GET_OPPORTUNITIES_WITH_VID);

            return extractOpprtunitiesVid(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnectionsAndStatements(connection, statement);
        }

        return null;
    }

    private Map<Integer, Integer> extractOpprtunitiesVid(ResultSet resultSet) throws SQLException {
        Map<Integer, Integer> idWithsVids = new HashMap<Integer, Integer>();

        while(resultSet.next()){
            idWithsVids.put(resultSet.getInt("opportunityId"), resultSet.getInt("opportunityVId"));
        }
        return  idWithsVids;
    }
}
