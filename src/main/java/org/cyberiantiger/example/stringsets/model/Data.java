package org.cyberiantiger.example.stringsets.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author antony
 */
public class Data {

    public static final Data instance = new Data();
    /**
     * The next id for a created StringSet.
     */
    private int nextId = 1;

    /**
     * Our in memory data store.
     */
    private final Map<Integer, StringSet> data = new LinkedHashMap<>();

    /**
     * Get all our data.
     * 
     * @return An unmodifiable copy of all the data
     */
    public synchronized Map<Integer,Set<String>> getMap() {
        Map<Integer, Set<String>> result = new LinkedHashMap<>();
        data.entrySet().stream().forEach((e) -> {
            result.put(e.getKey(), Collections.unmodifiableSet(e.getValue().getSet()));
        });
        return Collections.unmodifiableMap(result);
    }

    /**
     * Clear the data store, primarily used by tests.
     */
    public synchronized void clear() {
        this.data.clear();
    }

    /**
     * Get all the keys for our data.
     * 
     * @return An unmodifiable Set of all our keys
     */
    public synchronized Set<Integer> getKeys() {
        return Collections.unmodifiableSet(data.keySet());
    }

    /**
     * Get the StringSet for a specific key.
     * 
     * @param id the id.
     * @return The StringSet or null if it does not exist
     */
    public synchronized StringSet get(int id) {
        return data.get(id);
    }

    /**
     * Create a StringSet from a set of strings, and return the
     * id it is stored under.
     * 
     * The set must not be empty, and it must contain no empty strings.
     * 
     * @param set The set of strings (expected to have order, such as a LinkedHashSet).
     * @return the id of the newly created StringSet
     * @throws IllegalArgumentException if the set is empty, or contains empty strings.
     */
    public synchronized int create(Set<String> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Empty sets not allowed");
        }
        for (String s : set) {
            if (s.length() == 0) throw new IllegalArgumentException("Empty strings not allowed");
        }
        int result = nextId++;
        data.put(result, new StringSet(set));
        return result;
    }

    /**
     * Create a new StringSet as an intersection of two existing StringSets.
     * 
     * Note: order is implied by the first parameter's order, the order of
     * the second parameter is ignored.
     * 
     * StringSets with the ids must exist, and the resulting Set must satisfy
     * the constraints of {@link Data#create(java.util.Set)}. 
     * 
     * @param a The id of the first StringSet
     * @param b The id of the second StringSet
     * @return The id of the newly created StringSet
     * @throws IllegalArgumentException if either string set does not exist, or
     *         if the resulting set is empty or contains empty strings.
     */
    public synchronized int createIntersection(int a, int b) {
        if (!data.containsKey(a)) {
            throw new IllegalArgumentException("Unknown id: " + a);
        }
        if (!data.containsKey(b)) {
            throw new IllegalArgumentException("Unknown id: " + b);
        }
        Set<String> newSet = new LinkedHashSet<>();
        newSet.addAll(data.get(a).getSet());
        newSet.retainAll(data.get(b).getSet());
        return create(newSet);
    }

    /**
     * Search for StringSets containing a specific String.
     * 
     * @param query the string to search for
     * @return A list of ids of matching StringSets
     */
    public synchronized List<Integer> search(String query) {
        return data.entrySet().stream()
                .filter(e -> (e.getValue().getSet().contains(query)))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    /**
     * Delete a StringSet.
     * @param id The id of the StringSet to delete
     * @return the deleted StringSet or null if it did not exist
     */
    public synchronized StringSet delete(int id) {
        return data.remove(id);
    }

    /**
     * Find the most common words in all StringSets.
     * In the case of multiple results they are returned in alphabetical order.
     * 
     * @return A list of the most common Strings in alphabetical order
     */
    public synchronized List<String> getMostCommon() {
        if (data.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Integer> collect = data.values().stream().flatMap(s -> s.getSet().stream()).collect(Collectors.toMap(e -> e, e -> 1, Integer::sum));
        int max = collect.entrySet().stream().map(e -> e.getValue()).max(Integer::compare).get();
        return collect.entrySet().stream().filter(e -> max == e.getValue()).map(e -> e.getKey()).sorted().collect(Collectors.toList());
    }

    /**
     * Find the Longest words in all StringSets.
     * 
     * @return A list of the longest Strings in alphabetical order
     */
    public synchronized List<String> getLongest() {
        if (data.isEmpty()) {
            return Collections.emptyList();
        }
        int maxLength = data.values().stream().flatMap(s -> s.getSet().stream().map(ss -> ss.length())).max(Integer::compare).get();
        return data.values().stream().flatMap(s -> s.getSet().stream().filter(ss -> ss.length() == maxLength)).sorted().collect(Collectors.toList());
    }

    /**
     * Find the words repeated in exactly count StringSets.
     * 
     * @param count The repetition count to search for
     * @return A list of strings repeated exactly count times in alphabetical order
     */
    public synchronized List<String> getExactlyIn(int count) {
        Map<String, Integer> collect = data.values().stream().flatMap(s -> s.getSet().stream()).collect(Collectors.toMap(e -> e, e -> 1, Integer::sum));
        return collect.entrySet().stream().filter(e -> count == e.getValue()).map(e -> e.getKey()).sorted().collect(Collectors.toList());
    }
}
