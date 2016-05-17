package org.cyberiantiger.example.stringsets.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import org.cyberiantiger.example.stringsets.model.Data;
import org.cyberiantiger.example.stringsets.model.SetStatistics;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Integration tests for StringSetResource
 * @author antony
 */
public class StringSetResourceIntegrationTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(StringSetResource.class);
    }

    private static final List<String> sampleList = Arrays.asList(new String[] {"a", "b", "c"});
    private static final Set<String> sampleSet = new LinkedHashSet(sampleList);

    @Test
    public void testIntegration() {
        Data.instance.clear();
        // list
        assertEquals(Collections.emptyMap(), target("").request().get(Map.class));
        // upload
        int id = target("upload").request().post(Entity.entity(sampleList, MediaType.APPLICATION_JSON), Integer.class);
        // get
        assertEquals(sampleList, target(String.valueOf(id)).request().get(List.class));
        // search
        assertEquals(Collections.singletonList((Integer)id), target("search").request().post(Entity.entity("a", MediaType.APPLICATION_JSON), List.class));
        // set_statistic
        assertEquals(new SetStatistics(sampleSet), target(String.format("%d/set_statistic", id)).request().get(SetStatistics.class));
        // most_common
        assertEquals(sampleList, target("most_common").request().get(List.class));
        // longest
        assertEquals(sampleList, target("longest").request().get(List.class));
        // exactly_in
        assertEquals(sampleList, target("exactly_in").request().post(Entity.entity((Integer)1, MediaType.APPLICATION_JSON), List.class));
        // create_intersection
        int intersectionId = target(String.format("%d/%d/create_intersection", id, id)).request().get(Integer.class);
        assertEquals(sampleList, target(String.valueOf(intersectionId)).request().get(List.class));
        // delete
        assertEquals(sampleList, target(String.format("%d/delete", id)).request().get(List.class));
    }
    
}