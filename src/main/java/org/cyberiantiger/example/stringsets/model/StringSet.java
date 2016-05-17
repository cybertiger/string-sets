/*
   Copyright 2016 Cyberian Tiger

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
