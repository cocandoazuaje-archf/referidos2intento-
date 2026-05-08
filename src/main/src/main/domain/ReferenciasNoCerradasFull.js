//= wrapped

angular
    .module("todo")
    .factory("ReferenciasNoCerradasFull", ReferenciasNoCerradasFull);

function ReferenciasNoCerradasFull($resource) {
    var ReferenciasNoCerradasFull = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/nocerrado/full",
        {},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ReferenciasNoCerradasFull;
}
 