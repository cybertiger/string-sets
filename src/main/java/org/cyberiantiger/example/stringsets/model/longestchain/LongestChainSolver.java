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
package org.cyberiantiger.example.stringsets.model.longestchain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of an algorithm to find the longest chain given a list of 
 * string sets.
 * 
 * From a list of strings sets find the longest chain of strings such that:
 * <ul>
 * <li>every next string starts with the same character as the previous one ends with
 * <li>every next string belongs to the same set as previous one, except one jump to another set is allowed
 * <li>specific string from specific set may be used only once
 * </ul>
 * <p>
 * Example:
 * Set 1: foo oomph hgf
 * Set 2: hij jkl jkm lmn
 * Set 3: abc cde cdf fuf fgh
 * 
 * The longest chain is: abc - cdf - fuf - fgh - (set changed here) - hij - jkl - lmn
 * 
 * @author antony
 */
public class LongestChainSolver {
    
    public static List<String> solve(Collection<Set<String>> data) {
        // Construct a directed graph for each string set.
        Map<Set<String>, Map<Character,List<StringNode>>> graphs = new HashMap<>();
        Set<Set<String>> duplicated = new HashSet<>();
        for (Set<String> s : data) {
            // Don't duplicate graphs for duplicate string sets.
            if (graphs.get(s) != null) { 
                duplicated.add(s); // Keep track of which sets are duplicated
                continue;
            }
            // Create a graph for the string set.
            Map<Character, List<StringNode>> graph = new HashMap<>();
            graphs.put(s, graph);
            for (String ss : s) {
                StringNode node = new StringNode(ss);
                List currList = graph.get(node.getFirstCharacter());
                if(currList == null) {
                    currList = new ArrayList();
                    graph.put(node.getFirstCharacter(), currList);
                }
                currList.add(node);
            }
            for (List<StringNode> nodeList : graph.values()) {
                for(StringNode node : nodeList) {
                    List<StringNode> out = graph.get(node.getLastCharacter());
                    if (out != null) {
                        node.setChildren(out);
                    } else {
                        node.setChildren(Collections.emptyList());
                    }
                }
            }
        }

        List<String> result;
        if (data.size() == 1) {
            // Special case if we only have one uploaded set, use an empty graph
            // for the second string set.
            result = findLongestPath(graphs.values().iterator().next(), Collections.emptyMap());
        } else {
            result = Collections.emptyList();
            
            // Choose first string set
            for (Map.Entry<Set<String>, Map<Character, List<StringNode>>> e : graphs.entrySet()) {
                boolean foundSecond = false;
                // Choose second string set
                for (Map.Entry<Set<String>, Map<Character, List<StringNode>>> ee : graphs.entrySet()) {
                    // if the second is the same as the first skip it, unless it is a duplicated string set
                    if (ee.getKey() == e.getKey() && !duplicated.contains(e.getKey())) {
                        continue;
                    }
                    
                    foundSecond = true;
                    
                    List<String> newResult = findLongestPath(e.getValue(), ee.getValue());
                    
                    if (newResult.size() > result.size()) {
                        result = newResult;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Find the longest chain given two graphs from two string sets.
     * 
     * @param first graph from the first string set
     * @param second graph from the second string set
     * @return A list of strings representing a longest path (it is not guaranteed to be the only longest path).
     */
    private static List<String> findLongestPath(Map<Character, List<StringNode>> first, Map<Character, List<StringNode>> second) {
        List<String> result = Collections.emptyList();
        Set<String> done = new LinkedHashSet<>();
        for (List<StringNode> l : first.values()) {
            for (StringNode node : l) {
                List<String> newResult = findLongestPathFirst(node, done, second);
                if (newResult.size() > result.size()) {
                    // TOOD: possible optimisation, terminate early if result.size() is equal to the number of nodes in both graphs.
                    result = newResult;
                }
            }
        }
        return result;
    }

    /**
     * Find the longest chain given a current node, a ordered (linkedhashset) of done nodes, and a graph from a second string set.
     * 
     * @param current The current node
     * @param done A ordered set of strings already used in the chain.
     * @param second graph from second string set.
     * @return Longest chain with remaining nodes.
     */
    private static List<String> findLongestPathFirst(StringNode current, Set<String> done, Map<Character, List<StringNode>> second) {
        done.add(current.getData());
        List<String> result = Collections.emptyList();
        for (StringNode child : current.getChildren()) {
            if (child == current || done.contains(child.getData())) {
                continue;
            }
            List<String> newResult = findLongestPathFirst(child, done, second);
            if (newResult.size() > result.size()) {
                result = newResult;
            }
        }
        List<StringNode> secondChildren = second.get(current.getLastCharacter());
        if (secondChildren != null) {
            Set<String> secondDone = new LinkedHashSet<>();
            for (StringNode child : secondChildren) {
                List<String> newResult = findLongestPathSecond(done, child, secondDone);
                if (newResult.size() > result.size()) {
                    result = newResult;
                }
            }
        }
        if (result.isEmpty()) {
            result = new ArrayList(done);
        } 
        done.remove(current.getData());
        return result;
    }

    /**
     * Find the longest chain, given the path in the first graph, from the remaining nodes in the second graph.
     * 
     * @param doneFirst Set containing the String path traversed in the first graph.
     * @param current Current node in the second graph.
     * @param done Set containing the String path traversed in the second graph.
     * @return The longest chain given the traversed path in the first and second graph.
     */
    private static List<String> findLongestPathSecond(Set<String> doneFirst, StringNode current, Set<String> done) {
        done.add(current.getData());
        List<String> result = Collections.emptyList();
        for (StringNode child : current.getChildren()) {
            if (child == current || done.contains(child.getData())) {
                continue;
            }
            List<String> newResult = findLongestPathSecond(doneFirst, child, done);
            if (newResult.size() > result.size()) {
                result = newResult;
            }
        }
        if (result.isEmpty()) {
            result = new ArrayList<>(doneFirst.size() + done.size());
            result.addAll(doneFirst);
            result.addAll(done);
        }
        done.remove(current.getData());
        return result;
    }
}