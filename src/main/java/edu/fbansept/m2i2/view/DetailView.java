package edu.fbansept.m2i2.view;

/**
 * Detail view for entities with comprehensive information.
 * This view extends SummaryView and includes the most detailed information
 * including all relationships and sensitive data when appropriate.
 * Fields annotated with @JsonView(DetailView.class) will be included
 * along with all SummaryView and BasicView fields.
 */
public class DetailView extends SummaryView {
    // Marker class for Jackson JsonView
    // Inherits all SummaryView and BasicView fields and adds detail-level information
}