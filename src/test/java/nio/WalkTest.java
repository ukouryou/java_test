/**
 *
 */
package nio;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Notes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.record.TextHeaderAtom;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * Apr 29, 2014
 * @author andy
 */
public class WalkTest {

    static String words="Rafael Nadal,tennis,winner of Roland Garros,BNP Paribas tournament draws";
    static ArrayList<String> wordsarray = new ArrayList<>();

//refer to SearchFileVisitor.java,DeleteDirectory.java,CopyTree.java,MoveTree.java

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        StringTokenizer st = new StringTokenizer(words, ",");
        while (st.hasMoreTokens()) {
            wordsarray.add(st.nextToken());
        }
    }

    /**
    * Calculate the factorial of n (n! = 1 * 2 * 3 * ... * n).
    *
    * @param n the number to calculate the factorial of.
    * @return n! - the factorial of n.
    */
    static int fact(int n) {
        // Base Case:
        // If n <= 1 then n! = 1.
        if (n <= 1) {
            return 1;
        }
        // Recursive Case:
        // If n > 1 then n! = n * (n-1)!
        else {
            return n * fact(n - 1);
        }
    }

    /**
     * method List
     */
    //FileVisitor.visitFile()
    //FileVisitor.preVisitDirectory()
    //FileVisitor.postVisitDirectory()

    /**
     * Starting the Recursive Process
     */
    @Test
    public void testSRP() {
        Path listDir = Paths.get("/home/andy/tmp"); //define the starting file tree
        ListTree walk = new ListTree();
        //instantiate the walk
        try{
        Files.walkFileTree(listDir, walk);
        } catch(IOException e){
        System.err.println(e);
        }
    }

    /**
     * common walks
     */
    /**
     * Searching for Files by Name
     * @throws IOException
     */
    @Test
    public void testSFN() throws IOException{
        Path searchFile = Paths.get("test.py");
        Search walk = new Search(searchFile);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        for (Path root : dirs) {
            if (!walk.found) {
                Files.walkFileTree(root, opts, Integer.MAX_VALUE, walk);
            }
        }
        if (!walk.found) {
            System.out.println("The file " + searchFile + " was not found!");
        }
    }


    /**
     * Searching for Files by Glob Pattern
     * @throws IOException
     */
    @Test
    public void testSFGP() throws IOException{
        String glob = "*.jpg";
        Path fileTree = Paths.get("/home/andy/Downloads");
        SearchGP walk = new SearchGP(glob);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
    }

    /**
     * Searching for Files by Glob Pattern
     * @throws IOException
     */
    @Test
    public void testSFC() throws IOException{
        String glob = "*.jpg";
        long size = 102400; //100 kilobytes in bytes
        Path fileTree = Paths.get("/home/andy/Downloads");
        SearchComplex walk = new SearchComplex(glob, size);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    // search in PDF files using iText library
    boolean searchInPDF_iText(String file) {
        PdfReader reader = null;
        boolean flag = false;
        try {
            reader = new PdfReader(file);
            int n = reader.getNumberOfPages();
            OUTERMOST: for (int i = 1; i <= n; i++) {
                String str = PdfTextExtractor.getTextFromPage(reader, i);
                flag = searchText(str);
                if (flag) {
                    break OUTERMOST;
                }
            }
        } catch (Exception e) {
        } finally {
            if (reader != null) {
                reader.close();
            }
            return flag;
        }
    }


    // search text
    private boolean searchText(String text) {
        boolean flag = false;
        for (int j = 0; j < wordsarray.size(); j++) {
            if ((text.toLowerCase()).contains(wordsarray.get(j).toLowerCase())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //PDFBox
    boolean searchInPDF_PDFBox(String file) {
        PDFParser parser = null;
        String parsedText = null;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        boolean flag = false;
        int page = 0;
        File pdf = new File(file);
        try {
            parser = new PDFParser(new FileInputStream(pdf));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            OUTERMOST: while (page < pdDoc.getNumberOfPages()) {
                page++;
                pdfStripper.setStartPage(page);
                pdfStripper.setEndPage(page + 1);
                parsedText = pdfStripper.getText(pdDoc);
                flag = searchText(parsedText);
                if (flag) {
                    break OUTERMOST;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                if (pdDoc != null) {
                    pdDoc.close();
                }
            } catch (Exception e) {
            }
            return flag;
        }
    }

    /**
     * POI operations
     */
    /**
     * POI Word
     * @param file
     * @return
     */
    boolean searchInWord(String file) {
        POIFSFileSystem fs = null;
        boolean flag = false;
        try {
            fs = new POIFSFileSystem(new FileInputStream(file));
            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            OUTERMOST: for (int i = 0; i < paragraphs.length; i++) {
                flag = searchText(paragraphs[i]);
                if (flag) {
                    break OUTERMOST;
                }
            }
        } catch (Exception e) {
        } finally {
            return flag;
        }
    }

    /**
     * POI Excel
     * @param file
     * @return
     */
    boolean searchInExcel(String file) {
        Row row;
        Cell cell;
        String text;
        boolean flag = false;
        InputStream xls = null;
        try {
            xls = new FileInputStream(file);
            HSSFWorkbook wb = new HSSFWorkbook(xls);
            int sheets = wb.getNumberOfSheets();
            OUTERMOST: for (int i = 0; i < sheets; i++) {
                HSSFSheet sheet = wb.getSheetAt(i);
                Iterator<Row> row_iterator = sheet.rowIterator();
                while (row_iterator.hasNext()) {
                    row = (Row) row_iterator.next();
                    Iterator<Cell> cell_iterator = row.cellIterator();
                    while (cell_iterator.hasNext()) {
                        cell = cell_iterator.next();
                        int type = cell.getCellType();
                        if (type == HSSFCell.CELL_TYPE_STRING) {
                            text = cell.getStringCellValue();
                            flag = searchText(text);
                            if (flag) {
                                break OUTERMOST;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (xls != null) {
                    xls.close();
                }
            } catch (IOException e) {
            }
            return flag;
        }
    }

    /**
     * POI PowerPoint
     */
    boolean searchInPPT(String file) {
        boolean flag = false;
        InputStream fis = null;
        String text;
        try {
            fis = new FileInputStream(new File(file));
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSLFSlideShow show = new HSLFSlideShow(fs);
            SlideShow ss = new SlideShow(show);
            Slide[] slides = ss.getSlides();
            OUTERMOST: for (int i = 0; i < slides.length; i++) {
                TextRun[] runs = slides[i].getTextRuns();
                for (int j = 0; j < runs.length; j++) {
                    TextRun run = runs[j];
                    if (run.getRunType() == TextHeaderAtom.TITLE_TYPE) {
                        text = run.getText();
                    } else {
                        text = run.getRunType() + " " + run.getText();
                    }
                    flag = searchText(text);
                    if (flag) {
                        break OUTERMOST;
                    }
                }
                Notes notes = slides[i].getNotesSheet();
                if (notes != null) {
                    runs = notes.getTextRuns();
                    for (int j = 0; j < runs.length; j++) {
                        text = runs[j].getText();
                        flag = searchText(text);
                        if (flag) {
                            break OUTERMOST;
                        }
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
            return flag;
        }
    }

    /**
     * Searching in Text Files(.txt, .html, .xml, etc.)
     */
    boolean searchInText(Path file) {
        boolean flag = false;
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            OUTERMOST: while ((line = reader.readLine()) != null) {
                flag = searchText(line);
                if (flag) {
                    break OUTERMOST;
                }
            }
        } catch (IOException e) {
        } finally {
            return flag;
        }
    }
    /**
     *
     * Apr 29, 2014
     * @author andy
     */


    class ListTree extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            System.out.println("Visited directory: " + dir.toString());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.out.println(exc);
            return FileVisitResult.CONTINUE;
        }
    }

    class Search implements FileVisitor {
        private final Path searchedFile;
        public boolean found;

        public Search(Path searchedFile) {
            this.searchedFile = searchedFile;
            this.found = false;
        }

        void search(Path file) throws IOException {
            Path name = file.getFileName();
            if (name != null && name.equals(searchedFile)) {
                System.out.println("Searched file was found: " + searchedFile
                        + " in " + file.toRealPath().toString());
                found = true;
            }
        }

        @Override
        public FileVisitResult postVisitDirectory(Object dir, IOException exc)
                throws IOException {
            System.out.println("Visited: " + (Path) dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Object dir,
                BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
                throws IOException {
            search((Path) file);
            if (!found) {
                return FileVisitResult.CONTINUE;
            } else {
                return FileVisitResult.TERMINATE;
            }
        }

        @Override
        public FileVisitResult visitFileFailed(Object file, IOException exc)
                throws IOException {
            // report an error if necessary
            return FileVisitResult.CONTINUE;
        }
    }

    class SearchGP implements FileVisitor {
        private final PathMatcher matcher;

        public SearchGP(String glob) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        }

        void search(Path file) throws IOException {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                System.out.println("Searched file was found: " + name + " in "
                        + file.toRealPath().toString());
            }
        }

        @Override
        public FileVisitResult postVisitDirectory(Object dir, IOException exc)
                throws IOException {
            System.out.println("Visited: " + (Path) dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Object dir,
                BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
                throws IOException {
            search((Path) file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Object file, IOException exc)
                throws IOException {
            // report an error if necessary
            return FileVisitResult.CONTINUE;
        }
    }

    class SearchComplex implements FileVisitor {
        private final PathMatcher matcher;
        private final long accepted_size;

        public SearchComplex(String glob, long accepted_size) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
            this.accepted_size = accepted_size;
        }

        void search(Path file) throws IOException {
            Path name = file.getFileName();
            long size = (Long) Files.getAttribute(file, "basic:size");
            if (name != null && matcher.matches(name) && size <= accepted_size) {
                System.out.println("Searched file was found: " + name + " in "
                        + file.toRealPath().toString() + " size (bytes):"
                        + size);
            }
        }

        @Override
        public FileVisitResult postVisitDirectory(Object dir, IOException exc)
                throws IOException {
            System.out.println("Visited: " + (Path) dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Object dir,
                BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
                throws IOException {
            search((Path) file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Object file, IOException exc)
                throws IOException {
            // report an error if necessary
            return FileVisitResult.CONTINUE;
        }
    }


}
