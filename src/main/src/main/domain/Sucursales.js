//= wrapped

angular
    .module("todo")
    .factory("Sucursales", Sucursales);

function Sucursales($resource) {
    var Sucursales = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.sucursales/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return Sucursales;
}
 