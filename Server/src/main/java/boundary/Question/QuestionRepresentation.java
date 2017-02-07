package boundary.Question;

import boundary.Point.PointRepresentation;
import boundary.Point.PointResource;
import entity.Point;
import entity.Question;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/question")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionRepresentation {

    @EJB
    private QuestionResource questionResource;

    @EJB
    private PointResource pointResource;

    @GET
    public Response get(@Context UriInfo uriInfo) {
      List<Question> questions = questionResource.findAll();
       questions.parallelStream().forEach(question -> {
           question.getLinks().clear();
           question.addLink(getUriForSelfQuestion(uriInfo, question), "self");
           question.getPoints().forEach(point -> {
               point.getLinks().clear();
               point.addLink(getUriForSelfPoint(uriInfo, point), "point");
           });
       });

        GenericEntity<List<Question>> list = new GenericEntity<List<Question>>(questions){};

        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @POST
    public Response add(@Context UriInfo uriInfo, Question question) {
        if (question == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Error : you sent an empty object")
                    .build();

        if (!question.isPointsValid())
                return Response.status(400)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity("Error : make sure you correctly created your points, only one point can be final and check you didn't add more than " + Question.PATH_LENGTH + " points.")
                        .build();


        question.getLinks().clear();
        question.addLink(getUriForSelfQuestion(uriInfo, question), "self");

        question.getPoints().forEach(point -> {
            pointResource.insert(point);
            point.getLinks().clear();
            point.addLink(getUriForSelfPoint(uriInfo, point), "point");
        });

        question = questionResource.insert(question);

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    public Response delete(Question question) {
        
        if (question == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Error : you sent an empty object")
                    .build();
        
        if (pathResource.findById(question.getId()) == null)
            return Response.noContent().build();
        
        pathResource.delete(question);
        
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

}
