package boundary.Question;

import boundary.Point.PointRepresentation;
import boundary.Point.PointResource;
import boundary.Representation;
import entity.Point;
import entity.Question;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/questions")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionRepresentation extends Representation {

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

    @GET
    @Path("/{id}")
    public Response get(@Context UriInfo uriInfo, @PathParam("id") String id) {
        Question question = questionResource.findById(id);

        if (question == null)
            return Response.noContent().build();

        question.getLinks().clear();
        question.addLink(this.getUriForSelfQuestion(uriInfo, question),"self");

        List<Point> points = question.getPoints();
        points.parallelStream().forEach(point -> {
             point.getLinks().clear();
             point.addLink(getUriForSelfPoint(uriInfo, point), "point");
        });

        question.setPoints(points);

        return Response.ok(question, MediaType.APPLICATION_JSON).build();
    }

    @POST
    public Response add(@Context UriInfo uriInfo, Question question) {
        if (question == null)
            flash(400, EMPTY_JSON);

        if (!question.isPointsValid())
            flash(400, INVALID_JSON);

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
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Question question = questionResource.findById(id);

        if (question == null)
            return Response.noContent().build();
        
        questionResource.delete(question);
        
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
