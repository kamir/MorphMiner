package de.bitocean.zkrc;

/**
 * 
 * Based on the data stored in ZNode this program starts and
 * stops executables. A DataMonitor is used. 
 * 
 * The program watches the specified znode and saves the data that 
 * corresponds to the znode in the filesystem. It also starts the 
 * specified program with the specified arguments when the znode 
 * exists and kills the program if the znode goes away.
 * 
 * Adding or deleting a ZNode triggers activity on the remote nodes, which
 * watch out for this ZNode.
 * 
 */
import de.bitocean.zkrc.DataMonitor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener
{

    static Executor getExecutor(String[] args) {

        Executor ex = null;
                
        if (args.length < 4) {
            System.err
                    .println("USAGE: Executor hostPort znode filename program [args ...]");
            System.exit(2);
        }

        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        String clientID = args[3];
        
        String exec[] = new String[args.length - 4];
        System.arraycopy(args, 4, exec, 0, exec.length);
        try {
            for( String e : exec ) {
                System.out.println( e );
            }    
            ex = new Executor( hostPort, znode, filename, exec, clientID );
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        return ex;
    }

    // the ZNode we are watching for
    String znode;

    // a
    DataMonitor dm;

    ZooKeeper zk;

    String filename;

    public String exec[];

    Process child;

    public Executor(String hostPort, String znode, String filename,
            String exec[], String clientID) throws KeeperException, IOException {
        this.filename = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(clientID, zk, znode, null, this );
        System.out.println(">>> Executor was started ... clientID="+clientID);
    }

    /**
     * @param args
     */ 
    public static void main(String[] args) {    
        Executor exec = Executor.getExecutor(args);
        exec.run();
    }
    
    public static void stop() {
        System.out.println(">>> ZooView will be closed soon ... ***~~~ Have a nice day ~~~***");
    }

    /***************************************************************************
     * We do process any events ourselves, we just need to forward them on.
     *
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
     */
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void setCMD(String data) {
        int lastEntry = exec.length-1;
        exec[ lastEntry ] = "-Dcmd="+data;
    }

    static class StreamWriter extends Thread {
        OutputStream os;

        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
