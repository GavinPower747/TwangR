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

    public String getUserName() {
        return UserName;
    }

    public String getUserRealName() {
        return UserRealName;
    }

    public String getUserNickName() {
        return UserNickName;
    }

    public String toDBFields()
    {
        return "UserId, UserName, UserPassword, UserRealName, UserEmail, UserNickName, UserStatus";
    }

    public String toDBString()
    {
        return " " + this.UserId + ","
                + this.UserName + ","
                + this.UserPassword + ","
                + this.UserRealName + ","
                + this.UserEmail + ","
                + this.UserNickName + ","
                + this.UserStatus;
    }
}
