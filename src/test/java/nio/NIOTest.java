/**
 *
 */
package nio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 14, 2014
 * @author andy
 */
public class NIOTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testPath() throws IOException {
        Path path = Paths.get("./src/test/java/nio/NIOTest.java");
        System.out.println(path.toString());
        System.out.println(path.toAbsolutePath());
        Path noNormalize = Paths.get("C:/rafaelnadal/tournaments/./2009/dummy/../BNP.txt");
        Path normalize = Paths.get("C:/rafaelnadal/tournaments/./2009/dummy/../BNP.txt").normalize();
        System.out.println(noNormalize);
        System.out.println(normalize);
        Path path1 = Paths.get(URI.create("file:///rafaelnadal/tournaments/2009/BNP.txt"));
        System.out.println(path1.toString());
        Path path2 = FileSystems.getDefault().getPath("/rafaelnadal/tournaments/2009/BNP.txt");
        System.out.println(path2.toString());
        Path path3 = Paths.get(System.getProperty("user.home"), "downloads", "game.exe");
        System.out.println(path3.getFileName());
        System.out.println(path1.getParent());
        System.out.println(path1.getNameCount());
        System.out.println(path3.toUri().toString());
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().normalize());
        System.out.println(path.toRealPath(LinkOption.NOFOLLOW_LINKS));


      //output: BNP.txt
        File path_to_file = path.toFile();
        //output: \rafaelnadal\tournaments\2009\BNP.txt
        Path file_to_path = path_to_file.toPath();
        System.out.println("Path to file name: " + path_to_file.getName());
        System.out.println("File to path: " + file_to_path.toString());


        Path base = Paths.get("C:/rafaelnadal/tournaments/2009");
          //resolve BNP.txt file
          Path path_1 = base.resolve("BNP.txt");
          //output: C:\rafaelnadal\tournaments\2009\BNP.txt
          System.out.println(path_1.toString());
          //resolve AEGON.txt file
          Path path_2 = base.resolve("AEGON.txt");
          //output: C:\rafaelnadal\tournaments\2009\AEGON.txt
          System.out.println(path_2.toString());

          Path path_3 = base.resolveSibling("AEGON33.jpg");
        //output: C:\rafaelnadal\tournaments\2009\AEGON.txt
        System.out.println(path_3.toString());

        Path path01 = Paths.get("/rafaelnadal/tournaments/2009/BNP.txt");
        Path path02 = Paths.get("/rafaelnadal/tournaments/2009/BNP.txt");
       /* if(path01.equals(path02)){
        System.out.println("The paths are equal!");
        } else {
        System.out.println("The paths are not equal!"); //true

        }*/


        boolean check = Files.isSameFile(path01, path02);
            System.out.println(check);
            if (check) {
                System.out.println("The paths locate the same file!"); // true
            } else {
                System.out.println("The paths does not locate the same file!");
            }



            FileSystem fs = FileSystems.getDefault();
            for (FileStore store : fs.getFileStores()) {
            boolean supported = store.supportsFileAttributeView(FileOwnerAttributeView.class);
            System.out.println(store.name() + " ---" + supported);
            }

    }


    @Test
    public void testView() {
        Path path = Paths.get("./src/test/java/nio/NIOTest.java");
        try {
        long size = (Long)Files.getAttribute(path, "basic:size", LinkOption.NOFOLLOW_LINKS);
        System.out.println("Size: " + size);
        } catch (IOException e) {
        System.err.println(e);
        }

        long time = System.currentTimeMillis();
        FileTime fileTime = FileTime.fromMillis(time);
        try {
        Files.getFileAttributeView(path,
        BasicFileAttributeView.class).setTimes(fileTime, fileTime, fileTime);
        } catch (IOException e) {
        System.err.println(e);
        }

        DosFileAttributes attr = null;
        try {
        attr = Files.readAttributes(path, DosFileAttributes.class);
        } catch (IOException e) {
        System.err.println(e);
        }
        System.out.println("Is read only ? " + attr.isReadOnly());
        System.out.println("Is Hidden ? " + attr.isHidden());
        System.out.println("Is archive ? " + attr.isArchive());
        System.out.println("Is system ? " + attr.isSystem());

//get owner 1
        FileOwnerAttributeView foav = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        try {
            String owner = foav.getOwner().getName();
            System.out.println(owner);
        } catch (IOException e) {
            System.err.println(e);
        }
//get owner 2
        try {
            UserPrincipal owner = (UserPrincipal) Files.getAttribute(path,
                    "owner:owner", LinkOption.NOFOLLOW_LINKS);
            System.out.println(owner.getName());
        } catch (IOException e) {
            System.err.println(e);
        }
// set owner 1
        UserPrincipal owner = null;
        try {
        owner = path.getFileSystem().getUserPrincipalLookupService().
        lookupPrincipalByName("andy");
        //Files.setOwner(path, owner);
        System.out.println(owner.getName());
        } catch (IOException e) {
        System.err.println(e);
        }
// set owner 2
        UserPrincipal owner1 = null;
        FileOwnerAttributeView foav1 = Files.getFileAttributeView(path,
        FileOwnerAttributeView.class);
        try {
        owner1 = path.getFileSystem().getUserPrincipalLookupService().
        lookupPrincipalByName("apress");
        foav1.setOwner(owner1);
        } catch (IOException e) {
        System.err.println(e);
        }

// set owner 3
        UserPrincipal owner2 = null;
        try {
        owner2 = path.getFileSystem().getUserPrincipalLookupService().
        lookupPrincipalByName("apress");
        Files.setAttribute(path, "owner:owner", owner2, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
        System.err.println(e);
        }


        PosixFileAttributes attr1 = null;
        try {
        attr1 = Files.readAttributes(path, PosixFileAttributes.class);
        } catch (IOException e) {
        System.err.println(e);
        }
        System.out.println("File owner: " + attr1.owner().getName());
        System.out.println("File group: " + attr1.group().getName());
        System.out.println("File permissions: " + attr1.permissions().toString());


        try {
            PosixFileAttributes attr2 = Files.getFileAttributeView(path,
            PosixFileAttributeView.class).readAttributes();
            } catch (IOException e) {
            System.err.println(e);
            }


         try {
             Set owne4 = (Set) Files.getAttribute(path,"posix:permissions", LinkOption.NOFOLLOW_LINKS);
             System.out.println("owner4 : " + owne4.size());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
