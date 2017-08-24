package in.co.nmsworks.hibernate5;

import in.co.nmsworks.hibernate5.domain.Address;
import in.co.nmsworks.hibernate5.domain.Event;
import in.co.nmsworks.hibernate5.domain.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class NativeAPIIllustration {
    
    private static final Logger logger = LoggerFactory.getLogger(NativeAPIIllustration.class);
    private static final SessionFactory sf = HibernateUtil.sf;
    
    public static void testBasicPostCommitListeners() throws SQLException {
        Session session = sf.openSession();
        Event e1 = new Event("Our first Event!!", 25, new Date());
        Event e2 = new Event("A follow up event", 26, new Date());

        // Add
        logger.info("Inserting two events, E1 : {}, E2 : {}", e1, e2);
        session.beginTransaction();
        session.save(e1);
        session.save(e2);
        session.getTransaction().commit();

        // Update
        logger.info("Updating the event");
        session.beginTransaction();
        List<Event> list = session.createQuery("from Event").list();
        for (Event event : list) {
            event.setDate(new Date(new Date().getTime() + 5000));
            event.setPrice(event.getPrice() + 5);
            session.save(event);
            logger.info("Updated : {}", event);
        }
        session.getTransaction().commit();

        // Delete
        logger.info("Deleting the event");
        session.beginTransaction();
        list = session.createQuery("from Event").list();
        for (Event event : list) {
            event.setDate(new Date());
            session.delete(event);
            logger.info("Deleted : {}", event);
        }
        session.getTransaction().commit();

        // Read
        list = session.createQuery("from Event").list();
        logger.info("Event size : {}", list.size());
        session.close();
    }

    public static String[][] executeQueryInConnection(final Connection conn, String sqlQuery) throws DatabaseException {
        logger.trace("sqlQuery : " + sqlQuery);
        try {
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                return resultSetAsStringArray(resultSet);
            } finally {
                if (conn != null)
                    conn.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    public static String [][] resultSetAsStringArray(ResultSet resultSet	) throws DatabaseException {
        java.sql.ResultSetMetaData metaData = null;
        List<String> queryResult = new LinkedList<String>();
        int columns = -1;
        try {
            metaData = resultSet.getMetaData();
            columns = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int ndx = 1; ndx <= columns; ndx++) {
                    queryResult.add(resultSet.getString(ndx));
                }
            }
        } catch (SQLException se) {
            throw new DatabaseException(se.getMessage(), se);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException se) {
                logger.warn("Exception while closing ResultSet.", se);
            }
        }

        int size = queryResult.size();
        if (size % columns != 0) {
            String msg = "Corrupted ResultSet size "+size+", rows "+ (size / columns)+", columns "+ columns;
            logger.error(msg);
            throw new DatabaseException("Corrupted ResultSet.");
        }
        int rows = size / columns;
        String[][] result = new String[rows][columns];

        Iterator<String> itr = queryResult.iterator();
        for (int j = 0, k = 0; itr.hasNext(); k++) {
            if (k == columns) {
                j++;
                k = 0;
            }
            result[j][k] = itr.next();
        }
        return result;
    }

    public static void testCollectionPostCommitListener() {
        Set<Address> addressSet = new HashSet<>();
        addressSet.add(new Address("AA", "AA", 600021, "India"));
        Person kamal = new Person("Kamal", addressSet);
        
        Session session = sf.openSession();
        
        // Insert
        logger.info("Inserting the person : {}", kamal);
        session.beginTransaction();
        session.save(kamal);
        session.getTransaction().commit();

        // Update1: Add one more address to the same person..
        List<Person> list = session.createQuery("from Person").list();
        session.beginTransaction();
        for (Person person : list) {
            Address homeAddr = new Address("BB", "BB", 560001,"India");
            person.getAddresses().add(homeAddr);
            session.save(person);
            logger.info("Updated the address in Person : {}", person);
        }
        session.getTransaction().commit();

        // Update2: Add another address to the same person
        list = session.createQuery("from Person").list();
        session.beginTransaction();
        for (Person person : list) {
            // Add one more address to the same person..
            Address ofcAddr = new Address("CC", "CC", 600113, "India");
            person.getAddresses().add(ofcAddr);
            session.save(person);
            logger.info("Updated2 the address in Person : {}", person);
        }
        session.getTransaction().commit();
        
        // Delete
        logger.info("Deleting the person");
        list = session.createQuery("from Person").list();
        session.beginTransaction();
        for (Person person : list) {
            session.delete(person);
        }
        session.getTransaction().commit();
        
        session.close();
    }

    public static void main(String[] args) throws InterruptedException, SQLException {
        
        testBasicPostCommitListeners();
        // testCollectionPostCommitListener();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.shutdown();
            }
        }));
    }
    
    public static class DatabaseException extends SQLException {
        
        public DatabaseException(String message) {
            super(message);
        }
        
        public DatabaseException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
