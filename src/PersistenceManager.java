import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
    private EntityManagerFactory emFactory;

    public PersistenceManager() {
        emFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public void close() {
        emFactory.close();
    }
}
