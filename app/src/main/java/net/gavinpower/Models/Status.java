package net.gavinpower.Models;

public class Status {
    public int StatusId ;
    public String StatusContent ;
    public int StatusAuthorID;
    public String StatusAuthor ;
    public int StatusLikes ;
    public String LogDate;

    public Status(int statusId, String statusContent, int statusLikes, int statusAuthorID,String statusAuthor, String logDate) {
        StatusId = statusId;
        StatusContent = statusContent;
        StatusLikes = statusLikes;
        StatusAuthorID = statusAuthorID;
        StatusAuthor = statusAuthor;
        LogDate = logDate;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public String getStatusContent() {
        return StatusContent;
    }

    public void setStatusContent(String statusContent) {
        StatusContent = statusContent;
    }

    public int getStatusAuthorID() {
        return StatusAuthorID;
    }

    public void setStatusAuthorID(int statusAuthorID) {
        StatusAuthorID = statusAuthorID;
    }

    public String getStatusAuthor() {
        return StatusAuthor;
    }

    public void setStatusAuthor(String statusAuthor) {
        StatusAuthor = statusAuthor;
    }

    public int getStatusLikes() {
        return StatusLikes;
    }

    public void setStatusLikes(int statusLikes) {
        StatusLikes = statusLikes;
    }

    public String getLogDate() {
        return LogDate;
    }

    public void setLogDate(String logDate) {
        LogDate = logDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (StatusAuthorID != status.StatusAuthorID) return false;
        if (StatusId != status.StatusId) return false;
        if (StatusLikes != status.StatusLikes) return false;
        if (LogDate != null ? !LogDate.equals(status.LogDate) : status.LogDate != null)
            return false;
        if (StatusAuthor != null ? !StatusAuthor.equals(status.StatusAuthor) : status.StatusAuthor != null)
            return false;
        if (StatusContent != null ? !StatusContent.equals(status.StatusContent) : status.StatusContent != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = StatusId;
        result = 31 * result + (StatusContent != null ? StatusContent.hashCode() : 0);
        result = 31 * result + StatusAuthorID;
        result = 31 * result + (StatusAuthor != null ? StatusAuthor.hashCode() : 0);
        result = 31 * result + StatusLikes;
        result = 31 * result + (LogDate != null ? LogDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Status{" +
                "StatusId=" + StatusId +
                ", StatusContent='" + StatusContent + '\'' +
                ", StatusAuthorID=" + StatusAuthorID +
                ", StatusAuthor='" + StatusAuthor + '\'' +
                ", StatusLikes=" + StatusLikes +
                ", LogDate='" + LogDate + '\'' +
                '}';
    }
}
