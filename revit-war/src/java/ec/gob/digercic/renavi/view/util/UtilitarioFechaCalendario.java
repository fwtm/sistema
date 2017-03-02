/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author daniel.porras
 */
public class UtilitarioFechaCalendario {

    public static Date convertirDate(String fecha) {
        //SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
            return fechaDate;
        } catch (ParseException ex) {
            //ex.printStackTrace();
            System.out.println(ex.getMessage());
            return fechaDate;
        }
    }

    public static Date convertirHour(String fecha) {
        //SimpleDateFormat formatoEnglish = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat formatoEnglish = new SimpleDateFormat("HH:mm:ss");
        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formatoEnglish.parse(fecha);
            return fechaDate;
        } catch (ParseException ex) {
            //ex.printStackTrace();
            System.out.println(ex.getMessage());
            return fechaDate;
        }
    }

    public static String convertirDate(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String fechaString = formato.format(fecha);
            return fechaString;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return "";
        }
    }

    public static Calendar aniadirDias(Date fecha, int diasParaAniadir) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(fecha);
            c1.add(Calendar.DAY_OF_MONTH, diasParaAniadir);
            return c1;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar aniadirMeses(Date fecha, int mesesParaAniadir) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(fecha);
            c1.add(Calendar.MONTH, mesesParaAniadir);
            return c1;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar aniadirAnios(Date fecha, int aniosParaAniadir) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(fecha);
            c1.add(Calendar.YEAR, aniosParaAniadir);
            return c1;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar ponerAnioActual(Date fecha, Date fechaActual) {
        try {
            Calendar cActual = Calendar.getInstance();
            cActual.setTime(fechaActual);
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            //la fecha nueva
            Calendar c1 = Calendar.getInstance();
            c1.set(cActual.get(Calendar.YEAR), cfecha.get(Calendar.MONTH), cfecha.get(Calendar.DATE));
            return c1;
        } catch (Exception e) {
            return null;
        }
    }

    public static int calcularAniosAFechaActual(Date fecha) {
        try {
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            Calendar fechaActual = Calendar.getInstance();
            int anio = fechaActual.get(Calendar.YEAR) - cfecha.get(Calendar.YEAR);
            int mes = fechaActual.get(Calendar.MONTH) - cfecha.get(Calendar.MONTH);
            int dia = fechaActual.get(Calendar.DATE) - cfecha.get(Calendar.DATE);
            if (mes < 0 || (mes == 0 && dia < 0)) {
                anio--;
            }
            int anios = anio;
            return anios;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Metodo que aclcula los anios que han pasado desde la fecha inicio hasta
     * la fecha fin
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public static int calcularAniosAFecha(Date fechaInicio, Date fechaFin) {
        try {
            Calendar cfechaInicio = Calendar.getInstance();
            cfechaInicio.setTime(fechaInicio);
            Calendar cfechaFin = Calendar.getInstance();
            cfechaFin.setTime(fechaFin);
            int anio = cfechaFin.get(Calendar.YEAR) - cfechaInicio.get(Calendar.YEAR);
            int mes = cfechaFin.get(Calendar.MONTH) - cfechaInicio.get(Calendar.MONTH);
            int dia = cfechaFin.get(Calendar.DATE) - cfechaInicio.get(Calendar.DATE);
            if (mes < 0 || (mes == 0 && dia < 0)) {
                anio--;
            }
            int anios = anio;
            return anios;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int calcularDias(Date fechaInicio, Date fechaFin) {
        try {
            long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
            Calendar cfechaInicio = Calendar.getInstance();
            cfechaInicio.setTime(fechaInicio);
            Calendar cfechaFin = Calendar.getInstance();
            cfechaFin.setTime(fechaFin);
            Long diferencia = (cfechaFin.getTime().getTime() - cfechaInicio.getTime().getTime()) / MILLSECS_PER_DAY;
            return diferencia.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int calcularMesesAFechaActual(Date fecha) {
        try {
            Date fechaActual = new Date();
            long diff = fechaActual.getTime() - fecha.getTime();
            /*long diffSeconds = diff / 1000 % 60;
             long diffMinutes = diff / (60 * 1000) % 60;
             long diffHours = diff / (60 * 60 * 1000) % 24;
             long diffDays = diff / (24 * 60 * 60 * 1000);*/
            Long diffMonths = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
            /*System.out.print(diffDays + " days, ");
             System.out.print(diffHours + " hours, ");
             System.out.print(diffMinutes + " minutes, ");
             System.out.print(diffSeconds + " seconds.");
             System.out.print(diffMonths + " meses.");*/
            return diffMonths.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Método que calcula los meses que han poasado dese la fecha inicio hasta
     * la fecha fin
     *
     * @param fecha
     * @param fechaParaCalcular
     * @return
     */
    public static int calcularMesesAFecha(Date fechaInicio, Date fechaFin) {
        try {
            long diff = fechaFin.getTime() - fechaInicio.getTime();
            /*long diffSeconds = diff / 1000 % 60;
             long diffMinutes = diff / (60 * 1000) % 60;
             long diffHours = diff / (60 * 60 * 1000) % 24;
             long diffDays = diff / (24 * 60 * 60 * 1000);*/
            Long diffMonths = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
            /*System.out.print(diffDays + " days, ");
             System.out.print(diffHours + " hours, ");
             System.out.print(diffMinutes + " minutes, ");
             System.out.print(diffSeconds + " seconds.");
             System.out.print(diffMonths + " meses.");*/
            return diffMonths.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static short obtenerAnio(Date fecha) {
        try {
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            Integer anio = cfecha.get(Calendar.YEAR);
            return anio.shortValue();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static short obtenerMes(Date fecha) {
        try {
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            Integer mes = cfecha.get(Calendar.MONTH);
            return mes.shortValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     *
     * @param fecha
     * @return 1=Domingo, 2=Lunes, 3....
     */
    public static int diaDeLaSemana(Date fecha) {
        Calendar cfecha = Calendar.getInstance();
        cfecha.setTime(fecha);
        return cfecha.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Método que retorna la fecha con el primer dia del mes
     *
     * @param fecha
     * @return
     */
    public static Date obtenerPrimerDiaDelMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMinimum(Calendar.DAY_OF_MONTH),
                cal.getMinimum(Calendar.HOUR_OF_DAY),
                cal.getMinimum(Calendar.MINUTE),
                cal.getMinimum(Calendar.SECOND));
        return cal.getTime();
    }

    public static Date obtenerUltimoDiaDelMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMaximum(Calendar.DAY_OF_MONTH),
                cal.getMaximum(Calendar.HOUR_OF_DAY),
                cal.getMaximum(Calendar.MINUTE),
                cal.getMaximum(Calendar.SECOND));
        return cal.getTime();
    }

    public static void main(String[] args) {
     Date fecha = new Date();
     Date fechaN = aniadirMeses(fecha,20).getTime();
     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     System.out.println("Fecha: " + sdf.format(fecha));
     System.out.println("Fecha: " + fecha);
     System.out.println("FechaN: " + sdf.format(fechaN));
     System.out.println("Fecha Formateada: " + sdf.format(ponerAnioActual(fecha,fechaN).getTime()));
     System.out.println("Fecha Formateada ania: " + sdf.format(aniadirMeses(fecha,11).getTime()));
     System.out.println("dias: " + calcularDias(fecha, fecha));
     System.out.println("meses: " + obtenerMes(fecha));
     //System.out.println(convertirDate(fecha));
     String peru = "ALEMANA";
     String peruana = "ALEMANIA";
     System.out.println("--> 1: " + UtilitarioCaracteres.stripAccents(peru)
             .contains(UtilitarioCaracteres.stripAccents(peruana)));
     System.out.println("--> 2: " + UtilitarioCaracteres.stripAccents(peruana)
             .contains(UtilitarioCaracteres.stripAccents(peru)));
     }
}
