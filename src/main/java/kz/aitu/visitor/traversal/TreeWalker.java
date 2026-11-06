
package kz.aitu.visitor.traversal;

import kz.aitu.visitor.model.*;
import kz.aitu.visitor.visitor.Visitor;

import java.util.Objects;

public class TreeWalker {

    public void walk(Element root, Visitor visitor) {
        Objects.requireNonNull(root, "Root cannot be null");
        Objects.requireNonNull(visitor, "Visitor cannot be null");

        visitor.reset();
        new Session(visitor).traverse(root, "", 0);
    }

    private static class Session implements Visitor {

        private final Visitor visitor;
        private String path;
        private int depth;

        public Session(Visitor visitor) {
            this.visitor = visitor;
        }

        public void traverse(Element element, String path, int depth) {
            this.path = path;
            this.depth = depth;
            element.accept(this);
        }

        @Override
        public void visit(FileNode file) {
            String filePath = path.isEmpty() ? file.getName() : path;

            visitor.setLocation(filePath, depth);
            visitor.visit(file);
        }

        @Override
        public void visit(DirectoryNode directory) {
            String currentPath = path;
            int currentDepth = depth;

            visitor.setLocation(currentPath, currentDepth);
            visitor.visit(directory);

            for (Element child : directory.getChildren()) {
                String childPath = currentPath.isEmpty()
                        ? child.getName()
                        : currentPath + "/" + child.getName();

                traverse(child, childPath, currentDepth + 1);
            }
        }
    }
}
