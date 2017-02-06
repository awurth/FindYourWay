package entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

@Entity
@XmlRootElement
public class Point implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private double longitude;
    private double latitude;
    private String name;
    private String hint;
    private boolean isFinal = false;

    /**
     * Empty constructor
     */
    public Point() {}

    /**
     * Constructor for a Point
     * @param longitude x
     * @param latitude y
     * @param hint (image url)
     */
    public Point(String name, double longitude, double latitude, String hint) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.hint = hint;
        this.name = name;
    }

    /**
     * Helper method to generate an id and set it to this.id
     * this method also removes hyphens
     */
    public void generateId() {
        id = UUID.fromString(UUID.randomUUID().toString()).toString();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
