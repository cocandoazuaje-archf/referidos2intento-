//= wrapped

angular
    .module("todo")
    .factory("ImportarReferidos", ImportarReferidos);

function ImportarReferidos($resource) {
    var ImportarReferidos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/importarreferidos",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return ImportarReferidos;
}
 