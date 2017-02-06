package entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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

    @Id
    private String id;

    Point goal;


    //HashMap<Point, String> points = new HashMap<>(5);

  //  List<String> hints = new ArrayList<>(5);

    /**
     * Empty Constructor
     */
    public Path() {}

    /**
     * Constructor to create the path of a game without hints for the final point
     * @param points the points associated to hints
     * @param goal final point
     */
  /**  public Path(HashMap<Point,String> points, Point goal) {
        this.points = points;
        this.goal = goal;
    }*/

    /**
     * Constructor to create the path of a game
     * @param points the points associated to hints
     * @param hints the hints for the final point
     * @param goal final point
     */
   /** public Path(HashMap<Point,String> points, List<String> hints, Point goal) {
        this.points = points;
        this.hints = hints;
        this.goal = goal;
    }*/

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

    public Point getGoal() {
        return goal;
    }

    public void setGoal(Point goal) {
        this.goal = goal;
    }

   /** public HashMap<Point, String> getPoints() {
        return points;
    }

    public void setPoints(HashMap<Point, String> points) {
        this.points = points;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }
    */
}
