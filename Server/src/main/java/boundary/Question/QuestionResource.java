package boundary.Question;

import entity.Question;
import entity.Point;

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
     * Method to find a Question by id
     * @param id
     * @return Path
     */
    public Question findById(String id) {
        return entityManager.find(Question.class, id);
    }

    /**
     * Method to get all the questions
     * @return List of Questions
     */
    public List<Question> findAll() {
        return entityManager.createNamedQuery("Question.findAll", Question.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

    /**
     * Method that inserts a question into the database
     * @param question to add
     * @return the question added
     */
    public Question insert(Question question){
        question.generateId();
        return entityManager.merge(question);
    }

    /**
     * Method to delete a question and its points
     * @param path the Question to delete
     */
    public void delete(Question path) {
        for (Point point : path.getPoints())
            entityManager.remove(point);

        entityManager.remove(path);
    }

}
