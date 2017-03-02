/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author daniel.porras
 */
public class UtilitarioDate {

    public static Date larga(String fecha) {
        SimpleDateFormat formatoEnglish = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        if (fecha != null) {
            Date fechaDate = null;
            try {
                fechaDate = formatoEnglish.parse(fecha);
                return fechaDate;
            } catch (ParseException ex) {
                //ex.printStackTrace();
                System.out.println(ex.getMessage());
                return null;
            }
        } else {
            return null;
        }

    }
    
    /*public static Date largaToCorta(String fecha) {
        SimpleDateFormat formatoEnglish = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        if (fecha != null) {
            Date fechaDate = null;
            try {
                fechaDate = formatoEnglish.parse(fecha);
                Calendar fechaCalendar = Calendar.getInstance();
                fechaCalendar.setTime(fechaDate);
                int anio = fechaCalendar.get(Calendar.YEAR);
                int mes = (fechaCalendar.get(Calendar.MONTH) + 1);
                int dia = fechaCalendar.get(Calendar.DATE);
                SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
                String dateString = dia + "/" + mes + "/" + anio;
                fechaDate = sdf.parse(dateString);
                System.out.println(fechaDate + " **********");
                return fechaDate;
            } catch (ParseException ex) {
                //ex.printStackTrace();
                System.out.println(ex.getMessage());
                return null;
            }
        } else {
            return null;
        }

    }*/
    
    public static Date convertirNormal(String fecha) {
        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        if (fecha != null) {
            Date fechaDate = null;
            try {
                fechaDate = formato.parse(fecha);
                return fechaDate;
            } catch (ParseException ex) {
                //ex.printStackTrace();
                System.out.println(ex.getMessage());
                return null;
            }
        } else {
            return null;
        }

    }
    /*public static void main(String[] args) {
     String fecha = "Wed Nov 05 00:00:00 COT 2014";
     //String fecha = "2013-08-02";
     Date f = new Date();
        System.out.println( f  + "------------");
    }*/
}
