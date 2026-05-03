//= wrapped

angular
    .module("todo")
    .factory("VerificaReferenciaActiva", VerificaReferenciaActiva);

function VerificaReferenciaActiva($resource) {
    var VerificaReferenciaActiva = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/verificaReferenciaActiva/:rut",
        {"rut": "@rut"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return VerificaReferenciaActiva;
}
  