//= wrapped

angular
    .module("todo")
    .factory("ReferenciasReagendadasFull", ReferenciasReagendadasFull);

function ReferenciasReagendadasFull($resource) {
    var ReferenciasReagendadasFull = $resource(
        "/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/reagendada/full", {
            "id": "@id"
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
    return ReferenciasReagendadasFull;
}