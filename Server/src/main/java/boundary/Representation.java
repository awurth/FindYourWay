package boundary;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class Representation {

    public final static String EMPTY_JSON = "Error : Empty object";
    public final static String INVALID_JSON = "Error : Invalid object";
    public final static String MISSING_FIELDS = "Error : One or many fields have not been filled";

    /**
     * Helper function to create flash message
     * @param code
     * @param message
     * @return a Response
     */
    protected Response flash(int code, String message) {
        return Response.status(code)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(message)
                .build();
    }


}
