package model;

public class User {
    private String userName;
    private Integer userID;

    public User(String inputUserName,Integer inputUserId) {
        userName = inputUserName;
        userID = inputUserId;

    }

    public String getUserName() {
        return userName;
    }
    public int getUserId() {
        return userID;
    }
}
