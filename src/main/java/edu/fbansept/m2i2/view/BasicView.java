package edu.fbansept.m2i2.view;

/**
 * Base view for basic entity information.
 * This is the foundation view that includes the most minimal set of fields.
 * Other views can extend this to include additional information.
 */
public class BasicView {
    // Marker class for Jackson JsonView
    // Fields annotated with @JsonView(BasicView.class) will be included
    // in all views that extend this class
}