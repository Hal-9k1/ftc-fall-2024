package org.firstinspires.ftc.teamcode.logging;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SeverityFilter {
    private boolean allow;

    private Set<String> elements;

    SeverityFilter(boolean allow, Collection<String> elements) {
        this.allow = allow;
        this.elements = new HashSet<>(elements);
    }

    boolean permit(String severity) {
        return allow == elements.contains(severity);
    }
}
