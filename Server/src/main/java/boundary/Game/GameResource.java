/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary.Game;

import boundary.Question.QuestionResource;
import entity.Game;
import entity.Question;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GameResource {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @EJB
    private QuestionResource questionResource;
    
    /**
     * Method to find a Game by id
     * @param id
     * @return Game
     */
    public Game findById(String id) {
        return entityManager.find(Game.class, id);
    }
    
    /**
     * Method to get all the games
     * @return List of Games
     */
    public List<Game> findAll() {
        return entityManager.createNamedQuery("Game.findAll", Game.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }
    
    /**
     * Method to insert a Game 
     * @param g the game to be inserted
     * @return the game added
     */
    public Game insert(Game g) {
        //on génére l'id de la partie
        g.generateId();
        //on l'insère dans la base de donnée en revoyant une éventuelle erreur
        return entityManager.merge(g);
    }
    
    /**
     * method to delete a Game, and the question created with
     * @param g the Game to be deleted
     */
    public void delete(Game g) {
        
        //on utilise la resource de question pour supprimer la question 
        questionResource.delete(g.getPoints());
        
        //on supprime la partie
        entityManager.remove(g);
    }
    
}
