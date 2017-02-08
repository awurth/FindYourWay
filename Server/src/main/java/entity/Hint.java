package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Hint.findAll", query = "SELECT h FROM Hint h"),
})
public class Hint {

    @Id
    private String id;

    @ManyToOne
    private Question question;

    private String value;

    /**
     * Empty Constructor
     */
    public Hint() {}

    /**
     * Hint constructor
     * @param question associated to this hint
     * @param value the message
     */
    public Hint(Question question, String value) {
        this.question = question;
        this.value = value;
    }

    /**
     * Helper method to know if critical fields have been filled
     * @return if the hint is valid
     */
    public boolean isValid() {
        return (question != null && value != null);
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
