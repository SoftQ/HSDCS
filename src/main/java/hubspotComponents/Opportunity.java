package hubspotComponents;

import java.util.List;

/**
 * This class refer to first2trade opportunity, and it supposed to be mapped to Hubspot Deal component
 */
public class Opportunity {

    private transient int id;
    private int vid;

    private String status;

    private int providerId;
    private int providerVId;

    /**
     * Delete date
     */
    private String expireDate;

    private String description;

    private String title;

    public Opportunity(int id, String status, int providerId, int providerVId, String expireDate, String description, String title, int vid) {
        this.id = id;
        this.status = status;
        this.providerId = providerId;
        this.providerVId = providerVId;
        this.expireDate = expireDate;
        this.description = description;
        this.title = title;
        this.vid = vid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getProviderVId() {
        return providerVId;
    }

    public void setProviderVId(int providerVId) {
        this.providerVId = providerVId;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }


//    private List<Integer> associatedUsers;




}
