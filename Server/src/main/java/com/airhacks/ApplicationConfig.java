package com.airhacks;
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("docs")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Method to load classes to the javascript doc function
     * @param resources
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(boundary.Game.GameRepresentation.class);
        resources.add(boundary.Point.PointRepresentation.class);
        resources.add(boundary.Question.HintRepresentation.class);
        resources.add(boundary.Question.QuestionRepresentation.class);
        resources.add(boundary.Score.ScoreRepresentation.class);
        resources.add(boundary.User.UserRepresentation.class);
        resources.add(boundary.User.AuthenticationEndpoint.class);
    }

}