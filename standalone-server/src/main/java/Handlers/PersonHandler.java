/** The PersonHandler class is used to handle requests whose URL denotes that the client
 *  requests data for a single Person in the database that they may access based on their authentication
 *
 * @author Cody Uhi
 * @version 1.0.0
 */

package Handlers;

import Errors.*;
import Responses.Response;
import Service.GetPersonService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonHandler extends RequestHandler implements HttpHandler {

    /**
     * handle takes the given request data, performs business logic by calling the PersonService class,
     * then sends an HTTP response to the client containing the requested Person data
     *
     * Use the adopted handle method from the RequestHandler class to work with the request data
     * If the request method is not a GET request, then an error response is returned to the client
     * If there was no authToken, then an error response is returned to the client
     * If the firstParameter is malformed or does not exist, then perform business logic that the GetAllPersonsHandler would usually do
     *      A Response POJO is created and set to be the results of the getAllPersons method from the GetPersonService class
     *      The authToken is passed as a parameter to validate authentication
     *      If an internal server error or a data access exception is thrown during the service call,
     *      an error response is returned to the client
     *      If an invalid authToken error is thrown during the service call, an error response is returned to the client
     *      If there were no errors, a successful response containing the appropriate GetAllPersonsResponse data is returned to the client
     * A Response POJO is created and set to be the results of the getEvent method from the GetPersonService class
     * If an internal server error or a data access exception is thrown during the service call,
     *      an error response is returned to the client
     * If an invalid authToken error is thrown during the service call, an error response is returned to the client
     * If the User does not own the requested Person, an error response is returned to the client
     * If the PersonID does not exist, an error response is returned to the client
     * If there were no errors, a successful response containing the appropriate GetPersonResponse data is returned to the client
     *
     * If the response encounters an error while trying to send, it is caught and printed
     *
     * @param httpExchange represents the httpExchange that takes place as part of the client-server architecture
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            System.out.println("\nCalled the PersonHandler");
//            Use the adopted handle method from the RequestHandler class to work with the request data
            super.handle(httpExchange);
            if(!"GET".equals(requestMethod)) {
//                If the request method is not a GET request, then an error response is returned to the client
                respond(defineFailure("Invalid Request Method Error"), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }  else if(authToken == null) {
//                If there was no authToken, then an error response is returned to the client
                respond(defineFailure("Invalid Authorization Token Error"), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            } else if(firstParameter == null || "".equals(firstParameter)) {
//                If the firstParameter is malformed or does not exist, then perform business logic that the GetAllPersonsHandler would usually do
                try {
//                    A Response POJO is created and set to be the results of the getAllPersons method from the GetPersonService class
                    Response allPersonsResponse = GetPersonService.getAllPersons(authToken);
//                    If there were no errors, a successful response containing the appropriate GetAllPersonsResponse data is returned to the client
                    respond(allPersonsResponse, HttpURLConnection.HTTP_OK);
                } catch (InternalServerError | DataAccessException internalServerError) {
//                    If an internal server error or a data access exception is thrown during the service call,
//                    an error response is returned to the client
                    internalServerError.printStackTrace();
                    respond(defineFailure("Internal Server Error"), HttpURLConnection.HTTP_INTERNAL_ERROR);
                } catch (InvalidAuthTokenError invalidAuthTokenError) {
//                    If an invalid authToken error is thrown during the service call, an error response is returned to the client
                    invalidAuthTokenError.printStackTrace();
                    respond(defineFailure("Invalid Authorization Token Error"), HttpURLConnection.HTTP_BAD_REQUEST);
                }
                return;
            }
            try {
//                A Response POJO is created and set to be the results of the getEvent method from the GetPersonService class
                Response personResponse = GetPersonService.getPerson(secondParameter, authToken);
//                If there were no errors, a successful response containing the appropriate GetPersonResponse data is returned to the client
                respond(personResponse, HttpURLConnection.HTTP_OK);
            } catch (InvalidPersonIDError invalidPersonIDError) {
//                If the PersonID does not exist, an error response is returned to the client
                invalidPersonIDError.printStackTrace();
                respond(defineFailure("Invalid Person ID Error"), HttpURLConnection.HTTP_BAD_REQUEST);
            } catch (InternalServerError | DataAccessException internalServerError) {
//                If an internal server error or a data access exception is thrown during the service call,
//                an error response is returned to the client
                internalServerError.printStackTrace();
                respond(defineFailure("Internal Server Error"), HttpURLConnection.HTTP_INTERNAL_ERROR);
            } catch (InvalidAuthTokenError invalidAuthTokenError) {
//                If the authToken could not be found, then an error response is returned to the client
                invalidAuthTokenError.printStackTrace();
                respond(defineFailure("Invalid Auth Token Error"), HttpURLConnection.HTTP_BAD_REQUEST);
            } catch (RequestedPersonDoesNotBelongToThisUser requestedPersonDoesNotBelongToThisUser) {
//                If the requested Person does not belong to the authenticated user, then an error response is returned to the client
                requestedPersonDoesNotBelongToThisUser.printStackTrace();
                respond(defineFailure("Requested Person Does Not Belong To This User Error"), HttpURLConnection.HTTP_BAD_REQUEST);
            }
        } catch (IOException e) {
//            If the response encounters an error while trying to send, it is caught and printed
            e.printStackTrace();
        }
    }
}
