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
     * Method to find a Game by its id
     * @param id
     * @return Game
     */
    public Game findById(String id) {
        return entityManager.find(Game.class, id);
    }
    
    /**
     * Method to get all the games
     * @return List of Game
     */
    public List<Game> findAll() {
        return entityManager.createNamedQuery("Game.findAll", Game.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }
    
    /**
     * Method to insert a Game 
     * @param game
     * @return the game added
     */
    public Game insert(Game game) {
        game.generateId();
        return entityManager.merge(game);
    }
    
    /**
     * Method to delete a Game
     * @param game
     */
    public void delete(Game game) {
        entityManager.remove(game);
    }
    
}
