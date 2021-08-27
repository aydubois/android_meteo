package com.ayponyo.android.meteo.models;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class City   {
    private String mStringJson;

    @SerializedName("coord")
    @Expose
    private City.Coord coord;
    @SerializedName("weather")
    @Expose
    private List<City.Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private City.Main main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind")
    @Expose
    private City.Wind wind;
    @SerializedName("clouds")
    @Expose
    private City.Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private City.Sys sys;
    @SerializedName("timezone")
    @Expose
    private Integer timezone;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public City.Coord getCoord() {
        return coord;
    }

    public void setCoord(City.Coord coord) {
        this.coord = coord;
    }

    public List<City.Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<City.Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public City.Main getMain() {
        return main;
    }

    public void setMain(City.Main main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public City.Wind getWind() {
        return wind;
    }

    public void setWind(City.Wind wind) {
        this.wind = wind;
    }

    public City.Clouds getClouds() {
        return clouds;
    }

    public void setClouds(City.Clouds clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public City.Sys getSys() {
        return sys;
    }

    public void setSys(City.Sys sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getStringJson() {
        return mStringJson;
    }

    public void setStringJson(String json) {
        this.mStringJson = json;
    }
    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public class Clouds {

        @SerializedName("all")
        @Expose
        private Integer all;

        public Integer getAll() {
            return all;
        }

        public void setAll(Integer all) {
            this.all = all;
        }

    }

    public class Coord {

        @SerializedName("lon")
        @Expose
        private Double lon;
        @SerializedName("lat")
        @Expose
        private Double lat;

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

    }

    public class Main {

        @SerializedName("temp")
        @Expose
        private Double temp;
        @SerializedName("feels_like")
        @Expose
        private Double feelsLike;
        @SerializedName("temp_min")
        @Expose
        private Double tempMin;
        @SerializedName("temp_max")
        @Expose
        private Double tempMax;
        @SerializedName("pressure")
        @Expose
        private Integer pressure;
        @SerializedName("humidity")
        @Expose
        private Integer humidity;

        public Double getTemp() {
            return temp;
        }
        public String getTempString(){
            return temp+"";
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(Double feelsLike) {
            this.feelsLike = feelsLike;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public void setTempMin(Double tempMin) {
            this.tempMin = tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }

        public Integer getPressure() {
            return pressure;
        }

        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

    }

    public class Sys {

        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("sunrise")
        @Expose
        private Integer sunrise;
        @SerializedName("sunset")
        @Expose
        private Integer sunset;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getSunrise() {
            return sunrise;
        }

        public void setSunrise(Integer sunrise) {
            this.sunrise = sunrise;
        }

        public Integer getSunset() {
            return sunset;
        }

        public void setSunset(Integer sunset) {
            this.sunset = sunset;
        }

    }

    public class Weather {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("main")
        @Expose
        private String main;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("icon")
        @Expose
        private String icon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

    }

    public class Wind {

        @SerializedName("speed")
        @Expose
        private Double speed;
        @SerializedName("deg")
        @Expose
        private Integer deg;

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Integer getDeg() {
            return deg;
        }

        public void setDeg(Integer deg) {
            this.deg = deg;
        }

    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        String name = ((City) obj).getName();
        return this.getName().equals(name) && this.getId() == ((City) obj).getId() ;
    }
    
    public boolean isAlreadyPresent(ArrayList<City> cities){
        //TODO : A revoir
        for ( City city :cities) {
            if(this.equals(city)){
                return true;
            }
        }
        return false;
    }
}
