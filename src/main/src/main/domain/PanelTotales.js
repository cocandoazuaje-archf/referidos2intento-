//= wrapped

angular
.module("todo")
.factory("PanelTotales", PanelTotales);

function PanelTotales($resource) {
	var PanelTotales = $resource(
		"/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/paneltotales/:sup/:anio/:mes/:suc",
		{"sup":"@sup", "anio":"@anio", "mes":"@mes", "suc":"@suc"},
		{"update": {method: "PUT"},  "list": {method: "GET", isArray: false}}
		);
	return PanelTotales;
}
