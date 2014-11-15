package de.bitocean.zkrc;

/**
 * 
 * A monitor which monitors data and existence in a ZooKeeper 
 * node. It works with the asynchronous ZooKeeper APIs.
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher, StatCallback {

    protected static final ArrayList<ACL> NO_ACL = Ids.OPEN_ACL_UNSAFE;
    
    String clientID = "DEMO";
    
    ZooKeeper zk;

    String znode;

    Watcher chainedWatcher;

    boolean dead;

    DataMonitorListener listener;

    byte prevData[];

    public DataMonitor(String clientID, ZooKeeper zk, String znode, Watcher chainedWatcher,
            DataMonitorListener listener) {
        this.clientID = clientID;
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        
        // sayHallo to the server 
        sayHallo();
        
        // Get things started by checking if the node exists. We are going
        // to be completely event driven
        zk.exists(znode, true, this, null);
    }

    private void sayHallo() {
        try {
            Date date = new Date( System.currentTimeMillis() );
            boolean isNew = false;
            try {
                zk.create(znode+"/"+clientID, date.toString().getBytes(), NO_ACL, CreateMode.PERSISTENT);
                isNew = true;
            }
            catch(Exception ex) {
                System.err.println( ex.getMessage() );
                System.out.println( ">>> znode exists ...");
            }    
            
            if ( isNew ) {
                System.out.println( ">>> lets define some values in zNode ...");
                zk.create(znode + "/" + clientID +"/started", date.toString().getBytes(), NO_ACL, CreateMode.PERSISTENT);
                //zk.create(znode + "/" + clientID +"/status", "good".getBytes(), NO_ACL, CreateMode.PERSISTENT);
                //zk.create(znode + "/" + clientID +"/spool", "empty".getBytes(), NO_ACL, CreateMode.PERSISTENT_SEQUENTIAL);
            }
        } 
        catch (KeeperException ex) {
            Logger.getLogger(DataMonitor.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(DataMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Other classes use the DataMonitor by implementing this method
     */
    public interface DataMonitorListener {
        /**
         * The existence status of the node has changed.
         */
        void exists(byte data[]);

        /**
         * The ZooKeeper session is no longer valid.
         *
         * @param rc
         *                the ZooKeeper reason code
         */
        void closing(int rc);

        public void setCMD(String data);
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            // We are are being told that the state of the
            // connection has changed
            switch (event.getState()) {
            case SyncConnected:
                // In this particular example we don't need to do anything
                // here - watches are automatically re-registered with 
                // server and any watches triggered while the client was 
                // disconnected will be delivered (in order of course)
                break;
            case Expired:
                // It's all over
                dead = true;
                listener.closing(KeeperException.Code.SessionExpired);
                break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                // Something has changed on the node, let's find out
                zk.exists(znode, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
        case Code.Ok:
            exists = true;
            break;
        case Code.NoNode:
            exists = false;
            break;
        case Code.SessionExpired:
        case Code.NoAuth:
            dead = true;
            listener.closing(rc);
            return;
        default:
            // Retry errors
            zk.exists(znode, true, this, null);
            return;
        }

        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(znode + "/" + clientID, false, null);
                String data = new String(b);
                listener.setCMD( data );
                System.out.println( "... b={" + data + "}\n");
        
            } 
            catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((b == null && b != prevData)
                || (b != null && !Arrays.equals(prevData, b))) {
            listener.exists(b);
            prevData = b;
        }
    }
}

 