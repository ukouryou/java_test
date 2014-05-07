/**
 *
 */
package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * May 6, 2014
 * @author andy
 */
public class AsynchronousSocketTest {

    /**
     * Writing an Asynchronous Server (Based on Future)
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testAsynchronousServerBF() throws IOException, InterruptedException, ExecutionException {
        /**
         * Creating a New Asynchronous Server Socket Channel
         */
        AsynchronousServerSocketChannel asynchronousServerSocketChannel=
                AsynchronousServerSocketChannel.open();
        //check if an AsynchronousServerSocketChannel is open using AsynchronousServerSocketChannel.isOpen() method

        /**
         * Setting Asynchronous Server Socket Channel Options
         */
        asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
        asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        Set<SocketOption<?>> options = asynchronousServerSocketChannel.supportedOptions();
        for(SocketOption<?> option : options) System.out.println(option);

        /**
         * Binding the Asynchronous Server Socket Channel
         */
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
        //asynchronousServerSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));
        //asynchronousServerSocketChannel.bind(asynchronousServerSocketChannel.getLocalAddress());

        /**
         * Accepting Connections
         */
        Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture =asynchronousServerSocketChannel.accept();
        AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();
        System.out.println("Incoming connection from: " + asynchronousSocketChannel.getRemoteAddress());

        /**
         * Transmitting Data over a Connection
         */
        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (asynchronousSocketChannel.read(buffer).get() != -1) {
            buffer.flip();
            asynchronousSocketChannel.write(buffer).get();
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                buffer.clear();
            }
        }


    }

    /**
     * Writing an Asynchronous Client (Based on Future)
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testAsynchronousClientBF() throws IOException, InterruptedException, ExecutionException{
        /**
         * Creating a New Asynchronous Socket Channel
         */
        AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();

        /**
         * Setting Asynchronous Socket Channel Options
         */
        asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
        asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
        asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

        /**
         * Connecting the Asynchronous Channel’s Socket
         */
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        Void connect = asynchronousSocketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT)).get();

        /**
         * Transmitting Data over a Connection
         */
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        asynchronousSocketChannel.write(helloBuffer).get();
        while (asynchronousSocketChannel.read(buffer).get() != -1) {
            buffer.flip();
            charBuffer = decoder.decode(buffer);
            System.out.println(charBuffer.toString());
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                buffer.clear();
            }
            int r = new Random().nextInt(100);
            if (r == 50) {
                System.out.println("50 was generated! Close the asynchronous socket channel!");
                break;
            } else {
                randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                asynchronousSocketChannel.write(randomBuffer).get();
            }
        }
    }

    public void testEchoServer() {
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        // create an asynchronous server socket channel bound to the default
        // group
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                // set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                // bind the asynchronous server socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP,DEFAULT_PORT));
                // display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");
                while (true) {
                    Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();
                    try (AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get()) {
                        System.out.println("Incoming connection from: " + asynchronousSocketChannel.getRemoteAddress());
                        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                        // transmitting data
                        while (asynchronousSocketChannel.read(buffer).get() != -1) {
                            buffer.flip();
                            asynchronousSocketChannel.write(buffer).get();
                            if (buffer.hasRemaining()) {
                                buffer.compact();
                            } else {
                                buffer.clear();
                            }
                        }
                        System.out.println(asynchronousSocketChannel.getRemoteAddress() + " was successfully served!");
                    } catch (IOException | InterruptedException | ExecutionException ex) {
                        System.err.println(ex);
                    }
                }
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void testEchoServerMultiThread() {
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        // create asynchronous server socket channel bound to the default group
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                // set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                // bind the server socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP,
                        DEFAULT_PORT));
                // display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");
                while (true) {
                    Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();
                    try {
                        final AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();
                        Callable<String> worker = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String host = asynchronousSocketChannel.getRemoteAddress().toString();
                                System.out.println("Incoming connection from: " + host);
                                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                                // transmitting data
                                while (asynchronousSocketChannel.read(buffer).get() != -1) {
                                    buffer.flip();
                                    asynchronousSocketChannel.write(buffer).get();
                                    if (buffer.hasRemaining()) {
                                        buffer.compact();
                                    } else {
                                        buffer.clear();
                                    }
                                }
                                asynchronousSocketChannel.close();
                                System.out.println(host + " was successfully served!");
                                return host;
                            }
                        };
                        taskExecutor.submit(worker);
                    } catch (InterruptedException | ExecutionException ex) {
                        System.err.println(ex);
                        System.err.println("\n Server is shutting down ...");
                        // this will make the executor accept no new threads
                        // and finish all existing threads in the queue
                        taskExecutor.shutdown();
                        // wait until all threads are finished
                        while (!taskExecutor.isTerminated()) {
                        }
                        break;
                    }
                }
            } else {
                System.out
                        .println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    @Test
    public void testEchoClient(){
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        // create an asynchronous socket channel bound to the default group
        try (AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open()) {
            if (asynchronousSocketChannel.isOpen()) {
                // set some options
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                // connect this channel's socket
                Void connect = asynchronousSocketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT)).get();
                if (connect == null) {
                    System.out.println("Local address: " + asynchronousSocketChannel.getLocalAddress());
                    // transmitting data
                    asynchronousSocketChannel.write(helloBuffer).get();
                    while (asynchronousSocketChannel.read(buffer).get() != -1) {
                        buffer.flip();
                        charBuffer = decoder.decode(buffer);
                        System.out.println(charBuffer.toString());
                        if (buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }
                        int r = new Random().nextInt(100);
                        if (r == 50) {
                            System.out.println("50 was generated! Close the asynchronoussocket channel!");
                            break;
                        } else {
                            randomBuffer = ByteBuffer.wrap("Randomnumber:".concat(String.valueOf(r)).getBytes());
                            asynchronousSocketChannel.write(randomBuffer).get();
                        }
                    }
                } else {
                    System.out.println("The connection cannot be established!");
                }
            } else {
                System.out.println("The asynchronous socket channel cannot be opened!");
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            System.err.println(ex);
        }
    }

    @Test
    public void testEchoServerCompletionHandler() {
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        //create an asynchronous server socket channel bound to the default group
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                // set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                // bind the server socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(IP,DEFAULT_PORT));
                // display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");
                asynchronousServerSocketChannel.accept(null,
                                new CompletionHandler<AsynchronousSocketChannel, Void>() {
                                    final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                                    @Override
                                    public void completed(AsynchronousSocketChannel result,Void attachment) {
                                        asynchronousServerSocketChannel.accept(null, this);
                                        try {
                                            System.out.println("Incoming connection from: " + result.getRemoteAddress());
                                            // transmitting data
                                            while (result.read(buffer).get() != -1) {
                                                buffer.flip();
                                                result.write(buffer).get();
                                                if (buffer.hasRemaining()) {
                                                    buffer.compact();
                                                } else {
                                                    buffer.clear();
                                                }
                                            }
                                        } catch (IOException | InterruptedException | ExecutionException ex) {
                                            System.err.println(ex);
                                        } finally {
                                            try {
                                                result.close();
                                            } catch (IOException e) {
                                                System.err.println(e);
                                            }
                                        }
                                    }

                                    @Override
                                    public void failed(Throwable exc, Void attachment) {
                                        asynchronousServerSocketChannel.accept(null, this);
                                        throw new UnsupportedOperationException("Cannot accept connections!");
                                    }
                                });
                // Wait
                System.in.read();
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    @Test
    public void testEchoClientCompletionHandler(){
        final int DEFAULT_PORT = 5555;
        final String IP = "127.0.0.1";
        // create an asynchronous socket channel bound to the default group
        try (AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open()) {
            if (asynchronousSocketChannel.isOpen()) {
                // set some options
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                // connect this channel's socket
                asynchronousSocketChannel.connect(new InetSocketAddress(IP,DEFAULT_PORT), null,
                        new CompletionHandler<Void, Void>() {
                            final ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
                            final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                            CharBuffer charBuffer = null;
                            ByteBuffer randomBuffer;
                            final Charset charset = Charset.defaultCharset();
                            final CharsetDecoder decoder = charset.newDecoder();

                            @Override
                            public void completed(Void result, Void attachment) {
                                try {
                                    System.out.println("Successfully connected at: " + asynchronousSocketChannel.getRemoteAddress());
                                    // transmitting data
                                    asynchronousSocketChannel.write(helloBuffer).get();
                                    while (asynchronousSocketChannel.read(buffer).get() != -1) {
                                        buffer.flip();
                                        charBuffer = decoder.decode(buffer);
                                        System.out.println(charBuffer.toString());
                                        if (buffer.hasRemaining()) {
                                            buffer.compact();
                                        } else {
                                            buffer.clear();
                                        }
                                        int r = new Random().nextInt(100);
                                        if (r == 50) {
                                            System.out.println("50 was generated! Close the asynchronous socket channel!");
                                            break;
                                        } else {
                                            randomBuffer = ByteBuffer.wrap("Randomnumber:".concat(String.valueOf(r)).getBytes());
                                            asynchronousSocketChannel.write(randomBuffer).get();
                                        }
                                    }
                                } catch (IOException | InterruptedException | ExecutionException ex) {
                                    System.err.println(ex);
                                } finally {
                                    try {
                                        System.out.println("close finally");
                                        asynchronousSocketChannel.close();
                                    } catch (IOException ex) {
                                        System.err.println(ex);
                                    }
                                }
                            }

                            @Override
                            public void failed(Throwable exc, Void attachment) {
                                throw new UnsupportedOperationException(
                                        "Connection cannot be established!");
                            }
                        });
                System.in.read();
            } else {
                System.out.println("The asynchronous socket channel cannot be opened!");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void testGroup() throws IOException {
        AsynchronousChannelGroup threadGroup = null;
        ExecutorService executorService = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        try {
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        //The following example creates a fixed thread pool with exactly five threads:
        threadGroup = null;
        try {
            threadGroup = AsynchronousChannelGroup.withFixedThreadPool(5,Executors.defaultThreadFactory());
        } catch (IOException ex) {
            System.err.println(ex);
        }

        AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);

    }

}
