//= wrapped

angular
    .module("todo")
    .factory("ImportarRolesUsuarios", ImportarRolesUsuarios);

function ImportarRolesUsuarios($resource) {
    var ImportarRolesUsuarios = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.rolesusuarios/importarrolesusuarios",
        {},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return ImportarRolesUsuarios;
}
 