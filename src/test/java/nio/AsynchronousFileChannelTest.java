/**
 *
 */
package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

/**
 * May 5, 2014
 *
 * @author andy
 */
public class AsynchronousFileChannelTest {

    /**
     * File Read and Future
     */
    @Test
    public void testFRF() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        String encoding = System.getProperty("file.encoding");
        Path path = Paths.get("/home/andy/work", "test1.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel
                .open(path, StandardOpenOption.READ)) {
            Future<Integer> result = asynchronousFileChannel.read(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Do something else while reading ...");
            }
            System.out.println("Read done: " + result.isDone());
            System.out.println("Bytes read: " + result.get());
        } catch (Exception ex) {
            System.err.println(ex);
        }
        buffer.flip();
        System.out.print(Charset.forName(encoding).decode(buffer));
        buffer.clear();
    }

    /**
     * File Write and Future
     */
    @Test
    public void testFWF() {
        ByteBuffer buffer = ByteBuffer
                .wrap("The win keeps Nadal at the top of the bay.".getBytes());
        Path path = Paths.get("/home/andy/work", "story.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel
                .open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            Future<Integer> result = asynchronousFileChannel.write(buffer, 100);
            while (!result.isDone()) {
                System.out.println("Do something else while writing ...");
            }
            System.out.println("Written done: " + result.isDone());
            System.out.println("Bytes written: " + result.get());
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * File Read and CompletionHandler
     */
    @Test
    public void testFRC() {
        final Thread current;
        ByteBuffer buffer = ByteBuffer.allocate(100);
        Path path = Paths.get("/home/andy/work", "story.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            current = Thread.currentThread();
            asynchronousFileChannel.read(buffer, 0, "Read operation status ...",
                    new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            System.out.println(attachment);
                            System.out.print("Read bytes: " + result);
                            current.interrupt();
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println(attachment);
                            System.out.println("Error:" + exc);
                            current.interrupt();
                        }
                    });
            System.out.println("\nWaiting for reading operation to end ...\n");
            try {
                current.join();
            } catch (InterruptedException e) {
                System.out.println();
                System.out.println("InterruptedException");
            }
            // now the buffer contains the read bytes
            System.out.println("\n\nClose everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * File Lock with Future
     */
    @Test
    public void testFileLockFuture(){
        ByteBuffer buffer = ByteBuffer.wrap("Argentines At Home In Buenos Aires Cathedral\n".getBytes());
        Path path = Paths.get("/home/andy/work", "story.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
            Future<FileLock> featureLock = asynchronousFileChannel.lock();
            System.out.println("Waiting for the file to be locked ...");
            FileLock lock = featureLock.get();
            //or, use shortcut
            //FileLock lock = asynchronousFileChannel.lock().get();
            if (lock.isValid()) {
                Future<Integer> featureWrite = asynchronousFileChannel.write(buffer, 0);
                System.out.println("Waiting for the bytes to be written ...");
                int written = featureWrite.get();
                //or, use shortcut
                //int written = asynchronousFileChannel.write(buffer,0).get();
                System.out.println("I’ve written " + written + " bytes into " + path.getFileName() + " locked file!");
                lock.release();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * File Lock with Completion
     */
    @Test
    public void testFileLockCompletion(){
        final Thread current;
        final ByteBuffer buffer = ByteBuffer.wrap("Argentines At Home In Buenos Aires Cathedral\n".getBytes());
        Path path = Paths.get("/home/andy/work", "story.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            current = Thread.currentThread();
            asynchronousFileChannel.lock("Lock operation status:",
                    new CompletionHandler<FileLock, Object>() {
                        @Override
                        public void completed(FileLock result, Object attachment) {
                            System.out.println(attachment + " " + result.isValid());
                            if (result.isValid()) {
                                // ... processing ...
                                System.out.println("Processing the locked file ...");
                                asynchronousFileChannel.write(buffer, 0);
                                System.out.println("write file finished!");
                                // ...
                                try {
                                    result.release();
                                } catch (IOException ex) {
                                    System.err.println(ex);
                                }
                            }
                            current.interrupt();
                        }
                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println(attachment);
                            System.out.println("Error:" + exc);
                            current.interrupt();
                        }
                    });
            System.out.println("Waiting for file to be locked and process ... \n");
            try {
                current.join();
            } catch (InterruptedException e) {
            }
            System.out.println("\n\nClosing everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * AsynchronousFileChannel and ExecutorService
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testAFCES() throws InterruptedException {
        final int THREADS = 5;
        ExecutorService taskExecutor = Executors.newFixedThreadPool(THREADS);
        ExecutorService taskExecutor1 = Executors.newFixedThreadPool(THREADS);
        String encoding = System.getProperty("file.encoding");
        List<Future<ByteBuffer>> list = new ArrayList<>();
        int sheeps = 0;
        Path path = Paths.get("/home/andy/work", "story.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, withOptions(), taskExecutor1)) {
            for (int i = 0; i < 50; i++) {
                Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {
                    @Override
                    public ByteBuffer call() throws Exception {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
                        asynchronousFileChannel.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));
                        return buffer;
                    }
                };
                Future<ByteBuffer> future = taskExecutor.submit(worker);
                list.add(future);
            }
            // this will make the executor accept no new threads
            // and finish all existing threads in the queue
            taskExecutor.shutdown();
            // wait until all threads are finished
            while (!taskExecutor.isTerminated()) {
                // do something else while the buffers are prepared
                System.out.println("Counting sheep while filling up some buffers! So far I counted: "+ (sheeps += 1));
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
        System.out.println("\nDone! Here are the buffers:\n");
        for (Future<ByteBuffer> future : list) {
            ByteBuffer buffer;
            try {
                buffer = future.get();
                System.out.println("\n\n" + buffer);
                System.out.println("______________________________________________________");
                buffer.flip();
                System.out.print(Charset.forName(encoding).decode(buffer));
                buffer.clear();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static Set withOptions() {
        final Set options = new TreeSet<>();
        options.add(StandardOpenOption.READ);
        return options;
        }



}
