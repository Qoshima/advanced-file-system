
package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.model.DirectoryNode;

public interface Visitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}