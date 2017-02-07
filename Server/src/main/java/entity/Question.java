package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Path.findAll", query = "SELECT p FROM Question p"),
        @NamedQuery(name = "Path.findById", query = "SELECT p FROM Question p WHERE p.id = :id"),
        @NamedQuery(name = "Path.countAll", query = "SELECT COUNT(p) FROM Question p")
})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int PATH_LENGTH = 5;

    @Id
    private String id;

    @OneToMany
    private List<Point> points = new ArrayList<>(PATH_LENGTH);

    /**
     * Empty Constructor
     */
    public Question() {}

    public Question(List<Point> points) {
        this.points = points;
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

        return (counter == 0 || counter > 1);
    }

    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.fromString(UUID.randomUUID().toString()).toString();
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

}
