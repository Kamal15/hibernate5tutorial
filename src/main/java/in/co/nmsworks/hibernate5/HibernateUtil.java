package in.co.nmsworks.hibernate5;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.config.spi.ConfigurationService;

import java.util.Map;
import java.util.Properties;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class HibernateUtil {
    
    public static final SessionFactory sf = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        Properties props = new Properties();
        props.put("Hello", "World");
        props.put("AA", "BB");
        
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // configure settings from hibernate.cfg.xml
                .applySettings(props)
                .build();
        try {
            final ConfigurationService service = serviceRegistry.getService(ConfigurationService.class);
            final Map settings = service.getSettings();
            System.out.println("My Settings: ");
            for (Object key : settings.keySet()) {
                System.out.println("[ " + key + "] : [" + settings.get(key) + "]");
            }
            
            Metadata metadataSources = new MetadataSources().buildMetadata(serviceRegistry);
            return metadataSources.buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void shutdown() {
        if (sf != null) {
            sf.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("India ");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
    }
}
