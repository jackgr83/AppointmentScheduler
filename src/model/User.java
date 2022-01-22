package model;

public class User {
    private String userName;
    private Integer userID;

    /**
     * @param inputUserId
     * @param inputUserName
     * Initialize the User
     */
    public User(String inputUserName,Integer inputUserId) {
        userName = inputUserName;
        userID = inputUserId;

    }

    /**
     * Getter functions for retrieving user id or username
     */
    public String getUserName() {
        return userName;
    }
    public int getUserId() {
        return userID;
    }
}
