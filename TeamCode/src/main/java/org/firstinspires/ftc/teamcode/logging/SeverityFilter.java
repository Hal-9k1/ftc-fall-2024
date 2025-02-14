package org.firstinspires.ftc.teamcode.logging;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes per-severity boolean configuration that has a default value and exception severities.
 */
class SeverityFilter {
    /**
     * The default configuration value.
     */
    private boolean allow;

    /**
     * Exception severities.
     */
    private Set<String> elements;

    /**
     * Constructs a SeverityFilter.
     *
     * @param allow the default configuration value.
     * @param elements severities which are exceptions to the default value.
     */
    SeverityFilter(boolean allow, Collection<String> elements) {
        this.allow = allow;
        this.elements = new HashSet<>(elements);
    }

    /**
     * Gets a severity's configured value.
     *
     * @param severity the severity to test.
     * @return The severity's configured value.
     */
    boolean permit(String severity) {
        return allow == elements.contains(severity);
    }
}
