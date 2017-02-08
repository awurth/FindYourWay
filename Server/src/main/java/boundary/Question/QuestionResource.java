package boundary.Question;

import boundary.Score.ScoreResource;
import entity.Question;
import entity.Point;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class QuestionResource {

    @PersistenceContext
    EntityManager entityManager;

    @EJB
    ScoreResource scoreResource;

    @EJB
    HintResource hintResource;


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
     * Method to delete a question, its points, its scores and its hints
     * @param question the Question to delete
     */
    public void delete(Question question) {
        for (Point point : question.getPoints())
            entityManager.remove(point);

        scoreResource.findByQuestion(question).parallelStream().forEach(score -> {
            scoreResource.delete(score);
        });

        hintResource.findByQuestion(question).parallelStream().forEach(hint -> {
            hintResource.delete(hint);
        });

        entityManager.remove(question);
    }

}
