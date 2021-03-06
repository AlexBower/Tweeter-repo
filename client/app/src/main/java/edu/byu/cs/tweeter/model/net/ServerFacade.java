package edu.byu.cs.tweeter.model.net;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.TimeFormatter;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static Map<User, List<User>> followeesByFollower;
    private static Map<User, List<User>> followersByFollowee;
    private static Map<User, List<Status>> storyByUser;
    private static Map<User, List<Status>> feedByUser;

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://ow1s7a4qmg.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowCountResponse getFollowCount(FollowCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowCountResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowerResponse getFollowers(FollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowResponse follow(FollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowResponse response = clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public GetUserResponse getUser(GetUserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        GetUserResponse response = clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest request, String urlPath) throws IOException, TweeterRemoteException {
        IsFollowingResponse response = clientCommunicator.doPost(urlPath, request, null, IsFollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LogoutResponse response = clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        PostStatusResponse response = clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        RegisterResponse response = clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public StatusResponse getStory(StatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        StatusResponse response = clientCommunicator.doPost(urlPath, request, null, StatusResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public StatusResponse getFeed(StatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        StatusResponse response = clientCommunicator.doPost(urlPath, request, null, StatusResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        UnfollowResponse response = clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest request) {

        return new LogoutResponse(true);

    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) {
        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        return new LoginResponse(user, new AuthToken());
    }

    @SuppressLint("NewApi")
    public RegisterResponse register(RegisterRequest request) {
        //byte [] bytes = ByteArrayUtils.bytesFromUrl(request.getImageUrl());
        //user.setImageBytes(bytes);
        // Used in place of assert statements because Android does not support them
        byte[] image = Base64.getDecoder().decode(request.getEncodedImageBytes().getBytes());
        if (image == null) {
            try {
                image = ByteArrayUtils.bytesFromUrl("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (request.getUsername() == null ||
                request.getFirstName() == null ||
                request.getLastName() == null ||
                request.getPassword() == null) {
            return new RegisterResponse("Please fill in all fields");
        }

        if (!request.getUsername().startsWith("@")) {
            request.setUsername(String.format("@%s", request.getUsername()));
        }

        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), image);
        return new RegisterResponse(user, new AuthToken());
    }

    public FollowCountResponse getFollowCount(FollowCountRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }
        int followingCount = 0;
        if (followeesByFollower.get(request.getUser()) != null) {
            followingCount = (Objects.requireNonNull(followeesByFollower.get(request.getUser()))).size();
        }

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }
        int followersCount = 0;
        if (followersByFollowee.get(request.getUser()) != null) {
            followersCount = Objects.requireNonNull(followersByFollowee.get(request.getUser())).size();
        }

        return new FollowCountResponse(followersCount, followingCount);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getUsername() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }

        for (Map.Entry<User, List<User>> followerToFollowees : followeesByFollower.entrySet()) {
            User follower = followerToFollowees.getKey();
            if (follower.getAlias().equals(request.getUsername())) {
                return new GetUserResponse(follower);
            }
        }
        return new GetUserResponse("Exception: Cannot find user '" + request.getUsername() + "'");
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the following response.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollower() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }

        List<User> allFollowees = followeesByFollower.get(request.getFollower());
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

                for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Generates the followee data.
     */
    private Map<User, List<User>> initializeFollowees() {

        Map<User, List<User>> followeesByFollower = new HashMap<>();

        List<Follow> follows = new ArrayList<Follow>();
        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for(Follow follow : follows) {
            List<User> followees = followeesByFollower.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                followeesByFollower.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        return followeesByFollower;
    }

    public FollowerResponse getFollowers(FollowerRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollowee() == null) {
                throw new AssertionError();
            }
        }

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }

        List<User> allFollowers = followersByFollowee.get(request.getFollowee());
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    private int getFollowersStartingIndex(User lastFollower, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollower != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollower.equals(allFollowers.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                }
            }
        }

        return followersIndex;
    }

    private Map<User, List<User>> initializeFollowers() {

        Map<User, List<User>> followersByFollowee = new HashMap<>();

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }

        for (Map.Entry<User, List<User>> followerToFollowees : followeesByFollower.entrySet()) {
            User follower = followerToFollowees.getKey();
            for (User followee : followerToFollowees.getValue()) {
                List<User> currentList = followersByFollowee.get(followee);
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.add(follower);
                followersByFollowee.put(followee, currentList);
            }
        }

        return followersByFollowee;
    }

    public StatusResponse getStory(StatusRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        if(storyByUser == null) {
            storyByUser = initializeStories();
        }

        List<Status> allStatuses = storyByUser.get(request.getUser());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    public StatusResponse getFeed(StatusRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        if (feedByUser == null) {
            feedByUser = initializeFeeds();
        }

        List<Status> allStatuses = feedByUser.get(request.getUser());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    private Map<User, List<Status>> initializeFeeds() {

        Map<User, List<Status>> feedByUser = new HashMap<>();

        if(storyByUser == null) {
            storyByUser = initializeStories();
        }

        for (Map.Entry<User, List<User>> entry : followersByFollowee.entrySet()) {
            if (!feedByUser.containsKey(entry.getKey())) {
                List<Status> statuses = new ArrayList<>();
                for (User user : entry.getValue()) {
                    statuses.add(storyByUser.get(user).get(0));
                }

                feedByUser.put(entry.getKey(), statuses);
            }
        }

        return feedByUser;
    }

    private int getStatusStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int storyIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    storyIndex = i + 1;
                }
            }
        }

        return storyIndex;
    }

    @SuppressLint("NewApi")
    private Map<User, List<Status>> initializeStories() {

        Map<User, List<Status>> storyByUser = new HashMap<>();

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }

        for (Map.Entry<User, List<User>> entry : followersByFollowee.entrySet()) {
            if (!storyByUser.containsKey(entry.getKey())) {
                List<Status> statuses = new ArrayList<>();
                for (User user : entry.getValue()) {
                    statuses.add(new Status("Hey, "
                            + user.getAlias()
                            + " check out this really cool url: "
                            + "http://google.com",
                            TimeFormatter.format(LocalDateTime.now()),
                            entry.getKey()));
                }
                storyByUser.put(entry.getKey(), statuses);
            }
        }

        return storyByUser;
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getCurrentUser() == null || request.getOtherUser() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }

        List<User> allFollowees = followeesByFollower.get(request.getCurrentUser());
        if (allFollowees == null) {
            return new IsFollowingResponse(false);
        } else {
            return new IsFollowingResponse(allFollowees.contains(request.getOtherUser()));
        }
    }

    public FollowResponse follow(FollowRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getCurrentUser() == null || request.getOtherUser() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }
        List<User> allFollowees = followeesByFollower.get(request.getCurrentUser());
        if (allFollowees == null) {
            allFollowees = new ArrayList<>();
        }
        allFollowees.remove(request.getOtherUser());
        allFollowees.add(request.getOtherUser());
        followeesByFollower.put(request.getCurrentUser(), allFollowees);

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }
        List<User> allFollowers = followersByFollowee.get(request.getOtherUser());
        if (allFollowers == null) {
            allFollowers = new ArrayList<>();
        }
        allFollowers.remove(request.getCurrentUser());
        allFollowers.add(request.getCurrentUser());
        followersByFollowee.put(request.getOtherUser(), allFollowers);

        return new FollowResponse(true);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getCurrentUser() == null || request.getOtherUser() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }
        List<User> allFollowees = followeesByFollower.get(request.getCurrentUser());
        if (allFollowees == null) {
            allFollowees = new ArrayList<>();
        }
        allFollowees.remove(request.getOtherUser());
        followeesByFollower.put(request.getCurrentUser(), allFollowees);

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }
        List<User> allFollowers = followersByFollowee.get(request.getOtherUser());
        if (allFollowers == null) {
            allFollowers = new ArrayList<>();
        }
        allFollowers.remove(request.getCurrentUser());
        followersByFollowee.put(request.getOtherUser(), allFollowers);

        return new UnfollowResponse(true);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getStatus() == null) {
                throw new AssertionError();
            }
        }

        if(storyByUser == null) {
            storyByUser = initializeStories();
        }
        List<Status> allStatuses = storyByUser.get(request.getStatus().getUser());
        if (allStatuses == null) {
            allStatuses = new ArrayList<>();
        }
        allStatuses.remove(request.getStatus());
        allStatuses.add(0, request.getStatus());
        storyByUser.put(request.getStatus().getUser(), allStatuses);


        if(feedByUser == null) {
            feedByUser = initializeFeeds();
        }
        for (Map.Entry<User, List<User>> entry : followersByFollowee.entrySet()) {
            if (entry.getValue().contains(request.getStatus().getUser())) {
                User follower = entry.getKey();
                List<Status> statuses = feedByUser.get(follower);
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                statuses.add(0, request.getStatus());

                feedByUser.put(follower, statuses);
            }
        }

        return new PostStatusResponse(true);
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }
}
