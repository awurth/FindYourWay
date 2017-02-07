/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary.Score;

import entity.Score;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Utilisateur
 */
@Path("/score")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScoreRepresentation {
    
    @EJB
    private ScoreResource scoreResource;
    
    @GET
    public Response get() {
        GenericEntity<List<Score>> list = new GenericEntity<List<Score>>(scoreResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    public Response add(Score score) {
        
        //à faire les vérifs
        
        score = scoreResource.insert(score);
        
        return Response.ok(score, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    public Response delete(Score score) {
        
        if(score == null)
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("You sent an empty object")
                    .build();
        
        if(scoreResource.findById(score.getId()) == null) 
            return Response.noContent().build();
        
        scoreResource.delete(score);
        
        return Response.status(204).build();
    }
    
    @PUT
    public Response update(Score score) {
        
        if(score == null)
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("You sent an empty object")
                    .build();
        
        score = scoreResource.findById(score.getId());
        if(score == null) 
            return Response.noContent().build();
        
        Long l = score.getValue();
        if((score.getUser() == null) || (l == null)) 
            return Response.status(404).entity("Not Found").build();
        
        return Response.status(204).build();
    }
}
