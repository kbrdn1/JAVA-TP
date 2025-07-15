package edu.fbansept.m2i2.view;

/**
 * Summary view for entities with additional information beyond basic fields.
 * This view extends BasicView and adds more detailed information like relationships.
 * Fields annotated with @JsonView(SummaryView.class) will be included 
 * along with all BasicView fields.
 */
public class SummaryView extends BasicView {
    // Marker class for Jackson JsonView
    // Inherits all BasicView fields and adds summary-level information
}