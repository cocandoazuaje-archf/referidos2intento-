/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.clases.CrearReferidoReferenciaBitacora;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CORSFilter
 */
// Enable it for Servlet 3.x implementations
public class CORSFilter implements Filter {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
            CrearReferidoReferenciaBitacora.class.getName());

    /**
     * Default constructor.
     */
    public CORSFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        LOGGER.info("doFilter:");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Allow-Origin",
                "*");
        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Allow-Headers",
                "Origin, Content-Type, X-Accept, Authorization");
        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Allow-Methods",
                "GET, OPTIONS, HEAD, PUT, POST, DELETE");
        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Max-Age",
                "1209600");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS
        // handshake
        if (request.getMethod().equals("OPTIONS")) {
            LOGGER.info("For HTTP OPTIONS verb.");
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
}
