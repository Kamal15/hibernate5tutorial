package my.com.examples.hibernate5.dao;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.persister.collection.CollectionPersister;

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
