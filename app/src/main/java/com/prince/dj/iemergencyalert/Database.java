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
private String uri;
    public Database() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Database(String uri) {
        this.uri = uri;
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
    private String incident;
    private String location;
    private String date;
    private String reportedBy;

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Database(String incident, String location, String date, String reportedBy) {
        this.incident = incident;
        this.location = location;
        this.date = date;
        this.reportedBy = reportedBy;
    }
}
