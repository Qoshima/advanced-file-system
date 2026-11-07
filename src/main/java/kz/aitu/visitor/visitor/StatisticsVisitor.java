
package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.model.DirectoryNode;

public class StatisticsVisitor implements Visitor {

    private int fileCount;
    private int directoryCount;
    private int maxDepth;
    private int currentDepth;

    @Override
    public void setLocation(String path, int depth) {
        currentDepth = depth;
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        updateMaxDepth();
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        updateMaxDepth();
    }

    private void updateMaxDepth() {
        maxDepth = Math.max(maxDepth, currentDepth);
    }

    @Override
    public void reset() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
        currentDepth = 0;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
