package cl.cns.integracion.wsreferidosrrvv.util;

import java.util.Arrays;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.log4j.Logger;

public class LogInterceptor {

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        Logger logger = Logger.getLogger(ctx.getTarget().getClass().getName());
        logger.info(Constantes.INICIO);
        logger.info(
                "ENTRANDO : "
                        + ctx.getMethod().getName()
                        + ",  Params"
                        + Arrays.toString(ctx.getParameters()));
        long start = System.currentTimeMillis();
        Object returnMe = ctx.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info(
                "SALIENDO : "
                        + ctx.getMethod().getName()
                        + ":"
                        + executionTime
                        + "ms"
                        + " respuesta:"
                        + returnMe);
        logger.info(Constantes.TERMINO);
        return returnMe;
    }
}
