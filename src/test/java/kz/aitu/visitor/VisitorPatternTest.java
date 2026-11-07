package kz.aitu.visitor;

import kz.aitu.visitor.model.*;
import kz.aitu.visitor.traversal.TreeWalker;
import kz.aitu.visitor.visitor.TotalSizeVisitor;

import kz.aitu.visitor.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;

import kz.aitu.visitor.visitor.SearchVisitor;

import kz.aitu.visitor.visitor.StatisticsVisitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VisitorPatternTest {

    @Test
    void testTotalSize() {
        DirectoryNode root = new DirectoryNode("root");

        root.add(new FileNode("first.txt", 100));
        root.add(new FileNode("second.txt", 200));

        TotalSizeVisitor visitor = new TotalSizeVisitor();

        new TreeWalker().walk(root, visitor);

        assertEquals(300, visitor.getTotalSize());
    }

    @Test
    void testDoubleDispatch() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("hello.txt", 100));

        List<String> visited = new ArrayList<>();

        Visitor visitor = new Visitor() {
            @Override
            public void visit(FileNode file) {
                visited.add("File: " + file.getName());
            }

            @Override
            public void visit(DirectoryNode directory) {
                visited.add("Directory: " + directory.getName());
            }
        };

        new TreeWalker().walk(root, visitor);

        assertEquals(
                List.of("Directory: root", "File: hello.txt"),
                visited
        );
    }

    @Test
    void testNestedTotalSize() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode archive = new DirectoryNode("archive");
        DirectoryNode empty = new DirectoryNode("empty");

        root.add(new FileNode("notes.txt", 100));
        documents.add(new FileNode("report.pdf", 200));
        archive.add(new FileNode("backup.zip", 300));

        documents.add(archive);
        root.add(documents);
        root.add(empty);

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        new TreeWalker().walk(root, visitor);

        assertEquals(600, visitor.getTotalSize());
    }


    @Test
    void testSearchFilters() {
        DirectoryNode root = new DirectoryNode("root");

        root.add(new FileNode("report-small.txt", 100));
        root.add(new FileNode("report-exact.txt", 500));
        root.add(new FileNode("report-large.pdf", 1500));
        root.add(new FileNode("notes.txt", 2000));

        SearchVisitor visitor = new SearchVisitor("report", 500);

        new TreeWalker().walk(root, visitor);

        assertEquals(
                List.of("report-exact.txt", "report-large.pdf"),
                visitor.getMatches()
        );
    }

    @Test
    void testSearchPathsAndSorting() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode folderZ = new DirectoryNode("z-folder");
        DirectoryNode folderA = new DirectoryNode("a-folder");
        DirectoryNode deep = new DirectoryNode("deep");

        // Add files in unsorted order
        folderZ.add(new FileNode("log-z.txt", 100));
        deep.add(new FileNode("log-deep.txt", 200));
        folderA.add(new FileNode("log-a.txt", 300));

        folderA.add(deep);

        root.add(folderZ);
        root.add(new FileNode("log-root.txt", 400));
        root.add(folderA);

        SearchVisitor visitor = new SearchVisitor("log", 0);
        new TreeWalker().walk(root, visitor);

        assertEquals(
                List.of(
                        "a-folder/deep/log-deep.txt",
                        "a-folder/log-a.txt",
                        "log-root.txt",
                        "z-folder/log-z.txt"
                ),
                visitor.getMatches()
        );
    }

    @Test
    void testStatistics() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode archive = new DirectoryNode("archive");
        DirectoryNode empty = new DirectoryNode("empty");

        root.add(new FileNode("notes.txt", 100));

        documents.add(new FileNode("report.pdf", 500));
        archive.add(new FileNode("backup.zip", 1000));

        documents.add(archive);
        root.add(documents);
        root.add(empty);

        StatisticsVisitor visitor = new StatisticsVisitor();

        new TreeWalker().walk(root, visitor);

        assertEquals(3, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
    }

    @Test
    void testEmptyStructuresAndNoMatches() {
        DirectoryNode root = new DirectoryNode("root");
        TreeWalker walker = new TreeWalker();

        // Test empty directory
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        StatisticsVisitor statsVisitor = new StatisticsVisitor();

        walker.walk(root, sizeVisitor);
        walker.walk(root, statsVisitor);

        assertEquals(0, sizeVisitor.getTotalSize());
        assertEquals(0, statsVisitor.getFileCount());
        assertEquals(1, statsVisitor.getDirectoryCount());
        assertEquals(0, statsVisitor.getMaxDepth());

        // Test search with no matching files
        root.add(new FileNode("notes.txt", 200));

        SearchVisitor searchVisitor = new SearchVisitor("report", 500);
        walker.walk(root, searchVisitor);

        assertTrue(searchVisitor.getMatches().isEmpty());
    }

    @Test
    void testRepeatedExecutions() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("first.txt", 100));
        root.add(new FileNode("second.txt", 200));

        TreeWalker walker = new TreeWalker();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        SearchVisitor searchVisitor = new SearchVisitor(".txt", 0);
        StatisticsVisitor statsVisitor = new StatisticsVisitor();

        // First execution
        walker.walk(root, sizeVisitor);
        walker.walk(root, searchVisitor);
        walker.walk(root, statsVisitor);

        assertEquals(300, sizeVisitor.getTotalSize());
        assertEquals(2, searchVisitor.getMatches().size());
        assertEquals(2, statsVisitor.getFileCount());

        // Second execution with the same visitors
        walker.walk(root, sizeVisitor);
        walker.walk(root, searchVisitor);
        walker.walk(root, statsVisitor);

        assertEquals(300, sizeVisitor.getTotalSize());
        assertEquals(2, searchVisitor.getMatches().size());
        assertEquals(2, statsVisitor.getFileCount());
        assertEquals(1, statsVisitor.getDirectoryCount());
    }

    @Test
    void testTreeIntegrity() {

        // 1. Negative file size
        assertThrows(IllegalArgumentException.class,
                () -> new FileNode("invalid.txt", -100));

        DirectoryNode root = new DirectoryNode("root");
        FileNode file = new FileNode("notes.txt", 100);

        root.add(file);

        // 2. Duplicate sibling names
        assertThrows(IllegalArgumentException.class,
                () -> root.add(new FileNode("notes.txt", 200)));

        // 3. Multiple parent ownership
        DirectoryNode anotherRoot = new DirectoryNode("another");

        assertThrows(IllegalArgumentException.class,
                () -> anotherRoot.add(file));

        // 4. Cycle prevention
        DirectoryNode folder = new DirectoryNode("folder");
        root.add(folder);

        assertThrows(IllegalArgumentException.class,
                () -> folder.add(root));

        assertThrows(IllegalArgumentException.class,
                () -> folder.add(folder));

        // 5. Valid nesting still works
        folder.add(new FileNode("inside.txt", 300));

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        TreeWalker walker = new TreeWalker();

        walker.walk(root, sizeVisitor);
        walker.walk(root, statsVisitor);

        assertEquals(400, sizeVisitor.getTotalSize());
        assertEquals(2, statsVisitor.getFileCount());
        assertEquals(2, statsVisitor.getDirectoryCount());
        assertEquals(2, statsVisitor.getMaxDepth());
    }
}
