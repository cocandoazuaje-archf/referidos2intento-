//= wrapped

angular
    .module("todo")
    .factory("Canales", Canales);

function Canales($resource) {
    var Canales = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/canales",
        {},
        {"list": {method: "GET", isArray: true}}
    );
    return Canales;
}
 