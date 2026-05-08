//= wrapped

angular
    .module("todo")
    .factory("Referencias", Referencias);

function Referencias($resource) {
    var Referencias = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/:id",
        {"id": "@id"},
        {"update": {method: "PUT"}, "list": {method: "GET", isArray: false}}
    );
    return Referencias;
}
 