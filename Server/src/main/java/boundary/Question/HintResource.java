package boundary.Question;

import entity.Hint;
import entity.Question;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class HintResource {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method to find a Hint by its id
     *
     * @param id
     * @return Path
     */
    public Hint findById(String id) {
        return entityManager.find(Hint.class, id);
    }

    /**
     * Method to get all the hints from a Question
     *
     * @param question
     * @return List of Hint
     */
    public List<Hint> findByQuestion(Question question) {
        return entityManager.createQuery("SELECT h FROM Hint h WHERE h.question =: question")
                .setParameter("question", question)
                .getResultList();
    }

    /**
     * Method that inserts a hint into the database
     *
     * @param hint to add
     * @return the hint added
     */
    public Hint insert(Hint hint) {
        hint.generateId();
        return entityManager.merge(hint);
    }

    /**
     * Method to delete a hint
     *
     * @param hint
     */
    public void delete(Hint hint) {
        entityManager.remove(hint);
    }

    /**
     * Method to update a hint
     *
     * @param hint
     * @return the updated hint
     */
    public Hint update(Hint hint) {
        return entityManager.merge(hint);
    }
}
