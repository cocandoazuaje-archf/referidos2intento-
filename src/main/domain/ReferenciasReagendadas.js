//= wrapped

angular
    .module("todo")
    .factory("ReferenciasReagendadas", ReferenciasReagendadas);

function ReferenciasReagendadas($resource) {
    var ReferenciasReagendadas = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/reagendada/:id/:tipo", {
            "id": "@id", "tipo":"@tipo"
        }, {
            "update": {  
                method: "PUT"
            },
            "list": {
                method: "GET",
                isArray: true
            }
        }
    );
    return ReferenciasReagendadas;
}