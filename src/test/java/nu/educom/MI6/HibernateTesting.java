package nu.educom.MI6;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;

/**
     * Unit test for simple App.
     */
public class HibernateTesting {

    private SessionFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        String mysqlUser = System.getenv("MYSQL_USER");
        String mysqlPassword = System.getenv("MYSQL_PASSWORD");

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .applySetting("hibernate.connection.username", mysqlUser)
                .applySetting("hibernate.connection.password", mysqlPassword)
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    public void hql_update_login(){

        LoginAttempts loginattempt = new LoginAttempts("007", null, true);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(loginattempt);
            session.getTransaction().commit();
        }
    }

    @Test
    @Disabled
    public void hql_fetch_user(){
        String servicenumber = "030";
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //List<Agent> agents = session.createQuery( "select a from Agent a" , Agent.class).list();
        String queryString = "select a from Agent a where a.serviceNumber = :servicenumber";
        Agent agent = session.createQuery(queryString, Agent.class)
                .setParameter("servicenumber", servicenumber)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    @Disabled
    public void hql_fetch_users(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Agent> agents = session.createQuery( "select a from Agent a" , Agent.class).list();
        session.getTransaction().commit();
        session.close();
    }
}

