/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferidosJpaController;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosReferidos;
import cl.cns.integracion.wsreferidosrrvv.vo.ReferidosRRVV_Out;
import cl.cnsv.referidosrrvv.controller.exceptions.IllegalOrphanException;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import cl.cnsv.referidosrrvv.models.Referidos;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cow
 */
public class ActualizarDatosReferidos {

    public void actualizarSinUso2(
            List<EntidadDeCargaJs> entity,
            EntityManager em)
            throws NonexistentEntityException, RollbackFailureException, IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException {
        ReferidosJpaController rc = new ReferidosJpaController(em);

        for (EntidadDeCargaJs e : entity) {
            Referidos r = rc.findReferidos(e.getID());
            r.setFechanac(e.getFECHANAC());
            rc.edit(r);
        }
    }

    public ReferidosRRVV_Out actualizarReferido(
            DatosReferidos entity,
            EntityManager em) throws NonexistentEntityException, RollbackFailureException {
        ReferidosRRVV_Out retornoActu = new ReferidosRRVV_Out();
        try {
            ReferidosJpaController rc = new ReferidosJpaController(em);
            ReferenciasJpaController rfc = new ReferenciasJpaController(em);
            BitacorasJpaController bc = new BitacorasJpaController(em);
            Date datefechanac = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            String fechaNacimiento = entity.getFechanacimiento() != null
                    ? entity.getFechanacimiento()
                    : " ";

            if (!fechaNacimiento.trim().equals("")) {
                datefechanac = formatter.parse(fechaNacimiento);
            }
            BigInteger Version = (entity.getVersion() != null ? entity.getVersion() : BigInteger.ONE);

            Referidos r = rc.findByRut(entity.getRut());

            if (r != null) {
                continuar(entity, r);

                continuar2(entity, r);

                continuar3(entity, r, datefechanac);

                continuar4(entity, r);

                continuar5(entity, r);

                continuar6(entity, r);

                continuar7(entity, r, Version);

                rc.edit(r);

                Referencias rf = rfc.findReferenciasIdReferido(r);

                String comentarios = "DATOS DEL REFERIDO ACTUALIZADO CORRECTAMENTE-> ";

                Bitacoras b = new Bitacoras();
                b.setFecha(new Date());
                b.setComentarios(comentarios);
                b.setVersion(Version);
                b.setUsuario(entity.getUsuario());
                b.setReferenciaId(rf);
                bc.create(b);
                retornoActu.setCodigo(0);
                retornoActu.setMensaje("Ok.");
            } else {
                retornoActu.setCodigo(1);
                retornoActu.setMensaje("El referido que quiere actualizar no existe");
            }
        } catch (Exception e) {
            retornoActu.setCodigo(-1);
            retornoActu.setMensaje("Error" + e);
        }
        return retornoActu;
    }

    private void continuar(DatosReferidos entity, Referidos r) {
        String apellido;
        if (entity.getApellidos() == null) {
            if (r.getApellido() == null) {
                apellido = " ";
            } else {
                apellido = r.getApellido();
            }
        } else {
            apellido = entity.getApellidos();
        }
        r.setApellido(apellido.trim().toUpperCase());

        String calle;
        if (entity.getCalle() == null) {
            if (r.getCalle() == null) {
                calle = "";
            } else {
                calle = r.getCalle();
            }
        } else {
            calle = entity.getCalle();
        }
        r.setCalle(calle.trim().toUpperCase());
    }

    private void continuar2(DatosReferidos entity, Referidos r) {
        String comuna;
        if (entity.getComuna() == null) {
            if (r.getComuna() == null) {
                comuna = "";
            } else {
                comuna = r.getComuna();
            }
        } else {
            comuna = entity.getComuna();
        }
        r.setComuna(comuna.trim().toUpperCase());

        String correo;
        if (entity.getCorreo() == null) {
            if (r.getCorreo() == null) {
                correo = "";
            } else {
                correo = r.getCorreo();
            }
        } else {
            correo = entity.getCorreo();
        }
        r.setCorreo(correo.trim().toUpperCase());
    }

    private void continuar3(
            DatosReferidos entity,
            Referidos r,
            Date datefechanac) {
        String dptoCasa;
        if (entity.getDpto_casa() == null) {
            if (r.getDptoCasa() == null) {
                dptoCasa = "";
            } else {
                dptoCasa = r.getDptoCasa();
            }
        } else {
            dptoCasa = entity.getDpto_casa();
        }
        r.setDptoCasa(dptoCasa.trim().toUpperCase());

        Date fechanac;
        if (datefechanac == null) {
            if (r.getFechanac() == null) {
                fechanac = null;
            } else {
                fechanac = r.getFechanac();
            }
        } else {
            fechanac = datefechanac;
        }
        r.setFechanac(fechanac);
    }

    private void continuar4(DatosReferidos entity, Referidos r) {
        String nombre;
        if (entity.getNombre() == null) {
            if (r.getNombre() == null) {
                nombre = "";
            } else {
                nombre = r.getNombre();
            }
        } else {
            nombre = entity.getNombre();
        }
        r.setNombre(nombre.trim().toUpperCase());

        String numDptoCasa;
        if (entity.getNum_dpto_casa() == null) {
            if (r.getNumDptoCasa() == null) {
                numDptoCasa = "";
            } else {
                numDptoCasa = r.getNumDptoCasa();
            }
        } else {
            numDptoCasa = entity.getNum_dpto_casa();
        }
        r.setNumDptoCasa(numDptoCasa);

        String region;
        if (entity.getRegion() == null) {
            if (r.getRegion() == null) {
                region = "";
            } else {
                region = r.getRegion();
            }
        } else {
            region = entity.getRegion();
        }
        r.setRegion(region.trim().toUpperCase());
    }

    private void continuar5(DatosReferidos entity, Referidos r) {
        continuar01(entity, r);
        // Extraer el operador ternario para r.setTelefonos()
        String telefonos;
        if (entity.getTelefono() == null) {
            if (r.getTelefonos() == null) {
                telefonos = "";
            } else {
                telefonos = r.getTelefonos();
            }
        } else {
            telefonos = entity.getTelefono();
        }
        r.setTelefonos(telefonos.trim().toUpperCase());

        continuar02(entity, r);
    }

    private void continuar6(DatosReferidos entity, Referidos r) {
        continuar03(entity, r);

        if (entity.getClientesolicito() == null) {
            if (r.getClienteSolicito() == null) {
                r.setClienteSolicito("");
            } else {
                r.setClienteSolicito(r.getClienteSolicito());
            }
        } else {
            r.setClienteSolicito(entity.getClientesolicito());
        }
        r.setClienteSolicito(r.getClienteSolicito().trim().toUpperCase());

        if (entity.getAccionrealizo() == null) {
            if (r.getAccionRealizo() == null) {
                r.setAccionRealizo("");
            } else {
                r.setAccionRealizo(r.getAccionRealizo());
            }
        } else {
            r.setAccionRealizo(entity.getAccionrealizo());
        }
        r.setAccionRealizo(r.getAccionRealizo().trim().toUpperCase());
    }

    private void continuar7(
            DatosReferidos entity,
            Referidos r,
            BigInteger Version) {
        if (entity.getTipopension() == null) {
            if (r.getTipoPension() == null) {
                r.setTipoPension("");
            } else {
                r.setTipoPension(r.getTipoPension());
            }
        } else {
            r.setTipoPension(entity.getTipopension());
        }
        r.setTipoPension(r.getTipoPension().trim().toUpperCase());

        r.setVersion(Version);

        if (entity.getSexo() == null) {
            if (r.getSexo() == null) {
                r.setSexo("");
            } else {
                r.setSexo(r.getSexo());
            }
        } else {
            r.setSexo(entity.getSexo());
        }
        r.setSexo(r.getSexo().trim().toUpperCase());
    }

    private void continuar01(DatosReferidos entity, Referidos r) {
        // Extraer el operador ternario para r.setRut()
        String rut;
        if (entity.getRut() == null) {
            if (r.getRut() == null) {
                rut = "";
            } else {
                rut = r.getRut();
            }
        } else {
            rut = entity.getRut();
        }
        r.setRut(rut.trim().toUpperCase());

        // Extraer el operador ternario para r.setScore()
        String score;
        if (entity.getScore() == null) {
            if (r.getScore() == null) {
                score = "";
            } else {
                score = r.getScore();
            }
        } else {
            score = entity.getScore();
        }
        r.setScore(score.trim().toUpperCase());
    }

    private void continuar02(DatosReferidos entity, Referidos r) {
        // Extraer el operador ternario para r.setTelefonos2()
        String telefonos2;
        if (entity.getTelefono2() == null) {
            if (r.getTelefonos2() == null) {
                telefonos2 = "";
            } else {
                telefonos2 = r.getTelefonos2();
            }
        } else {
            telefonos2 = entity.getTelefono2();
        }
        r.setTelefonos2(telefonos2.trim().toUpperCase());
    }

    private void continuar03(DatosReferidos entity, Referidos r) {
        // Extraer el operador ternario para r.setTelefonos3()
        String telefonos3;
        if (entity.getTelefono3() == null) {
            if (r.getTelefonos3() == null) {
                telefonos3 = "";
            } else {
                telefonos3 = r.getTelefonos3();
            }
        } else {
            telefonos3 = entity.getTelefono3();
        }
        r.setTelefonos3(telefonos3.trim().toUpperCase());

        // Extraer el operador ternario para r.setPensionarse()
        String pensionarse;
        if (entity.getPensionarse() == null) {
            if (r.getPensionarse() == null) {
                pensionarse = "";
            } else {
                pensionarse = r.getPensionarse();
            }
        } else {
            pensionarse = entity.getPensionarse();
        }
        r.setPensionarse(pensionarse.trim().toUpperCase());
    }
}
