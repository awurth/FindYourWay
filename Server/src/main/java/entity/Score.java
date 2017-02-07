/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Utilisateur
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Score.findAll", query = "SELECT u FROM Score u ORDER BY u.value DESC")
})
public class Score implements Serializable {
    
    @Id
    private String id;
    
    @OneToMany
    private User user;
    
    //the highest score ever for a specific user
    private long value;
    
    public Score() {}
    
    public Score(User joueur, long score) {
        this.user = joueur;
        this.value = score;
    }
    
    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.fromString(UUID.randomUUID().toString()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
  
    
}
