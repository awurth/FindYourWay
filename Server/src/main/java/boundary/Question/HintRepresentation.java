package boundary.Question;

import boundary.Representation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.Hint;
import entity.Question;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/hints")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Api(value = "/hints", description = "Hints management")

public class HintRepresentation extends Representation {
    @EJB
    private QuestionResource questionResource;

    @EJB
    private HintResource hintResource;

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get a hint by its id", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get(@PathParam("id") String id) {
        Hint hint = hintResource.findById(id);
        if (hint == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(hint, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/question/{id}")
    @ApiOperation(value = "Get a hint by a question id", notes = "Access : Everyone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response getByQuestion(@PathParam("id") String id) {
        Question question = questionResource.findById(id);
        if (question == null)
            flash(404, "Error : Question does not exist");

        List<Hint> hints = hintResource.findByQuestion(question);

        GenericEntity<List<Hint>> list = new GenericEntity<List<Hint>>(hints) {};

        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Secured({UserRole.CUSTOMER, UserRole.ADMIN})
    @ApiOperation(value = "Add a Hint", notes = "Access : Customer and Admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Question not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response add(Hint hint) {
        if (hint == null)
            flash(400, EMPTY_JSON);

        if (!hint.isValid())
            flash(400, INVALID_JSON);

        if (questionResource.findById(hint.getQuestion().getId()) == null)
            flash(404, "Error : question does not exist");

        return Response.ok(hint, MediaType.APPLICATION_JSON).build();
    }

}
