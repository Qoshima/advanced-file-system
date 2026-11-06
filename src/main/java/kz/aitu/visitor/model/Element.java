package kz.aitu.visitor.model;

import kz.aitu.visitor.visitor.Visitor;

public interface Element {
    String getName();
    void accept(Visitor visitor);
}