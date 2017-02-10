package boundary.Question;

import boundary.Point.PointResource;
import boundary.Score.ScoreResource;
import entity.Hint;
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

    @EJB
    PointResource pointResource;


    /**
     * Method to find a Question by id
     *
     * @param id
     * @return Path
     */
    public Question findById(String id) {
        return entityManager.find(Question.class, id);
    }

    /**
     * Method to get all the questions
     *
     * @return List of Questions
     */
    public List<Question> findAll() {
        return entityManager.createNamedQuery("Question.findAll", Question.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

    /**
     * Method that inserts a question into the database
     *
     * @param question to add
     * @return the question added
     */
    public Question insert(Question question) {
        question.generateId();
        return entityManager.merge(question);
    }


    /**
     * Method to update a question, it also removes the points and hints which are not used anymore
     * all the scores associated to the old question are removed (to avoid cheats)
     * @param originalQuestion the original question to update
     * @param question the question containing new data
     * @return the question updated
     */
    public Question update(Question originalQuestion, Question question) {
        List<Point> originalPoints = originalQuestion.getPoints();
        List<Point> points = question.getPoints();
        List<Hint> originalHints = originalQuestion.getHints();
        List<Hint> hints = question.getHints();

        for (int i = 0; i < Question.PATH_LENGTH ; i++) {
            if (originalPoints.get(i) != points.get(i))
                pointResource.delete(originalPoints.get(i));
            if (originalHints.get(i) != hints.get(i))
                hintResource.delete(originalHints.get(i));
        }

        originalQuestion.setPoints(points);
        originalQuestion.setHints(hints);

        scoreResource.findByQuestion(question).parallelStream().forEach(score -> {
            scoreResource.delete(score);
        });


        return entityManager.merge(question);
    }

    /**
     * Method to delete a question, its points, its scores and its hints
     *
     * @param question the Question to delete
     */
    public void delete(Question question) {
        for (Point point : question.getPoints())
            pointResource.delete(point);

        scoreResource.findByQuestion(question).parallelStream().forEach(score -> {
            scoreResource.delete(score);
        });

        hintResource.findByQuestion(question).parallelStream().forEach(hint -> {
            hintResource.delete(hint);
        });

        entityManager.remove(question);
    }

}