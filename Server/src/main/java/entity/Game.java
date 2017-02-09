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
    
    @Id
    private String id;

    @ManyToOne
    private Question question;

    @OneToOne
    private User user;

    private long minimumDistance;

    /**
     * Empty Constructor
     */
    public Game() { }
    
    public void init() {
        this.minimumDistance = 10L;
    }
    
    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.randomUUID().toString().replaceAll("-", "");;
    }
    
    public boolean isValid() {
        return (question != null && minimumDistance >= 0 && user != null);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(long minimumDistance) {
        this.minimumDistance = minimumDistance;
    }
}
