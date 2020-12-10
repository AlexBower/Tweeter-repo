package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthTokenDAOTest {

    private AuthTokenDAO authTokenDAO;
    private String alias = "@testAuthToken";

    @BeforeEach
    public void setup() {
        authTokenDAO = new AuthTokenDAO();
    }

    @Test
    public void testDeleteAuthToken_validRequest() {
        AuthToken authToken = authTokenDAO.createAuthToken(alias);
        Assertions.assertNotNull(authToken);
        Assertions.assertTrue(authTokenDAO.deleteAuthToken(alias, authToken.getToken()));
    }

    @Test
    public void testCreateAuthToken_validRequest() {
        AuthToken authToken = authTokenDAO.createAuthToken(alias);
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void testCheckAuthToken_validRequest() {
        AuthToken authToken = authTokenDAO.createAuthToken(alias);
        Assertions.assertNotNull(authToken);
        Assertions.assertTrue(authTokenDAO.checkAuthToken(alias, authToken.getToken()));
    }

    @Test
    public void testCheckAuthToken_invalidRequest() {
        AuthToken authToken = new AuthToken("SomeGarbage");

        String failureResponse = "BadRequest: Invalid or Expired AuthToken";
        try {
            authTokenDAO.checkAuthToken(alias, authToken.getToken());
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}
