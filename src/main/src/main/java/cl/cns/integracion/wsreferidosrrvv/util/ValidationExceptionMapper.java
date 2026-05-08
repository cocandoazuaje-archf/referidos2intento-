package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.vo.ReferidosRRVV_Out;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.log4j.Logger;

public class ValidationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(
            ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(exception))
                .type("application/json")
                .build();
    }

    private ReferidosRRVV_Out prepareMessage(
            ConstraintViolationException exception) {
        LOGGER.error("Se capturo una excepcion de validacion", exception);
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            String[] path = String.valueOf(cv.getPropertyPath()).split("\\.");
            String propiedad = path[path.length - 1];
            message.append(propiedad + " " + cv.getMessage() + "\n");
        }
        ReferidosRRVV_Out out = new ReferidosRRVV_Out();
        out.setCodigo(1);
        out.setMensaje(String.valueOf(message));
        return out;
    }
}
