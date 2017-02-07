package boundary.Point;

import entity.Point;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PointResource {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method to find a Point by its id
     * @param id : primary key
     * @return Point
     */
    public Point findById(String id) {
        return entityManager.find(Point.class, id);
    }

    /**
     * Method to insert a point into the database
     * the id is also generated in this part
     * @param point Point to add
     * @return the Point added
     */
    public Point insert(Point point) {
        point.generateId();
        return entityManager.merge(point);
    }

}
