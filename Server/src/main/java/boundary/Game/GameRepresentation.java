/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary.Game;

import boundary.Question.QuestionResource;
import boundary.Representation;
import boundary.User.UserResource;
import entity.Game;
import entity.User;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/partie")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRepresentation extends Representation {
    
    //la ressource d'une partie, indispensable si on souhaite appeler ses méthodes
    @EJB
    private GameResource gameResource;
    
    @EJB
    private UserResource userResource;
    
    @EJB
    private QuestionResource questionResource;
    
    /**
     * Methode récupérant une partie selon un id 
     * @param id
     * @return Response contenant soit un message d'erreur, soit la partie
     */
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
       Game game = gameResource.findById(id);
       //si la partie récupérée avec l'id passé en paramètre est nulle, on envoie un message d'erreur de noContent
       if(game == null) 
            flash(404, "Error : Game does not exist");
       //sinon on renvoie la partie en réponse
       return Response.ok(game, MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * Methode permettant d'ajouter une partie
     * @param game la partie (contenant un utilisateur et des points à découvrir)
     * @return Response contenant un message d'erreur ou la partie créée
     */
    @POST
    public Response add(Game game) {
        if(game == null) 
            flash(400, EMPTY_JSON);
        
        if(!game.isValid())
            flash(400, EMPTY_JSON);
        
        if(questionResource.findById(game.getPoints().getId()) == null || userResource.findByEmail(game.getJoueur().getEmail()) == null)
            flash(400, "Error : Les points et/ou l'utilisateur n'existent pas");
    
        game = gameResource.insert(game);
        return Response.ok(game,MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * Methode permettant de supprimer une partie
     * @param game à supprimer
     * @return Response contenant un message d'erreur ou un message vide
     */
    @DELETE
    public Response delete(Game game) {
        if(game == null)
            flash(400, EMPTY_JSON);
        
        if(gameResource.findById(game.getId()) == null)
            return Response.noContent().build();
        
        //un moyen de vérifier si c'est faisable avant ?
        questionResource.delete(game.getPoints());
        
        gameResource.delete(game);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
