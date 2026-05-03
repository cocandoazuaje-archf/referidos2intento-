//= wrapped

angular
    .module("todo")
    .factory("ActualizarReferidosNo", ActualizarReferidosNo);

function ActualizarReferidosNo($resource) {
    var ActualizarReferidosNo = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/actualizarreferidono",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ActualizarReferidosNo;
}
 