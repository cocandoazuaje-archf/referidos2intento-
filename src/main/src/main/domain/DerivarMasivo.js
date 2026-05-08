//= wrapped

angular
    .module("todo")
    .factory("DerivarMasivo", DerivarMasivo);

function DerivarMasivo($resource) {
    var DerivarMasivo = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/derivarmasivo/:usr/:id/:eje/:sup/:anio/:mes/:suc",
        {"usr": "@usr", "id": "@id", "eje":"@eje", "sup":"@sup", "anio":"@anio", "mes":"@mes", "suc":"@suc"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return DerivarMasivo;
}
 