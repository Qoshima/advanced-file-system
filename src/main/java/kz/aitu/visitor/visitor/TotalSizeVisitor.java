
package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.model.DirectoryNode;

public class TotalSizeVisitor implements Visitor {

    private long totalSize = 0;

    @Override
    public void visit(FileNode file) {
        totalSize += file.getSize();
    }

    @Override
    public void visit(DirectoryNode directory) {
        // Directories do not contribute to file size
    }

    @Override
    public void reset() {
        totalSize = 0;
    }

    public long getTotalSize() {
        return totalSize;
    }
}
