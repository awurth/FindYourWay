package boundary.Score;

import boundary.Question.QuestionResource;
import boundary.Representation;
import boundary.User.UserResource;
import entity.Score;
import entity.User;
import entity.UserRole;
import provider.Secured;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/score")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScoreRepresentation extends Representation {
    
    @EJB
    private ScoreResource scoreResource;

    @EJB
    private UserResource userResource;

    @EJB
    private QuestionResource questionResource;
    
    @GET
    public Response get() {
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scoreResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Secured({UserRole.CUSTOMER})
    public Response add(@Context SecurityContext securityContext, Score score) {
        if(score == null)
            flash(400, EMPTY_JSON);

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        score.setUser(user);

        if (!score.isValid())
            flash(400, MISSING_FIELDS + ", also be sure the score is greater than 0");

        if (questionResource.findById(score.getQuestion().getId()) == null)
            flash(404, "Error : Question does not exist");

        score = scoreResource.insert(score);
        
        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Secured({UserRole.CUSTOMER})
    public Response delete(@PathParam("id") String id, @Context SecurityContext securityContext) {
        Score score = scoreResource.findById(id);

        if(score == null)
            return Response.noContent().build();

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        if (!score.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        scoreResource.delete(score);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Path("/{id}")
    @Secured({UserRole.CUSTOMER})
    public Response update(@PathParam("id") String id, @Context SecurityContext securityContext, Score score) {
        if(score == null)
            flash(400, EMPTY_JSON);

        if(!score.isValid())
            flash(400, INVALID_JSON);

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());
        Score originalScore = scoreResource.findById(id);

        if (originalScore == null)
            return Response.noContent().build();

        if (!originalScore.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        if (questionResource.findById(score.getQuestion().getId()) == null)
            flash(404, "Error : Question does not exist");

        originalScore.update(score);
        scoreResource.update(score);
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
