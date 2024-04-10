package nu.educom.MI6;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "agent")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "servicenumber")
    private String serviceNumber;
    private String passphrase;
    private String active;
    @Column(name = "licenced_to_kill")
    private String licensed;
    @Column(name = "licence_expiration")
    private String expiration;

    public Agent(){
    }

    public Agent(int id, String serviceNumber, String passphrase, String active, String licensed, String expiration ) {
        this.id = id;
        this.serviceNumber = serviceNumber;
        this.passphrase = passphrase;
        this.active = active;
        this.licensed = licensed;
        this.expiration = expiration;
    }

    public int getId() {
        return id;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getActive() {
        return active;
    }

    public String getLicensed() {
        return licensed;
    }

    public String getExpiration() {
        return expiration;
    }

}
