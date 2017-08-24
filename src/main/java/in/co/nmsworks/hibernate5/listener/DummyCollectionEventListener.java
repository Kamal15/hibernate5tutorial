package in.co.nmsworks.hibernate5.listener;

import in.co.nmsworks.hibernate5.dao.MyPostCollectionUpdateEvent;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.event.spi.AbstractCollectionEvent;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class DummyCollectionEventListener implements PostCollectionUpdateEventListener { 
    
    private final Logger logger = LoggerFactory.getLogger(DummyCollectionEventListener.class);
    
    @Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        printEvent("OnPostUpdate : ", event);

        /*if (event instanceof MyPostCollectionUpdateEvent) {

            Object oldObjects = ((MyPostCollectionUpdateEvent) event).getOldObj();
            PersistentCollection persistentCollection = event.getCollection();

            String role = persistentCollection.getRole();
            String propertyName = role.substring(role.lastIndexOf(".") + 1, role.length());
            logger.info("property name : " + propertyName);

            if (persistentCollection instanceof PersistentList && oldObjects instanceof List) {
                logger.info("Old Object List : " + ((List<?>) oldObjects));
            } else if (oldObjects instanceof Map<?, ?>) {
                Map<?, ?> props = (Map<?, ?>) oldObjects;
                if (persistentCollection instanceof PersistentMap)
                    logger.info("Old Object Map : " + props);
                else if (persistentCollection instanceof PersistentSet)
                    logger.info("Old Object Set : " + props.keySet());
                else
                    logger.info("Unknown Class type : " + persistentCollection.getClass());
            } else {
                logger.info("Bye Bye!! I'm not gonna to work");
            }
        }*/
    }

    private void printEvent(String header, AbstractCollectionEvent event) {
        final PersistentCollection persistentCollection = event.getCollection();
        String role = persistentCollection.getRole();
        String propertyName = role.substring(role.lastIndexOf(".") + 1, role.length());
        Object oldObjects = ((MyPostCollectionUpdateEvent) event).getOldObj();
        
        logger.info("************************************");
        logger.info(header + "Affected Owner : {}", event.getAffectedOwnerOrNull());
        logger.info(header + "Affected Owner Entity Name : {}", event.getAffectedOwnerEntityName());
        logger.info(header + "Affected Owner Id : {}", event.getAffectedOwnerIdOrNull());
        logger.info(header + "Old objects : {}", oldObjects);
        logger.info(header + "Persistent Collection : {}", persistentCollection);
        logger.info(header + "Role : {}", role);
        logger.info(header + "Property Name : {}", propertyName);
        logger.info(header + "StoredSnapshot : {}", persistentCollection.getStoredSnapshot());
        logger.info(header + "Event Class : {}", event.getClass());
        logger.info("************************************");
    }
}
