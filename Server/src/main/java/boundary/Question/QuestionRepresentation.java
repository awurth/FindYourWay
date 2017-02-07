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

        for (Point point : question.getPoints()) {
            if (pointResource.findById(point.getId()) == null)
                return Response.status(400)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity("One or many points do not exist")
                        .build();
            // ToDo : Links to Point + Add Link List in Question
        }

        if (question.getPoints().size() == Question.PATH_LENGTH)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Your question does not have enough point (" + Question.PATH_LENGTH + " required)")
                    .build();


        question = pathResource.insert(question);

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    public Response delete(Question question) {
        
        pathResource.delete(question);
        
        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }

}
