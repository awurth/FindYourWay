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
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partie.findAll", query = "SELECT p FROM Partie p"),
    @NamedQuery(name = "Partie.findById", query = "SELECT p FROM Partie p WHERE p.id = :id")
})
public class Game implements Serializable {
    
    @Id
    private String id;
    
    private Question points;
    
    //pourra changer si on implémente différents modes de difficulté
    private long distanceMin;
   
    @OneToOne
    private User joueur;
    
    public Game() {}
    
    public Game(long dmin, Question q, User u) {
        this.distanceMin = dmin;
        this.points = q;
        this.joueur = u;
    }
    
    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.randomUUID().toString().replaceAll("-", "");;
    }
    
    public boolean isValid() {
        Long l = distanceMin;
        return (points != null && l != null && joueur != null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Question getPoints() {
        return points;
    }

    public void setPoints(Question points) {
        this.points = points;
    }

    public long getDistanceMin() {
        return distanceMin;
    }

    public void setDistanceMin(long distanceMin) {
        this.distanceMin = distanceMin;
    }

    public User getJoueur() {
        return joueur;
    }

    public void setJoueur(User joueur) {
        this.joueur = joueur;
    }
    
    
    
}
