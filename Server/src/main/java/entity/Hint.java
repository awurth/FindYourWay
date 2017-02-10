package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
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

    @Transient
    @XmlElement(name="_links")
    private List<Link> links = new ArrayList<>();

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

    /**
     * Method to add a link
     * @param uri uri link
     * @param rel name
     */
    public void addLink(String uri, String rel) {
        this.links.add(new Link(rel, uri));
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

    public List<Link> getLinks() {
        return links;
    }
}
