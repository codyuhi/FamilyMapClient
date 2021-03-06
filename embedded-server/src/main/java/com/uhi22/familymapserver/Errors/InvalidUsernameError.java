/** The InvalidUsernameError exception is thrown when a Person or Event query is performed for a username that has no
 *  presence in the table he/she is attempting to access
 *
 *  This supports principles of Confidentiality, Integrity, and Availability of data
 *
 * @author Cody Uhi
 * @version 1.0.0
 */

package com.uhi22.familymapserver.Errors;

public class InvalidUsernameError extends Throwable {

    private String message;     // Contains the message that will be included in the response body

    /**
     * Constructor to define the specific error message associated with this exception
     */
    public InvalidUsernameError() {
        this.message = "Invalid Username Provided";
    }

    /**
     * Getter for the message
     * @return  the message for this specific exception
     */
    @Override
    public String getMessage() {
        return message;
    }
}
