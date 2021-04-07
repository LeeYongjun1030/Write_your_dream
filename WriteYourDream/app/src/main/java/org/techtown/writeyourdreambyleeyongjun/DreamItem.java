package org.techtown.writeyourdreambyleeyongjun;

public class DreamItem {

    String dream;

    String start_year;
    String start_month;
    String start_day;

    String finish_year;
    String finish_month;
    String finish_day;

    String image_uri;

    public DreamItem(String dream, String start_year, String start_month, String start_day, String finish_year, String finish_month, String finish_day) {
        this.dream = dream;
        this.start_year = start_year;
        this.start_month = start_month;
        this.start_day = start_day;
        this.finish_year = finish_year;
        this.finish_month = finish_month;
        this.finish_day = finish_day;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getStart_month() {
        return start_month;
    }

    public void setStart_month(String start_month) {
        this.start_month = start_month;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getFinish_year() {
        return finish_year;
    }

    public void setFinish_year(String finish_year) {
        this.finish_year = finish_year;
    }

    public String getFinish_month() {
        return finish_month;
    }

    public void setFinish_month(String finish_month) {
        this.finish_month = finish_month;
    }

    public String getFinish_day() {
        return finish_day;
    }

    public void setFinish_day(String finish_day) {
        this.finish_day = finish_day;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getImage_uri() {
        return image_uri;
    }

    @Override
    public String toString() {
        return "DreamItem{" +
                "dream='" + dream + '\'' +
                ", start_year='" + start_year + '\'' +
                ", start_month='" + start_month + '\'' +
                ", start_day='" + start_day + '\'' +
                ", finish_year='" + finish_year + '\'' +
                ", finish_month='" + finish_month + '\'' +
                ", finish_day='" + finish_day + '\'' +
                '}';
    }

}
