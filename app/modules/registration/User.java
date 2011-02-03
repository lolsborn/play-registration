package modules.registration;

import controllers.Registration;
import javax.persistence.*;
import play.db.jpa.Model;


@Entity
//@Table(name="users")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class User extends Model {

    @Basic(optional=false)
    @Column(nullable=false, unique=true)
    public String email;

    @Column(nullable=false, length=80)
    private String pwHash;

    @Column(length=22, nullable=true, unique=true)
    private String regConfirm;

    public String confirmationCode() {
        return this.regConfirm;
    }

    public User(String email, String password) {
        if(email == null || email.isEmpty())
            throw new RuntimeException("User must have an email");
        if (password == null || email.isEmpty())
            throw new RuntimeException("User must have a password");
        this.email = email;
        int saltFactor = Integer.parseInt(
                play.Play.configuration.getProperty("registration.salt_factor",
                    "10"));
        this.pwHash = BCrypt.hashpw(password, BCrypt.gensalt(saltFactor));
        this.regConfirm = Registration.shortUUID();
    }

//    @Basic(optional=false)
//    public String name;
//
//    @Basic(optional=false)
//    @Column(nullable = false, length=10)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date created;
//
//    @Basic(optional=false)
//    @Column(nullable=false)
//    public String email;
//
//    @OneToMany
//    public List<Book> books;

}