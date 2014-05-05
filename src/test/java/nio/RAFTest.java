/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * May 4, 2014
 * @author andy
 */
public class RAFTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Using the SeekableByteChannel Interface for Random Access to Files
     */
    @Test
    public void testSeekableByteChannel(){
        /**
         * Reading a File with SeekableByteChannel
         */
        Path path = Paths.get("/home/andy/work","test1.txt");
        // read a file using SeekableByteChannel
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(
                path, EnumSet.of(StandardOpenOption.READ))) {
            ByteBuffer buffer = ByteBuffer.allocate(12);
            String encoding = System.getProperty("file.encoding");
            buffer.clear();
            while (seekableByteChannel.read(buffer) > 0) {
                buffer.flip();
                System.out.print(Charset.forName(encoding).decode(buffer));
                buffer.clear();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Writing a File with SeekableByteChannel
         */
      //write a file using SeekableByteChannel
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(
                path, EnumSet.of(StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING))) {
            ByteBuffer buffer = ByteBuffer
                    .wrap("Rafa Nadal produced another masterclass of clay-court tennis to win his fifth French Open title ..."
                            .getBytes());
            int write = seekableByteChannel.write(buffer);
            System.out.println("Number of written bytes: " + write);
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * SeekableByteChannel and File Attributes
         */
        path = Paths.get("/home/andy/work", "email.txt");
        ByteBuffer buffer = ByteBuffer
                .wrap("Hi Rafa, I want to congratulate you for the amazing match that you played ... "
                        .getBytes());
        // create the custom permissions attribute for the email.txt file
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r-----");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        // write a file using SeekableByteChannel
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(
                path, EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND), attr)) {
            int write = seekableByteChannel.write(buffer);
            System.out.println("Number of written bytes: " + write);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        buffer.clear();

        /**
         * Reading a File with the Old ReadableByteChannel Interface
         */
        // read a file using ReadableByteChannel
        try (ReadableByteChannel readableByteChannel = Files
                .newByteChannel(path)) {
            buffer = ByteBuffer.allocate(12);
            buffer.clear();
            String encoding = System.getProperty("file.encoding");
            while (readableByteChannel.read(buffer) > 0) {
                buffer.flip();
                System.out.print(Charset.forName(encoding).decode(buffer));
                buffer.clear();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Writing a File with the Old WritableByteChannel Interface
         */
        // write a file using WritableByteChannel
        try (WritableByteChannel writableByteChannel = Files
                .newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND))) {
            buffer = ByteBuffer.wrap("Vamos Rafa!".getBytes());
            int write = writableByteChannel.write(buffer);
            System.out.println("Number of written bytes: " + write);
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Playing with SeekableByteChannel Position
         */
        //Read One Character from Different Positions
        buffer = ByteBuffer.allocate(1);
        String encoding = System.getProperty("file.encoding");
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(
                path, EnumSet.of(StandardOpenOption.READ)))) {
            // the initial position should be 0 anyway
            seekableByteChannel.position(0);
            System.out.println("Reading one character from position: "
                    + seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.rewind();
            // get into the middle
            seekableByteChannel.position(seekableByteChannel.size() / 2);
            System.out.println("\nReading one character from position: "
                    + seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.rewind();
            // get to the end
            seekableByteChannel.position(seekableByteChannel.size() - 1);
            System.out.println("\nReading one character from position: "
                    + seekableByteChannel.position());
            seekableByteChannel.read(buffer);
            buffer.flip();
            System.out.print(Charset.forName(encoding).decode(buffer));
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Write Characters at Different Positions
         */
        path = Paths.get("/home/andy/work", "MovistarOpen.txt");
        ByteBuffer buffer_1 = ByteBuffer
                .wrap("Great players participate in our tournament, like: Tommy Robredo, Fernando Gonzalez, Jose Acasuso or Thomaz Bellucci."
                        .getBytes());
        ByteBuffer buffer_2 = ByteBuffer.wrap("Gonzalez".getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(
                path, EnumSet.of(StandardOpenOption.WRITE)))) {
            // append some text at the end
            seekableByteChannel.position(seekableByteChannel.size());
            while (buffer_1.hasRemaining()) {
                seekableByteChannel.write(buffer_1);
            }
            // replace "Gonsales" with "Gonzalez"
            seekableByteChannel.position(301);
            while (buffer_2.hasRemaining()) {
                seekableByteChannel.write(buffer_2);
            }
            buffer_1.clear();
            buffer_2.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Copy a Portion of a File from the Beginning to the End
         */
        ByteBuffer copy = ByteBuffer.allocate(25);
        copy.put("\n".getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(
                path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
            int nbytes;
            do {
                nbytes = seekableByteChannel.read(copy);
            } while (nbytes != -1 && copy.hasRemaining());
            copy.flip();
            seekableByteChannel.position(seekableByteChannel.size());
            while (copy.hasRemaining()) {
                seekableByteChannel.write(copy);
            }
            copy.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         *  Replace a File Portion with Truncate Capability
         */
        buffer = ByteBuffer
                .wrap("The tournament has taken a lead in environmental conservation efforts, with highlights including the planting of 500 trees to neutralise carbon emissions and providing recyclable materials to local children for use in craft work."
                        .getBytes());
        try (SeekableByteChannel seekableByteChannel = (Files.newByteChannel(
                path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
            seekableByteChannel.truncate(200);
            seekableByteChannel.position(seekableByteChannel.size() - 1);
            while (buffer.hasRemaining()) {
                seekableByteChannel.write(buffer);
            }
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    /**
     * Working with FileChannel
     */
    @Test
    public void testFileChannel(){
        /**
         * Mapping a Channel’s File Region Directly into Memory
         */
        Path path = Paths.get("/home/andy/work", "test1.txt");
        MappedByteBuffer buffer = null;
        try (FileChannel fileChannel = (FileChannel.open(path,
                EnumSet.of(StandardOpenOption.READ)))) {
            buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0,
                    fileChannel.size());
        } catch (IOException ex) {
            System.err.println(ex);
        }
        if (buffer != null) {
            try {
                Charset charset = Charset.defaultCharset();
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer = decoder.decode(buffer);
                String content = charBuffer.toString();
                System.out.println(content);
                buffer.clear();
            } catch (CharacterCodingException ex) {
                System.err.println(ex);
            }
        }

        /**
         * Locking a Channel’s File
         */
        ByteBuffer byteBuffer = ByteBuffer.wrap("Vamos Rafa!".getBytes());
        try (FileChannel fileChannel = (FileChannel.open(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE)))) {
            // Use the file channel to create a lock on the file.
            // This method blocks until it can retrieve the lock.
            FileLock lock = fileChannel.lock();
            // Try acquiring the lock without blocking. This method returns
            // null or throws an exception if the file is already locked.
            // try {
            //
            lock = fileChannel.tryLock();
            // } catch (OverlappingFileLockException e) {
            // File is already locked in this thread or virtual machine
            // }
            if (lock.isValid()) {
                System.out.println("Writing to a locked file ...");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
                fileChannel.position(0);
                fileChannel.write(byteBuffer);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
            }
            // Release the lock
            lock.release();
            System.out.println("\nLock released!");
        } catch (IOException ex) {
            System.err.println(ex);
        }

        /**
         * Copying Files with FileChannel
         */
        //Copying Files with FileChannel and a Direct or Non-direct ByteBuffer
        final Path copy_from = Paths
                .get("C:/rafaelnadal/tournaments/2009/videos/Rafa Best Shots.mp4");
        final Path copy_to = Paths.get("C:/Rafa Best Shots.mp4");
        int bufferSizeKB = 4;
        int bufferSize = bufferSizeKB * 1024;
        System.out.println("Using FileChannel and direct buffer ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
                FileChannel fileChannel_to = (FileChannel.open(copy_to, EnumSet
                        .of(StandardOpenOption.CREATE_NEW,
                                StandardOpenOption.WRITE)))) {
            // Allocate a direct ByteBuffer
            ByteBuffer bytebuffer = ByteBuffer.allocateDirect(bufferSize);
            // Read data from file into ByteBuffer
            int bytesCount;
            while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
                // flip the buffer which set the limit to current position, and
                // position to 0
                bytebuffer.flip();
                // write data from ByteBuffer to file
                fileChannel_to.write(bytebuffer);
                // for the next read
                bytebuffer.clear();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        //Copying Files with FileChannel.transferTo() or FileChannel.transferFrom()
        System.out.println("Using FileChannel.transferTo method ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
                FileChannel fileChannel_to = (FileChannel.open(copy_to, EnumSet
                        .of(StandardOpenOption.CREATE_NEW,
                                StandardOpenOption.WRITE)))) {
            fileChannel_from.transferTo(0L, fileChannel_from.size(),
                    fileChannel_to);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        //Copying Files with FileChannel.map()
        System.out.println("Using FileChannel.map method ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
                FileChannel fileChannel_to = (FileChannel.open(copy_to, EnumSet
                        .of(StandardOpenOption.CREATE_NEW,
                                StandardOpenOption.WRITE)))) {
            buffer = fileChannel_from.map(
                    FileChannel.MapMode.READ_ONLY, 0, fileChannel_from.size());
            fileChannel_to.write(buffer);
            buffer.clear();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        //Benchmarking FileChannel Copy Capabilities


    }


    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
