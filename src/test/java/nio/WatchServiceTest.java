/**
 *
 */
package nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Apr 30, 2014
 * @author andy
 */
public class WatchServiceTest {

    /**
     * Registering Objects with the Watch Service
     * @throws IOException
     */
    @Test
    public void testRegister() throws IOException {
        final Path path = Paths.get("C:/rafaelnadal");
        // Creating a WatchService
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        watchService.close();

        /**
         * Waiting for the Incoming Events (infinite loop)
         */
        /*while(true){
            //retrieve and process the incoming events
            ...
        }
        //or
        for (;;) {
            // retrieve and process the incoming events
            ...
        }*/

        /**
         * Getting a Watch Key
         */
        //poll(),poll(long,TimeUnit),take()
        //poll method, without arguments
        /*while (true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.poll();
            //the thread flow gets here immediately with an available key or a null value
            ...
        }*/
        //poll method, with arguments
        /*while (true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.poll(10, TimeUnit.SECONDS);
            //the thread flow gets here immediately if a key is available, or after 10 seconds
            //with an available key or null value
            ...
        }*/
        //take method
        /* while (true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.take();
            //the thread flow gets here immediately if a key is available, or it will wait until a
            //key is available, or the loop breaks
            ...
        }*/

        /**
         * Retrieving Pending Events for a Key
         */
        /*while (true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.take();
            //get list of pending events for the watch key
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
            ...
            }
            ...
        }*/

        /**
         * Retrieving the Event Type and Count
         */
        //get list of pending events for the watch key
        /*for (WatchEvent<?> watchEvent : key.pollEvents()) {
            // get the kind of event (create, modify, delete)
            final Kind<?> kind = watchEvent.kind();
            // handle OVERFLOW event
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }
            System.out.println(kind);
            System.out.println(watchEvent.count());
        }*/

        /**
         * Retrieving the File Name Associated with an Event
         */
        /*final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
        final Path filename = watchEventPath.context();
        System.out.println(filename);*/

        /**
         * Putting the Key Back in Ready State
         */
       /* while(true){
            ...
            //reset the key
            boolean valid = key.reset();
            //exit loop if the key is not valid (if the directory was deleted, for example)
            if (!valid) {
              break;
            }
        }*/

        /**
         * Closing the Watch Service
         */
        //WatchService.close() or  placing the creation code in a try-with-resources block
        /*try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            ...
        }*/

    }

    /**
     * Gluing It All Together
     */
    @Test
    public void test() {
        final Path path = Paths.get("C:/rafaelnadal");
        WatchRafaelNadal watch = new WatchRafaelNadal();
        try {
            watch.watchRNDir(path);
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }

    class WatchRafaelNadal {
        public void watchRNDir(Path path) throws IOException,
                InterruptedException {
            try (WatchService watchService = FileSystems.getDefault()
                    .newWatchService()) {
                path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE);
                // start an infinite loop
                while (true) {
                    // retrieve and remove the next watch key
                    final WatchKey key = watchService.take();
                    // get list of pending events for the watch key
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        // get the kind of event (create, modify, delete)
                        final Kind<?> kind = watchEvent.kind();
                        // handle OVERFLOW event
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        // get the filename for the event
                        final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                        final Path filename = watchEventPath.context();
                        // print it out
                        System.out.println(kind + " -> " + filename);
                    }
                    // reset the key
                    boolean valid = key.reset();
                    // exit loop if the key is not valid (if the directory was
                    // deleted, for example)
                    if (!valid) {
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     */
    @Test
    public void testExample1(){
        final Path path = Paths.get("/home/andy/tmp");
        WatchRecursiveRafaelNadal watch = new WatchRecursiveRafaelNadal();
        try {
            watch.watchRNDir(path);
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }

    class WatchRecursiveRafaelNadal {
        private WatchService watchService;
        private final Map<WatchKey, Path> directories = new HashMap<>();

        private void registerPath(Path path) throws IOException {
            // register the received path
            WatchKey key = path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            // store the key and path
            directories.put(key, path);
        }

        private void registerTree(Path start) throws IOException {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                        BasicFileAttributes attrs) throws IOException {
                    System.out.println("Registering:" + dir);
                    registerPath(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        public void watchRNDir(Path start) throws IOException,
                InterruptedException {
            watchService = FileSystems.getDefault().newWatchService();
            registerTree(start);
            // start an infinite loop
            while (true) {
                // retrieve and remove the next watch key
                final WatchKey key = watchService.take();
                // get list of events for the watch key
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    // get the kind of event (create, modify, delete)
                    final Kind<?> kind = watchEvent.kind();
                    // get the filename for the event
                    final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                    final Path filename = watchEventPath.context();
                    // handle OVERFLOW event
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    // handle CREATE event
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        final Path directory_path = directories.get(key);
                        final Path child = directory_path.resolve(filename);
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                            registerTree(child);
                        }
                    }// print it out
                    System.out.println(kind + " -> " + filename);
                }
                // reset the key
                boolean valid = key.reset();
                // remove the key if it is not valid
                if (!valid) {
                    directories.remove(key);
                    // there are no more keys registered
                    if (directories.isEmpty()) {
                        break;
                    }
                }
            }
            watchService.close();
        }
    }

    /**
     * example2
     */
    public void testExample2() {
        final Path path = Paths.get("C:/security");
        SecurityWatch watch = new SecurityWatch();
        try {
            watch.watchVideoCamera(path);
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }

    class SecurityWatch {
        WatchService watchService;

        private void register(Path path, Kind<Path> kind) throws IOException {
            // register the directory with the watchService for Kind<Path> event
            path.register(watchService, kind);
        }

        public void watchVideoCamera(Path path) throws IOException,
                InterruptedException {
            watchService = FileSystems.getDefault().newWatchService();
            register(path, StandardWatchEventKinds.ENTRY_CREATE);
            // start an infinite loop
            OUTERMOST: while (true) {
                // retrieve and remove the next watch key
                final WatchKey key = watchService.poll(11, TimeUnit.SECONDS);
                if (key == null) {
                    System.out.println("The video camera is jammed - security watch system is canceled!");
                    break;
                } else {// get list of events for the watch key
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        // get the kind of event (create, modify, delete)
                        final Kind<?> kind = watchEvent.kind();
                        // handle OVERFLOW event
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            // get the filename for the event
                            final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                            final Path filename = watchEventPath.context();
                            final Path child = path.resolve(filename);
                            if (Files.probeContentType(child).equals("image/jpeg")) {
                                // print out the video capture time
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                System.out.println("Video capture successfully at: " + dateFormat.format(new Date()));
                            } else {
                                System.out.println("The video camera capture format failed! This could be a virus!");
                                break OUTERMOST;
                            }
                        }
                    }
                    // reset the key
                    boolean valid = key.reset();
                    // exit loop if the key is not valid
                    if (!valid) {
                        break;
                    }
                }
            }
            watchService.close();
        }
    }


    class Print implements Runnable {
        private Path doc;

        Print(Path doc) {
            this.doc = doc;
        }

        @Override
        public void run() {
            try {
                // sleep a random number of seconds for simulating dispatching
                // and printing
                Thread.sleep(20000 + new Random().nextInt(30000));
                System.out.println("Printing: " + doc);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    @Test
    public void testExample3(){
        final Path path = Paths.get("C:/printertray");
        WatchPrinterTray watch = new WatchPrinterTray();
        try {
            watch.watchTray(path);
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }

    class WatchPrinterTray {
        private final Map<Thread, Path> threads = new HashMap<>();

        public void watchTray(Path path) throws IOException,
                InterruptedException {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE);
                // start an infinite loop
                while (true) {
                    // retrieve and remove the next watch key
                    final WatchKey key = watchService
                            .poll(10, TimeUnit.SECONDS);
                    // get list of events for the watch key
                    if (key != null) {
                        for (WatchEvent<?> watchEvent : key.pollEvents()) {
                            // get the filename for the event
                            final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                            final Path filename = watchEventPath.context();
                            // get the kind of event (create, modify, delete)
                            final Kind<?> kind = watchEvent.kind();
                            // handle OVERFLOW event
                            if (kind == StandardWatchEventKinds.OVERFLOW) {
                                continue;
                            }
                            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                System.out.println("Sending the document to print ->" + filename);
                                Runnable task = new Print( path.resolve(filename));
                                Thread worker = new Thread(task);
                                // we can set the name of the thread
                                worker.setName(path.resolve(filename).toString());
                                // store the thread and the path
                                threads.put(worker, path.resolve(filename));
                                // start the thread, never call method run()
                                // direct
                                worker.start();
                            }
                            if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                System.out.println(filename
                                        + " was successfully printed!");
                            }
                        }
                        // reset the key
                        boolean valid = key.reset();
                        // exit loop if the key is not valid
                        if (!valid) {
                            threads.clear();
                            break;
                        }
                    }
                    if (!threads.isEmpty()) {
                        for (Iterator<Map.Entry<Thread, Path>> it = threads
                                .entrySet().iterator(); it.hasNext();) {
                            Map.Entry<Thread, Path> entry = it.next();
                            if (entry.getKey().getState() == Thread.State.TERMINATED) {
                                Files.deleteIfExists(entry.getValue());
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
    }


}
