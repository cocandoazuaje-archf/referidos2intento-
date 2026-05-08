package cl.cns.integracion.wsreferidosrrvv.services;

import cl.cns.integracion.wsreferidosrrvv.exceptions.NegocioException;
import cl.cns.integracion.wsreferidosrrvv.models.*;
import cl.cns.integracion.wsreferidosrrvv.util.DatosProperties;
import cl.cns.integracion.wsreferidosrrvv.util.EMCUtil;
import cl.cns.integracion.wsreferidosrrvv.util.LogInterceptor;
import cl.cns.integracion.wsreferidosrrvv.util.SMSUtil;
import cl.cns.integracion.wsreferidosrrvv.vo.*;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Pregunta;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.apache.log4j.Logger;

@Stateless
public class EncuestaService {

  @PersistenceContext(unitName = "com.cox_referidos_war_1.0PU")
  private transient EntityManager em;

  @Context
  SecurityContext sc;

  @Inject
  ReferidosService referidosService;

  private String valueNoseEncontro = "No se encontro el referido con rut: ";

  private static final Logger LOGGER = Logger.getLogger(EncuestaService.class);
  private static final DatosContactoPropertiesMainVO propertiesVO = DatosProperties.loadPropertiesMain();

  @Interceptors(LogInterceptor.class)
  public List<RespuestaEncuesta> findRespuestasPorRut(String rut) {
    Query cnq = em.createNamedQuery(
        "RespuestaEncuesta.findRespuestasByRut",
        RespuestaEncuesta.class);
    // Query cnq = em.createNativeQuery("RespuestaEncuesta.findRespuestasByRut",
    // RespuestaEncuesta.class);
    cnq.setParameter("rut", rut);
    return cnq.getResultList();
  }

  @Interceptors(LogInterceptor.class)
  public List<RespuestaEncuesta> findRespuestasPorRutAndIdPregunta(
      String rut,
      Long idPregunta) {
    Query cnq = em.createNamedQuery(
        "RespuestaEncuesta.findRespuestasByRutAndIdPregunta",
        RespuestaEncuesta.class);
    cnq.setParameter("rut", rut);
    cnq.setParameter("idPregunta", idPregunta);
    return cnq.getResultList();
  }

  @Interceptors(LogInterceptor.class)
  public void guardaRespuestas(ListarespuestasWrapper listarespuestas)
      throws NegocioException {
    List<Referidos> referidos = ServiceUtils.findBy(
        em,
        "rut",
        listarespuestas.getRut(),
        Referidos.class);
    BigDecimal idreferido = null;
    if (!referidos.isEmpty()) {
      idreferido = referidos.get(0).getId();
    } else {
      LOGGER.error(
          valueNoseEncontro +
              listarespuestas.getRut() +
              "No se realizara ninguna actualizacion");
      throw new NegocioException(
          valueNoseEncontro +
              listarespuestas.getRut() +
              " No se realizara ninguna actualizacion");
    }
    List<RespuestaEncuesta> anteriores = findRespuestasPorRut(
        listarespuestas.getRut());
    for (RespuestaEncuestaInput r : listarespuestas.getRespuestas()) {
      // se busca la respuesta anterior a la pregunta para revisar si ae debe
      // actualizae la respuesta anterior o se debe crear una nueva
      RespuestaEncuesta valores = anteriores
          .stream()
          .filter(resp -> r.getIdPregunta().equals(resp.getIdPregunta()))
          .findFirst()
          .orElse(null);
      if (valores != null) {
        valores.setRespuesta(r.getRespuesta());
        valores.setDescripcionRespuesta(r.getDescripcionRespuesta());
        valores.setFechaRespuesta(r.getFechaRespuesta());
      } else {
        valores = r.toModel();
      }
      valores.setIdReferido(idreferido);
      valores.setFecha(new Date());
      valores.setUsuario(ServiceUtils.geUsername(sc));
      valores.setVersion(1l);
      em.persist(valores);
      em.flush();
      em.refresh(valores);
      // Crear bitacora
      // crearBitacora(listarespuestas);
    }
  }

  @Interceptors(LogInterceptor.class)
  public void guardaRespuestasConEnvio(
      ListarespuestasWrapper listarespuestas,
      boolean envio) throws NegocioException {
    List<Referidos> referidos = ServiceUtils.findBy(
        em,
        "rut",
        listarespuestas.getRut(),
        Referidos.class);
    BigDecimal idreferido = null;
    if (!referidos.isEmpty()) {
      idreferido = referidos.get(0).getId();
    } else {
      LOGGER.error(
          valueNoseEncontro +
              listarespuestas.getRut() +
              "No se realizara ninguna actualizacion");
      throw new NegocioException(
          valueNoseEncontro +
              listarespuestas.getRut() +
              " No se realizara ninguna actualizacion");
    }
    List<RespuestaEncuesta> anteriores = findRespuestasPorRut(
        listarespuestas.getRut());
    for (RespuestaEncuestaInput r : listarespuestas.getRespuestas()) {
      // se busca la respuesta anterior a la pregunta para revisar si ae debe
      // actualizae la respuesta anterior o se debe crear una nueva
      RespuestaEncuesta valores = anteriores
          .stream()
          .filter(resp -> r.getIdPregunta().equals(resp.getIdPregunta()))
          .findFirst()
          .orElse(null);

      if (valores != null) {
        valores.setRespuesta(r.getRespuesta());
        valores.setDescripcionRespuesta(r.getDescripcionRespuesta());
        valores.setFechaRespuesta(r.getFechaRespuesta());
      } else {
        valores = r.toModel();
      }

      ArrayList<String> preguntasContacto = new ArrayList<>(
          Arrays.asList(propertiesVO.getAPIEMPreguntasEnvio().split(",")));
      Boolean respuestaContacto = propertiesVO.isAPIEMRespuestaEnvio();

      LOGGER.info(
          "[EncuestaService][guardaRespuestasConEnvio] Evaluar envío comunicaciones con parámetros:");
      LOGGER.info(
          "[EncuestaService][guardaRespuestasConEnvio] Enviar comunicaciones = " +
              envio);
      LOGGER.info(
          "[EncuestaService][guardaRespuestasConEnvio] Pregunta contestada = " +
              r.getIdPregunta().toString() +
              ", Preguntas esperadas = " +
              preguntasContacto);
      LOGGER.info(
          "[EncuestaService][guardaRespuestasConEnvio] Respuesta indicada = " +
              r.getRespuesta() +
              ", Respuesta esperada = " +
              respuestaContacto);
      valores.setIdReferido(idreferido);
      valores.setFecha(new Date());

      valores.setUsuario(ServiceUtils.geUsername(sc));
      valores.setVersion(1l);
      LOGGER.info(
          "[EncuestaService][guardaRespuestasConEnvio]  Antes de persistir");
      em.persist(valores);
      em.flush();
      em.refresh(valores);
      crearBitacora(listarespuestas);

      if (envio &&
          preguntasContacto.contains(r.getIdPregunta().toString()) &&
          (r.getRespuesta() == respuestaContacto)) {
        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] INI Envío comunicaciones");
        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] Enviando Correo a referido: ");
        this.enviarCorreo(listarespuestas.getRut(), r.getIdPregunta());
        SMSParametros paramSMS = new SMSParametros();

        String nombreReferido = referidos.get(0).getNombre().split(" ")[0];
        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] Nombre referido split: " +
                nombreReferido);
        int maxNombre = propertiesVO.getSMSMaxNombre();
        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] Param Max Length Nombre referido: " +
                maxNombre);
        nombreReferido = nombreReferido.length() >= maxNombre
            ? nombreReferido.substring(0, maxNombre)
            : nombreReferido;
        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] Nombre substr: " +
                nombreReferido);
        paramSMS.setParametro1(nombreReferido);
        referidos
            .get(0)
            .setTelefonos(
                referidos.get(0).getTelefonos() == null
                    ? "0"
                    : referidos.get(0).getTelefonos());

        LOGGER.info(
            "[EncuestaService][guardaRespuestasConEnvio] Enviando SMS a referido: " +
                referidos.get(0).toString());
        this.enviarSMS(
            referidos.get(0).getTelefonos(),
            r.getIdPregunta().intValue(),
            paramSMS);
      }
    }
  }

  @Interceptors(LogInterceptor.class)
  private void crearBitacora(ListarespuestasWrapper listarespuestas) {
    LOGGER.info("[EncuestaService][guardaRespuestasConEnvio]  Buscando Ref");
    Referencias idultimareferencia = referidosService.findReferidoActivo(
        listarespuestas.getRut());
    if (idultimareferencia == null) {
      LOGGER.error(
          "No se puedo insertar un registro en la bitacora porque el referido no tiene una referencia activa" +
              listarespuestas);
      return;
    }

    Bitacoras b = new Bitacoras();
    b.setComentarios("Actualizacion encuesta");
    b.setFecha(new Date());

    LOGGER.info(
        "[EncuestaService][guardaRespuestasConEnvio]  Bidultimareferencia" +
            idultimareferencia);
    LOGGER.info(
        "[EncuestaService][guardaRespuestasConEnvio]  id ref:" +
            idultimareferencia.getId());

    b.setReferenciaId(idultimareferencia);
    b.setVersion(BigInteger.ONE);
    b.setUsuario(ServiceUtils.geUsername(sc));
    em.persist(b);
    em.flush();
    LOGGER.info(
        "[EncuestaService][guardaRespuestasConEnvio]  termino metodo crearBitacora");
  }

  public List<Pregunta> obtenerPreguntas() {
    return ServiceUtils.findAll(em, Pregunta.class);
  }

  @Interceptors(LogInterceptor.class)
  public void deleteRespuestasPorRut(String rut) {
    List<Referidos> referido = ServiceUtils.findBy(
        em,
        "rut",
        rut,
        Referidos.class);
    Query cnq = em.createNamedQuery(
        "RespuestaEncuesta.deleteRespuestasByIdReferido",
        RespuestaEncuesta.class);
    if (referido != null && !referido.isEmpty()) {
      cnq.setParameter("idreferido", referido.get(0).getId());
      cnq.executeUpdate();
    }
  }

  @Interceptors(LogInterceptor.class)
  public EnvioCorreoOutput enviarCorreo(String rut, Long idPregunta) {
    try {
      LOGGER.info("[EncuestaService][enviarCorreo] INI");
      LOGGER.info(
          "[EncuestaService][enviarCorreo] RUT: " +
              rut +
              ", IDPREGUNTA: " +
              idPregunta);

      EnvioCorreoOutput output = new EnvioCorreoOutput();

      Referencias referencias = referidosService.findReferidoActivo(rut);

      if (referencias != null) {
        LOGGER.info("[EncuestaService][enviarCorreo] Referencia encontrada");

        Referidos referido = referencias.getReferidoId();
        LOGGER.info(
            "[EncuestaService][enviarCorreo] Referido encontrado: " +
                referido.getNombre() +
                " " +
                referido.getApellido());
        List<RespuestaEncuesta> respuestas = this.findRespuestasPorRutAndIdPregunta(rut, idPregunta);
        if (referido.getCorreo() != null) {
          if (respuestas != null) {
            LOGGER.info(
                "[EncuestaService][enviarCorreo] Configurando emailMaticoCall");
            // output = EMCUtil.enviaCorreo(referido, idPregunta);

          } else {
            // El referido no ha contestado la pregunta indicada
            output.setCodigo(0);
            output.setMensaje(
                "El referido no ha contestado la pregunta indicada.");
          }
        } else {
          // El referido no ha contestado la pregunta indicada
          output.setCodigo(0);
          output.setMensaje("El referido no cuenta con correo.");
        }
      } else {
        // El referido no se encuentra en DB
        output.setCodigo(0);
        output.setMensaje("El referido no existe en base de datos.");
      }

      return output;
    } catch (Exception e) {
      LOGGER.error(e);
      return null;
    }
  }

  @Interceptors(LogInterceptor.class)
  public SMSOutput enviarSMS(
      String movil,
      int idPregunta,
      SMSParametros parametros) {
    try {
      LOGGER.info(
          "[EncuestaService][enviarSMS] Iniciando llamada a SMSUtil com parámetros " +
              movil +
              ":" +
              idPregunta +
              ":" +
              parametros.toString());

      SMSOutput outputError = new SMSOutput(0, "Número de móvil no válido");

      LOGGER.info("[EncuestaService][enviarSMS] Validando numero de móvil");
      if (movil.length() < 8) {
        LOGGER.error(
            "[EncuestaService][enviarSMS] Número de móvil no válido (largo): " +
                movil);
        return outputError;
      } else {
        String movilAux = movil;

        if (movil.length() > 8) {
          movilAux = movil.substring(movil.length() - 8, movil.length());
        }

        String regex = "[5-9][0-9]{7}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(movilAux);

        if (m.matches()) {
          movilAux = "9" + movilAux;

          LOGGER.info(
              "[EncuestaService][enviarSMS] Número de móvil válido: " + movilAux);
          return SMSUtil.enviaSMS(movilAux, idPregunta, parametros);
        } else {
          LOGGER.error(
              "[EncuestaService][enviarSMS] Número de móvil no válido: " +
                  movilAux);
          return outputError;
        }
      }
    } catch (Exception e) {
      LOGGER.error(e);
      return null;
    }
  }
}
