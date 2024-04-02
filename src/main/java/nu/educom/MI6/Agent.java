package nu.educom.MI6;

public class Agent {
    private String passphrase;
    //private int id;
    private String serviceNumber;
    //private String passphrase;
    //private String active;
    private String licensed;
    private String expiration;

    public Agent(String serviceNumber, String licensed, String expiration, String passphrase) {
        //this.id = id;
        this.serviceNumber = serviceNumber;
        this.passphrase = passphrase;
        //this.active = active;
        this.licensed = licensed;
        this.expiration = expiration;
    }

    //public int getId() {
    //    return id;
    //}

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getPassphrase() {
        return passphrase;
    }

    //public String getActive() {
    //    return active;
    //}

    public String getLicensed() {
        return licensed;
    }

    public String getExpiration() {
        return expiration;
    }

}
