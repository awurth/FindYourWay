package boundary.User;

import entity.User;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless
public class UserResource {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Method that returns an account for an id given
     * @param email
     * @return Account
     */
    public User findByEmail(String email) {
        return entityManager.find(User.class, email);
    }

    /**
     * Method that returns all the users
     * @return List of User
     */
    public List<User> findAll(){
        return entityManager.createNamedQuery("User.findAll", User.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                .getResultList();
    }

    /**
     * Method that inserts an account into the database
     * @param user to add
     * @return the account
     */
    public User insert(User user) throws DuplicateKeyException {
        try {
            return entityManager.merge(user);
        }catch (Exception e){
            throw new DuplicateKeyException();
        }
    }

}
