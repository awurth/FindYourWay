package boundary.Score;

import boundary.Point.PointRepresentation;
import boundary.Question.HintRepresentation;
import boundary.Question.QuestionResource;
import boundary.Representation;
import boundary.User.UserResource;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.*;
import provider.Secured;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
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

    @Context
    private UriInfo uriInfo;
    
    @GET
    @Context
    @ApiOperation(value = "Get all the scores (ordered by DESC)", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response getAll() {
        List<Score> scores = makeLinks(this.uriInfo, scoreResource.findAll());;
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scores){};
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

        Question question = questionResource.findById(score.getQuestion().getId());
        question.getLinks().clear();
        question.addLink(this.getUriForSelfQuestion(uriInfo, question),"self");

        List<Point> points = question.getPoints();
        for (Point point : points) {
            point.getLinks().clear();
            point.addLink(getUriForSelfPoint(uriInfo, point), "self");
        }

        List<Hint> hints = question.getHints();
        for (Hint hint : hints) {
            hint.getLinks().clear();
            hint.addLink(getUriForSelfHint(uriInfo, hint), "self");
        }

        question.setPoints(points);
        score.setQuestion(question);

        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Context
    @Path("/items")
    @ApiOperation(value = "Get all the scores (ordered by DESC) with a pagination method. Warning : Offset starts at 1 ! Limit : 0 is unlimited", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response pagination(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        if (offset < 0 || limit < 0)
            flash(404, "Error : there is no results on this page");

        List<Score> scores = scoreResource.offsetLimit(offset,limit);

        if (scores == null || scores.isEmpty() )
            flash(404, "Error : there is no results on this page");

        scores = makeLinks(this.uriInfo, scores);
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scores){};

        return Response.ok(list, MediaType.APPLICATION_JSON)
                .header("X-Pagination-Count", scoreResource.countAll())
                .header("X-Pagination-Page", offset)
                .header("X-Pagination-Limit", limit)
                .build();
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

        Question question = questionResource.findById(score.getQuestion().getId());

        if (question == null)
            return flash(404, "Error : question does not exist");

        question.getLinks().clear();
        question.addLink(this.getUriForSelfQuestion(uriInfo, question),"self");

        List<Point> points = question.getPoints();

        for (Point point : points) {
            point.getLinks().clear();
            point.addLink(getUriForSelfPoint(uriInfo, point), "self");
        }

        List<Hint> hints = question.getHints();

        for (Hint hint : hints) {
            hint.getLinks().clear();
            hint.addLink(getUriForSelfHint(uriInfo, hint), "self");
        }

        question.setPoints(points);

        Score scoreInsert = new Score(user, question, score.getValue());

        if (!scoreInsert.isValid())
            return flash(400, MISSING_FIELDS + ", also be sure the score is greater than 0");


        return Response.ok(scoreResource.insert(scoreInsert), MediaType.APPLICATION_JSON).build();
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


    private String getUriForSelfPoint(UriInfo uriInfo, Point point) {
        return uriInfo.getBaseUriBuilder()
                .path(PointRepresentation.class)
                .path('/' + point.getId())
                .build()
                .toString();
    }

    private String getUriForSelfQuestion(UriInfo uriInfo, Question question) {
        return uriInfo.getBaseUriBuilder()
                .path(PointRepresentation.class)
                .path('/' + question.getId())
                .build()
                .toString();
    }

    private String getUriForSelfHint(UriInfo uriInfo, Hint hint) {
        return uriInfo.getBaseUriBuilder()
                .path(HintRepresentation.class)
                .path('/' + hint.getId())
                .build()
                .toString();
    }

    /**
     * Method to create URI Links into the JSON result
     * @param scores
     * @return List of Score
     */
    private List<Score> makeLinks(@Context UriInfo uriInfo, List<Score> scores) {
        scores.forEach(score -> {
            Question question = questionResource.findById(score.getQuestion().getId());
            question.getLinks().clear();
            question.addLink(this.getUriForSelfQuestion(this.uriInfo, question),"self");

            List<Point> points = question.getPoints();
            List<Hint> hints = question.getHints();
            for (Point point : points) {
                point.getLinks().clear();
                point.addLink(getUriForSelfPoint(this.uriInfo, point), "self");
            }

            for (Hint hint : hints) {
                hint.getLinks().clear();
                hint.addLink(getUriForSelfHint(this.uriInfo, hint), "self");
            }

            question.setPoints(points);
            score.setQuestion(question);
        });

        return scores;
    }

}
