//= wrapped

angular
    .module("todo")
    .factory("Referidos", Referidos);

function Referidos($resource) {
    var Referidos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return Referidos;
}
 