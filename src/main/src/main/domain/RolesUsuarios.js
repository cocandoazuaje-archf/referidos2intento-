//= wrapped

angular
    .module("todo")
    .factory("RolesUsuarios", RolesUsuarios);

function RolesUsuarios($resource) {
    var RolesUsuarios = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.rolesusuarios/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: true}}
    );
    return RolesUsuarios;
}
 