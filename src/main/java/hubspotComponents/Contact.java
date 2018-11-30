package hubspotComponents;

public class Contact {

    private transient int id;
    private String firstName;
    private String lastName;
    private String phone;

    private int numberOfCreatedOpp;
    private int numberOfBuyedOpp;
    //  private String mobile;

    //  private String creationDate;
    private boolean verified;
    public Contact(int id, String firstName,String lastName,  String phone, int verified/*, String mobile, String creationDate*/) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
       /* if (name.contains(" ")){
            this.firstName = name.substring(0, name.indexOf(' '));
            this.lastName = name.substring(name.indexOf(' ') + 1);
        }else{
            this.firstName = name;
            this.lastName = "";
        }*/
        this.phone = phone;
        this.verified = verified == 1;
      //  this.mobile = mobile;
     //   this.creationDate = creationDate;
    }

    public Contact(){

    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getNumberOfCreatedOpp() {
        return numberOfCreatedOpp;
    }

    public void setNumberOfCreatedOpp(int numberOfCreatedOpp) {
        this.numberOfCreatedOpp = numberOfCreatedOpp;
    }

    public int getNumberOfBuyedOpp() {
        return numberOfBuyedOpp;
    }

    public void setNumberOfBuyedOpp(int numberOfBuyedOpp) {
        this.numberOfBuyedOpp = numberOfBuyedOpp;
    }
    /*    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }*/
}
