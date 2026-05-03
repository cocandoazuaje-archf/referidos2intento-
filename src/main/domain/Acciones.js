//= wrapped

angular
    .module("todo")
    .factory("Acciones", Acciones);

function Acciones($resource) {
    var Acciones = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.acciones/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return Acciones;
}
 