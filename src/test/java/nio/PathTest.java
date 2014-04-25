/**
 *
 */
package nio;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 25, 2014
 * @author andy
 */
public class PathTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testDefinePath() throws URISyntaxException{
        //basic code snippet
        /*FileSystem fileSystem = FileSystems.getDefault();
        fileSystem = FileSystems.getFileSystem(new URI(".."));*/
        Path path = Paths.get("..");

        /**Define an Absolute Path
         * (which contains the root directory and all other subdirectories that contains a file or folder.)
         **/
        path = Paths.get("/home/andy/work/test.py");
        /**get() also allows you to split a path into a set of chunks. NIO will reconstruct the path for you, no
        matter how many chunks there are. **/
        path = Paths.get("/home/andy","work","test.py");
        assertEquals("/home/andy/work/test.py", path.toString());



        /**Define a Path Relative to the File Store Root
         * A relative path is only a portion of the full path. A relative path is often used in creating a web page.
         *  Relative paths are used much more frequently than absolute paths.
         **/
        assertEquals("/home", Paths.get("/home").toString());

        /** Define a Path Relative to the Working Folder**/
        assertEquals("/home/andy/work/python", Paths.get(path.getParent().toString(),"python").toString());

        /** Define a Path Relative to the Working Folder
         *  Defining paths using the notation “.” (indicates the current directory) or “..” (indicates the parent
         *  directory) is a common practice.
         *  These kinds of paths can be processed by NIO.2 to eliminate possible
         *  cases of redundancy if you call the Path.normalize() method
         *  **/
        assertEquals("/home/andy/python", Paths.get("/home/andy/work/../python").normalize().toString());
        assertEquals("/home/andy/work/test.py", Paths.get("/home/andy/work/./test.py").normalize().toString());

        /**
         * Define a Path from a URI
         * In some cases, you may need to create a Path from a Uniform Resource Identifier (URI). You can do so by
         * using the URI.create() method to create a URI from a given string and by using the Paths.get() method
         * that takes a URI object as an argument.
         */
        path = Paths.get(URI.create("file:///home/andy/work/test.py"));
        assertEquals("/home/andy/work/test.py",path.toString());

        /**
         * Define a Path using FileSystems.getDefault().getPath() Method
         *
         */
        path = FileSystems.getDefault().getPath("/home/andy/python");
        assertEquals("/home/andy/python", path.toString());

        /**
         * Get the Path of the Home Directory
         */
        path = Paths.get(System.getProperty("user.home"), "work", "test.py");
        assertEquals("/home/andy/work/test.py",path.toString());
    }


    @Test
    public void testGetPathInfo(){
        Path path = Paths.get("/home/andy", "work","test.py");
        /**
         * Get the Path File/Directory Name
         * The file/directory indicated by a path is returned by the getFileName() method, which is the farthest
         * element from the root in the directory hierarchy:
         */
        assertEquals("test.py", path.getFileName().toString());

        /**
         * Get the Path Root
         */
        assertEquals("/",path.getRoot().toString());

        /**
         * Get the Path Parent
         */
        assertEquals("/home/andy/work", path.getParent().toString());

        /**
         * Get Path Name Elements
         */
        assertEquals(4, path.getNameCount());
        assertEquals("andy", path.getName(1).toString());

        /**
         * Get a Path Subpath
         */
        assertEquals("home/andy", path.subpath(0, 2).toString());
    }

    /**
     * how to convert a Path object into a string, a URI, an absolute path,
     *  a real path, and a File object.
     */
    @Test
    public void testConvertPath() {
        Path path = Paths.get("/home/andy", "work","test.py");

        /**
         *  Convert a Path to a String
         */
        String pathString = path.toString();
        assertEquals("/home/andy/work/test.py", pathString);

        /**
         * Convert a Path to a URI
         */
        URI path_to_uri = path.toUri();
        URI uri = null;
        try {
            uri = new URI("file:///home/andy/work/test.py");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(uri, path_to_uri);

        /**
         * Convert a Relative Path to an Absolute Path
         */
        Path path_to_absolute_path = path.toAbsolutePath();
        assertEquals("/home/andy/work/test.py", path_to_absolute_path.toString());

        /**
         * Convert a Path to a Real Path
         * The toRealPath() method returns the real path of an existing file—this means
         * that the file must exist
         *
         */
        try {
            Path real_path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
            assertEquals(path, real_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Convert a Path to a File
         */
        File file = new File("/home/andy/work/test.py");
        assertEquals(file, path.toFile());
    }

    /**
     * Combining Two Paths
     */
    @Test
    public void testCombinPath() {
        //define the fixed path
        Path base = Paths.get("/home/andy", "work");

        //resolve test.py file
        Path path_1 = base.resolve("test.py");
        assertEquals("/home/andy/work/test.py", path_1.toString());
        Path path_2 = base.resolve("AEGON.txt");
        assertEquals("/home/andy/work/AEGON.txt", path_2.toString());

        //define the fixed path
        base = Paths.get("/home/andy/work/test.py");
        //resolve sibling AEGON.txt file
        Path path = base.resolveSibling("AEGON.txt");
        assertEquals("/home/andy/work/AEGON.txt", path.toString());
    }

    /**
     * Constructing a Path Between Two Locations
     */
    @Test
    public void testPathTwoLocations() {
        Path path01 = Paths.get("BNP.txt");
        Path path02 = Paths.get("AEGON.txt");

        //output: ..\AEGON.txt
        Path path01_to_path02 = path01.relativize(path02);
        System.out.println(path01_to_path02);

        //output: ..\BNP.txt
        Path path02_to_path01 = path02.relativize(path01);
        System.out.println(path02_to_path01);

        path01 = Paths.get("/tournaments/2009/BNP.txt");
        path02 = Paths.get("/tournaments/2011");

      //output: ..\..\2011
        path01_to_path02 = path01.relativize(path02);
        System.out.println(path01_to_path02);
    }

    /**
     * Comparing Two Paths
     */
    @Test
    public void testComparePath() {
        Path path01 = Paths.get("C:/rafaelnadal/tournaments/2009/BNP.txt");
        Path path02 = Paths.get("C:/rafaelnadal/tournaments/2009/BNP.txt");
        if (path01.equals(path02)) {
            System.out.println("The paths are equal!");
        } else {
            System.out.println("The paths are not equal!"); // true
        }

        try {
            assertEquals(true,Files.isSameFile(path01, path02));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //output: 24
        int compare = path01.compareTo(path02);
        System.out.println(compare);

        boolean sw = path01.startsWith("/rafaelnadal/tournaments");
        boolean ew = path01.endsWith("BNP.txt");
        System.out.println(sw); //output: true
        System.out.println(ew); //output: true

    }

    /**
     * Iterate over the Name Elements of a Path
     *
     */
    @Test
    public void testIteratorElements() {
        Path path = Paths.get("C:", "rafaelnadal/tournaments/2009", "BNP.txt");
        for (Path name : path) {
            System.out.println(name);
        }

    }

    @Test
    public void test() {
    }

}
