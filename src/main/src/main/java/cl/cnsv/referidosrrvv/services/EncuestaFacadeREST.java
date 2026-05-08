/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import cl.cns.integracion.wsreferidosrrvv.exceptions.NegocioException;
import cl.cns.integracion.wsreferidosrrvv.exceptions.SMSException;
import cl.cns.integracion.wsreferidosrrvv.models.SMSParametros;
import cl.cns.integracion.wsreferidosrrvv.services.EncuestaService;
import cl.cns.integracion.wsreferidosrrvv.services.ReferidosValidaciones;
import cl.cns.integracion.wsreferidosrrvv.util.LogInterceptor;
import cl.cns.integracion.wsreferidosrrvv.vo.*;
import com.wordnik.swagger.annotations.*;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
@Stateless
@Path("cl.cnsv.referidosrrvv.models.encuesta")
@Api(value = "cl.cnsv.referidosrrvv.models.encuesta", description = "Servicios Para mantener las preguntas y respuesta de la encuesta del referido")
public class EncuestaFacadeREST {

        @Inject
        EncuestaService encuestaService;

        private static final Logger LOGGER = Logger.getLogger(
                        EncuestaFacadeREST.class);

        @GET
        @Path("/rut/{rut}")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Obtiene datos de la encuesta para un referido", response = RespuestasOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 404, message = "No se encontro el dato"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public RespuestasOutput findRespuestasPorRut(
                        @ApiParam(value = "Rut en formato 12312312-3", required = true) @PathParam("rut") String rut) {
                return new RespuestasOutput(
                                encuestaService.findRespuestasPorRut(validaYnormalizaRut(rut)));
        }

        private String validaYnormalizaRut(String rut) {
                if (!ReferidosValidaciones.validarRut(rut)) {
                        throw new BadRequestException(
                                        "El rut no tiene el formato correcto o es inválido, el formato debe ser por ejemplo 11111111-1");
                }
                // normaliza dejando en formato 1111111-1 (con quion y sin puntos)
                return rut.replaceAll("[\\.\\-]", "").replaceAll("[\\dkK]$", "-$0");
        }

        @DELETE
        @Path("/rut/{rut}")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Elimina los datos del la encuesta para un referido", response = RespuestasOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 404, message = "No se encontro el dato"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public GenericOutput deleteRespuestasPorRut(
                        @ApiParam(value = "Rut en formato 12312312-3", required = true) @PathParam("rut") String rut) {
                encuestaService.deleteRespuestasPorRut(validaYnormalizaRut(rut));
                return GenericOutput.ok();
        }

        @GET
        @Path("/rut/{rut}/idpregunta/{idpregunta}")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Obtiene datos del la encuesta para un referido", response = RespuestasOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 404, message = "No se encontro el dato"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public RespuestasOutput findRespuestasPorRutAndIdPregunta(
                        @ApiParam(value = "Rut en formato 12312312-3", required = true) @PathParam("rut") String rut,
                        @PathParam("idpregunta") Long idpregunta) {
                return new RespuestasOutput(
                                encuestaService.findRespuestasPorRutAndIdPregunta(
                                                validaYnormalizaRut(rut),
                                                idpregunta));
        }

        @PUT
        @Path("/respuesta")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Crea o actualiza rspuestas de una encuesta", response = GenericOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public GenericOutput guardaRespuestas(
                        @Valid ListarespuestasWrapper listarespuestas) {
                listarespuestas.setRut(validaYnormalizaRut(listarespuestas.getRut()));
                try {
                        encuestaService.guardaRespuestas(listarespuestas);
                } catch (NegocioException e) {
                        LOGGER.error(e);
                        throw new InternalServerErrorException(e.getMessage());
                } catch (Exception e) {
                        LOGGER.error(e);
                        throw new InternalServerErrorException(
                                        "Ocurio un error al grabar las respuestas");
                }
                return GenericOutput.ok();
        }

        @PUT
        @Path("/respuesta/{envio}")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Crea o actualiza rspuestas de una encuesta", response = GenericOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public GenericOutput guardaRespuestasConEnvio(
                        @Valid ListarespuestasWrapper listarespuestas,
                        @ApiParam(value = "1 = Envía comunicación, 2 = No envía", required = true) @PathParam("envio") Long flagEnvio) {
                listarespuestas.setRut(validaYnormalizaRut(listarespuestas.getRut()));
                try {
                        Boolean envio = flagEnvio == 1;
                        encuestaService.guardaRespuestasConEnvio(listarespuestas, envio);
                } catch (NegocioException e) {
                        LOGGER.error(e);
                        throw new InternalServerErrorException(e.getMessage());
                } catch (Exception e) {
                        LOGGER.error(e);
                        throw new InternalServerErrorException(
                                        "Ocurio un error al grabar las respuestas");
                }
                return GenericOutput.ok();
        }

        @GET
        @Path("/preguntas")
        @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
        @ApiOperation(value = "Obtiene todas las preguntas", response = PreguntasOutput.class)
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "OK"),
                        @ApiResponse(code = 400, message = "Ocurrió un error de validación"),
                        @ApiResponse(code = 500, message = "Ocurrió un error no identificado"), })
        @Interceptors(LogInterceptor.class)
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "Authorization", value = "Token de autorizacion", required = true, dataType = "string", paramType = "header"), })
        public PreguntasOutput obtenerPreguntas() {
                try {
                        return new PreguntasOutput(encuestaService.obtenerPreguntas());
                } catch (Exception e) {
                        LOGGER.error(e);
                        GenericOutput resp = new GenericOutput();
                        resp.setCodigo(1);
                        resp.setMensaje("No se pudo grabar las respuestas");
                        throw new InternalServerErrorException();
                }
        }
}
