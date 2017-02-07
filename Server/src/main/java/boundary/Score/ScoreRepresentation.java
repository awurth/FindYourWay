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
import javax.ws.rs.GET;
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
    
    //methodes get et post todo
    
}
