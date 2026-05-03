//= wrapped

angular
    .module("todo")
    .factory("Ejecutivos", Ejecutivos);

function Ejecutivos($resource) {
    var Ejecutivos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.ejecutivos/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return Ejecutivos;
}
 