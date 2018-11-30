import dao.ContactDAO;
import dao.DBConnection;
import dao.OpportunityDAO;
import dao.Querries;
import hubspotComponents.Contact;
import hubspotComponents.Opportunity;
import javafx.util.Pair;
import utils.HubSpotManager;
import utils.JsonManager;
import utils.PropertiesManager;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {

        ContactDAO contactDAO = new ContactDAO();
        OpportunityDAO opportunityDAO = new OpportunityDAO();
        HubSpotManager.propertiesManager = new PropertiesManager();

        // collection contain pair of userId from db and vid from hubspot
      //  List<Pair<Integer, Integer>> addedContactsWithVid = new LinkedList<Pair<Integer, Integer>>();

        // add user to hubspot
    //    List<Contact> contactsToInsert = contactDAO.getAllContacts(Querries.GET_ALL_CONTACTS_TO_INSERT);
     //   syncNewContacts(contactDAO, addedContactsWithVid, contactsToInsert);  // TO DZIAŁA
      //  syncNewContactsRs(contactDAO/*.getAllContactsAsMaps(Querries.GET_ALL_CONTACTS_TO_INSERT)*/);

//        List<Contact> contactsToUpdate = contactDAO.getAllContacts(Querries.GET_ALL_CONTACTS_TO_UPDATE);
//        syncExistingContacts(contactDAO, contactsToUpdate);  // to też działa

        // add opportunities to hs normal way
/*        List<Pair<Integer, Integer>> addedOpportunitiesWithVid = new LinkedList<Pair<Integer, Integer>>();
        List<Opportunity> opportunitiesToInsert = opportunityDAO.getAllOpportunities(Querries.GET_ALL_OPPORTUNITIES_TO_INSERT);
        syncNewOpportunties(opportunitiesToInsert, addedOpportunitiesWithVid, opportunityDAO);

        List<Opportunity> opportunitiesToUpdate = opportunityDAO.getAllOpportunities(Querries.GET_ALL_OPPORTUNITIES_TO_UPDATE);
        syncExistingOpportunities( opportunityDAO ,opportunitiesToUpdate);*/





        // generyczne
        genericSyncNewContacs(contactDAO);
        genericSyncExistingContacts(contactDAO);

        genericSyncNewOpportunity(opportunityDAO);
        genericSyncExistingOpportunity(opportunityDAO);
        System.out.println("program exit");
    }

    private static void genericSyncNewContacs(ContactDAO contactDAO) {
        List<Map<String, String>> allContactsAsMaps = contactDAO.getAllContactsAsMaps(Querries.GET_ALL_CONTACTS_TO_INSERT);
        if (allContactsAsMaps.size() >0 ) {
            List<Pair<Integer, Integer>> addedContactsWithVid = new LinkedList<Pair<Integer, Integer>>();
            for (Map contact : allContactsAsMaps){
                try {
                    addedContactsWithVid.add(HubSpotManager.createContact(contact));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            contactDAO.addCreatedContactsToJoiningTable(addedContactsWithVid);
        }
    }

    private static void genericSyncExistingContacts(ContactDAO contactDAO) {
        List<Map<String, String>> allContactsAsMaps = contactDAO.getAllContactsAsMaps(Querries.GET_ALL_CONTACTS_TO_UPDATE);

        for (Map contact: allContactsAsMaps){
            try {
                HubSpotManager.updateContact(contact);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void genericSyncNewOpportunity(OpportunityDAO dao){
        List<Map<String, String>> allOpportunitiesAsMaps = dao.getAllOpportunitiesAsMap(Querries.GET_ALL_OPPORTUNITIES_TO_INSERT);
        List<Pair<Integer, Integer>> addedOpportunitiesWithVid = new LinkedList<Pair<Integer, Integer>>();

        if (allOpportunitiesAsMaps.size() > 0) {
            for (Map opportunity : allOpportunitiesAsMaps){
                try {
                    addedOpportunitiesWithVid.add(HubSpotManager.createOpportunity(opportunity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dao.addCreatedOpportunitiesToJoiningTable(addedOpportunitiesWithVid);
        }

    }

    private static void genericSyncExistingOpportunity(OpportunityDAO dao){
        List<Map<String, String>> allOpportunitiesAsMapAsMaps = dao.getAllOpportunitiesAsMap(Querries.GET_ALL_OPPORTUNITIES_TO_UPDATE);

        if (allOpportunitiesAsMapAsMaps.size() > 0) {
            for (Map contact: allOpportunitiesAsMapAsMaps){
                try {
                    HubSpotManager.updateOpportunity(contact);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private static void syncNewContactsRs(ContactDAO dao) {
//        Connection connection = DBConnection.getConnection();
//        List<Contact> contactList = new LinkedList<Contact>();
//        List<Pair<Integer, Integer>> addedContactsWithVid = new LinkedList<Pair<Integer, Integer>>();
//
//        Statement statement = null;
//        ResultSet resultSet;
//        try {
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery(Querries.GET_ALL_CONTACTS_TO_INSERT);
//
//            ResultSetMetaData rsmd = resultSet.getMetaData();
//            int columnCount = rsmd.getColumnCount();
//            List<Pair<String, String>> row = null;
//            while (resultSet.next()){
//                row = new LinkedList<Pair<String, String>>();
//                for (int i = 1; i <= columnCount; i++ ) {
////                    String name = rsmd.getColumnName(i);
//                    if (resultSet.getObject(i) != null){
//                        if (resultSet.getObject(i).getClass().equals(Integer.class)){
//                            row.add(new Pair<String, String>(rsmd.getColumnName(i), String.valueOf(resultSet.getObject(i))));
//
//                        }
//                        row.add(new Pair<String, String>(rsmd.getColumnName(i), String.valueOf(resultSet.getObject(i)) ));
//                    }else {
//                        row.add(new Pair<String, String>(rsmd.getColumnName(i), ""));
//                    }
//
//                }
//                addedContactsWithVid.add(HubSpotManager.createContactRs(row));
//
//            }
//            dao.addCreatedContactsToJoiningTable(addedContactsWithVid);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                statement.close();
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private static void syncExistingOpportunities(OpportunityDAO dao, List<Opportunity> opportunitiesToUpdate) {
        if (opportunitiesToUpdate.size() != 0) {
            for (Opportunity opp : opportunitiesToUpdate) {
                Map<Integer, Integer> oppWithVid = dao.getOpportunitiesWithVid();
                int vid = findVidOfGivenOpportunity(opp.getId(), oppWithVid);
                try {
                    HubSpotManager.updateOpportunity(opp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Opportunity vith id " + opp.getId() + " succesfuly updated");
            }
        }
    }

    private static int findVidOfGivenOpportunity(int contactIdNeedToBeFound, Map<Integer, Integer> oppWithVid) {
        return oppWithVid.get(contactIdNeedToBeFound);
    }

    private static void syncNewOpportunties(List<Opportunity> opportunityList, List<Pair<Integer, Integer>> addedOpportunitiesWithVid, OpportunityDAO opportunityDAO) {
        if (opportunityList.size() != 0) {

            for (Opportunity opportunity : opportunityList) {
                try {
                    addedOpportunitiesWithVid.add(HubSpotManager.createOpportunity(opportunity));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // add values to opportunityId/vid table
            opportunityDAO.addCreatedOpportunitiesToJoiningTable(addedOpportunitiesWithVid);
        }
    }

    private static void syncNewContacts(ContactDAO contactDAO, List<Pair<Integer, Integer>> addedContactsWithVid, List<Contact> contactsToInsert) {
        if (contactsToInsert.size() != 0) {
            for (Contact contact : contactsToInsert) {
                try {
                    addedContactsWithVid.add(HubSpotManager.createContact(contact));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // insert values to userid_vid table in db
            contactDAO.addCreatedContactsToJoiningTable(addedContactsWithVid);
        }
    }

    private static void syncExistingContacts(ContactDAO contactDAO, List<Contact> contactsToUpdate) {
        if (contactsToUpdate.size() != 0) {
            Map<Integer, Integer> contactsWithVid = contactDAO.getContactsWithVid();

            for (Contact contact : contactsToUpdate) {
                int vidOfGivenContact = findVidOfGivenContact(contact.getId(), contactsWithVid);
                try {
                    contact.setNumberOfCreatedOpp(contactDAO.getAllCreatedOpportuntiesOfGivenUser(String.valueOf(contact.getId())));
                    contact.setNumberOfBuyedOpp(contactDAO.getAllBuyedOpportunitiesOfGivenUser(String.valueOf(contact.getId())));
                    JsonManager.getCreateContactJson(contact);
                    HubSpotManager.updateContract(contact, vidOfGivenContact);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int findVidOfGivenContact(int contactIdNeedToBeFound, Map idVidMap) {
        return (Integer) idVidMap.get(contactIdNeedToBeFound);
    }



}
