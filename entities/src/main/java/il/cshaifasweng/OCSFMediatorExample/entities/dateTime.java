package il.cshaifasweng.OCSFMediatorExample.entities;

public class dateTime {
    private String day;

    private  String openningHours;

    private  String closingTime;

    public dateTime(String day, String openningHours, String closingTime) {
        this.day = day;
        this.openningHours = openningHours;
        this.closingTime = closingTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpenningHours() {
        return openningHours;
    }

    public void setOpenningHours(String openningHours) {
        this.openningHours = openningHours;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
}
