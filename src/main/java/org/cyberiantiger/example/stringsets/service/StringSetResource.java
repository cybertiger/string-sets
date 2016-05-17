package org.cyberiantiger.example.stringsets.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.cyberiantiger.example.stringsets.model.Data;
import org.cyberiantiger.example.stringsets.model.SetStatistics;
import org.cyberiantiger.example.stringsets.model.StringSet;

/**
 * JAX-RS endpoint for StringSet api.
 * @author antony
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class StringSetResource {

    /**
     * Get our data store.
     * @return our data store.
     */
    protected Data getData() {
        return Data.instance;
    }

    /**
     * Get all the uploaded string sets.
     * 
     * @return a map of all the data in the system with ids as keys, and string sets as values
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer,Set<String>> list() {
        return getData().getMap();
    }

    /**
     * Upload a string set.
     * 
     * @param strings the strings to upload
     * @return the id of the newly created string set.
     * @throws BadRequestException If an uploaded string set contains duplicate strings
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("upload")
    public int upload(List<String> strings) {
        Set<String> set = new LinkedHashSet<>();
        for (String s : strings) {
            if (set.add(s)) continue;
            throw new BadRequestException("Duplicated string: " + s);
        }
        try {
            return getData().create(set);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    /**
     * Search for string sets containing a specific string.
     * 
     * @param search the string to search for in string sets
     * @return a list of string set ids which contain the search string
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("search")
    public List<Integer> search(String search) {
        return getData().search(search);
    }

    /**
     * Get an alphabetically sorted list of the most common strings in string sets.
     * 
     * @return an alphabetically sorted list of the most common strings in string sets
     */
    @GET
    @Path("most_common")
    public List<String> mostCommon() {
        return getData().getMostCommon();
    }

    /**
     * Get an alphabetically sorted list of the longest strings in string sets.
     * 
     * @return an alphabetically sorted list of the longest strings in string sets
     */
    @GET
    @Path("longest")
    public List<String> longest() {
        return getData().getLongest();
    }

    /**
     * Get an alphabetically sorted list of strings in exactly count string sets.
     * 
     * @param count the numer of string sets the string should be in
     * @return an alphabetically sorted list of strings exactly in count string sets
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("exactly_in")
    public List<String> exactlyIn(int count) {
        return getData().getExactlyIn(count);
    }

    /**
     * Get a string set by it's id
     * @param id
     * @return the string set
     * @throws NotFoundException if the string set does not exist
     */
    @GET
    @Path("{id}")
    public Set<String> get(@PathParam("id") int id) {
        StringSet set = getData().get(id);
        if (set == null)
            throw new NotFoundException();
        else
            return set.getSet();
    }

    /**
     * Delete a string set by it's id
     * @param id
     * @return the deleted string set
     * @throws NotFoundException if the string set does not exist
     */
    @GET
    @Path("{id}/delete")
    public Set<String> delete(@PathParam("id") int id) {
        StringSet deleted = getData().delete(id);
        if(deleted == null) {
            throw new NotFoundException();
        } else {
            return deleted.getSet();
        }
    }
    
    /**
     * Get statistics for a string set.
     * @param id the id of the string set
     * @return statistics on the string set
     */
    @GET
    @Path("{id}/set_statistic")
    public SetStatistics setStatistics(@PathParam("id") int id) {
        StringSet set = getData().get(id);
        if (set == null) return null;
        else return set.getStatistics();
    }

    /**
     * Create a new string set as an intersection of two existing string sets.
     * 
     * @param a id of first string set
     * @param b id of second string set
     * @return id of newly created string set
     * @throws BadRequestException if either id does not exist, or if the created string set would be empty
     */
    @GET
    @Path("{ida}/{idb}/create_intersection")
    public int createIntersection(@PathParam("ida") int a, @PathParam("idb") int b) {
        try {
            return getData().createIntersection(a, b);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
