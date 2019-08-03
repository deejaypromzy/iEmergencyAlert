package com.prince.dj.iemergencyalert;

class Database {
    private String FirstName;
    private String LastName;
    private String OtheNames;
    private String IdType;
    private String IdNumber;
    private String Phone;
    private String GpsAddress;
    private String EmergencyContact;
    private String UserName;

    public Database() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getOtheNames() {
        return OtheNames;
    }

    public void setOtheNames(String otheNames) {
        OtheNames = otheNames;
    }

    public String getIdType() {
        return IdType;
    }

    public void setIdType(String idType) {
        IdType = idType;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGpsAddress() {
        return GpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        GpsAddress = gpsAddress;
    }

    public String getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        EmergencyContact = emergencyContact;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Database(String firstName,
                    String lastName,
                    String otheNames,
                    String idType,
                    String idNumber,
                    String phone,
                    String gpsAddress,
                    String emergencyContact,
                    String userName) {
        FirstName = firstName;
        LastName = lastName;
        OtheNames = otheNames;
        IdType = idType;
        IdNumber = idNumber;
        Phone = phone;
        GpsAddress = gpsAddress;
        EmergencyContact = emergencyContact;
        UserName = userName;
    }

}
