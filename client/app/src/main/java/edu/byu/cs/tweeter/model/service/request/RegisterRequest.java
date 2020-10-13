package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest extends LoginRequest {

    private String firstName;
    private String lastName;
    private byte [] imageBytes;

    /**
     * Creates an instance.
     *
     * @param firstName
     * @param lastName
     * @param username the username of the user to be registered.
     * @param password the password of the user to be registered in.
     */
    public RegisterRequest(String username, String password, String firstName, String lastName, byte [] imageBytes) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageBytes = imageBytes;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public byte [] getImageBytes() {
        return imageBytes;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}