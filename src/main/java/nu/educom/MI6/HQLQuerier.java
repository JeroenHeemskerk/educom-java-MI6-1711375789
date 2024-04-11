package nu.educom.MI6;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HQLQuerier {
    private static SessionFactory sessionFactory;
    private static void setUpSessionFactory() {

        String mysqlUser = System.getenv("MYSQL_USER");
        String mysqlPassword = System.getenv("MYSQL_PASSWORD");

        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .applySetting("hibernate.connection.username", mysqlUser)
                    .applySetting("hibernate.connection.password", mysqlPassword)
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                System.out.println("Error setting up session factory: " + e.getMessage());
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static Agent readAgent(String servicenumber) {
        setUpSessionFactory(); // Ensure session factory is initialized
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String queryString = "select a from Agent a where a.serviceNumber = :servicenumber";
            Agent agent = session.createQuery(queryString, Agent.class)
                    .setParameter("servicenumber", servicenumber)
                    .uniqueResult();
            session.getTransaction().commit();
            return agent;
        } catch (Exception e) {
            System.out.println("Error reading agent: " + e.getMessage());
            return null;
        }
    }

    public static List<LoginAttempts> readLastLoginAttempts(String serviceNumber) {
        setUpSessionFactory(); // Ensure session factory is initialized
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String queryString = "FROM LoginAttempts " +
                    "WHERE loginTime >= (" +
                    "SELECT MAX(loginTime) " +
                    "FROM LoginAttempts " +
                    "WHERE loginSuccess = true " +
                    "AND serviceNumber = :serviceNumber) " +
                    "AND loginSuccess = false " +
                    "AND serviceNumber = :serviceNumber " +
                    "ORDER BY loginTime DESC";
            List<LoginAttempts> loginAttemptsList = session.createQuery(queryString, LoginAttempts.class)
                    .setParameter("serviceNumber", serviceNumber)
                    .setParameter("serviceNumber", serviceNumber)
                    .list();
            session.getTransaction().commit();
            return loginAttemptsList;
        } catch (Exception e) {
            System.out.println("Error reading loginAttempts: " + e.getMessage());
            return null;
        }
    }

    public static void updateLoginAttempt(String servicenumber, boolean success) {
        setUpSessionFactory(); // Ensure session factory is initialized
        LoginAttempts loginattempt = new LoginAttempts(servicenumber, null, success);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(loginattempt);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error updating login attempt: " + e.getMessage());
        }
    }
}