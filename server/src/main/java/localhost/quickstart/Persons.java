package localhost.quickstart;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/persons")
public class Persons {
    private static List<Person> people = Collections.synchronizedList(new ArrayList<Person>(Arrays.asList(
            new Person(1, "Kevin", "Malakoff"), new Person(2, "Scott", "Idler")
    )));
    private static AtomicInteger nextId = new AtomicInteger(3);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> list() {
        return people;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Person find(@PathParam("id") int id) {
        for (Person p : people) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Person update(@PathParam("id") int id, Person person) {
        if (id != person.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == id) {
                people.set(i, person);
                return person;
            }
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Person person) {
        int id = nextId.getAndIncrement();
        person.setId(id);
        people.add(person);
        return Response.created(UriBuilder.fromResource(Persons.class).path("{id}").build(id))
                .entity(person)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") int id) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == id) {
                people.remove(i);
                return Response.status(Response.Status.ACCEPTED).build();
            }
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }


}
