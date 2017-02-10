package boundary.Question;

import boundary.Point.PointRepresentation;
import boundary.Point.PointResource;
import com.wordnik.swagger.annotations.Api;
import boundary.Representation;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.Hint;
import entity.Point;
import entity.Question;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;

@Path("/questions")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Api(value = "/questions", description = "Questions management")
public class QuestionRepresentation extends Representation {


    @EJB
    private QuestionResource questionResource;

    @EJB
    private PointResource pointResource;

    @EJB
    private HintResource hintResource;

    @Context
    UriInfo uriInfo;

    @GET
    @ApiOperation(value = "Get all the questions", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get() {
        List<Question> questions = questionResource.findAll();
        questions.parallelStream().forEach(question -> {
            question.getLinks().clear();
            question.addLink(getUriForSelfQuestion(uriInfo, question), "self");
            question.getPoints().forEach(point -> {
                point.getLinks().clear();
                point.addLink(getUriForSelfPoint(uriInfo, point), "point");
            });
        });

        GenericEntity<List<Question>> list = new GenericEntity<List<Question>>(questions) {};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get a question by its id", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get(@Context UriInfo uriInfo, @PathParam("id") String id) {
        Question question = questionResource.findById(id);

        if (question == null)
            return flash(404, "Error : question does not exist");

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

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Secured({UserRole.CUSTOMER, UserRole.ADMIN})
    @ApiOperation(value = "Add a question", notes = "Access : Customer and Admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response add(Question question) {
        if (question == null)
            return flash(400, EMPTY_JSON);

        if (!question.isValid())
            return flash(400, INVALID_JSON);

        question.getLinks().clear();
        question.addLink(getUriForSelfQuestion(uriInfo, question), "self");

        question.getPoints().forEach(point -> {
            if (point.getId() == null) {
                pointResource.insert(point);
            } else {
                if (pointResource.findById(point.getId()) == null)
                    pointResource.insert(point);
            }

            point.getLinks().clear();
            point.addLink(getUriForSelfPoint(uriInfo, point), "point");
        });

        question.getHints().forEach(hint -> {
            if (hint.getId() == null) {
                hintResource.insert(hint);
            } else {
                if (hintResource.findById(hint.getId()) == null)
                    hintResource.insert(hint);
            }

            hint.getLinks().clear();
            hint.addLink(getUriForSelfHint(uriInfo, hint), "hint");
        });

        question = questionResource.insert(question);

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Secured({UserRole.ADMIN})
    @ApiOperation(value = "Delete a question by its id", notes = "Access : Admin")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response delete(@PathParam("id") String id) {
        Question question = questionResource.findById(id);

        if (question == null)
            return flash(404, "Error : question does not exist");

        questionResource.delete(question);
        
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    @Secured({UserRole.CUSTOMER/*UserRole.ADMIN*/})
    @ApiOperation(value = "Update a question by its id", notes = "Access : Admin")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response edit(@PathParam("id") String id, Question question) {
        Question originalQuestion = questionResource.findById(id);

        if (originalQuestion == null)
            return flash(404, "Error : question does not exist");

        if (question == null)
            return flash(400, EMPTY_JSON);

        if (!question.isValid())
            return flash(400, INVALID_JSON);

        questionResource.update(originalQuestion, question);

        return Response.status(204).build();
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

}
