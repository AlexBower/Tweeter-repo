package edu.byu.cs.tweeter.server.util;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class Filler {

    //public static void main(String[] args) {
    //    Filler.fillDatabase();
    //}

    private final static int NUM_USERS = 10000;

    private final static String FOLLOW_TARGET = "@followed";

    public static void fillDatabase() {

        UserDAO userDAO = new UserDAO();
        FollowDAO followDAO = new FollowDAO();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= NUM_USERS; i++) {

            String alias = "@guy" + i;
            users.add(new User("Guy",
                    "" + i,
                    alias,
                    "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));

            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}
