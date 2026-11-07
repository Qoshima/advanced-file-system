
package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.model.DirectoryNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchVisitor implements Visitor {

    private final String nameFilter;
    private final long minSize;
    private final List<String> matches = new ArrayList<>();

    private String currentPath;

    public SearchVisitor(String nameFilter, long minSize) {
        if (nameFilter == null) {
            throw new IllegalArgumentException("Name filter cannot be null");
        }

        if (minSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }

        this.nameFilter = nameFilter;
        this.minSize = minSize;
    }

    @Override
    public void setLocation(String path, int depth) {
        this.currentPath = path;
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(nameFilter)
                && file.getSize() >= minSize) {
            matches.add(currentPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        // Search only files
    }

    @Override
    public void reset() {
        matches.clear();
        currentPath = null;
    }

    public List<String> getMatches() {
        List<String> result = new ArrayList<>(matches);
        Collections.sort(result);
        return List.copyOf(result);
    }
}
