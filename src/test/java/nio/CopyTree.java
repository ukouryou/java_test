/**
 * Writing a Copy Files Application
 */
package nio;

import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

/**
 * Apr 29, 2014
 *
 * @author andy
 */
public class CopyTree implements FileVisitor {
    private final Path copyFrom;
    private final Path copyTo;

    public CopyTree(Path copyFrom, Path copyTo) {
        this.copyFrom = copyFrom;
        this.copyTo = copyTo;
    }

    static void copySubTree(Path copyFrom, Path copyTo) throws IOException {
        try {
            Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.err.println("Unable to copy " + copyFrom + " [" + e + "]");
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc)
            throws IOException {
        if (exc == null) {
            Path newdir = copyTo.resolve(copyFrom.relativize((Path) dir));
            try {
                FileTime time = Files.getLastModifiedTime((Path) dir);
                Files.setLastModifiedTime(newdir, time);
            } catch (IOException e) {
                System.err.println("Unable to copy all attributes to: "
                        + newdir + " [" + e + "]");
            }
        } else {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir,
            BasicFileAttributes attrs) throws IOException {
        System.out.println("Copy directory: " + (Path) dir);
        Path newdir = copyTo.resolve(copyFrom.relativize((Path) dir));
        try {
            Files.copy((Path) dir, newdir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.err.println("Unable to create " + newdir + " [" + e + "]");
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
            throws IOException {
        System.out.println("Copy file: " + (Path) file);
        copySubTree((Path) file,
                copyTo.resolve(copyFrom.relativize((Path) file)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc)
            throws IOException {
        if (exc instanceof FileSystemLoopException) {
            System.err.println("Cycle was detected: " + (Path) file);
        } else {
            System.err.println("Error occurred, unable to copy:" + (Path) file
                    + " [" + exc + "]");
        }
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path copyFrom = Paths.get("C:/rafaelnadal");
        Path copyTo = Paths.get("C:/rafaelnadal_copy");
        CopyTree walk = new CopyTree(copyFrom, copyTo);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(copyFrom, opts, Integer.MAX_VALUE, walk);
    }

}
