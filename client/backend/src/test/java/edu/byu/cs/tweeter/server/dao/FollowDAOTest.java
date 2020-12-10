package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;

public class FollowDAOTest extends TestWithAuthToken {

    private FollowDAO followDAO;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", "@AllenAnderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", "@AmyAmes", FEMALE_IMAGE_URL);

    List<User> getDummyFollowers() {
        return Arrays.asList(user1, user2);
    }

    @BeforeEach
    public void setup() {
        followDAO = new FollowDAO();
    }

    @Test
    public void testGetFollowers_validRequest() {
        User currentUser = new User("FirstName", "LastName", null);
        FollowerRequest validRequest = new FollowerRequest(currentUser, 10, null, authToken);
        FollowerResponse successResponse = new FollowerResponse(getDummyFollowers(), false);

        FollowerResponse response = followDAO.getFollowers(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowees_validRequest() {
        User currentUser = new User("FirstName", "LastName", null);
        FollowingRequest validRequest = new FollowingRequest(currentUser, 10, null, authToken);
        FollowingResponse successResponse = new FollowingResponse(getDummyFollowers(), false);

        FollowingResponse response = followDAO.getFollowees(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetAllFollowers_validRequest() {
        List<String> allFollowers = followDAO.getAllFollowers("@FirstNameLastName");
        Assertions.assertEquals(allFollowers.get(0), getDummyFollowers().get(0).getAlias());
        Assertions.assertEquals(allFollowers.get(1), getDummyFollowers().get(1).getAlias());
    }

    @Test
    public void testFollow_validRequest() {
        User currentUser = new User("Follow", "Test", null);
        User otherUser = new User("Allen", "Anderson", "@AllenAnderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        FollowRequest validRequest = new FollowRequest(currentUser, otherUser, authToken);
        FollowResponse successResponse = new FollowResponse(true);

        FollowResponse response = followDAO.follow(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testUnfollow_validRequest() {
        User currentUser = new User("Follow", "Test", null);
        User otherUser = new User("Allen", "Anderson", "@AllenAnderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        UnfollowRequest validRequest = new UnfollowRequest(currentUser, otherUser, authToken);
        UnfollowResponse successResponse = new UnfollowResponse(true);

        UnfollowResponse response = followDAO.unfollow(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testIsfollowing_validRequest() {
        User currentUser = new User("Follow", "Test", null);
        User otherUser = new User("Amy", "Ames", "@AmyAmes", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        IsFollowingRequest validRequest = new IsFollowingRequest(currentUser, otherUser, authToken);
        IsFollowingResponse successResponse = new IsFollowingResponse(true);

        IsFollowingResponse response = followDAO.isFollowing(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testAddFollowersBatch_validRequest() {
        String currentUser = "@FollowTest";
        List<String> followers = new ArrayList<>();
        followers.add("@1");
        followers.add("@2");
        followers.add("@3");
        followers.add("@4");
        followers.add("@5");

        followDAO.addFollowersBatch(followers, currentUser);
        // Since this did not throw an error the test was successful
    }
}
