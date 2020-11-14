package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest extends LoginRequest {

    private String firstName;
    private String lastName;
    private String encodedImageBytes;

    private RegisterRequest() {

    }

    /**
     * Creates an instance.
     *
     * @param firstName
     * @param lastName
     * @param username the username of the user to be registered.
     * @param password the password of the user to be registered in.
     */
    public RegisterRequest(String username, String password, String firstName, String lastName, String encodedImageBytes) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.encodedImageBytes = encodedImageBytes;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEncodedImageBytes() {
        return encodedImageBytes;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEncodedImageBytes(String encodedImageBytes) {
        this.encodedImageBytes = encodedImageBytes;
    }
}