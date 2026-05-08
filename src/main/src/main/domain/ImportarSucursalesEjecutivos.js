//= wrapped

angular
    .module("todo")
    .factory("ImportarSucursalesEjecutivos", ImportarSucursalesEjecutivos);

function ImportarSucursalesEjecutivos($resource) {
    var ImportarSucursalesEjecutivos = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/importarsucursalesejecutivos",
        {},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return ImportarSucursalesEjecutivos;
}
 