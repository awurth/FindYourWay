package boundary.Question;

import boundary.Point.PointResource;
import entity.Point;
import entity.Question;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/question")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionRepresentation {

    @EJB
    private QuestionResource pathResource;

    @EJB
    private PointResource pointResource;

    @GET
    public Response get() {
        GenericEntity<List<Question>> list = new GenericEntity<List<Question>>(pathResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @POST
    public Response add(Question question) {

        if (!question.isPointsValid())
                return Response.status(400)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity("Error : make sure you correctly created your points, only one point can be final and check you didn't add more than " + Question.PATH_LENGTH + " points.")
                        .build();


        for (Point point : question.getPoints()) {
            pointResource.insert(point);
            // ToDo : Links to Point + Add Link List in Question
        }


        question = pathResource.insert(question);

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }

}
