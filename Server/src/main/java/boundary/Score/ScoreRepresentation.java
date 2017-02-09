package boundary.Score;

import boundary.Question.QuestionResource;
import boundary.Representation;
import boundary.User.UserResource;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.Score;
import entity.User;
import entity.UserRole;
import provider.Secured;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/scores")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/scores", description = "Scores management")
public class ScoreRepresentation extends Representation {
    
    @EJB
    private ScoreResource scoreResource;

    @EJB
    private UserResource userResource;

    @EJB
    private QuestionResource questionResource;
    
    @GET
    @ApiOperation(value = "Get all the scores", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response getAll() {
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scoreResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get a score by its id", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get(@PathParam("id") String id) {
        Score score = scoreResource.findById(id);
        if (score == null)
            return flash(404, "Error : Score does not exist");
        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Secured({UserRole.CUSTOMER})
    @ApiOperation(value = "Add a new score", notes = "Access : Customer only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Question not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response add(@Context SecurityContext securityContext, Score score) {
        if(score == null)
            return flash(400, EMPTY_JSON);

        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        score.setUser(user);

        if (!score.isValid())
            return flash(400, MISSING_FIELDS + ", also be sure the score is greater than 0");

        if (questionResource.findById(score.getQuestion().getId()) == null)
            return flash(404, "Error : Question does not exist");

        score = scoreResource.insert(score);
        
        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    @Secured({UserRole.CUSTOMER})
    @Path("/{id}")
    @ApiOperation(value = "Delete a score by its id", notes = "Access : Owner only")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Question not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response delete(@PathParam("id") String id, @Context SecurityContext securityContext) {
        Score score = scoreResource.findById(id);

        if(score == null)
            return Response.status(Response.Status.NOT_FOUND).build();
      
        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        if (!score.getUser().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        scoreResource.delete(score);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{id}")
    //@Secured({UserRole.ADMIN})
    @ApiOperation(value = "Update a score by its id", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Question not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response update(@PathParam("id") String id, Score score) {
        if (score == null)
            return flash(400, EMPTY_JSON);
        
        score = scoreResource.findById(score.getId());

        if(score == null) 
            return Response.noContent().build();

        if (!score.isValid())
            return flash(400, INVALID_JSON);

       Score originalScore = scoreResource.findById(id);

        if (originalScore == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if (questionResource.findById(score.getQuestion().getId()) == null)
            return flash(404, "Error : Question does not exist");
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
