/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named
@ViewScoped
public class Contador implements Serializable {

    private Long days, hour, minute, second, milliseconds;
    private Long daysNewYear, hourNewYear, minuteNewYear, secondNewYear, millisecondsNewYear;
    private Date today, merryChristmasDay, newYearDay;
    private String happyMerryChristmas, happyNewYear;

    @PostConstruct
    public void init() {

        merryChristmas();
        newYear();

    }

    public void merryChristmas() {
        try {
            today = new Date();
            merryChristmasDay = new SimpleDateFormat("yyyyMMdd").parse(Utils.getAnio(today).toString() + "1225");

            days = Utils.restarFechas(today, merryChristmasDay);
            milliseconds = merryChristmasDay.getTime() - today.getTime();

            hour = ((milliseconds / (1000 * 60 * 60)) % 24);
            minute = ((milliseconds / (1000 * 60)) % 60);
            second = (milliseconds / 1000) % 60;
            happyMerryChristmas = days + " dias " + hour + ":" + minute + ":" + (second < 10 ? "0" + second.toString() : second);
        } catch (ParseException ex) {
            Logger.getLogger(Contador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newYear() {
        try {
            today = new Date();
            newYearDay = new SimpleDateFormat("yyyyMMdd").parse(Utils.getAnio(today).toString() + "1231");

            daysNewYear = Utils.restarFechas(today, newYearDay);
            millisecondsNewYear = newYearDay.getTime() - today.getTime();

            hourNewYear = ((millisecondsNewYear / (1000 * 60 * 60)) % 24);
            minuteNewYear = ((millisecondsNewYear / (1000 * 60)) % 60);
            secondNewYear = (millisecondsNewYear / 1000) % 60;
            happyNewYear = daysNewYear.toString() + " dias " + hourNewYear + ":" + minuteNewYear + ":" + (secondNewYear < 10 ? "0" + secondNewYear.toString() : secondNewYear);
        } catch (ParseException ex) {
            Logger.getLogger(Contador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHappyMerryChristmas() {
        return happyMerryChristmas;
    }

    public void setHappyMerryChristmas(String happyMerryChristmas) {
        this.happyMerryChristmas = happyMerryChristmas;
    }

    public String getHappyNewYear() {
        return happyNewYear;
    }

    public void setHappyNewYear(String happyNewYear) {
        this.happyNewYear = happyNewYear;
    }

}
