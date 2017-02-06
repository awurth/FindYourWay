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

}
