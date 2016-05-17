package org.cyberiantiger.example.stringsets.model;

import java.util.Set;

/**
 * A class to represent a set of Strings.
 * @author antony
 */
public class StringSet {
    
    /**
     * The Set of Strings.
     */
    private final Set<String> set;

    /**
     * Statistics about the Strings.
     */
    private final SetStatistics statistics;
    
    /**
     * Create a new StringSet with the specified set of Strings.
     * 
     * Note: it is expected that set is some sort of ordered Set such as
     * LinkedHashSet.
     * 
     * @param set the (ordered) set of Strings
     */
    public StringSet(Set<String> set) {
        this.set = set;
        this.statistics = new SetStatistics(set);
    }
    
    /**
     * Get the set of Strings.
     * @return the set of Strings
     */
    public Set<String> getSet() {
        return set;
    }

    /**
     * Get the SetStatistics for the set.
     * @return the SetStatistics for the set
     */
    public SetStatistics getStatistics() {
        return statistics;
    }
}
