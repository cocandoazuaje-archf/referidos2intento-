//= wrapped

angular
    .module("todo")
    .factory("RolesUsuariosNombre", RolesUsuariosNombre);

function RolesUsuariosNombre($resource) {
    var RolesUsuariosNombre = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.rolesusuarios/nombre/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return RolesUsuariosNombre;
}
 