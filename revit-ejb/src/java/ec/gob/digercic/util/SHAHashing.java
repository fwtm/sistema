/*
 * Clase encargada de encriptar los passwords de Usuario usando EL ALGORITMO SHA-256.
 */
package ec.gob.digercic.util;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class SHAHashing implements Serializable{
        
    public static String encripta(String dato) throws EntidadException{
        try{
           String password = dato; 
            MessageDigest md = MessageDigest.getInstance("SHA-256");     
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println(sb.toString());
            return sb.toString(); 
        } catch(NoSuchAlgorithmException nsae){
           throw new EntidadException(nsae.getMessage(), nsae.getCause());
        }catch(Exception e){
            throw new EntidadException(e.getMessage(), e.getCause());
        }
    }    

}
