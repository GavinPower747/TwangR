package net.gavinpower.Models;

public class User
{
    private int userId;
    private String userName;
    private String userRealName;
    private String userEmail;
    private String userNickName;

    public User() {}
    public User(int userId, String userName, String userRealName, String userEmail, String userNickName)
    {
        this.userId = userId;
        this.userName = userName;
        this.userRealName = userRealName;
        this.userEmail = userEmail;
        this.userNickName = userNickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}
