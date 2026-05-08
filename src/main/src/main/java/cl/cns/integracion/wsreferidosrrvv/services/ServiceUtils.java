package cl.cns.integracion.wsreferidosrrvv.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.SecurityContext;
import org.apache.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class ServiceUtils {

    private static final Logger LOGGER = Logger.getLogger(ServiceUtils.class);

    public static <T> List<T> findBy(
            EntityManager em,
            String nombreColumna,
            String valorBuscado,
            Class<T> clase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clase);
        Root<T> c = q.from(clase);
        CriteriaQuery<T> q2 = q.select(c);
        ParameterExpression<String> p = cb.parameter(String.class);
        q2.where(cb.equal(c.get(nombreColumna), valorBuscado));
        TypedQuery<T> tquery = em.createQuery(q);
        return tquery.getResultList();
    }

    public static <T> List<T> findAll(EntityManager em, Class<T> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = em
                .getCriteriaBuilder()
                .createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public static String geUsername(SecurityContext sc) {
        String userName = null;
        try {
            // se usa el id como usuario
            userName = sc.getUserPrincipal().getName();
            // si esta loguado con un token de keycloack se usa el login name
            if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc
                        .getUserPrincipal();
                userName = kp.getKeycloakSecurityContext().getToken().getPreferredUsername();
            }
        } catch (Exception e) {
            userName = "ANONIMO";
            LOGGER.error("No se pudo obtener el usuario loguado desde el token.", e);
        }
        return userName;
    }
}
