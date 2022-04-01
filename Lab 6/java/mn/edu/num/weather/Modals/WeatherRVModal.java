package mn.edu.num.weather.Modals;

public class WeatherRVModal {
    private String time;
    private String temp;
    private String icon;
    private String wind_speed;

    public WeatherRVModal(String time, String temp, String icon, String wind_speed) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.wind_speed = wind_speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }
}
