package org.firstinspires.ftc.teamcode.logging;

class SeverityFilter {
    boolean allow;

    Set<String> elements;

    SeverityFilter(boolean allow, Collection<String> elements) {
        this.allow = allow;
        this.elements = new HashSet<>(elements);
    }

    boolean permit(String severity) {
        return allow == elements.contains(severity);
    }
}
