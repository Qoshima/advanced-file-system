
package kz.aitu.visitor;

import kz.aitu.visitor.model.*;
import kz.aitu.visitor.traversal.TreeWalker;
import kz.aitu.visitor.visitor.*;

public class Main {

    public static void main(String[] args) {

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode documents = new DirectoryNode("documents");
        DirectoryNode photos = new DirectoryNode("photos");

        root.add(new FileNode("notes.txt", 200));
        root.add(new FileNode("report.txt", 500));

        documents.add(new FileNode("report.pdf", 1500));
        photos.add(new FileNode("image.png", 3000));

        documents.add(photos);
        root.add(documents);

        TreeWalker walker = new TreeWalker();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        walker.walk(root, sizeVisitor);

        System.out.println("Total size: "
                + sizeVisitor.getTotalSize() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor("report", 500);
        walker.walk(root, searchVisitor);

        System.out.println("Search results:");
        for (String path : searchVisitor.getMatches()) {
            System.out.println(" - " + path);
        }

        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        walker.walk(root, statsVisitor);

        System.out.println("Files: " + statsVisitor.getFileCount());
        System.out.println("Directories: " + statsVisitor.getDirectoryCount());
        System.out.println("Max depth: " + statsVisitor.getMaxDepth());
    }
}
