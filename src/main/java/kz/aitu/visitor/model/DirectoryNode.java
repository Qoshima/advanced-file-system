
package kz.aitu.visitor.model;

import kz.aitu.visitor.visitor.Visitor;
import java.util.ArrayList;
import java.util.List;

public class DirectoryNode extends AbstractNode {

    private final String name;
    private final List<Element> children = new ArrayList<>();

    public DirectoryNode(String name) {
        if (name == null || name.isBlank() || name.contains("/")) {
            throw new IllegalArgumentException("Invalid directory name");
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    public void add(AbstractNode child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }

        if (child.getParent() != null) {
            throw new IllegalArgumentException(
                    "Element already has a parent"
            );
        }

        for (DirectoryNode current = this;
             current != null;
             current = current.getParent()) {

            if (current == child) {
                throw new IllegalArgumentException(
                        "Cycle detected"
                );
            }
        }

        for (Element existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException(
                        "Duplicate name"
                );
            }
        }

        child.attachTo(this);
        children.add(child);
    }

    public List<Element> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
