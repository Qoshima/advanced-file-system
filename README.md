
# Advanced File System - Visitor Pattern

Assignment 6 - Software Design Patterns

## Overview

This project implements an in-memory hierarchical file system
using the Visitor behavioral design pattern in Java 17.

The file system supports files, directories, and nested directories.

## Features

- TotalSizeVisitor - calculates the total size of files.
- SearchVisitor - searches files by name and minimum size.
- StatisticsVisitor - counts files, directories, and maximum depth.
- TreeWalker - traverses the file system recursively.
- Tree integrity - prevents cycles, duplicate names,
  negative file sizes, and multiple parent ownership.

## Design

The Element interface defines the accept() method.

FileNode and DirectoryNode implement double dispatch
by calling visitor.visit(this).

The Visitor interface declares overloaded visit() methods
for both element types.

TreeWalker owns the traversal logic. Visitors only
perform their own operations.

Search paths are relative to the selected root and sorted
lexicographically. The root has depth 0.

Visitors are reusable because TreeWalker calls reset()
before each execution.

New operations can be added through new Visitor classes
without modifying the existing element classes.

Adding a new element type, however, requires updating
the Visitor interface and its implementations.

## Requirements

- Java 17
- Maven
- JUnit 5

## Run Application

From the project root:

```bash
mvn compile
java -cp target/classes kz.aitu.visitor.Main
```

## Run Tests

```bash
mvn clean test
```

## Test Coverage

VisitorPatternTest contains 9 tests covering:

- Double dispatch
- Total size calculation
- Nested directory traversal
- Search filters
- Relative paths and sorting
- File system statistics
- Empty directories and no-match searches
- Repeated visitor execution
- Tree integrity and invalid mutations

## Example Output

```text
Total size: 5200 bytes
Search results:
 - documents/report.pdf
 - report.txt
Files: 4
Directories: 3
Max depth: 3
```
