package dao;

public class Querries {
    // contact
    public static final String GET_ALL_CONTACTS_TO_INSERT = "SELECT * FROM first2trade.getAllContract WHERE userVid is null;";

    public static final String GET_ALL_CONTACTS_TO_UPDATE = "SELECT * FROM first2trade.getAllContract WHERE userVid is not null;";

    public static final String SET_NEW_CONTACTS = "INSERT INTO first2trade.userId_vid(userId, userVId) VALUES";

    public static final String GET_CONTACTS_WITH_VID = "SELECT * FROM first2trade.userId_vid;";

    public static final String GET_CONTACTVID_BY_GIVEN_ID = "SELECT userVid FROM first2trade.userId_vid WHERE userId = ???;";

    public static final String GET_ALL_CREATED_OPPORTUNITES = "SELECT * FROM first2trade.getNumberOfCreatedOpportunities  where provider_id = ???;";

    public static final String GET_ALL_BUYED_OPPORTUNITES = "SELECT * FROM first2trade.getNumberOfBuyedOpportunies where user_id = ???;";

    // opportunity

    public static final String GET_ALL_OPPORTUNITIES_TO_INSERT = "SELECT * FROM first2trade.getAllOpportunitiesToInsert;";

    public static final String SET_NEW_OPPORTUNITIES = "INSERT INTO first2trade.opportunityId_vid(opportunityId, opportunityVId) VALUES";

    public static final String GET_ALL_OPPORTUNITIES_TO_UPDATE = "SELECT * FROM first2trade.allOpportunitiesToUpdate;";

    public static final String GET_OPPORTUNITIES_WITH_VID = "SELECT * FROM first2trade.opportunityId_vid;";

}
