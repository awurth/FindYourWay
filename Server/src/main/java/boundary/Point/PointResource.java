package boundary.Point;

import entity.Point;

import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PointResource {

    @PersistenceContext
    EntityManager entityManager;

    public List<Point> findAll() {
        return entityManager.createNamedQuery("Question.findAll", Point.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

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
    
    /**
     * Method to update a point
     * @param point
     * @return the updated point
     */
    public Point update(Point point) {
        return entityManager.merge(point);
    }

    /**
     * Method to delete a point
     * @param point
     */
    public void delete(Point point) {
        entityManager.remove(point);
    }

}
