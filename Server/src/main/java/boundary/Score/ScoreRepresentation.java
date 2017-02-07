package boundary.Score;

import boundary.User.UserResource;
import entity.Score;
import entity.User;
import entity.UserRole;
import provider.Secured;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/score")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScoreRepresentation {
    
    @EJB
    private ScoreResource scoreResource;

    @EJB
    private UserResource userResource;
    
    @GET
    public Response get() {
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scoreResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Secured({UserRole.CUSTOMER})
    public Response add(@Context SecurityContext securityContext, Score score) {
        if(score == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("You sent an empty object")
                    .build();

        if (!score.isValid())
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("One or many fields have not been filled, also be sure the score is greater than 0")
                    .build();

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        if (!score.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        score = scoreResource.insert(score);
        
        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    @Secured({UserRole.CUSTOMER})
    public Response delete(@Context SecurityContext securityContext, Score score) {
        if(score == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("You sent an empty object")
                    .build();
        
        if(scoreResource.findById(score.getId()) == null) 
            return Response.noContent().build();

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        if (!score.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        scoreResource.delete(score);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Secured({UserRole.CUSTOMER})
    public Response update(@Context SecurityContext securityContext, Score score) {
        if(score == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("You sent an empty object")
                    .build();
        
        score = scoreResource.findById(score.getId());

        if(score == null) 
            return Response.noContent().build();

        if(!score.isValid())
            return Response.status(Response.Status.NOT_FOUND).build();

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        if (!score.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
