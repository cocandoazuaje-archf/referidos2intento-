//= wrapped

angular
    .module("todo")
    .factory("ImportarSucursales", ImportarSucursales);

function ImportarSucursales($resource) {
    var ImportarSucursales = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.sucursales/importarsucursales",
        {},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return ImportarSucursales;
}
 