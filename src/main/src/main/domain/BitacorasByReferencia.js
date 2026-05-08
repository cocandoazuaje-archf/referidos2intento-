//= wrapped

angular
    .module("todo")
    .factory("BitacorasByReferencia", BitacorasByReferencia);

function BitacorasByReferencia($resource) {
    var BitacorasByReferencia = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.bitacoras/byreferencia/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return BitacorasByReferencia;
}
 