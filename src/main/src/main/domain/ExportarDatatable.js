//= wrapped

angular
    .module("todo")
    .factory("ExportarDatatable", ExportarDatatable);

function ExportarDatatable($resource) {

    var ExportarDatatable = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/exportardatatable/:id/:sup/:anio/:mes/:suc", { "id": "@id", "sup": "@sup", "anio": "@anio", "mes": "@mes", "suc": "@suc" }, { "update": { method: "PUT" }, "list": { method: "GET", isArray: true } }
    );
    return ExportarDatatable;
}