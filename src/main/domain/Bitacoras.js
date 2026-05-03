//= wrapped

angular
    .module("todo")
    .factory("Bitacoras", Bitacoras);

function Bitacoras($resource) {
    var Bitacoras = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.bitacoras/:id", { "id": "@id" }, { "delete": { method: "DELETE" }, "update": { method: "PUT" }, "list": { method: "GET", isArray: true } }
    );
    return Bitacoras;
}