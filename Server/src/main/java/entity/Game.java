package entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g"),
    @NamedQuery(name = "Game.findById", query = "SELECT g FROM Game g WHERE g.id = :id")
})
public class Game implements Serializable {

    public final static int LIMIT_DISTANCE = 10;

    @Id
    private String id;

    private String token;

    @ManyToOne
    private Question question;

    private int distance;

    /**
     * Empty Constructor
     */
    public Game() { }

    /**
     * Method to init a game, it is a kind of session for a user
     * distance is set at LIMIT_DISTANCE (km), it is a condition for the gameplay :
     * at what distance maximum you have to place your marker on the map to get points
     */
    public void init() {
        distance = LIMIT_DISTANCE;
    }

    /**
     * Helper method to generate a token and set it
     * this method also removes hyphens
     */
    public void generateToken() {
        token = UUID.randomUUID().toString().replaceAll("-", "");;
    }
    
    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.randomUUID().toString().replaceAll("-", "");;
    }
    
    public boolean isValid() {
        return (question != null && distance >= 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getToken() {
        return token;
    }
}
