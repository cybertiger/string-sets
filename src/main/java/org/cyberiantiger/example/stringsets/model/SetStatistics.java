package org.cyberiantiger.example.stringsets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Statistics for a StringSet
 * 
 * @author antony
 */
public class SetStatistics {
    private final int count;
    private final int shortestLength;
    private final int longestLength;
    private final double averageLength;
    private final double medianLength;

    /**
     * Create a new SetStatistics for a given set of Strings.
     * 
     * @param strings The Strings to create the statistics for.
     */
    public SetStatistics(Set<String> strings) {
        this.count = strings.size();
        int shortestLength = Integer.MAX_VALUE;
        int longestLength = Integer.MIN_VALUE;
        int totalLength = 0;

        for (String s : strings) {
            int len = s.length();
            if (len < shortestLength) {
                shortestLength = len;
            }
            if (len > longestLength) {
                longestLength = len;
            }
            totalLength += len;
        }

        this.shortestLength = shortestLength;
        this.longestLength = longestLength;
        this.averageLength = 1D * totalLength / strings.size();

        List<Integer> collect = strings.stream().map(s -> s.length()).collect(Collectors.toList());

        if ((collect.size() & 1) == 1) {
            // Odd
            medianLength = collect.get((collect.size()-1)/2);
        } else {
            // Even, mean of middle two lengths.
            int offset = collect.size()/2;
            medianLength = (collect.get(offset) + collect.get(offset-1)) / 2d;
        }
    }

    /**
     * Constructor for jaxson to allow reconstructing this object.
     * 
     * @param count Count of strings in set
     * @param shortestLength Length of shortest string in set
     * @param longestLength Length of longest string in set
     * @param averageLength Average length of sting in set
     * @param medianLength Median length of string in set
     */
    @JsonCreator
    public SetStatistics(@JsonProperty("count") int count, @JsonProperty("shortestLength") int shortestLength, @JsonProperty("longestLength") int longestLength, @JsonProperty("averageLength") double averageLength, @JsonProperty("medianLength") double medianLength) {
        this.count = count;
        this.shortestLength = shortestLength;
        this.longestLength = longestLength;
        this.averageLength = averageLength;
        this.medianLength = medianLength;
    }

    /**
     * Get the count of strings.
     * 
     * @return the count of strings in the set
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the length of the shortest String.
     * @return the length of the shortest string in the set
     */
    public int getShortestLength() {
        return shortestLength;
    }

    /**
     * Get the length of the longest String.
     * 
     * @return the length of the longest string in the set
     */
    public int getLongestLength() {
        return longestLength;
    }

    /**
     * Get the (mean) average length of Strings in the set.
     * @return the average length of the Strings in the set.
     */
    public double getAverageLength() {
        return averageLength;
    }

    /**
     * Get the median average length of Strings in the set.
     * @return the median average length of the Strings in the set.
     */
    public double getMedianLength() {
        return medianLength;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.count;
        hash = 97 * hash + this.shortestLength;
        hash = 97 * hash + this.longestLength;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.averageLength) ^ (Double.doubleToLongBits(this.averageLength) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.medianLength) ^ (Double.doubleToLongBits(this.medianLength) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SetStatistics other = (SetStatistics) obj;
        if (this.count != other.count) {
            return false;
        }
        if (this.shortestLength != other.shortestLength) {
            return false;
        }
        if (this.longestLength != other.longestLength) {
            return false;
        }
        if (Double.doubleToLongBits(this.averageLength) != Double.doubleToLongBits(other.averageLength)) {
            return false;
        }
        if (Double.doubleToLongBits(this.medianLength) != Double.doubleToLongBits(other.medianLength)) {
            return false;
        }
        return true;
    }
}
