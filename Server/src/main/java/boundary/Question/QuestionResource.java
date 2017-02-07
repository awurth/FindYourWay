package boundary.Question;

import entity.Question;
import entity.Point;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class QuestionResource {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method to find a Path by id
     * @param id
     * @return Path
     */
    public Question findById(String id) {
        return entityManager.find(Question.class, id);
    }

    /**
     * Method to get all the paths
     * @return List of Path
     */
    public List<Question> findAll() {
        return entityManager.createNamedQuery("Path.findAll", Question.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

    /**
     * Method that inserts a path into the database
     * @param path to add
     * @return the path added
     */
    public Question insert(Question path){
        return entityManager.merge(path);
    }

    /**
     * Method to delete a path and its points
     * @param path the Path to delete
     */
    public void delete(Question path) {
        for (Point point : path.getPoints())
            entityManager.remove(point);

        entityManager.remove(path);
    }


}
