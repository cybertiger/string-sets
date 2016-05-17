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
package org.cyberiantiger.example.stringsets.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import junit.framework.AssertionFailedError;
import org.cyberiantiger.example.stringsets.model.Data;
import org.cyberiantiger.example.stringsets.model.SetStatistics;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Tests for StringSetResource.
 * 
 * @author antony
 */
public class StringSetResourceUnitTest {

    private static final List<String> sampleList = Arrays.asList(new String[]  { "a", "b", "c", "d" });
    private static final Set<String> sampleSet = new LinkedHashSet(sampleList);

    private StringSetResource createStringSetResource() {
        Data.instance.clear();
        StringSetResource result = new StringSetResource();
        return result;
    }

    /**
     * Test the upload endpoint
     */
    @Test
    public void testUpload() {
        StringSetResource res = createStringSetResource();
        Map<Integer, Set<String>> list = res.list();
        assertTrue(list.isEmpty());
        int id = res.upload(sampleList);
        list = res.list();
        assertEquals(1, list.size());
        assertEquals((Integer)id, list.keySet().iterator().next());
        assertEquals(sampleList, new ArrayList<>(list.values().iterator().next()));
        int id2 = res.upload(sampleList);
        assertNotEquals(id, id2);
        assertEquals(2, res.list().size());
        assertEquals(sampleSet, res.get(id));
        assertEquals(sampleSet, res.get(id2));

        try {
            id = res.upload(Collections.emptyList());
            throw new AssertionFailedError("API allowed uploading an empty list");
        } catch (BadRequestException ex) {
        }
        try {
            id = res.upload(Collections.singletonList(""));
            throw new AssertionFailedError("API allowed uploading a list containing an empty string");
        } catch (BadRequestException ex) {
        }
        try {
            id = res.upload(Arrays.asList(new String[] {"a", "a"}));
            throw new AssertionFailedError("API allowed uploading a string list containing duplicates");
        } catch (BadRequestException ex) {
        }
    }

    /**
     * Test the search endpoint.
     */
    @Test
    public void testSearch() {
        StringSetResource res = createStringSetResource();
        int idA = res.upload(Collections.singletonList("a"));
        int idB = res.upload(Collections.singletonList("b"));
        int idC = res.upload(Collections.singletonList("c"));
        int idC2 = res.upload(Collections.singletonList("c"));

        assertEquals(Collections.singletonList(idA), res.search("a"));
        assertEquals(Collections.singletonList(idB), res.search("b"));
        assertEquals(Arrays.asList(new Integer[] {idC, idC2}), res.search("c"));
        assertEquals(Collections.emptyList(), res.search("d"));
    }

    /**
     * Test the delete endpoint.
     */
    @Test
    public void testDelete() {
        StringSetResource res = createStringSetResource();
        int id = res.upload(sampleList);
        assertEquals(1, res.list().size());
        try {
            res.delete(id+1);
            throw new AssertionFailedError("Deleting non existant string set did not return not found");
        } catch (NotFoundException ex) {
        }
        assertEquals(sampleSet, res.delete(id));
    }

    /**
     * Test the set_statistic endpoint.
     */
    @Test
    public void testSetStatistic() {
        StringSetResource res = createStringSetResource();
        int id = res.upload(sampleList);
        SetStatistics stats = res.setStatistics(id);
        assertEquals(4, stats.getCount());
        assertEquals(1, stats.getShortestLength());
        assertEquals(1, stats.getLongestLength());
        assertEquals(1D, stats.getAverageLength(), 0D);
        assertEquals(1D, stats.getMedianLength(), 0D);
        id = res.upload(Arrays.asList(new String[] { "a", "aa", "aaa", "aaaa", "aaaaa" }));
        stats = res.setStatistics(id);
        assertEquals(5, stats.getCount());
        assertEquals(1, stats.getShortestLength());
        assertEquals(5, stats.getLongestLength());
        assertEquals(3D, stats.getAverageLength(), 0D);
        assertEquals(3D, stats.getMedianLength(), 0D);
        id = res.upload(Arrays.asList(new String[] { "a", "aa", "aaa", "aaaa" }));
        stats = res.setStatistics(id);
        assertEquals(4, stats.getCount());
        assertEquals(1, stats.getShortestLength());
        assertEquals(4, stats.getLongestLength());
        assertEquals(2.5D, stats.getAverageLength(), 0D);
        assertEquals(2.5D, stats.getMedianLength(), 0D);
    }

    /**
     * Test the most_common endpoint.
     */
    @Test
    public void testMostCommon() {
        StringSetResource res = createStringSetResource();
        int idA = res.upload(Arrays.asList(new String[] { "a", "b", "c", "d", "e" }));
        int idB = res.upload(Arrays.asList(new String[] { "a", "b", "c", "d" }));
        int idC = res.upload(Arrays.asList(new String[] { "a", "b", "c" }));
        int idD = res.upload(Arrays.asList(new String[] { "a", "b" }));
        int idE = res.upload(Arrays.asList(new String[] { "a" }));
        assertEquals(Collections.singletonList("a"), res.mostCommon());
        res.delete(idE);
        assertEquals(Arrays.asList("a", "b"), res.mostCommon());
        res.delete(idD);
        assertEquals(Arrays.asList("a", "b", "c"), res.mostCommon());
    }

    /**
     * Test the longest endpoint.
     */
    @Test
    public void testLongest() {
        StringSetResource res = createStringSetResource();
        int idA = res.upload(Arrays.asList(new String[] { "a", "aa", "aaa", "aaaa"}));
        int idB = res.upload(Arrays.asList(new String[] { "b", "bb", "bbb", "bbbb"}));
        int idC = res.upload(Arrays.asList(new String[] { "c", "cc", "ccc", "cccc"}));
        int idD = res.upload(Arrays.asList(new String[] { "d", "dd", "ddd" }));
        assertEquals(Arrays.asList(new String[] {"aaaa", "bbbb", "cccc"}), res.longest());
        res.delete(idB);
        assertEquals(Arrays.asList(new String[] {"aaaa", "cccc"}), res.longest());
    }

    /**
     * Test the exactly_in endpoint.
     */
    @Test
    public void testExactlyIn() {
        StringSetResource res = createStringSetResource();
        int idA = res.upload(Arrays.asList(new String[] { "a", "b", "c", "d", "e" }));
        int idB = res.upload(Arrays.asList(new String[] { "a", "b", "c", "d" }));
        int idC = res.upload(Arrays.asList(new String[] { "a", "b", "c" }));
        int idD = res.upload(Arrays.asList(new String[] { "a", "b" }));
        int idE = res.upload(Arrays.asList(new String[] { "a" }));
        assertEquals(Collections.emptyList(), res.exactlyIn(6));
        assertEquals(Collections.singletonList("a"), res.exactlyIn(5));
        assertEquals(Collections.singletonList("b"), res.exactlyIn(4));
        assertEquals(Collections.singletonList("c"), res.exactlyIn(3));
        assertEquals(Collections.singletonList("d"), res.exactlyIn(2));
        assertEquals(Collections.singletonList("e"), res.exactlyIn(1));
    }

    /**
     * Test the create_intersection endpoint.
     */
    @Test
    public void testCreateIntersection() {
        StringSetResource res = createStringSetResource();
        int idA = res.upload(sampleList);
        int idB = res.upload(sampleList);
        int idC = res.createIntersection(idA, idB);
        assertEquals(sampleSet, res.get(idC));
        try {
            res.createIntersection(-1, idA);
            throw new AssertionFailedError("createIntersection did not return bad request for invalid string set id");
        } catch (BadRequestException ex) {
        }
        try {
            res.createIntersection(idA, -1);
            throw new AssertionFailedError("createIntersection did not return bad request for invalid string set id");
        } catch (BadRequestException ex) {
        }
        int idD = res.upload(Arrays.asList(new String[] { "e", "f", "g", "h" }));
        try {
            res.createIntersection(idA, idD);
            throw new AssertionFailedError("createIntersection did not return bad request for creating an empty string set");
        } catch (BadRequestException ex) {
        }
        int idE = res.upload(Arrays.asList(new String[] { "d", "c", "b", "a" }));
        int idF = res.createIntersection(idA, idE);
        int idG = res.createIntersection(idE, idA);

        assertEquals(sampleList, new ArrayList<>(res.get(idF)));
        assertEquals(Arrays.asList(new String[] { "d", "c", "b", "a" }), new ArrayList<>(res.get(idG)));
    }
}
