/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 28, 2014
 * @author andy
 */
public class FileDirTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Checking Methods for Files and Directories
     */
    @Test
    public void testCheckFDM() {
        /**
         * Checking for the Existence of a File or Directory
         */
        //• Files.exists(): Checks whether a file exists
        //• notExists(): Checks whether a file does not exist

        /**
         * Checking File Accessibility
         */
        //isReadable(),    	isWritable(), and isExecutable(),Files.isRegularFile

        /**
         * Checking If Two Paths Point to the Same File
         */
        //Files.isSameFile

        /**
         * Checking the File Visibility
         */
        //Files.isHidden

    }

    /**
     * Creating and Reading Directories
     */
    @Test
    public void testCReadDir() {
        /**
         *Listing File System Root Directories
         */
        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        ArrayList<Path> list = new ArrayList<Path>();
        for (Path name : dirs) {
            System.out.println(name);
            list.add(name);
        }
        Path[] arr = new Path[list.size()];
        list.toArray(arr);
        for(Path path : arr) {
            System.out.println(path);
        }
        //If you need to extract the file system root directories as an array of File, use the Java 6 solution:
        File[] roots = File.listRoots();
        for (File root : roots) {
            System.out.println(root);
        }

        /**
         * Creating a New Directory
         */
        /*Path newdir = FileSystems.getDefault().getPath("/home/andy/work/2014/");
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            Files.createDirectory(newdir, attr);
//            Files.createDirectory(newdir);
        } catch (IOException e) {
            System.err.println(e);
        }

        newdir= FileSystems.getDefault().getPath("home/andy/work/2014/", "test/111");
        try {
            Files.createDirectories(newdir);
        } catch (IOException e) {
            System.err.println(e);
        }*/

        /**
         * Listing a Directory’s Content
         */
        //Files.newDirectoryStream()


        /**
         * Listing the Entire Content
         */
        Path path = Paths.get("/home/andy/work/");
        // no filter applied
        System.out.println("\nNo filter applied:");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Listing the Content by Applying a Glob Pattern
         */
        //glob pattern applied
        System.out.println("\nGlob pattern applied:");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path,
                "*.{png,jpg,bmp,csv}")) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Listing the Content by Applying a User-Defined Filter
         */
        //user-defined filter - only directories are accepted
        DirectoryStream.Filter<Path> dir_filter = new DirectoryStream.Filter<Path>() {
            public boolean accept(Path path) throws IOException {
                //return (Files.size(path) > 204800L);
                return (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS));
            }
        };
        System.out.println("\nUser defined filter applied:");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dir_filter)) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            System.err.println(e);
        }


    }

    /**
     * Creating, Reading, and Writing Files
     */
    @Test
    public void testCRWF() {
        /**
         * Using Standard Open Options
         */

        /**
         * Creating a New File
         */
        Path newfile = FileSystems.getDefault().getPath("/home/andy/work/test1.txt");
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            Files.createFile(newfile, attr);
//            Files.createFile(newfile);
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Writing a Small File
         */
        //Files.write()

        /**
         * Writing Bytes with the write() Method
         */
        //Files.write(path,bytes)

        /**
         * Writing Lines with the write() Method
         */
        Path rf_wiki_path = Paths.get("/home/andy/work", "wiki.txt");
        Charset charset = Charset.forName("UTF-8");
        ArrayList<String> lines = new ArrayList<>();
        lines.add("\n");
        lines.add("Rome Masters - 5 titles in 6 years");
        lines.add("Monte Carlo Masters - 7 consecutive titles (2005-2011)");
        lines.add("Australian Open - Winner 2009");
        lines.add("Roland Garros - Winner 2005-2008, 2010, 2011");
        lines.add("Wimbledon - Winner 2008, 2010");
        lines.add("US Open - Winner 2010");
        try {
            Files.write(rf_wiki_path, lines, charset, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Reading a Small File
         */
        //Files.readAllBytes()
        try {
            byte[] ballArray = Files.readAllBytes(newfile);
            //if the bytes are image
            //method1
            //Files.write(newfile.resolveSibling("bytes_to_ball.png"), ballArray);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(ballArray));
            ImageIO.write(bufferedImage, "png", (rf_wiki_path.resolveSibling("bytes_to_ball.png")).toFile());
        } catch (IOException e) {
            System.out.println(e);
        }
        //Files.readAllLines()
        charset = Charset.forName("ISO-8859-1");
        try {
           List<String> ListLines = Files.readAllLines(newfile, charset);
            for (String line : ListLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        /**
         * Working with Buffered Streams
         */
        //Files.newBufferedReader() and Files.newBufferedWriter()
        //Using the newBufferedWriter() Method
        charset = Charset.forName("UTF-8");
        String text = "\nVamos Rafa!";
        try (BufferedWriter writer = Files.newBufferedWriter(newfile,
                charset, StandardOpenOption.APPEND)) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println(e);
        }
        //Using the newBufferedReader() Method
        charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(newfile, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Working with Unbuffered Streams
         */
        //Files.newInputStream() and Files.newOutputStream()
        //Using the newOutputStream() Method
        //method1
        Path rn_racquet = Paths.get("C:/rafaelnadal/equipment", "racquet.txt");
        String racquet = "Racquet: Babolat AeroPro Drive GT";
        byte data[] = racquet.getBytes();
        try (OutputStream outputStream = Files.newOutputStream(rn_racquet)) {
            outputStream.write(data);
        } catch (IOException e) {
            System.err.println(e);
        }
        //method2
        String string = "\nString: Babolat RPM Blast 16";
        try (OutputStream outputStream = Files.newOutputStream(rn_racquet,
                StandardOpenOption.APPEND);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream))) {
            writer.write(string);
        } catch (IOException e) {
            System.err.println(e);
        }
        //Using the newInputStream() Method
        int n;
        try (InputStream in = Files.newInputStream(rn_racquet)) {
            while ((n = in.read()) != -1) {
                System.out.print((char) n);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        //method2
        byte[] in_buffer = new byte[1024];
        try (InputStream in = Files.newInputStream(rn_racquet)) {
            while ((n = in.read(in_buffer)) != -1) {
                System.out.println(new String(in_buffer));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        //method3
        try (InputStream in = Files.newInputStream(rn_racquet);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }


    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }



}
