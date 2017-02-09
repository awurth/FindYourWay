
package boundary.Game;

import boundary.Question.QuestionResource;
import boundary.Representation;
import boundary.User.UserResource;
import entity.Game;
import entity.Question;
import entity.User;
import entity.UserRole;
import java.util.List;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/games")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRepresentation extends Representation {

    @EJB
    private GameResource gameResource;
    
    @EJB
    private UserResource userResource;
    
    @EJB
    private QuestionResource questionResource;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
       Game game = gameResource.findById(id);
       if (game == null)
            flash(404, "Error : Game does not exist");

       return Response.ok(game, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Secured({UserRole.CUSTOMER})
    @ApiOperation(value = "Create a game from nothing", notes = "Access : Owner only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response add(@Context SecurityContext securityContext) {
        Game game = new Game();
        game.init();
        
        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());

        game.setUser(user);
        
        //on charge une question au hasard
        List<Question> questions = questionResource.findAll();
        double rnd = Math.random() * (questions.size()-1);
        int indice = (int)(rnd - (rnd%1));
        game.setQuestion(questions.get(indice));
        
        if (game == null)
            flash(400, EMPTY_JSON);
        
        if (!game.isValid())
            flash(400, INVALID_JSON);

        if (questionResource.findById(game.getQuestion().getId()) == null)
            flash(400, "Error : the question does not exist");

        return Response.ok(gameResource.insert(game), MediaType.APPLICATION_JSON).build();
    }
    

    @DELETE
    public Response delete(Game game) {
        if(game == null)
            flash(400, EMPTY_JSON);
        
        if (gameResource.findById(game.getId()) == null)
            return Response.noContent().build();

        gameResource.delete(game);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
