package in.co.nmsworks.hibernate5.listener;

import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class DummyListener implements PostCommitInsertEventListener, PostCommitUpdateEventListener, 
        PostCommitDeleteEventListener {

    private static final Logger logger = LoggerFactory.getLogger(DummyListener.class);
    
    @Override
    public void onPostDeleteCommitFailed(PostDeleteEvent event) {
    }

    @Override
    public void onPostInsertCommitFailed(PostInsertEvent event) {

    }

    @Override
    public void onPostUpdateCommitFailed(PostUpdateEvent event) {

    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        final Object entity = event.getEntity();
        final String entityClazzName = entity.getClass().getName();
        final String[] propertyNames = event.getPersister().getPropertyNames();
        final Object[] deletedState = event.getDeletedState();
        logger.info("DELETE: Entity : {}, clazzName : {}, propertyNames : {}, deletedState : {}", entity, 
                entityClazzName, Arrays.toString(propertyNames), Arrays.toString(deletedState));
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        final Object entity = event.getEntity();
        final String entityClazzName = entity.getClass().getName();
        final String[] propertyNames = event.getPersister().getPropertyNames();
        final Object[] state = event.getState();
        logger.info("INSERT: Entity : {}, clazzName : {}, propertyNames : {}, state : {}", entity, entityClazzName, 
                Arrays.toString(propertyNames), Arrays.toString(state));
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        final Object entity = event.getEntity();
        final String entityClazzName = entity.getClass().getName();
        final int[] dirtyProperties = event.getDirtyProperties();
        final String[] propertyNames = event.getPersister().getPropertyNames();
        final Object[] oldState = event.getOldState();
        final Object[] newState = event.getState();
        logger.info("UPDATE: Entity : {}, clazzName : {}, dirtyProperties : {}, propertyNames : {}, oldState : {}, " +
                        "newState : {}", entity, entityClazzName, Arrays.toString(dirtyProperties),
                Arrays.toString(propertyNames), Arrays.toString(oldState), Arrays.toString(newState));
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return true;
    }
    
}
