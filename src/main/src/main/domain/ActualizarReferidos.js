//= wrapped

angular
    .module("todo")
    .factory("ActualizarReferidos", ActualizarReferidos);

function ActualizarReferidos($resource) {
    var ActualizarReferidos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/actualizarreferido",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ActualizarReferidos;
}
 