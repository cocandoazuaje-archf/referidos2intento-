//= wrapped

angular
    .module("todo")
    .factory("ExportarReferidos", ExportarReferidos);

function ExportarReferidos($resource) {
    var ExportarReferidos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/exportarreferidos/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return ExportarReferidos;
}
 