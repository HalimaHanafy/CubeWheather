
package com.cubewheather.models.forcast16;

import java.util.ArrayList;

public class WeatherForecastResponseModel {

    private City city;
    private String cod;
    private double message;
    private int cnt;
    private java.util.List<com.cubewheather.models.forcast16.List> list = new ArrayList<com.cubewheather.models.forcast16.List>();

    /**
     * 
     * @return
     *     The city
     */
    public City getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * 
     * @return
     *     The cod
     */
    public String getCod() {
        return cod;
    }

    /**
     * 
     * @param cod
     *     The cod
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     * 
     * @return
     *     The message
     */
    public double getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(double message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The cnt
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * 
     * @param cnt
     *     The cnt
     */
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<com.cubewheather.models.forcast16.List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<com.cubewheather.models.forcast16.List> list) {
        this.list = list;
    }

}
