package entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s ORDER BY s.value DESC"),
})
public class Score implements Serializable {
    
    @Id
    private String id;
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Question question;

    private long value;

    /**
     * Empty constructor
     */
    public Score() {}

    /**
     * Score constructor
     * @param user who made this score
     * @param value : score points
     */
    public Score(User user, Question question, long value) {
        this.user = user;
        this.question = question;
        this.value = value;
    }

    /**
     * Helper method to know if critical fields have been filled
     * @return if the score is valid
     */
    public boolean isValid() {
        return (question != null && user != null && (Long.toString(value) != null && value >= 0));
    }
    
    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.randomUUID().toString().replaceAll("-", "");
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
