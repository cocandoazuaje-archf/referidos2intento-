//= wrapped

angular
    .module("todo")
    .factory("ReferenciasNoCerradas", ReferenciasNoCerradas);

function ReferenciasNoCerradas($resource) {
    var ReferenciasNoCerradas = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/nocerrado/:id/:tipo",
        {"id": "@id", "tipo":"@tipo"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ReferenciasNoCerradas;
}
  