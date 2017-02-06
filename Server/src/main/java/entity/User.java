package entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.email ASC"),
        @NamedQuery(name = "User.findByEmailAndPassword", query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password"),
        @NamedQuery(name = "User.countAll", query = "SELECT COUNT(u) FROM User u")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String email;

    private String name, password;

    private UserRole role;

    /**
     * Empty constructor
     */
    public User(){}

    /**
     * User constructor
     * @param n the name
     * @param e the email address
     * @param p the password
     */
    public User(String n, String e, String p) {
        this.name = n;
        this.email = e;
        this.password = p;
        this.role = UserRole.CUSTOMER;
    }


    /**
     * - Getter and Setter functions -
     */

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}