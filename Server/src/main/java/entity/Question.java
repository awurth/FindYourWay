package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
        @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id"),
        @NamedQuery(name = "Question.countAll", query = "SELECT COUNT(q) FROM Question q")
})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int PATH_LENGTH = 6;

    @Id
    private String id;

    @OneToMany
    private List<Point> points = new ArrayList<>(PATH_LENGTH);

    @OneToMany
    private List<Hint> hints = new ArrayList<>(PATH_LENGTH);

    @Transient
    @XmlElement(name="_links")
    private List<Link> links = new ArrayList<>();

    /**
     * Empty Constructor
     */
    public Question() {}

    public Question(List<Point> points, List<Hint> hints ) {
        this.points = points;
        this.hints = hints;
    }

    /**
     * Method to find the final point in a question
     * @return final point
     */
    public Point findFinal() {
        for (Point point : points) {
            if (point.isFinal())
                    return point;
        }
        return null;
    }

    /**
     * Method to check if points are okay : check if
     *  - the length is okay
     *  - the points are valid
     *  - there is a final point and if the question does not have many
     * @return if it's valid
     */
    public boolean isPointsValid() {
        int counter = 0;

        if (points.size() != PATH_LENGTH)
            return false;

        for (Point point : points) {
            if (!point.isValid())
                return false;
            if (point.isFinal())
                counter++;
        }

        return (counter == 1);
    }

    /**
     * Method to check if a question is well constructed
     * @return if the object is ok
     */
    public boolean isValid() {
        return (isHintsValid() && isPointsValid());
    }

    /**
     * Method to check if hints are okay
     * @return if it's valid
     */
    public boolean isHintsValid() {
        if (hints.size() != PATH_LENGTH)
            return false;

        for (Hint hint : hints) {
            if (!hint.isValid())
                return false;
        }

        return true;
    }

    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.randomUUID().toString().replaceAll("-", "");;
    }

    /**
     * Method to add a point to the path
     * @param point
     * @return if it was added
     */
    public boolean addPoints(Point point) {
        if (points.size() >= PATH_LENGTH)
            return false;

        points.add(point);
        return true;
    }

    /**
     * Method to add a hint for the final point
     * @param hint
     * @return if it was added
     */
    public boolean addHints(Hint hint) {
        if (hints.size() >= PATH_LENGTH)
            return false;

        hints.add(hint);
        return true;
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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
