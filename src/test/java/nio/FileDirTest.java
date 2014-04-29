/**
 *
 */
package nio;

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
import java.nio.file.StandardCopyOption;
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

    /**
     * Creating Temporary Directories and Files
     */
    @Test
    public void testCTDF(){

        /**
         * Creating a Temporary Directory
         */
        //createTempDirectory()
        String tmp_dir_prefix = "nio_";
        try {
            // passing null prefix
            Path tmp_1 = Files.createTempDirectory(null);
            System.out.println("TMP: " + tmp_1.toString());
            // set a prefix
            Path tmp_2 = Files.createTempDirectory(tmp_dir_prefix);
            System.out.println("TMP: " + tmp_2.toString());
        } catch (IOException e) {
            System.err.println(e);
        }
        //output
        //TMP: /tmp/8662825716781946819
        //TMP: /tmp/nio_2355283620031125088
        String default_tmp = System.getProperty("java.io.tmpdir");
        System.out.println(default_tmp);
        //
        Path basedir = FileSystems.getDefault().getPath("/home/andy/tmp/");
        tmp_dir_prefix = "rafa_";
        try {
            // create a tmp directory in the base dir
            Path tmp = Files.createTempDirectory(basedir, tmp_dir_prefix);
            System.out.println("TMP: " + tmp.toString());
        } catch (IOException e) {
            System.err.println(e);
        }
        //shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown-hook activated ...");
                // ... here, cleanup/save resources
                System.out.println("Shutdown-hook successfully executed ...");
            }
        });
        //combine list files and shutdown hook
        try {
            // create a tmp directory in the base dir
            final Path tmp_dir = Files.createTempDirectory(basedir,
                    tmp_dir_prefix);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Deleting the temporary folder ...");
                    try (DirectoryStream<Path> ds = Files.newDirectoryStream(tmp_dir)) {
                        for (Path file : ds) {
                            Files.delete(file);
                        }
                        System.out.println("delete tmp_dir" + tmp_dir.toString());
                        Files.delete(tmp_dir);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                    System.out.println("Shutdown-hook completed...");
                }
            });
            // simulate some I/O operations over the temporary file by sleeping
            // 10 seconds
            // when the time expires, the temporary file is deleted
            Thread.sleep(10000);
            // operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }

        /**
         * Deleting a Temporary Directory with the deleteOnExit() Method
         */
        try {
            // create a tmp directory in the base dir
            Path tmp_dir = Files.createTempDirectory(basedir, tmp_dir_prefix);
            File asFile = tmp_dir.toFile();
            asFile.deleteOnExit();
            // simulate some I/O operations over the temporary file by sleeping
            // 10 seconds
            // when the time expires, the temporary file is deleted
            // EACH CREATED TEMPORARY ENTRY SHOULD BE REGISTERED FOR DELETE ON
            // EXIT
            Thread.sleep(10000);
            // operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }

        /**
         * Creating Temporary Files
         */
        //createTempFile()
        //code snippet
        String tmp_file_prefix = "rafa_";
        String tmp_file_sufix=".txt";
        try {
            // passing null prefix/suffix
            Path tmp_1 = Files.createTempFile(null, null);
            System.out.println("TMP: " + tmp_1.toString());
            // set a prefix and a suffix
            Path tmp_2 = Files.createTempFile(tmp_file_prefix, tmp_file_sufix);
            System.out.println("TMP: " + tmp_2.toString());
        } catch (IOException e) {
            System.err.println(e);
        }
        //specify the default directory in which a temporary file is created
        try {
            Path tmp_3 = Files.createTempFile(basedir, tmp_file_prefix, tmp_file_sufix);
            System.out.println("TMP: " + tmp_3.toString());
        } catch (IOException e) {
            System.err.println(e);
        }
        //Deleting a Temporary File with Shutdown-Hook
        try {
            final Path tmp_file = Files.createTempFile(basedir,tmp_file_prefix, tmp_file_sufix);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Deleting the temporary file ...");
                    try {
                        Files.delete(tmp_file);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                    System.out.println("Shutdown hook completed...");
                }
            });
            // simulate some I/O operations over the temporary file by sleeping
            // 10 seconds
            // when the time expires, the temporary file is deleted
            Thread.sleep(10000);
            // operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        //Deleting a Temporary File with DELETE_ON_CLOSE
        Path tmp_file = null;
        try {
            tmp_file = Files.createTempFile(basedir, tmp_file_prefix,
                    tmp_file_sufix);
        } catch (IOException e) {
            System.err.println(e);
        }
        try (OutputStream outputStream = Files.newOutputStream(tmp_file,
                StandardOpenOption.DELETE_ON_CLOSE);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream))) {
            // simulate some I/O operations over the temporary file by sleeping
            // 10 seconds
            // when the time expires, the temporary file is deleted
            Thread.sleep(10000);
            // operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        //simulate
        /*tmp_file = FileSystems.getDefault().getPath("/home/andy/tmp/rafa_5898056209708961541.txt",
                tmp_file_prefix + "temporary" + tmp_file_sufix);*/
        tmp_file = FileSystems.getDefault().getPath("/home/andy/tmp/rafa_5898056209708961541.txt");
        try (OutputStream outputStream = Files.newOutputStream(tmp_file,
                StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream))) {
            // simulate some I/O operations over the temporary file by sleeping
            // 10 seconds
            // when the time expires, the temporary file is deleted
            Thread.sleep(10000);
            // operations done
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }

        /**
         * Deleting, Copying, and Moving Directories and Files
         */
        //Deleting Files and Directories
        //Files.delete() and Files.deleteIfExits().
        //Copying Files and Directories
        //Files.copy()
        //Copying Between Two Paths
        Path copy_from = Paths.get("C:/rafaelnadal/grandslam/AustralianOpen", "draw_template.txt");
        Path copy_to= Paths.get("C:/rafaelnadal/grandslam/USOpen",copy_from.getFileName().toString());
        try {
            Files.copy(copy_from, copy_to, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES,
                    LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            System.err.println(e);
        }
        //Copying from an Input Stream to a File
        //Copying from a File to an Output Stream
        //Moving Files and Directories
        //Files.move()
        Path movefrom = FileSystems.getDefault().getPath("C:/rafaelnadal/rafa_2.jpg");
        Path moveto = FileSystems.getDefault().getPath("C:/rafaelnadal/photos/rafa_2.jpg");
        try {
            Files.move(movefrom, moveto, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
        movefrom = FileSystems.getDefault().getPath("C:/rafaelnadal/rafa_2.jpg");
        Path moveto_dir = FileSystems.getDefault().getPath("C:/rafaelnadal/photos");
        try {
            Files.move(movefrom, moveto_dir.resolve(movefrom.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
        //Rename a File
        movefrom = FileSystems.getDefault().getPath("C:/rafaelnadal/photos/rafa_2.jpg");
        try {
            Files.move(movefrom, movefrom.resolveSibling("rafa_2_renamed.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }


    }


    @Test
    public void test() throws IOException {

    }



}
