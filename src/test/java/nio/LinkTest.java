/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 28, 2014
 * @author andy
 */
public class LinkTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Creating a Symbolic Link
     */
    @Test
    public void testCreateSLink() {
        Path link = FileSystems.getDefault().getPath("test.py1");
        Path target = FileSystems.getDefault().getPath("/home/andy/work","test.py");
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException | UnsupportedOperationException
                | SecurityException e) {
            if (e instanceof SecurityException) {
                System.err.println("Permission denied!");
            }
            if (e instanceof UnsupportedOperationException) {
                System.err.println("An unsupported operation was detected!");
            }
            if (e instanceof IOException) {
                System.err.println("An I/O error occurred!");
            }
            System.err.println(e);
        }

        Path link1 = FileSystems.getDefault().getPath("test.py2");
        Path target1 = FileSystems.getDefault().getPath("/home/andy/work","test.py");
        try {
            PosixFileAttributes attrs = Files.readAttributes(target1,PosixFileAttributes.class);
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(attrs.permissions());
            Files.createSymbolicLink(link1, target1, attr);
        } catch (IOException | UnsupportedOperationException| SecurityException e) {
            if (e instanceof SecurityException) {
                System.err.println("Permission denied!");
            }
            if (e instanceof UnsupportedOperationException) {
                System.err.println("An unsupported operation was detected!");
            }
            if (e instanceof IOException) {
                System.err.println("An I/O error occured!");
            }
            System.err.println(e);
        }

        Path link2 = FileSystems.getDefault().getPath("test.py3");
        Path target2 = FileSystems.getDefault().getPath("/home/andy/work","test.py");
        try {
            Files.createSymbolicLink(link2, target2);
            FileTime lm = (FileTime) Files.getAttribute(target2,"basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS);
            FileTime la = (FileTime) Files.getAttribute(target2,"basic:lastAccessTime", LinkOption.NOFOLLOW_LINKS);
            Files.setAttribute(link2, "basic:lastModifiedTime", lm,LinkOption.NOFOLLOW_LINKS);
            Files.setAttribute(link2, "basic:lastAccessTime", la, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException | UnsupportedOperationException
                | SecurityException e) {
            if (e instanceof SecurityException) {
                System.err.println("Permision denied!");
            }
            if (e instanceof UnsupportedOperationException) {
                System.err.println("An unsupported operation was detected!");
            }
            if (e instanceof IOException) {
                System.err.println("An I/O error occured!");
            }
            System.err.println(e);
        }


    }

    /**
     * Creating a Hard Link
     */
    @Test
    public void testCreateHLink() {
        Path link = FileSystems.getDefault().getPath("test.py1");
        Path target = FileSystems.getDefault().getPath("/home/andy/work","test.py");
        try {
            Files.createLink(link, target);
            System.out.println("The link was successfully created!");
        } catch (IOException | UnsupportedOperationException | SecurityException e) {
            if (e instanceof SecurityException) {
                System.err.println("Permission denied!");
            }
            if (e instanceof UnsupportedOperationException) {
                System.err.println("An unsupported operation was detected!");
            }
            if (e instanceof IOException) {
                System.err.println("An I/O error occured!");
            }
            System.err.println(e);
        }


    }

    /**
     * Checking a Symbolic Link
     */
    @Test
    public void testCheckSLink	() {
         Path link = FileSystems.getDefault().getPath("test.py1");
         Path target = FileSystems.getDefault().getPath("/home/andy/work","test.py");
         try {
             Files.createSymbolicLink(link, target);
         } catch (IOException | UnsupportedOperationException
                 | SecurityException e) {
             if (e instanceof SecurityException) {
                 System.err.println("Permission denied!");
             }
             if (e instanceof UnsupportedOperationException) {
                 System.err.println("An unsupported operation was detected!");
             }
             if (e instanceof IOException) {
                 System.err.println("An I/O error occurred!");
             }
             System.err.println(e);
         }
         //check if a path is a symbolic link - solution 1
         boolean link_isSymbolicLink_1 = Files.isSymbolicLink(link);
         boolean target_isSymbolicLink_1 = Files.isSymbolicLink(target);
         System.out.println(link.toString() + " is a symbolic link ? " + link_isSymbolicLink_1);
         System.out.println(target.toString() + " is a symbolic link ? " + target_isSymbolicLink_1);

        try {
            boolean link_isSymbolicLink_2 = (boolean) Files.getAttribute(link,"basic:isSymbolicLink");
            boolean target_isSymbolicLink_2 = (boolean) Files.getAttribute(target, "basic:isSymbolicLink");
            System.out.println(link.toString() + " is a symbolic link ? "+ link_isSymbolicLink_2);
            System.out.println(target.toString() + " is a symbolic link ? " + target_isSymbolicLink_2);
        } catch (IOException | UnsupportedOperationException e) {
            System.err.println(e);
        }


    }

    /**
     * Locating the Target of a Link
     */
    @Test
    public void testLocateLinkT() {
        Path link = FileSystems.getDefault().getPath("test.py1");
        Path target = FileSystems.getDefault().getPath("/home/andy/work","test.py");
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException | UnsupportedOperationException
                | SecurityException e) {
            if (e instanceof SecurityException) {
                System.err.println("Permission denied!");
            }
            if (e instanceof UnsupportedOperationException) {
                System.err.println("An unsupported operation was detected!");
            }
            if (e instanceof IOException) {
                System.err.println("An I/O error occurred!");
            }
            System.err.println(e);
        }

        try {
            Path linkedpath = Files.readSymbolicLink(link);
            System.out.println(linkedpath.toString());
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * Checking If a Link and a Target Point to the Same File
         */

    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
