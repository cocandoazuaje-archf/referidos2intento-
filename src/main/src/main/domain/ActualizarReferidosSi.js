//= wrapped

angular
    .module("todo")
    .factory("ActualizarReferidosSi", ActualizarReferidosSi);

function ActualizarReferidosSi($resource) {
    var ActualizarReferidosSi = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/actualizarreferidosi",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ActualizarReferidosSi;
}
 