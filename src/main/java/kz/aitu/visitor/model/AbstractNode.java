
package kz.aitu.visitor.model;

public abstract class AbstractNode implements Element {

    private DirectoryNode parent;

    final DirectoryNode getParent() {
        return parent;
    }

    final void attachTo(DirectoryNode newParent) {
        if (newParent == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }

        if (parent != null) {
            throw new IllegalArgumentException(
                    "Element already belongs to a directory"
            );
        }

        parent = newParent;
    }
}
