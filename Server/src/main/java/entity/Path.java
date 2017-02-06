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
        @NamedQuery(name = "Path.findAll", query = "SELECT p FROM Path p"),
        @NamedQuery(name = "Path.findById", query = "SELECT p FROM Path p WHERE p.id = :id"),
        @NamedQuery(name = "Path.countAll", query = "SELECT COUNT(p) FROM Path p")
})
public class Path implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int PATH_LENGTH = 5;

    @Id
    private String id;

    @ManyToMany
    private List<Point> points = new ArrayList<>(PATH_LENGTH);

    /**
     * Empty Constructor
     */
    public Path() {}

    public Path(List<Point> points) {
        this.points = points;
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
