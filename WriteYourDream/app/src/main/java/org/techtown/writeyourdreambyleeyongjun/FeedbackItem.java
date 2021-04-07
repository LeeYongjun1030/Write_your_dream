package org.techtown.writeyourdreambyleeyongjun;

public class FeedbackItem {
    String year;
    String month;
    String day;
    String feedback;


    public FeedbackItem(String feedback) {
        //this.year = year;
        //this.month = month;
        //this.day = day;
        this.feedback = feedback;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "FeedbackItem{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
