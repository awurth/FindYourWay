/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary.Score;

import entity.Question;
import entity.Score;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Utilisateur
 */
@Stateless
public class ScoreResource {
    
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method to find a Score by id
     * @param id
     * @return Score
     */
    public Score findById(String id) {
        return entityManager.find(Score.class, id);
    }
    
    /**
     * Method to get all the scores by value decreasing order
     * @return List of Score
     */
    public List<Score> findAll() {
        return entityManager.createNamedQuery("Score.findAll", Score.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

    /**
     * Method to get all the scores from a Question
     * @param question
     * @return List of Score
     */
    public List<Score> findByQuestion(Question question) {
        return entityManager.createQuery("SELECT s FROM Score s WHERE s.question =: question")
        .setParameter("question", question)
        .getResultList();
    }
    
    /**
     * Method that inserts a score into the database
     * @param score to add
     * @return the score added
     */
    public Score insert(Score score) {
        score.generateId();
        return entityManager.merge(score);
    }
    
    /**
     * Method to delete a score
     * @param score the Score to delete
     */
    public void delete(Score score) {
         entityManager.remove(score);
    }
    
    /**
     * Method to update a score
     * @param score
     * @return the updated score
     */
    public Score update(Score score) {
        return entityManager.merge(score);
    }
}
