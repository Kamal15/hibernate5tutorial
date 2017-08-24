package in.co.nmsworks.hibernate5.dao;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class MyPostCollectionUpdateEvent extends PostCollectionUpdateEvent {

    private static final long serialVersionUID = 1L;
    private Object oldObj;
    
    public MyPostCollectionUpdateEvent(Object oldObj, CollectionPersister collectionPersister, PersistentCollection collection, EventSource source) {
        super(collectionPersister, collection, source);
        this.oldObj = oldObj;
    }

    public Object getOldObj() {
        return oldObj;
    }

    public void setOldObj(Object oldObj) {
        this.oldObj = oldObj;
    }
}
