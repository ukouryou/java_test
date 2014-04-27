/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Apr 25, 2014
 * @author andy
 */
public class FileAttributeViewTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Determining Views Supported by a Particular File System
     */
    @Test
    public void testSupportedViews(){
        FileSystem fs = FileSystems.getDefault();
        Set<String> views = fs.supportedFileAttributeViews();
        for (String view : views) {
            System.out.println(view);
        }

        for (FileStore store : fs.getFileStores()) {
//            boolean supported = store.supportsFileAttributeView(BasicFileAttributeView.class);
            boolean supported = store.supportsFileAttributeView(DosFileAttributeView.class);
            System.out.println(store.name() + " ---" + supported);
        }

    }
    
    @Test
    public void testBasicView() {
    	/**
    	 * Get Bulk Attributes with readAttributes()
    	 */
    	BasicFileAttributes attr = null;
    	Path path = Paths.get("/home/andy/workspace/java_test/src/test/java/nio", "FileAttributeViewTest.java");
    	try {
    	attr = Files.readAttributes(path, BasicFileAttributes.class);
    	} catch (IOException e) {
    	System.err.println(e);
    	}
    	System.out.println("File size: " + attr.size());
    	System.out.println("File creation time: " + attr.creationTime());
    	System.out.println("File was last accessed at: " + attr.lastAccessTime());
    	System.out.println("File was last modified at: " + attr.lastModifiedTime());
    	System.out.println("Is directory? " + attr.isDirectory());
    	System.out.println("Is regular file? " + attr.isRegularFile());
    	System.out.println("Is symbolic link? " + attr.isSymbolicLink());
    	System.out.println("Is other? " + attr.isOther());
    	
    	/**
    	 * Get a Single Attribute with getAttribute()
    	 */
		try {
			long size = (Long) Files.getAttribute(path, "basic:size",LinkOption.NOFOLLOW_LINKS);
			System.out.println("Size: " + size);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * Update a Basic Attribute
		 */
		long time = System.currentTimeMillis();
		FileTime fileTime = FileTime.fromMillis(time);
		try {
			//method1
			Files.getFileAttributeView(path, BasicFileAttributeView.class)
					.setTimes(fileTime, fileTime, fileTime);
			//method2
			Files.setLastModifiedTime(path, fileTime);
			
			//method3
			Files.setAttribute(path, "basic:lastModifiedTime", fileTime, LinkOption.NOFOLLOW_LINKS);
			Files.setAttribute(path, "basic:creationTime", fileTime, LinkOption.NOFOLLOW_LINKS);
			Files.setAttribute(path, "basic:lastAccessTime", fileTime, LinkOption.NOFOLLOW_LINKS);
			
			//get the times
			FileTime lastModifiedTime = (FileTime)Files.getAttribute(path,"basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS);
			FileTime creationTime = (FileTime)Files.getAttribute(path,"basic:creationTime", LinkOption.NOFOLLOW_LINKS);
			FileTime lastAccessTime = (FileTime)Files.getAttribute(path,"basic:lastAccessTime", LinkOption.NOFOLLOW_LINKS);
			System.out.println("New last modified time: " + lastModifiedTime);
			System.out.println("New creation time: " + creationTime);
			System.out.println("New last access time: " + lastAccessTime);
		} catch (IOException e) {
			System.err.println(e);
		}

    }
    
    @Test
    public void testDoscView() {
    	DosFileAttributes attr = null;
    	Path path = Paths.get("/home/andy/workspace/java_test/src/test/java/nio", "FileAttributeViewTest.java");
    	try {
    	attr = Files.readAttributes(path, DosFileAttributes.class);
    	} catch (IOException e) {
    	System.err.println(e);
    	}
    	System.out.println("Is read only ? " + attr.isReadOnly());
    	System.out.println("Is Hidden ? " + attr.isHidden());
    	System.out.println("Is archive ? " + attr.isArchive());
    	System.out.println("Is system ? " + attr.isSystem());
    	
    	//setting the hidden attribute to true
    	try {
    	Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
    	} catch (IOException e) {
    	System.err.println(e);
    	}
    	//getting the hidden attribute
		try {
			boolean hidden = (Boolean) Files.getAttribute(path, "dos:hidden",LinkOption.NOFOLLOW_LINKS);
			System.out.println("Is hidden ? " + hidden);
		} catch (IOException e) {
			System.err.println(e);
		}
    	
    	
    }
    
    @Test
    public void testOwnerView() {
    	
    	/**
    	 * Set a File Owner Using Files.setOwner()
    	 */
    	UserPrincipal owner = null;
    	Path path = Paths.get("/home/andy/workspace/java_test/src/test/java/nio", "FileAttributeViewTest.java");
		try {
			owner = path.getFileSystem().getUserPrincipalLookupService()
					.lookupPrincipalByName("andy");
			System.out.println(owner.getName());
			Files.setOwner(path, owner);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * Set a File Owner Using FileOwnerAttributeView.setOwner()
		 */
		FileOwnerAttributeView foav = Files.getFileAttributeView(path,FileOwnerAttributeView.class);
		try {
			owner = path.getFileSystem().getUserPrincipalLookupService()
					.lookupPrincipalByName("andy");
			foav.setOwner(owner);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * Set a File Owner Using Files.setAttribute()
		 */
		try {
			owner = path.getFileSystem().getUserPrincipalLookupService()
					.lookupPrincipalByName("andy");
			Files.setAttribute(path, "owner:owner", owner, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * Get a File Owner Using FileOwnerAttributeView.getOwner()
		 */
		foav = Files.getFileAttributeView(path,
				FileOwnerAttributeView.class);
		try {
			String ownerN = foav.getOwner().getName();
			System.out.println(ownerN);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * Get a File Owner Using Files.getAttribute()
		 */
		try {
			owner = (UserPrincipal) Files.getAttribute(path, "owner:owner",
					LinkOption.NOFOLLOW_LINKS);
			System.out.println(owner.getName());
		} catch (IOException e) {
			System.err.println(e);
		}
		
		
    }
    
    /**
     * POSIX View
     */
    @Test
    public void testPosixView() {
		PosixFileAttributes attr = null;
		Path path = Paths.get("/home/andy/workspace/java_test/src/test/java/nio/FileAttributeViewTest.java");
		try {
			attr = Files.readAttributes(path, PosixFileAttributes.class);
			attr = Files.getFileAttributeView(path,
					PosixFileAttributeView.class).readAttributes();
		} catch (IOException e) {
			System.err.println(e);
		}
		System.out.println("File owner: " + attr.owner().getName());
		System.out.println("File group: " + attr.group().getName());
		System.out.println("File permissions: " + attr.permissions().toString());
		
		
		/**
		 * POSIX Permissions
		 */
		//method1
		Path new_path = Paths.get("/home/rafaelnadal/tournaments/2009/new_BNP.txt");
		FileAttribute<Set<PosixFilePermission>> posixattrs = PosixFilePermissions
				.asFileAttribute(attr.permissions());
		try {
			Files.createFile(new_path, posixattrs);
		} catch (IOException e) {
			System.err.println(e);
		}
		//method2
		Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
		try {
		Files.setPosixFilePermissions(new_path, permissions);
		} catch (IOException e) {
		System.err.println(e);
		}
		
		
		/**
		 * POSIX Group Owner
		 */
		try {
			GroupPrincipal group = path.getFileSystem()
					.getUserPrincipalLookupService()
					.lookupPrincipalByGroupName("andy");
			Files.getFileAttributeView(path, PosixFileAttributeView.class)
					.setGroup(group);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/**
		 * get group name
		 */
//		GroupPrincipal group = (GroupPrincipal) Files.getAttribute(path, "posix:group",LinkOption.NOFOLLOW_LINKS);
//		System.out.println(group.getName());
    	
    }

    
    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
