/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.view.util;

/**
 *
 * @author fabricio.toapanta
 */
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ExpiresFilter implements Filter{
    private Integer days = -1;

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                          throws IOException, ServletException
    {
        Calendar c = Calendar.getInstance();
        c.setTime( new Date() );
        
        if ( days > -1 )
        {
            c.add( Calendar.DATE, days );

            //HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
            String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format( c.getTime() );            
            ((HttpServletResponse) response).setHeader( "Expires", o );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "cache" );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "max-age=1209600" );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "private" );
            ((HttpServletResponse) response).setHeader( "Pragma", "cache" );
        }
        else
        {
            c.add( Calendar.DATE, 30 );
            
            String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format( c.getTime() );            
            ((HttpServletResponse) response).setHeader( "Expires", o );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "cache" );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "max-age=1209600" );
            ((HttpServletResponse) response).setHeader( "Cache-Control", "private" );
            ((HttpServletResponse) response).setHeader( "Pragma", "cache" );
        }
        
        

        chain.doFilter(request, response);
    }

    @Override
    public void init( FilterConfig filterConfig )
    {        
        String expiresAfter = filterConfig.getInitParameter("days");
        if ( expiresAfter != null )
        {
            try
            {
                days = Integer.parseInt( expiresAfter );
            }
            catch ( NumberFormatException nfe )
            {
                //badly configured
            }                       
        }
    }

    @Override
    public void destroy()
    {
    }
}
