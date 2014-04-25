/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
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
    public void test() {
        fail("Not yet implemented");
    }

}
