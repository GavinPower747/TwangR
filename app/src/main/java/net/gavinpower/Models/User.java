package net.gavinpower.Models;

public class User
{
    public int UserId;
    public String UserName;
    public String UserPassword;
    public String UserRealName;
    public String UserEmail;
    public String UserNickName;
    public String UserStatus;

    public User() {}

    public User(int userId, String userName, String userPassword, String userRealName, String userEmail, String userNickName, String userStatus) {
        UserId = userId;
        UserName = userName;
        UserPassword = userPassword;
        UserRealName = userRealName;
        UserEmail = userEmail;
        UserNickName = userNickName;
        UserStatus = userStatus;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserRealName() {
        return UserRealName;
    }

    public void setUserRealName(String userRealName) {
        UserRealName = userRealName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }
}
