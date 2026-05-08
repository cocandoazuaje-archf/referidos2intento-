angular
    .module("todo").config(function ($mdDateLocaleProvider) {
        $mdDateLocaleProvider.formatDate = function (date) {
            return date ? moment(date).format('DD-MM-YYYY') : '';
        };
    })
    .controller("TodoController", function (
        VerificaReferenciaActiva, Sucursales, Canales, RolesUsuarios, ExportarDatatable, DerivarMasivo,
        ImportarRolesUsuarios, ImportarSucursalesEjecutivos, ImportarSucursales,
        ExportarReferidos, RolesUsuariosNombre, ActualizarReferidosSi, ActualizarReferidosNo,
        ImportarReferidos, ReferenciasReagendadasFull, Referidos, PanelTotales,
        Ejecutivos, Referencias, ReferenciasReagendadas, Acciones, BitacorasByReferencia,
        Bitacoras, ReferenciasNoCerradas, $filter, $location, $http, $window, $scope,
        $resource, Auth) {

        //coxrestaurar poner arriba de ultimo en controller
        //, Auth 
 
        // Auth
        var vm = this;
        vm.usuario = '';
        vm.filtroPrioridad=false;

        //coxrestaurar 
        //vm.usuario = 'exitcs17';
        
        //vm.usuario = 'aeastorg';

        //vm.usuario = 'aeastorg'; 
        //vm.usuario = 'rentpriv'; 
        //vm.token = Auth.token;


        // console.log("*******************");
        // console.log(vm.usuario);
        // console.log("*******************");
        // console.log("*******************");
        // console.log(vm.usuario);
        // console.log("*******************"); 
        // console.log("*******************");
        // console.log(vm.usuario);
        // console.log("*******************");


        vm.rollUsuario = "";
        vm.dobleRoll = false;
        vm.old_rut = "";
        vm.canalDigital = [];
        vm.canalDigitalRespuestas = [];
        vm.verEncuesta1 = true;
        vm.verEncuesta2 = false;
        vm.traeRespuestas = 0;
        vm.fechaNac;
        vm.mesAnioFiltroGraph;
        vm.ReferenciasNoCerradas = [];
        vm.ReferenciasReagendadas = [];
        vm.referenciaSeleccionada = {};

        vm.anioParamUrlDataTable = 'undefined';
        vm.mesParamUrlDataTable = 'undefined';
        vm.ejecutivosSelectFiltroPanel = 'undefined';
        vm.sucursalesSelectFiltroPanel = 'undefined';
        vm.sucursalesSelectFiltroPanel2 = 'undefined';

        vm.op = 0;
        vm.filtroReferenciasNoCerradas = '';
        vm.filtroReferenciasTodoEje = '';
        vm.filtroReferenciasReagendadas = '';
        vm.filtroReferenciasReagendadasTodoEje = '';
        vm.ficha = {};


        vm.imagePath1 = 'img/ficharDatosPersonal.png';
        vm.imagePath2 = 'img/encuestaFicha.png';
        vm.imagePath3 = 'img/encuestaFicha.png';

        vm.mostrarLoading = false;
        vm.estoyViendoUnaRef = false;
        vm.acciones = [];
        vm.yaledi = false;
        vm.mostrarIngresarReferido = false;
        vm.verContactos = {
            contactos: true,
            actividades: false
        };
        vm.verProspectos = {
            prospectos: true,
            actividades: false
        };

        console.log("OK -> entro a TodoController");


        vm.logout = function () {
            Auth.logout();
            $window.reload();
        }

        vm.abriendoCalendar = function () {
            $('body').removeAttr('style');
        }



        vm.mensajesPreEstablecidos = [{
            "id": 1,
            "descrip": "AGENDADO"
        }, {
            "id": 2,
            "descrip": "ATENDIDO POR OTRO EJECUTIVO/ASESOR"
        }, {
            "id": 3,
            "descrip": "CERRADO EN AFP"
        }, {
            "id": 4,
            "descrip": "CERRADO EN OTRA CIA"
        }, {
            "id": 5,
            "descrip": "CIERRE TRAMITE CNL"
        }, {
            "id": 6,
            "descrip": "CIERRE TRAMITE CNS"
        }, {
            "id": 7,
            "descrip": "CLIENTE NO UBICABLE"
        }, {
            "id": 8,
            "descrip": "CLIENTE REFERIDO REPETIDO"
        }, {
            "id": 9,
            "descrip": "ESTUDIO PENSIÓN"
        }, {
            "id": 10,
            "descrip": "ESTUDIO PENSIÓN ANTICIPADA"
        }, {
            "id": 11,
            "descrip": "INICIA TRÁMITE"
        }, {
            "id": 12,
            "descrip": "NO APLICA"
        }, {
            "id": 13,
            "descrip": "NO CUMPLE REQUISITO EDAD"
        }, {
            "id": 14,
            "descrip": "NO CUMPLE REQUISITO PRIMA"
        }, {
            "id": 15,
            "descrip": "NO ESTA INTERESADO"
        }, {
            "id": 16,
            "descrip": "OFERTA EXTERNA"
        }, {
            "id": 17,
            "descrip": "POSTERGADO"
        }, {
            "id": 18,
            "descrip": "REQUERIMIENTO POST VENTA"
        }, {
            "id": 19,
            "descrip": "REQUIERE INFORMACIÓN"
        }, {
            "id": 20,
            "descrip": "SOLICITA INFORMACIÓN POR CORREO"
        }, {
            "id": 21,
            "descrip": "SOLICITA NUEVA LLAMADA"
        }, {
            "id": 22,
            "descrip": "SOLICITUD OTROS PRODUCTOS"
        }, {
            "id": 23,
            "descrip": "TELÉFONO ERRÓNEO"
        }, {
            "id": 24,
            "descrip": "TRÁMITE INVALIDEZ"
        }, {
            "id": 25,
            "descrip": "PENSIONADO"
        }, {
            "id": 26,
            "descrip": "CLIENTE FUTURO"
        }, {
            "id": 30,
            "descrip": "OTROS"
        }];


        vm.mostrarTodo = function () {
            vm.mesAnioFiltroGraph = null;
            vm.anioParamUrlDataTable = 'undefined';
            vm.mesParamUrlDataTable = 'undefined';
            vm.actualizarGraficoPorFecha();
        }

        vm.mostrarTodoSucursalesPanel = function () {

            vm.sucursalesSelectFiltroPanel = 'undefined';
            vm.actualizarGraficoPorSucursal();
        }

        vm.mostrarTodoEjecutivosPanel = function () {

            vm.ejecutivosSelectFiltroPanel = undefined;
            vm.usuarioSupervisaA2String = vm.ejecutivosSelectFiltroPanel;
            vm.actualizarGraficoPorEjecutivo();
        }

        vm.actualizarReferidosSi = function (jsonData, i) {

            //            console.log(JSON.stringify(jsonData[i]));

            actualizarReferidos = new ActualizarReferidosSi(jsonData[i]);
            actualizarReferidos.ID = parseInt(jsonData[i].ID);

            actualizarReferidos.NOMBRE = jsonData[i].Nombres_Tlmk;
            actualizarReferidos.USUARIO = vm.usuario;
            actualizarReferidos.COMENTARIOS = jsonData[i].Observaciones;
            actualizarReferidos.RUT = jsonData[i].Rut;

            actualizarReferidos.$save({},
                function (res) {
                    toastr.success("Se ha actualizado el referido con exito.", " Ok");

                    //console.log("*** Ok Actualizando referidos ");
                    //console.log(res);
                    i++;
                    if (jsonData[i]) {
                        vm.actualizarReferidosSi(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // console.log(" *********** salido1");

                    }

                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("Ha ocurrido un problema al actualizar el referido -> " + actualizarReferidos.NOMBRE, " " + err.status + " " + err.statusText)

                    i++;
                    if (jsonData[i]) {
                        vm.actualizarReferidosSi(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // console.log(" *********** salido1");

                    }

                    //console.log("*** Err Actualizando referidos ");
                    //console.log(err);

                });

        }

        vm.actualizarReferidosNo = function (jsonData, i) {

            //            console.log(JSON.stringify(jsonData[i]));

            actualizarReferidos = new ActualizarReferidosNo(jsonData[i]);
            actualizarReferidos.CANALNAME = jsonData[i].CANAL_REFERIDO;
            actualizarReferidos.ID = parseInt(jsonData[i].Id);
            actualizarReferidos.CONECTA_NOCONECTA = jsonData[i].Conecta_Noconecta;
            actualizarReferidos.NOMBRE = jsonData[i].NOMBRE_COMPLETO;
            actualizarReferidos.CORREO = jsonData[i].E_MAIL;
            actualizarReferidos.TELEFONO = jsonData[i].TELEFONO;
            actualizarReferidos.USUARIO = vm.usuario;
            actualizarReferidos.RUT = jsonData[i].Rut;

            actualizarReferidos.$save({},
                function (res) {
                    toastr.success("Se ha actualizado el referido con exito.", " Ok");

                    //console.log("*** Ok Actualizando referidos ");
                    //console.log(res);
                    i++;
                    if (jsonData[i]) {
                        vm.actualizarReferidosNo(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // console.log(" *********** salido1");

                    }

                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("Ha ocurrido un problema al actualizar el referido -> " + actualizarReferidos.NOMBRE, " " + err.status + " " + err.statusText)

                    i++;
                    if (jsonData[i]) {
                        vm.actualizarReferidosSi(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // console.log(" *********** salido1");

                    }

                    //console.log("*** Err Actualizando referidos ");
                    //console.log(err);

                });

        }

        vm.importarReferidos22 = function (jsonData, vengoDeLaFicha) {
            vm.mostrarLoading = true;
            if (!vengoDeLaFicha) {
                angular.forEach(jsonData, function (d) {
                    d.FECHA_RECEPCION = new Date((d.FECHA_RECEPCION - (25567 + 1)) * 86400 * 1000);
                    d.USUARIO = vm.usuario;
                });

            }


            //            console.log(JSON.stringify(jsonData));

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/importarreferidos',
                method: "POST",
                data: jsonData,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.ErrorCrearReferidoReferenciaBitacoraOut = res.data;
                if (vm.ErrorCrearReferidoReferenciaBitacoraOut.length > 0) {
                    vm.exportarErrorCrearReferidoReferenciaBitacora(vm.ErrorCrearReferidoReferenciaBitacoraOut);

                }
                vm.mostrarLoading = false;
                vm.mostrarContactos();
                toastr.success("Referidos importadas con exito.", "Ok")


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al importar referidos.", " " + err.status + " " + err.statusText)

            });
        };

        vm.importarEjecutivos2 = function (jsonData) {
            vm.mostrarLoading = true;
            //            console.log(JSON.stringify(jsonData));

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/importarsucursalesejecutivos',
                method: "POST",
                data: jsonData,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.ErrorCrearEjecutivoOut = res.data;
                if (vm.ErrorCrearEjecutivoOut.length > 0) {
                    vm.exportarErrorCrearEjecutivos(vm.ErrorCrearEjecutivoOut);

                }
                vm.mostrarLoading = false;
                vm.mostrarContactos();
                toastr.success("Ejecutivos importados con exito.", "Ok")


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al importar los ejecutivos.", " " + err.status + " " + err.statusText)

            });
        };

        vm.importarSucursales2 = function (jsonData) {
            vm.mostrarLoading = true;
            //            console.log(JSON.stringify(jsonData));

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.sucursales/importarsucursales',
                method: "POST",
                data: jsonData,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.ErrorCrearSucursalesOut = res.data;
                if (vm.ErrorCrearSucursalesOut.length > 0) {
                    vm.exportarErrorCrearSucursales(vm.ErrorCrearSucursalesOut);

                }
                vm.mostrarLoading = false;
                vm.mostrarContactos();
                toastr.success("Sucursales importadas con exito.", "Ok")


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al importar las sucursales.", " " + err.status + " " + err.statusText)

            });
        };

        vm.importarSucursales = function (jsonData, i) {

            //            console.log(JSON.stringify(jsonData[i]));

            importarSucursales = new ImportarSucursales(jsonData[i]);

            importarSucursales.$save({},
                function (res) {
                    toastr.success("Se ha creado la sucursal con exito -> " + jsonData[i].SUCURSAL, " Ok");

                    i++;
                    if (jsonData[i]) {
                        vm.importarSucursales(jsonData, i);
                    } else {
                        //redirige a pagina principal si ya no quedan mas datos en jsonData
                        vm.cargarAccionesEjecutivos();
                        vm.mostrarContactos();
                        console.log(" *********** salido1");

                    }

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("No Se ha creado la sucursal con exito -> " + jsonData[i].SUCURSAL, " " + err.status + " " + err.statusText)
                    i++;
                    if (jsonData[i]) {
                        vm.importarSucursales(jsonData, i);
                    } else {
                        vm.cargarAccionesEjecutivos();
                        vm.mostrarContactos();
                        console.log(" *********** salido2");

                    }

                });

        }

        vm.exportarErrorCrearSucursales = function (xlsRows) {

            var xlsRows2 = [];
            angular.forEach(xlsRows, function (val) {
                xlsRows2.push({
                    "CODSUCURSAL": val.CODSUCURSAL,
                    "SUCURSAL": val.EJECUTIVO,
                    "DIRECCION": val.DIRECCION,
                    "COMUNA": val.COMUNA,
                    "REGION": val.REGION,
                    "Error": val.ERROR
                });

            });


            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CODSUCURSAL",
                "SUCURSAL",
                "DIRECCION",
                "COMUNA",
                "REGION",
                "ERROR"
            ];

            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {


                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Error_CrearSucursales_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);


        };

        vm.exportarErrorCrearEjecutivos = function (xlsRows) {

            var xlsRows2 = [];
            angular.forEach(xlsRows, function (val) {
                xlsRows2.push({
                    "CODEJECUTIVO": val.CODEJECUTIVO,
                    "EJECUTIVO": val.EJECUTIVO,
                    "SUCURSAL": val.SUCURSAL,
                    "CORREO": val.CORREO,
                    "TELEFONO": val.TELEFONO,
                    "ERROR": val.ERROR
                });

            });


            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CODEJECUTIVO",
                "EJECUTIVO",
                "SUCURSAL",
                "CORREO",
                "TELEFONO",
                "ERROR"
            ];

            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {


                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Error_CrearEjecutivo_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);


        };


        vm.exportarErrorCrearReferidoReferenciaBitacora = function (xlsRows) {

            var xlsRows2 = [];
            angular.forEach(xlsRows, function (val) {
                xlsRows2.push({
                    "CANAL": val.CANAL,
                    "NOMBRE": val.NOMBRE,
                    "APELLIDOS": val.APELLIDOS,
                    "RUT": val.RUT,
                    "TELEFONO": val.TELEFONO,
                    "E_MAIL": val.E_MAIL,
                    "COMUNA": val.COMUNA,
                    "REGION": val.REGION,
                    "FECHA_RECEPCION": val.FECHA_RECEPCION,
                    "ERROR": val.ERROR

                });

            });


            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CANALNAME", "NOMBRE", "APELLIDOS", "RUT", "TELEFONO", "CORREO", "COMUNA", "REGION", "FECHA_RECEPCION", "ERROR"
            ];

            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {


                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Error_CrearReferidoReferenciaBitacora_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);


        };


        vm.filtrarDataTable = function (op) {
            var table = $("#datatableReferidos").DataTable();

            switch (op) {
                case 1:
                    table.search("iniciado").draw();
                    break;
                case 2:
                    table.search("interesado").draw();
                    break;
                case 3:
                    table.search("reagendado").draw();
                    break;
                case 4:
                    table.search("derivado").draw();
                    break;
                case 5:
                    table.search("atendido").draw();
                    break;
                case 6:
                    table.search("genesis").draw();
                    break;
                case 7:
                    table.search("sin éxito").draw();
                    break;
                case 8:
                    table.search("venta exitosa").draw();
                    break;
                case 9:
                    if (table.search() == '') {
                        vm.filtroPrioridad=true;
                        table.search("Prioritario").draw();

                    } else {
                        table.search("").draw();
                        vm.filtroPrioridad=false;


                    }
                    break;
                default:
                // code block
            }

        }

        vm.ordenarTablaEje = function (op, opDatos) {

            var res;

            if (opDatos == 1) {
                res = vm.ReferenciasNoCerradas;
                angular.forEach(res, function (c) {
                    c.fechaReal = new Date(c.fecha);
                });
            }

            if (opDatos == 2) {
                res = vm.ReferenciasTodoEje;
                angular.forEach(res, function (c) {
                    c.fechaReal = new Date(c.fecha);
                });
            }

            if (opDatos == 3) {
                res = vm.ReferenciasReagendadas;
                angular.forEach(res, function (c) {
                    c.fechaReal = new Date(c.fechaAccion);
                });
            }


            if (opDatos == 4) {
                res = vm.ReferenciasReagendadasTodoEje;
                angular.forEach(res, function (c) {
                    c.fechaReal = new Date(c.fechaAccion);
                });
            }

            if (op == 1) {
                if (vm.op == op) {
                    res = $filter('orderBy')(res, ['referidoId.nombre'], true);
                    vm.op = 0;
                } else {
                    res = $filter('orderBy')(res, ['referidoId.nombre']);
                    vm.op = op;
                }
            }
            if (op == 2) {
                if (vm.op == op) {
                    res = $filter('orderBy')(res, ['fechaReal'], true);
                    vm.op = 0;
                } else {
                    res = $filter('orderBy')(res, ['fechaReal']);
                    vm.op = op;
                }
            }
            if (op == 3) {
                if (vm.op == op) {
                    res = $filter('orderBy')(res, ['canalname'], true);
                    vm.op = 0;
                } else {
                    res = $filter('orderBy')(res, ['canalname']);
                    vm.op = op;
                }
            }
            if (op == 4) {
                if (vm.op == op) {
                    res = $filter('orderBy')(res, ['accionId.nombre'], true);
                    vm.op = 0;
                } else {
                    res = $filter('orderBy')(res, ['accionId.nombre']);
                    vm.op = op;
                }
            }


            if (opDatos == 1) {
                vm.ReferenciasNoCerradas = res;
            }

            if (opDatos == 2) {
                vm.ReferenciasTodoEje = res;
            }

            if (opDatos == 3) {
                vm.ReferenciasReagendadas = res;
            }

            if (opDatos == 3) {
                vm.ReferenciasReagendadas = res;
            }
            if (opDatos == 4) {
                vm.ReferenciasReagendadasTodoEje = res;
            }


        }



        vm.importarReferidos2 = function (jsonData, i) {
            referidos = new ImportarReferidos(jsonData[i]);
            referidos.CANALNAME = jsonData[i].CANAL;
            referidos.CORREO = jsonData[i].E_MAIL;
            referidos.USUARIO = vm.usuario;
            referidos.FECHANAC = (jsonData[i].fechanac != undefined) ? jsonData[i].fechanac : null;

            if (jsonData[i].RUT_1 == undefined) {
                if (jsonData[i].RUT == undefined || jsonData[i].RUT.length < 7) {
                    referidos.RUT = "SR" + new Date().getTime().toString().substr(5, 8);
                } else {
                    referidos.RUT = (jsonData[i].RUT != ' ') ? jsonData[i].RUT : "SR" + new Date().getTime().toString().substr(5, 8);

                }
            } else {
                referidos.RUT = jsonData[i].RUT_1;

            }


            referidos.VERSION = 1;
            referidos.FECHA_RECEPCION = new Date();

            //console.log(JSON.stringify(referidos));

            referidos.$save({},
                function (res) {
                    toastr.success("Se ha creado el referido con exito el referido -> " + jsonData[i].NOMBRE, " Ok");

                    //console.log("*** Ok Cargando referidos ");
                    //console.log(res);
                    i++;
                    if (jsonData[i] &&
                        jsonData[i].NOMBRE &&
                        jsonData[i].NOMBRE.length > 0) {
                        vm.importarReferidos2(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // vm.importarEjecutivos(jsonData, 0);
                        // console.log(" *********** salido1");

                    }

                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("No se ha cargado el referido -> " + jsonData[i].NOMBRE, " " + err.status + " " + err.statusText)
                    //console.log("*** Err Cargando referidos ");
                    //console.log(err);
                    i++;
                    if (jsonData[i] && jsonData[i].NOMBRE && jsonData[i].NOMBRE.length > 0) {
                        vm.importarReferidos2(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        // vm.importarEjecutivos(jsonData, 0);
                        // console.log(" *********** salido2");


                    }

                });

        }



        vm.descargarPlantillaReferenciasActualizar = function () {

            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "ID", "CANAL", "ESTADO", "COMENTARIOS", "FECHA_RECEPCION"
            ];

            var xlsRows2 = [];

            angular.forEach(vm.accionesTodos, function (val) {
                xlsRows2.push({
                    "ID": "",
                    "CANAL": "",
                    "ESTADO": val.nombre,
                    "COMENTARIOS": "",
                    "FECHA_RECEPCION": ""

                });

            });



            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato de actualizar datos Referencias_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }

        vm.descargarPlantillaCargarLosNo = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "Id", "Conecta_Noconecta", "COMENTARIOS"


            ];

            var xlsRows2 = [];
            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato para cargar los NO del Genesis_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }


        vm.descargarPlantillaCargarLosRoles = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "NOMBRE", "ROL", "AST", "SUP"



            ];

            var xlsRows2 = [];


            var xlsRows2 = [];
            angular.forEach(vm.rolesUsuarios, function (val, x) {
                xlsRows2.push({
                    "NOMBRE": val.nombre,
                    "ROL": val.rol,
                    "AST": val.ast,
                    "SUP": val.sup

                });

            });



            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato para cargar los Roles de Usuarios_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }


        vm.descargarPlantillaCargarEjecutivas = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CODEJECUTIVO", "EJECUTIVO", "SUCURSAL", "CORREO", "TELEFONO"
            ];
            var xlsRows2 = [];
            angular.forEach(vm.ejecutivos, function (val, x) {
                if (x > 0) {
                    xlsRows2.push({
                        "CODEJECUTIVO": val.codigo,
                        "EJECUTIVO": val.nombre,
                        "SUCURSAL": val.sucursalId.id,
                        "CORREO": val.correo,
                        "TELEFONO": val.telefono

                    });
                }

            });

            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato para cargar las Ejecutivas_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }

        vm.descargarPlantillaCargarSucursales = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CODSUCURSAL",
                //"VERSION", 
                "SUCURSAL",
                "DIRECCION",
                "COMUNA",
                "REGION"
            ];


            var xlsRows2 = [];
            angular.forEach(vm.sucursales, function (val, x) {
                if (x > 0) {
                    xlsRows2.push({
                        "CODSUCURSAL": val.id,
                        //"VERSION": val.version,
                        "SUCURSAL": val.nombre,
                        "DIRECCION": val.direccion,
                        "COMUNA": val.comuna,
                        "REGION": val.region

                    });
                }

            });



            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato para cargar las Sucursales_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }

        //fin descarga sucursales


        vm.descargarPlantillaCargarLosSi = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "ID", "Observaciones"

            ];

            var xlsRows2 = [];
            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato para cargar los SI del Genesis_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }



        vm.descargarPlantillaReferenciasCargaInicial = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "CANALNAME", "NOMBRE", "APELLIDOS", "RUT", "TELEFONO", "TELEFONO2", "TELEFONO3", "CORREO", "COMUNA", "REGION", "FECHA_RECEPCION"

            ];

            var xlsRows2 = [];
            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato de carga de Referidos_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }



        vm.descargarPlantillaReferidosDatosPersonales = function () {
            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "ID", "FECHANAC"

            ];

            var xlsRows2 = [];
            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {
                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Formato de carga para actualizar datos Personales de Referidos_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);
        }

        vm.limpiar = function (opDatos) {
            if (opDatos == 1) {
                vm.filtroReferenciasNoCerradas = '';
            }

            if (opDatos == 2) {
                vm.filtroReferenciasTodoEje = '';
            }

            if (opDatos == 3) {
                vm.filtroReferenciasReagendadas = '';
            }

            if (opDatos == 4) {
                vm.filtroReferenciasReagendadasTodoEje = '';
            }

        }

        vm.fExportarDatatablPantEje = function (opDatos) {

            window.scrollTo(0, 0);
            vm.mostrarLoading = true;

            vm.continuarExportarDatatablePantEje = function (xlsRows, opDatos) {
                var xlsRows2 = [];

                angular.forEach(xlsRows, function (val) {

                    fecha = $filter('date')(val.fechanac, "dd-MM-yyyy");
                    xlsRows2.push({
                        "nombre": val.referidoId.nombre,
                        "FECHANAC": val.referidoId.fechaNac,
                        "rut": val.referidoId.rut,
                        "telefono1": val.referidoId.telefonos,
                        "telefono2": val.referidoId.telefonos2,
                        "telefono3": val.referidoId.telefonos3,
                        "correo": val.referidoId.correo,
                        "fecha": (val.fecha) ? $filter('date')(val.fecha, "dd-MM-yyyy") : "",
                        "canal": val.canalname,
                        "afp": val.referidoId.afp,
                        "prima": val.referidoId.prima,
                        "comuna": val.comuna,
                        "region": val.region,
                        "ejecutivo": val.ownerename,
                        "estado": (val.fechaAccionFormat == undefined) ? val.accionId.nombre : val.accionId.nombre + " -> " + val.fechaAccionFormat,
                        "fechaAccion": (val.fechaAccion) ? $filter('date')(val.fechaAccion, "dd-MM-yyyy") : "",
                        "Comentarios": val.mailAccion
                    });

                });

                var createXLSLFormatObj = [];

                /* XLS Head Columns */
                var xlsHeader = [

                    "Nombre", "FECHANAC", "Rut", "Telefono1", "Telefono2", "Telefono3", "Correo", "Fecha_Recepcion", "Canal", "Afp", "Prima", "Comuna", "Region", "Ejecutivo", "Estado", "Fecha Reagendado", "Comentarios"
                ];


                createXLSLFormatObj.push(xlsHeader);
                $.each(xlsRows2, function (index, value) {

                    var innerRowData = [];
                    $.each(value, function (ind, val) {


                        innerRowData.push(val);
                    });
                    createXLSLFormatObj.push(innerRowData);
                });


                if (opDatos == 1) {
                    /* File Name */
                    var filename = "ListaDeTareas_Exportados_por_ejecutiva_" + vm.usuario + " " + $filter('date')(new Date(), 'medium') + ".xlsx";
                }

                if (opDatos == 2) {
                    /* File Name */
                    var filename = "HistoricoTodo_Exportados_por_ejecutiva_" + vm.usuario + " " + $filter('date')(new Date(), 'medium') + ".xlsx";
                }

                if (opDatos == 3) {
                    /* File Name */
                    var filename = "ReagendadosHoy_Exportados_por_ejecutiva_" + vm.usuario + " " + $filter('date')(new Date(), 'medium') + ".xlsx";
                }

                if (opDatos == 4) {
                    /* File Name */
                    var filename = "ReagendadosTodo_Exportados_por_ejecutiva_" + vm.usuario + " " + $filter('date')(new Date(), 'medium') + ".xlsx";
                }



                /* Sheet Name */
                var ws_name = "Referidos";

                var wb = XLSX.utils.book_new(),
                    ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

                /* Add worksheet to workbook */
                XLSX.utils.book_append_sheet(wb, ws, ws_name);

                vm.mostrarLoading = false;

                /* Write workbook and Download */
                XLSX.writeFile(wb, filename);

            }

            if (opDatos == 1) {
                res = $filter('filter')(vm.ReferenciasNoCerradas, vm.filtroReferenciasNoCerradas);
            }

            if (opDatos == 2) {
                res = $filter('filter')(vm.ReferenciasTodoEje, vm.filtroReferenciasTodoEje);
            }

            if (opDatos == 3) {
                res = $filter('filter')(vm.ReferenciasReagendadas, vm.filtroReferenciasReagendadas);
            }

            if (opDatos == 4) {
                res = $filter('filter')(vm.ReferenciasReagendadasTodoEje, vm.filtroReferenciasReagendadasTodoEje);

                angular.forEach(res, function (d) {
                    d.fechaAccionFormat = $filter('date')(d.fechaAccion, 'MMM d, yyyy');
                });
            }



            vm.continuarExportarDatatablePantEje(res, opDatos);

        }


        vm.fExportarDatatable = function () {

            window.scrollTo(0, 0);
            vm.mostrarLoading = true;

            var bSearch = $('[type="search"]').val();
            bSearch = (bSearch.length == 0) ? "%" : bSearch;

            vm.continuarExportarDatatable = function (xlsRows) {

                console.log("****************************************************************");
                console.log(JSON.stringify(xlsRows))
                console.log("****************************************************************");

                var xlsRows2 = [];
                angular.forEach(xlsRows, function (val) {
                    fecha = $filter('date')(val.fechanac, "dd-MM-yyyy");
                    xlsRows2.push({
                        "ID": val.ID,
                        "nombre": val.nombre,
                        "FECHANAC": (val.FECHANAC) ? $filter('date')(val.FECHANAC, "dd-MM-yyyy") : "",
                        "rut": val.rut,
                        "correo": val.correo,
                        "afp": val.afp,
                        "prima": val.prima,
                        "fecha": (val.fecha) ? $filter('date')(val.fecha, "dd-MM-yyyy") : "",
                        "canal": val.canal,
                        "comuna": val.comuna,
                        "region": val.region,
                        "ejecutivo": val.ejecutivo,
                        "estado": val.estado,
                        "fechaAccion": (val.fechaAccion) ? $filter('date')(val.fechaAccion, "dd-MM-yyyy") : "",
                        "ultimos2Coments": val.ultimos2Coments
                    });

                });

                var createXLSLFormatObj = [];

                /* XLS Head Columns */
                var xlsHeader = [

                    "ID", "Nombre", "FECHANAC", "Rut", "Correo", "Afp", "Prima", "Fecha_Recepcion", "Canal", "Comuna", "Region", "Ejecutivo", "Estado", "Fecha Reagendado", "ultimos2Coments"
                ];


                createXLSLFormatObj.push(xlsHeader);
                $.each(xlsRows2, function (index, value) {

                    var innerRowData = [];
                    $.each(value, function (ind, val) {


                        innerRowData.push(val);
                    });
                    createXLSLFormatObj.push(innerRowData);
                });


                /* File Name */
                var filename = "Referidos_Exportados_de_tabla_" + $filter('date')(new Date(), 'medium') + ".xlsx";

                /* Sheet Name */
                var ws_name = "Referidos";

                var wb = XLSX.utils.book_new(),
                    ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

                /* Add worksheet to workbook */
                XLSX.utils.book_append_sheet(wb, ws, ws_name);

                vm.mostrarLoading = false;

                /* Write workbook and Download */
                XLSX.writeFile(wb, filename);

            }

            var sup = (vm.usuarioSupervisaA2String == undefined) ? "undefined" : vm.usuarioSupervisaA2String;

            ExportarDatatable.list({
                "id": bSearch,
                "sup": sup,
                "anio": vm.anioParamUrlDataTable,
                "mes": vm.mesParamUrlDataTable,
                "suc": vm.sucursalesSelectFiltroPanel2
            }, function (res) {
                vm.mostrarLoading = false;
                // alert("paso1")
                var xlsRows = res;

                if (xlsRows.length == 0) {
                    toastr.error("No hay nada para exportar ", "Atencion");

                } else {
                    vm.continuarExportarDatatable(xlsRows);
                }

            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("No se ha cargado referidos para exportar. " + err.status + " " + err.statusText);


            });


        }

        //acaexportar
        vm.fExportarDatatableParaFicha = function () {

            window.scrollTo(0, 0);
            vm.mostrarLoading = true;

            var bSearch = $('[type="search"]').val();
            bSearch = (bSearch.length == 0) ? "%" : bSearch;

            vm.continuarExportarDatatableParaFicha = function (xlsRows) {

                var xlsRows2 = [];
                angular.forEach(xlsRows, function (val) {
                    fecha = $filter('date')(val.fechanac, "dd-MM-yyyy");
                    xlsRows2.push({
                        "id": val.ID,
                        "ejecutivo": val.ejecutivo,
                        "nombre": val.nombre,
                        "fechanac": (val.FECHANAC) ? $filter('date')(val.FECHANAC, "dd-MM-yyyy") : "",
                        "fecharecepcion": (val.fecha) ? $filter('date')(val.fecha, "dd-MM-yyyy") : "",
                        "fechaFicha": (val.fechaFicha) ? $filter('date')(val.fechaFicha, "dd-MM-yyyy") : "",
                        "rut": val.rut,
                        "telefonos1": val.telefonos,
                        "telefonos2": val.telefonos2,
                        "telefonos3": val.telefonos3,
                        "correo": val.correo,
                        "afp": val.afp,
                        "prima": val.prima,
                        "comoSupo": val.pensionarse,
                        "clienteSolicito": val.clienteSolicito,
                        "queAccion": val.accionRealizo,
                        "tipoPension": val.tipoPension,
                        "sexo": val.sexo,
                        "canal": val.canal,
                        "encDigitalResp1": (val.encDigitalPreg1 == null) ? "" : (val.encDigitalResp1) == 0 ? "NO" : "SI",
                        "encDigitalResp2": (val.encDigitalPreg2 == null) ? "" : (val.encDigitalResp2) == 0 ? "NO" : "SI",
                        "encDigitalResp3": (val.encDigitalPreg3 == null) ? "" : (val.encDigitalResp3) == 0 ? "NO" : "SI",
                        "encDigitalResp4": (val.encDigitalPreg4 == null) ? "" : $filter('date')(val.encDigitalResp4, "dd-MM-yyyy")


                    });

                });

                var createXLSLFormatObj = [];

                /* XLS Head Columns */
                var xlsHeader = [
                    "Id", "Nombre Ejecutivo(a)", "Nombre Cliente", "Fecha de Nacimiento", "Fecha de Recepcion", "Fecha Ficha", "RUT (formato 11111111-1)", "Telefono1 ", "Telefono2 ", "Telefono3 ", "Mail de Contacto", "Afp", "Prima", "¿Cómo supo que podía pensionarse en Consorcio?", "Para quien fue la atención ?", "¿Qué acción realizó con el cliente ? ", "Tipo de Pensión ?", "Sexo Pensionable", "Canal", "¿Tienes un Certificado de Saldo? ", "¿Haz realizado Solicitud de Oferta Anónima (SOA)?", "¿Recibiste una Oferta?", "Fecha de solicitud"
                ];


                createXLSLFormatObj.push(xlsHeader);
                $.each(xlsRows2, function (index, value) {

                    var innerRowData = [];
                    $.each(value, function (ind, val) {
                        innerRowData.push(val);
                    });
                    createXLSLFormatObj.push(innerRowData);
                });


                /* File Name */
                var filename = "Reporte de Ficha_" + $filter('date')(new Date(), 'medium') + ".xlsx";

                /* Sheet Name */
                var ws_name = "Referidos";

                var wb = XLSX.utils.book_new(),
                    ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

                /* Add worksheet to workbook */
                XLSX.utils.book_append_sheet(wb, ws, ws_name);

                vm.mostrarLoading = false;

                /* Write workbook and Download */
                XLSX.writeFile(wb, filename);

            }


            vm.usuarioSupervisaA2String = (vm.usuarioSupervisaA2String == undefined) ? 'undefined' : vm.usuarioSupervisaA2String;

            ExportarDatatable.list({
                "id": bSearch,
                "sup": vm.usuarioSupervisaA2String,
                "anio": vm.anioParamUrlDataTable,
                "mes": vm.mesParamUrlDataTable,
                "suc": vm.sucursalesSelectFiltroPanel2
            }, function (res) {
                // alert("paso2")
                var xlsRows = res;
                if (xlsRows.length == 0) {
                    toastr.error("No hay nada para exportar ", "Atencion");

                } else {

                    console.log(JSON.stringify(xlsRows));

                    vm.continuarExportarDatatableParaFicha(xlsRows);
                }

            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("No se ha cargado referidos para exportar. " + err.status + " " + err.statusText);


            });


        }



        vm.DescargarReferidos = function () {

            vm.mostrarLoading = true;

            vm.continuarExportar = function (xlsRows) {

                var xlsRows2 = [];
                angular.forEach(xlsRows, function (val) {
                    xlsRows2.push({
                        "ID": val.id,
                        "CANAL_REFERIDO": val.canalname,
                        "NOMBRE_COMPLETO": (val.referidoId.apellido != undefined) ? val.referidoId.nombre + " " + val.referidoId.apellido : val.referidoId.nombre,
                        "Rut": (val.referidoId.rut.substr(0, 2) == 'SR') ? val.referidoId.rut : val.referidoId.rut,
                        "TELEONO1": (val.referidoId.telefonos) ? val.referidoId.telefonos : "",
                        "TELEONO2": (val.referidoId.telefonos2) ? val.referidoId.telefonos2 : "",
                        "TELEONO3": (val.referidoId.telefonos3) ? val.referidoId.telefonos3 : "",
                        "E_MAIL": (val.referidoId.correo) ? val.referidoId.correo : "",
                        "FECHA_RECEPCIÓN": (val.fecha) ? $filter('date')(val.fecha, "dd-MM-yyyy") : "",
                        "FECHA_ENVÍO_CC": $filter('date')(new Date(), "dd-MM-yyyy")
                    });
                });

                var createXLSLFormatObj = [];

                /* XLS Head Columns */
                var xlsHeader = [
                    "ID", "CANAL REFERIDO", "NOMBRE COMPLETO", "Rut", "TELEFONO1", "TELEFONO2", "TELEFONO3", "E-MAIL", "FECHA RECEPCIÓN", "FECHA ENVÍO CC"
                ];


                createXLSLFormatObj.push(xlsHeader);
                $.each(xlsRows2, function (index, value) {

                    var innerRowData = [];
                    $.each(value, function (ind, val) {


                        innerRowData.push(val);
                    });
                    createXLSLFormatObj.push(innerRowData);
                });


                /* File Name */
                var filename = "Referidos_Exportados_" + $filter('date')(new Date(), 'medium') + ".xlsx";

                /* Sheet Name */
                var ws_name = "Referidos";

                var wb = XLSX.utils.book_new(),
                    ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

                /* Add worksheet to workbook */
                XLSX.utils.book_append_sheet(wb, ws, ws_name);

                /* Write workbook and Download */
                XLSX.writeFile(wb, filename);

            }


            ExportarReferidos.list({
                "id": vm.usuario
            }, function (res) {
                vm.mostrarLoading = false;
                // alert("paso3")
                var xlsRows = res;
                if (xlsRows.length == 0) {
                    toastr.error("No hay nada para exportar ", "Atencion");

                } else {
                    vm.continuarExportar(xlsRows);
                }

            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("No se ha cargado referidos para exportar. " + err.status + " " + err.statusText);


            });



        }



        $scope.readeje = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {

                try {
                    //lee encabeezado de excel
                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].EJECUTIVO &&
                            jsonData[i].EJECUTIVO.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.importarEjecutivos2(jsonData);
                    //                        vm.importarEjecutivos(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };

        $scope.readsucu = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {

                try {

                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].SUCURSAL &&
                            jsonData[i].SUCURSAL.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.importarSucursales2(jsonData, 0);
                    //                        vm.importarSucursales(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())

                }

            });

        };

        vm.importarRoles = function (jsonData, i) {

            //console.log(JSON.stringify(jsonData[i]));

            rolesUsuarios = new ImportarRolesUsuarios(jsonData[i]);
            rolesUsuarios.USUARIO = jsonData[i].NOMBRE;
            rolesUsuarios.AST = jsonData[i].AST;
            rolesUsuarios.SUP = jsonData[i].SUP;

            rolesUsuarios.$save({},
                function (res) {
                    toastr.success("Se ha creado o actualizado el usuario con exito -> " + jsonData[i].NOMBRE + ' - ' + jsonData[i].ROL, " Ok");

                    i++;
                    if (jsonData[i]) {
                        vm.importarRoles(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        console.log(" *********** salido1");

                    }

                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("Se ha creado o actualizado el usuario con exito -> " + jsonData[i].NOMBRE + ' - ' + jsonData[i].ROL, " " + err.status + " " + err.statusText)
                    i++;
                    if (jsonData[i]) {
                        vm.importarRoles(jsonData, i);
                    } else {
                        vm.mostrarContactos();
                        console.log(" *********** salido2");


                    }

                });

        }

        $scope.readroles = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {

                try {

                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].NOMBRE &&
                            jsonData[i].NOMBRE.length > 0 &&
                            jsonData[i].ROL &&
                            jsonData[i].ROL.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.importarRoles(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };

        $scope.read = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {

                try {

                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].NOMBRE &&
                            jsonData[i].NOMBRE.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.importarReferidos22(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };

        $scope.readsi = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {
                try {
                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i].Id) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.actualizarReferidosSi(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };


        $scope.readno = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {
                try {
                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i].Id) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.actualizarReferidosNo(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };


        vm.fmostrarCargarExcel = function () {
            vm.mostrarCargarExcel = true;
            vm.verContactos.contactos = false;
            vm.verContactos.actividades = false;
            vm.verProspectos.contactos = false;
            vm.verProspectos.actividades = false;
            vm.mostrarIngresarReferido = false;
        }

        vm.cargarComentario = function () {

            vm.mensajesPreEstablecidosSelect = JSON.parse(vm.mensajesPreEstablecidosSelect);

            if (vm.mensajesPreEstablecidosSelect.descrip == "OTROS") {
                vm.deshabilitadoOtros = false;
            } else {
                vm.messageActividad.comentarios = vm.mensajesPreEstablecidosSelect.descrip;

            }

        }



        vm.cargarAccionesTodos = function () {
            vm.accionesTodos = [];
            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.acciones',
                method: "GET",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.accionesTodos = res.data;
                vm.accionesTodos.splice(3, 1);

            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al cargar todos los Estados.", " " + err.status + " " + err.statusText)

            });
        }

        vm.fichaCanalDigitalSinRespuestas = function () {
            //llamada dinamica
            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.encuesta/preguntas',
                method: "GET",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8;'
                }
            }).then(function (res) {
                vm.traeRespuestas = 0;
                vm.canalDigital = res.data.preguntas;
                console.log(vm.canalDigital);
                angular.forEach(vm.canalDigital, function (p) {
                    p;
                });

            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al cargar las preguntas.", " " + err.status + " " + err.statusText)

            });
        }

        //primerarreglo
        vm.fichaCanalDigitalPreguntas = function () {
            //llamada dinamica
            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.encuesta/preguntas',
                method: "GET",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8;'
                }
            }).then(function (res) {
                vm.canalDigital = res.data.preguntas;
                console.log(vm.canalDigital);
                angular.forEach(vm.canalDigital, function (p, x) {
                    vm.canalDigital[x].respuesta = "";
                });
                vm.fichaCanalDigitalRespuestas();

            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al cargar las preguntas.", " " + err.status + " " + err.statusText)

            });
        }

        //segundoarreglo
        vm.fichaCanalDigitalRespuestas = function () {
            //llamada dinamica
            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.encuesta/rut/' + vm.ficha.RUT,
                method: "GET",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8;'
                }
            }).then(function (res) {
                vm.canalDigitalRespuestas = res.data.respuestas;

                angular.forEach(vm.canalDigitalRespuestas, function (p, x) {
                    vm.canalDigitalRespuestas;

                });
                vm.recorridoPreguntasRespuestas();
            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al cargar las preguntas.", " " + err.status + " " + err.statusText)

            });
        }

        vm.recorridoPreguntasRespuestas = function () {
            angular.forEach(vm.canalDigital, function (x) {
                angular.forEach(vm.canalDigitalRespuestas, function (y) {

                    if (x.id == y.idPregunta) {
                        x.respuesta2 = (y.respuesta) ? 1 : 2;
                    }
                    if (y.fechaRespuesta != undefined && x.id == y.idPregunta && y.idPregunta == 4) {
                        x.respuesta2 = $filter('date')(y.fechaRespuesta, "dd-MM-yyyy")
                    }
                });

            });
            console.log(vm.canalDigital);
        }

        vm.fichaCanalDigitalBorrar = function () {
            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.encuesta/rut/' + vm.ficha.RUT,
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                toastr.success("Ficha eliminada con exito.", "Ok")
                vm.cerrarFicha();
            }, function (err) {
                toastr.error("Ha ocurrido un problema al eliminar las respuestas.", " " + err.status + " " + err.statusText)
            });

        }


        vm.cargarSucursales = function () {

            Sucursales.list({},
                function (res) {
                    //console.log(" ** Ejecutivos Ok ->");
                    //console.log(res);
                    vm.sucursales = res;
                    vm.sucursales = $filter('orderBy')(vm.sucursales, ['nombre']);
                    // vm.sucursales.unshift("");


                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de sucursales.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Ejecutivos Err ->");
                    //console.log(err);

                });


        }

        vm.cargarAccionesEjecutivos = function () {

            vm.cargarAccionesTodos();
            vm.cargarSucursales();

            vm.accionSinActividad = Acciones.list({
                "id": "1"
            },
                function (res) {
                    //console.log(" ** accionSinActividad Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Sin/Actividad'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionSinActividad Err ->");
                    //console.log(err);

                });

            vm.accionInteresado = Acciones.list({
                "id": "2"
            },
                function (res) {

                    //console.log(" ** accionPendiente Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Pendiente'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionPendiente Err ->");
                    //console.log(err);

                });


            vm.accionReagendado = Acciones.list({
                "id": "3"
            },
                function (res) {
                    //console.log(" ** accionReagendado Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Reagendado'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionReagendado Err ->");
                    //console.log(err);

                });

            vm.accionCerradoEnviarMail = Acciones.list({
                "id": "4"
            },
                function (res) {
                    //console.log(" ** accionCerradoEnviarMail Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Cerrado / Envio Mail'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionCerradoEnviarMail Err ->");
                    //console.log(err);

                });


            vm.accionCerradoDerivoEjecutiva = Acciones.list({
                "id": "5"
            },
                function (res) {
                    //console.log(" ** accionCerradoDerivoEjecutiva Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Cerrado / Derivar ejecutiva'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionCerradoDerivoEjecutiva Err ->");
                    //console.log(err);

                });


            vm.accionCerradoSinExito = Acciones.list({
                "id": "6"
            },
                function (res) {
                    //console.log(" ** accionCerradoSinExito Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Cerrado / Sin exito'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionCerradoSinExito Err ->");
                    //console.log(err);

                });

            vm.accionCerradoConExito = Acciones.list({
                "id": "7"
            },
                function (res) {
                    //console.log(" ** accionCerradoConExito Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Cerrado / Con exito'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionCerradoConExito Err ->");
                    //console.log(err);

                });

            vm.accionAtendido = Acciones.list({
                "id": "8"
            },
                function (res) {
                    //console.log(" ** accionCerradoConExito Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de status para -> 'Atendido'.", " " + err.status + " " + err.statusText)

                    //console.log(" ** accionCerradoConExito Err ->");
                    //console.log(err);

                });


            Ejecutivos.list({},
                function (res) {
                    //console.log(" ** Ejecutivos Ok ->");
                    //console.log(res);
                    vm.ejecutivos = res;
                    vm.ejecutivos = $filter('orderBy')(vm.ejecutivos, ['nombre']);
                    vm.ejecutivos.unshift("");


                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de ejecutivos.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Ejecutivos Err ->");
                    //console.log(err);

                });

            Sucursales.list({},
                function (res) {
                    //console.log(" ** Sucursales Ok ->");
                    //console.log(res);
                    vm.sucursales = res;
                    vm.sucursales = $filter('orderBy')(vm.sucursales, ['nombre']);
                    vm.sucursales.unshift("");


                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de sucursales.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Ejecutivos Err ->");
                    //console.log(err);

                });

            RolesUsuarios.list({},
                function (res) {
                    //console.log(" ** Ejecutivos Ok ->");
                    //console.log(res);
                    vm.rolesUsuarios = res;

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar el catalogo de Roles de Usuario.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Ejecutivos Err ->");
                    //console.log(err);

                });


            vm.mostrarComentarios2();

        }


        vm.obtenerCheckboxSelect = function (s) {
            var oArray = [];
            var n = jQuery(s).length;
            if (n > 0) {
                jQuery(s).each(function () {
                    oArray.push($(this).val());
                });
            }

            return oArray;
        };

        vm.cerrarFicha = function (f) {
            vm.mostrarIngresarReferido = false;

            if (!vm.estoyViendoUnaRef) {
                vm.mostrarContactos();

            } else {
                f();
            }

        }


        vm.activarWizard = function () {
            $("#wizard").steps();
            vm.activarCalendarFicha();
        }


        vm.arrayPensionarse = [1, 2, 3, 4, 5, 6, 7];
        vm.arrayClienteSolicito = [5, 6];
        vm.arrayAccionRealizo = [11, 12, 13, 14, 15, 16];
        vm.arrayProximosPasos = [5, 6, 7, 8, 9, 10, 11];
        vm.arrayTipoPension = [5, 6, 7, 8];
        vm.arrayClientesEsperando = [5, 6, 7, 8];
        vm.arrayRelacionReferido = [5, 6, 7, 8];
        vm.arraysolicitoReferido = [1, 2];
        vm.arraySexo = [1, 2];


        vm.verificarCheckedCheckbox = function (id) {

            var resp = [];
            var estaCheck = false;

            if (id == "accionRealizo") {
                $.each(vm.arrayAccionRealizo, function (index, value) {

                    estaCheck = $("#wizard-p-2 > div > div:nth-child(" + value + ") > label > div").hasClass("checked");
                    if (estaCheck) {
                        resp.push(vm.arrayAccionRealizo[index]);
                    }
                });
            }

            if (id == "proximosPasos") {
                $.each(vm.arrayProximosPasos, function (index, value) {
                    estaCheck = $("#wizard-p-3 > div > div:nth-child(" + value + ") > label > div").hasClass("checked");
                    if (estaCheck) {
                        resp.push(vm.arrayProximosPasos[index]);
                    }

                });

                console.log(vm.arrayProximosPasos);
                console.log(resp.toString());

            }
            return resp;
        }


        //aca bitacora
        vm.crearBitacoraDeFicha = function () { 
            vm.bitacoraFicha = [];
            vm.bitacoraFicha.version = "1";
            vm.bitacoraFicha.fecha = new Date();
            vm.bitacoraFicha.referenciaId = vm.referenciaSeleccionada;
            vm.bitacoraFicha.usuario = vm.usuario;
            vm.bitacoraFicha.comentarios = "COMPLETAR FICHA -> Se ha editado datos de la ficha del referido.";

            vm.bitacora = new Bitacoras(vm.bitacoraFicha);
            
            vm.bitacora.referenciaId.prioritario = (vm.bitacora.referenciaId.prioritario) ? true : false;
            vm.bitacora.referenciaId.referidoId.afp = (vm.bitacora.referenciaId.referidoId.afp) ? vm.bitacora.referenciaId.referidoId.afp : '';
            vm.bitacora.referenciaId.referidoId.prima = (vm.bitacora.referenciaId.referidoId.prima) ? vm.bitacora.referenciaId.referidoId.prima : '';

            vm.bitacora.$save({},
                function (res) {
                    toastr.success("Bitacora actualizada con exito.", "Ok")
                    vm.cerrarFicha(function () {
                        vm.verificarBloqueo((vm.rollUsuario == 'usr' || vm.rollUsuario == 'ast') ? vm.referenciaSeleccionada : vm.referenciaSeleccionada.id);
                    });

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar la actividad.", " " + err.status + " " + err.statusText)

                });
        }

        vm.actualizarReferenciasSegunFicha = function (res) {
            vm.referencias = new Referencias(vm.referenciaSeleccionada);
            vm.referencias.canalname = res.CANALNAME;

            vm.referencias.$update({},
                function (res) {
                    toastr.success("Referencias actualizadas con exito.", "Ok");
                    vm.crearBitacoraDeFicha();

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar las referencias.", " " + err.status + " " + err.statusText)

                    //console.log(" ** referencias Err ->");
                    //console.log(err);

                });
        }

        vm.armarRut = function () {

            if (vm.ficha.RUT.substr(0, 2) == "SR") {
                return;
            }

            vm.ficha.RUT = vm.ficha.RUT.replace('-', '');
            var tam = vm.ficha.RUT.length;
            var cuerpo = vm.ficha.RUT.substr(0, tam - 1);
            var dv = vm.ficha.RUT.substr(tam - 1, 1)
            vm.ficha.RUT = cuerpo + "-" + dv;
            vm.ficha.RUT = vm.ficha.RUT.toUpperCase();
        }

        vm.getNombreEjecutiva = function (codigo) {

            var nombreEjecutiva = $filter('filter')(vm.ejecutivos, function (e) {
                if (e.codigo == codigo) {
                    return e;
                }
            });


            return nombreEjecutiva[0];

        }

        //acaguardar
        vm.guardarFicha2 = function () {
            vm.mostrarLoading = true;
            var ficha;

            vm.continuar5 = function () {
                if (vm.estoyViendoUnaRef) {

                    var vRut = new Date().getTime().toString();
                    vRut = "SR" + vRut.substring(5, 13);

                    vm.referenciaSeleccionada.referidoId.rut = vRut;
                    vm.referenciaSeleccionada.referidoId.fechanac = undefined;
                    vm.referenciaSeleccionada.referidoId.correo = undefined;
                    vm.referenciaSeleccionada.referidoId.afp = undefined;
                    vm.referenciaSeleccionada.referidoId.prima = undefined;
                    vm.referenciaSeleccionada.referidoId.telefonos = undefined;
                    vm.referenciaSeleccionada.referidoId.telefonos2 = undefined;
                    vm.referenciaSeleccionada.referidoId.telefonos3 = undefined;



                    // validaciones para el insert
                    if (vm.ficha.RUT != undefined && vm.ficha.RUT.length > 1) {
                        if (!vm.validarRut(vm.ficha.RUT)) {
                            return;
                        } else {
                            vm.referenciaSeleccionada.referidoId.rut = vm.ficha.RUT;

                        }
                    }

                    if (vm.ficha.FECHANAC != undefined && vm.ficha.FECHANAC.length > 0) {
                        var parts = vm.ficha.FECHANAC.split('-');
                        var partsToSfecha = parts[2] + '/' + parts[1] + '/' + parts[0];
                        var sToFecha = new Date(partsToSfecha);
                        vm.referenciaSeleccionada.referidoId.fechanac = sToFecha;

                    }

                    if (vm.ficha.CORREO != undefined && vm.ficha.CORREO.length > 0) {
                        vm.referenciaSeleccionada.referidoId.correo = vm.ficha.CORREO;

                    }

                    if (vm.ficha.PRIMA != undefined && vm.ficha.PRIMA.length > 0) {
                        vm.referenciaSeleccionada.referidoId.prima = vm.ficha.PRIMA;

                    }

                    if (vm.ficha.AFP != undefined && vm.ficha.AFP.length > 0) {
                        vm.referenciaSeleccionada.referidoId.afp = vm.ficha.AFP;

                    }

                    if (vm.ficha.celularReferido != undefined && vm.ficha.celularReferido.length > 0) {
                        vm.referenciaSeleccionada.referidoId.telefonos = vm.ficha.celularReferido;

                    }
                    if (vm.ficha.celularReferido2 != undefined && vm.ficha.celularReferido2.length > 0) {
                        vm.referenciaSeleccionada.referidoId.telefonos2 = vm.ficha.celularReferido2;

                    }
                    if (vm.ficha.celularReferido3 != undefined && vm.ficha.celularReferido3.length > 0) {
                        vm.referenciaSeleccionada.referidoId.telefonos3 = vm.ficha.celularReferido3;

                    }
                    vm.referenciaSeleccionada.referidoId.nombre = vm.ficha.NOMBRE;
                    vm.referenciaSeleccionada.referidoId.sexo = vm.ficha.sexo;
                    vm.referenciaSeleccionada.canalname = vm.ficha.CANALNAME;
                    vm.referenciaSeleccionada.referidoId.clienteSolicito = vm.ficha.clienteSolicito;
                    vm.referenciaSeleccionada.referidoId.pensionarse = vm.ficha.pensionarse;
                    vm.referenciaSeleccionada.referidoId.accionRealizo = vm.ficha.accionRealizo;
                    vm.referenciaSeleccionada.referidoId.tipoPension = vm.ficha.tipoPension;

                    ficha = new Referidos(vm.referenciaSeleccionada.referidoId);


                } else {
                    ficha = new Referidos();
                    ficha.COMENTARIOS = "REFERIDO CREADO POR FICHA POR EJECUTIVO(A) -> ";

                }

                //validacion guardado canal digital
                if (vm.ficha.CANALNAME.toUpperCase() == "MANUAL PENSIONADO") {
                    vm.fichaCanalDigitalGuardarActualizar();
                }



                // validaciones para el update
                if (vm.ficha.RUT != undefined && vm.ficha.RUT.length > 1) {
                    if (!vm.validarRut(vm.ficha.RUT)) {
                        return;
                    } else {
                        ficha.RUT = vm.ficha.RUT;
                        ficha.rut = ficha.RUT
                    }
                }

                if (vm.ficha.FECHANAC != undefined && vm.ficha.FECHANAC.length > 0) {
                    var parts = vm.ficha.FECHANAC.split('-');
                    var partsToSfecha = parts[2] + '/' + parts[1] + '/' + parts[0];
                    var sToFecha = new Date(partsToSfecha);
                    ficha.FECHANAC = sToFecha;
                    ficha.fechanac = ficha.FECHANAC;
                }

                if (vm.ficha.AFP != undefined && vm.ficha.AFP.length > 0) {
                    ficha.AFP = vm.ficha.AFP;
                    ficha.afp = ficha.AFP;
                }

                if (vm.ficha.PRIMA != undefined && vm.ficha.PRIMA.length > 0) {
                    ficha.PRIMA = vm.ficha.PRIMA;
                    ficha.prima = ficha.PRIMA;
                }

                if (vm.ficha.CORREO != undefined && vm.ficha.CORREO.length > 0) {
                    ficha.CORREO = vm.ficha.CORREO;
                    ficha.correo = ficha.CORREO;
                }

                if (vm.ficha.celularReferido != undefined && vm.ficha.celularReferido.length > 0) {
                    ficha.TELEFONO = vm.ficha.celularReferido;
                    ficha.telefonos = ficha.TELEFONO;
                }

                if (vm.ficha.celularReferido2 != undefined && vm.ficha.celularReferido2.length > 0) {
                    ficha.TELEFONO2 = vm.ficha.celularReferido2;
                    ficha.telefonos2 = ficha.TELEFONO2;
                }
                if (vm.ficha.celularReferido3 != undefined && vm.ficha.celularReferido3.length > 0) {
                    ficha.TELEFONO3 = vm.ficha.celularReferido3;
                    ficha.telefonos3 = ficha.TELEFONO3;
                }

                ficha.NOMBRE = vm.ficha.NOMBRE;
                ficha.nombre = ficha.NOMBRE

                ficha.FECHA_RECEPCION = new Date();

                ficha.CANALNAME = vm.ficha.CANALNAME;
                ficha.COMUNA = "";
                ficha.comuna = ficha.COMUNA;
                ficha.CALLE = "";
                ficha.calle = ficha.CALLE;
                ficha.DPTO_CASA = "";
                ficha.dpto_casa = ficha.DPTO_CASA;
                ficha.dptoCasa = ficha.dpto_casa;
                ficha.clienteSolicito = vm.ficha.clienteSolicito;
                ficha.pensionarse = vm.ficha.pensionarse;
                ficha.accionRealizo = vm.ficha.accionRealizo;
                ficha.tipoPension = vm.ficha.tipoPension;
                ficha.sexo = vm.ficha.sexo;


                // para administrador solo
                if (vm.rollUsuario == 'adm' && !vm.dobleRoll) {
                    ficha.OWNERE = "";
                    ficha.OWNERENAME = "";
                }

                // para supervisores & ejecutivos
                if (vm.dobleRoll || vm.rollUsuario == 'usr') {
                    ficha.OWNERE = vm.usuario;
					if (vm.getNombreEjecutiva(vm.usuario) != undefined)
					{
						ficha.OWNERENAME = vm.getNombreEjecutiva(vm.usuario).nombre;
					}
                }

                // para asistentes
                if (vm.rollUsuario == 'ast') {
                    ficha.OWNERE = vm.usuarioEsAsistenteDe2String;
					console.log(" *********** paso asistente");
					if (vm.getNombreEjecutiva(vm.usuarioEsAsistenteDe2String) != undefined)
					{
						ficha.OWNERENAME = vm.getNombreEjecutiva(vm.usuarioEsAsistenteDe2String).nombre;
					}
                }


                ficha.USUARIO = vm.usuario;
                ficha.usuario = ficha.USUARIO;

                if (vm.estoyViendoUnaRef) {
                    ficha.$update({},
                        function (res) {
                            vm.mostrarLoading = false
                            toastr.success("Ficha actualizadas con exito.", "Ok");
                            vm.actualizarReferenciasSegunFicha(res);

                        },
                        function (err) {
                            vm.mostrarLoading = false;
                            toastr.error("Ha ocurrido un problema al actualizar las Ficha.", " " + err.status + " " + err.statusText)

                            //console.log(" ** Ficha Err ->");
                            //console.log(err);

                        });

                } else {
                    vm.importarReferidos22([ficha], true);

                }
            }


            // validando referencia activa del rut
            if (vm.ficha.RUT != undefined && vm.ficha.RUT.length > 1) {
                if (vm.ficha.RUT.replace("-", "") != vm.old_rut.replace("-", "")) {
                    // if (vm.estoyViendoUnaRef) {
                    var rut = vm.ficha.RUT;
                    VerificaReferenciaActiva.list({
                        "rut": rut
                    }, function (res) {
                        if (res.tieneReferenciaActiva == true) {
                            swal("Are you sure?", {
                                icon: 'warning',
                                title: 'Atencion',
                                text: 'El RUT ingresado -> ' + rut + ' ó ' + rut.replace("-", "") + ',   ya tiene una referencia activa.',
                                dangerMode: true,
                            });
                            vm.mostrarLoading = false;
                            return;
                        } else {
                            vm.continuar5();
                        }

                    }, function (err) {
                        vm.mostrarLoading = false;
                        toastr.error("No se ha podido verificar Rut " + rut + " - " + err.status + " " + err.statusText);
                        return;

                    });

                    // } else {
                    //     vm.continuar5();
                    // }
                } else {
                    vm.continuar5();
                }

            } else {
                vm.continuar5();
            }
            // validando referencia activa del rut            

        };
        //acaguardar

        vm.fichaCanalDigitalGuardarActualizar = function () {
            data = [];
            angular.forEach(vm.canalDigital, function (f, x) {
                //si no se marca respuesta no se agrega al arreglo
                if (f.respuesta2 != undefined) {
                    respuestas = {
                        "idPregunta": f.id,
                        "respuesta": (x < 3) ? (f.respuesta2 == 1) ? true : false : "",
                        "descripcionRespuesta": "",
                        "fechaRespuesta": (x == 3) ? moment(f.respuesta2, 'DD/MM/YYYY').format('YYYY-MM-DD[T]HH:mm:ss') : ""
                    };

                    data.push(respuestas);
                }
            });

            dataRespuestas = {
                "rut": vm.ficha.RUT,
                "respuestas": data
            }

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.encuesta/respuesta',
                method: "PUT",
                data: dataRespuestas,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8;'
                }
            }).then(function (res) {
                console.log(res);
                toastr.success("Encuesta canal digital guardada con exito.", "Ok");
            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al guardar las respuestas.", " " + err.status + " " + err.statusText)

            });
        }

        vm.cargarItemsTable = function () {

            vm.nFilas = $("#datatableReferidos  tr").length - 1;

            vm.derivarMasivoList = [];
            var x;
            for (x = 1; x <= vm.nFilas; x++) {
                valorCombo = $('#datatableReferidos > tbody > tr:nth-child(' + x + ') > td.dt-body-center.sorting_1 > div > select').val();
                if (valorCombo != undefined && valorCombo != "") {
                    idRef = $('#datatableReferidos > tbody > tr:nth-child(' + x + ') > td:nth-child(11) > button')[0].id;
                    vm.derivarMasivoList.push(new Referencias({
                        "IDEJECUTIVO": valorCombo,
                        "ID": idRef.slice(1),
                        "USUARIO": vm.usuario
                    }));
                }
            }

            vm.hayItemsParaDerivar = (vm.derivarMasivoList.length > 0);

        };

        vm.exportarErrorActualizarDatosReferencias = function (xlsRows) {

            var xlsRows2 = [];
            angular.forEach(xlsRows, function (val) {
                xlsRows2.push({
                    "ID": val.ID,
                    "CANAL": val.CANAL,
                    "ESTADO": val.ESTADO,
                    "COMENTARIOS": val.COMENTARIOS,
                    "FECHA_RECEPCION": val.fecha,
                    "ERROR": val.ERROR

                });

            });

            var createXLSLFormatObj = [];

            /* XLS Head Columns */
            var xlsHeader = [

                "ID", "CANAL", "ESTADO", "COMENTARIOS", "FECHA_RECEPCION", "ERROR"
            ];


            createXLSLFormatObj.push(xlsHeader);
            $.each(xlsRows2, function (index, value) {

                var innerRowData = [];
                $.each(value, function (ind, val) {


                    innerRowData.push(val);
                });
                createXLSLFormatObj.push(innerRowData);
            });


            /* File Name */
            var filename = "Error_ActualizarDatosReferencias_" + $filter('date')(new Date(), 'medium') + ".xlsx";

            /* Sheet Name */
            var ws_name = "Hoja1";

            var wb = XLSX.utils.book_new(),
                ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

            /* Add worksheet to workbook */
            XLSX.utils.book_append_sheet(wb, ws, ws_name);

            /* Write workbook and Download */
            XLSX.writeFile(wb, filename);

        }



        vm.fActualizarDatosReferencias = function (jsonData) {

            //console.log(JSON.stringify(jsonData));

            vm.mostrarLoading = true;

            angular.forEach(jsonData, function (d) {
                d.USUARIO = vm.usuario;
                if (d.FECHA_RECEPCION != undefined && d.FECHA_RECEPCION != "") {
                    d.FECHA_RECEPCION = new Date((d.FECHA_RECEPCION - (25567 + 1)) * 86400 * 1000);

                }
            });

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/actualizardatosreferencias',
                method: "POST",
                data: jsonData,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.ErrorActualizarDatosReferenciasOut = res.data;
                vm.mostrarLoading = false;
                vm.mostrarContactos();
                toastr.success("Referencias actualizados con exito.", "Ok")
                if (vm.ErrorActualizarDatosReferenciasOut.length > 0) {
                    vm.exportarErrorActualizarDatosReferencias(vm.ErrorActualizarDatosReferenciasOut);

                }


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al actualizar Referencias.", " " + err.status + " " + err.statusText)


            });


        }


        vm.fActualizarDatosReferidos = function (jsonData) {
            vm.mostrarLoading = true;
            angular.forEach(jsonData, function (j) {
                j.FECHANAC = new Date((j.FECHANAC - (25567 + 1)) * 86400 * 1000);

            });

            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referidos/actualizardatosreferidos',
                method: "POST",
                data: jsonData,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.mostrarLoading = false;
                vm.mostrarContactos();
                toastr.success("Referidos actualizados con exito.", "Ok")


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al actualizar referidos.", " " + err.status + " " + err.statusText)


            });


        }



        $scope.readdatosrefe = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {
                try {

                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].ID &&
                            jsonData[i].ID.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.fActualizarDatosReferencias(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };



        $scope.readdatosref = function (workbook) {
            var jsonData
            var newWorkBook = workbook;
            var first_sheet_name = newWorkBook.SheetNames[0];

            $scope.$apply(function () {

                try {

                    jsonData = XLSX.utils.sheet_to_json(newWorkBook.Sheets[first_sheet_name]);

                    jsonData[0]["rowIndex"] = "0";

                    $scope.Elements = [jsonData[0]];

                    for (var i = 1; i < jsonData.length; i++) {
                        if (jsonData[i] &&
                            jsonData[i].ID &&
                            jsonData[i].ID.length > 0) {
                            $scope.Elements.push(
                                jsonData[i]
                            );
                        } else {
                            break;
                        }

                        jsonData[i]["rowIndex"] = i;
                    }
                    vm.fActualizarDatosReferidos(jsonData, 0);
                } catch (e) {
                    toastr.error("Ha ocurrido un problema al cargar el archivo.", e.toString())


                }

            });

        };


        vm.fDerivarMasivoListNo = function () {
            vm.mostrarContactos();
        };

        vm.fDerivarMasivoList = function () {
            if (!vm.hayItemsParaDerivar) {
				console.log("fDerivarMasivoList no hay items para derivar");
                vm.fDerivarMasivo();
                return;
            }

            window.scrollTo(0, 0);
            vm.mostrarLoading = true;
			//console.log("deriva 1");
            //console.log(JSON.stringify(vm.derivarMasivoList));


            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/derivarmasivolist',
                method: "POST",
                data: vm.derivarMasivoList,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                vm.mostrarLoading = false;

                vm.mostrarContactos();
                toastr.success("Referencias derivadas con exito.", "Ok")
                vm.ejecutivosSelect = null;


            }, function (err) {
                vm.mostrarLoading = false;
                vm.mostrarLoading = false;

                toastr.error("Ha ocurrido un problema al derivar masivamente las referencias.", " " + err.status + " " + err.statusText)
                vm.ejecutivosSelect = null;


            });

        };


        vm.fDerivarMasivo = function () {


            vm.mostrarLoading = true;
            window.scrollTo(0, 0);


            var bSearch = $('[type="search"]').val();
            bSearch = (bSearch.length == 0) ? "%" : bSearch;
            var bEjecutiva = JSON.parse(vm.ejecutivosSelect).id;

            var sup = (vm.usuarioSupervisaA2String == undefined) ? "undefined" : vm.usuarioSupervisaA2String;
			
			console.log(JSON.stringify({
                "usr": vm.usuario,
                "id": bSearch,
                "eje": bEjecutiva,
                "sup": sup,
                "anio": vm.anioParamUrlDataTable,
                "mes": vm.mesParamUrlDataTable,
                "suc": vm.sucursalesSelectFiltroPanel2}));

            DerivarMasivo.list({
                "usr": vm.usuario,
                "id": bSearch,
                "eje": bEjecutiva,
                "sup": sup,
                "anio": vm.anioParamUrlDataTable,
                "mes": vm.mesParamUrlDataTable,
                "suc": vm.sucursalesSelectFiltroPanel2

            },
                function (res) {
                    vm.mostrarLoading = false;

                    vm.mostrarContactos();
                    toastr.success("Referencias derivadas con exito.", "Ok")
                    vm.ejecutivosSelect = null;


                },
                function (err) {
                    vm.mostrarLoading = false;
                    vm.mostrarLoading = false;

                    toastr.error("Ha ocurrido un problema al derivar masivamente las referencias.", " " + err.status + " " + err.statusText)
                    vm.ejecutivosSelect = null;


                });


        };

        vm.cargarFicha2 = function (ref) {
            if (!vm.estoyViendoUnaRef) {
                vm.canales = [{
                    canalname: 'SUCURSAL'
                }, {
                    canalname: 'REFERIDO PROPIO'
                }, {
                    canalname: 'INBOUND'
                }];
            } else {
                Canales.list({},
                    function (res) {
                        //console.log(" ** Ejecutivos Ok ->");
                        //console.log(res);
                        vm.canales = res;

                    },
                    function (err) {
                        vm.mostrarLoading = false;
                        toastr.error("Ha ocurrido un problema al cargar el catalogo de Canales.", " " + err.status + " " + err.statusText)

                        //console.log(" ** Ejecutivos Err ->");
                        //console.log(err);

                    });
            }


            vm.ficha = {};

            if (ref != undefined) {
                vm.ficha.NOMBRE = ref.referidoId.nombre;
                vm.ficha.FECHANAC = $filter('date')(ref.referidoId.fechanac, "dd-MM-yyyy");

                vm.ficha.RUT = ref.referidoId.rut;
                vm.old_rut = vm.ficha.RUT;

                vm.ficha.sexo = ref.referidoId.sexo;
                vm.ficha.celularReferido = ref.referidoId.telefonos;
                vm.ficha.celularReferido2 = ref.referidoId.telefonos2;
                vm.ficha.celularReferido3 = ref.referidoId.telefonos3;
                vm.ficha.CORREO = ref.referidoId.correo;
                vm.ficha.CANALNAME = ref.canalname;
                vm.ficha.clienteSolicito = ref.referidoId.clienteSolicito;
                vm.ficha.pensionarse = ref.referidoId.pensionarse;
                vm.ficha.accionRealizo = ref.referidoId.accionRealizo;
                vm.ficha.tipoPension = ref.referidoId.tipoPension;
                vm.ficha.PRIMA = ref.referidoId.prima;
                vm.ficha.AFP = ref.referidoId.afp;
            }

        }


        //MOSTRAR ENCUESTA
        vm.fMostrarIngresarReferido = function (ref) {
            vm.ficha.FECHANAC = $filter('date')(new Date(), 'dd-MM-yyyy');

            $('#data_99 .input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true,
                format: 'dd-mm-yyyy'
            });

            $('#data_98 .input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true,
                format: 'dd-mm-yyyy'
            });


            window.scrollTo(0, 0);

            if (ref != undefined) {
                vm.estoyViendoUnaRef = true
            }


            vm.mostrarIngresarReferido = true;
            vm.verContactos.contactos = false;


            vm.cargarFicha2(ref);

			if (vm.ficha.CANALNAME != undefined)
			{
				if (vm.ficha.CANALNAME.toUpperCase() == 'MANUAL PENSIONADO') {
					vm.fverEncuesta1();
				} else {
					vm.fverEncuesta2();
				}
			}



        }



        vm.listarActividades = function () {
            BitacorasByReferencia.list({
                "id": vm.referenciaSeleccionada.id
            },
                function (res) {

                    vm.bitacoras = res;
                    //console.log(" ** Bitacoras Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar lista de actividades.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Bitacoras Err ->");
                    //console.log(err);

                });


        }

        vm.actualizarReferidoStatus = function () {

            vm.referenciaSeleccionada.accionId = vm.accionAtendido;

            vm.referenciaSeleccionada.fechaAccion = null;
            vm.referenciaSeleccionada.ejecutivoId = null;
            vm.referenciaSeleccionada.mailAccion = null;
            vm.referenciaSeleccionada.usuario = vm.usuario;


            if (vm.fechaHoraReagendar) {
                vm.referenciaSeleccionada.fechaAccion = vm.fechaHoraReagendar;
                vm.referenciaSeleccionada.accionId = vm.accionReagendado;

            }

            if (vm.ejecutivosSelect) {
                vm.referenciaSeleccionada.ejecutivoId = vm.ejecutivosSelect;
                vm.referenciaSeleccionada.ownere = vm.ejecutivosSelect.codigo;
                vm.referenciaSeleccionada.accionId = vm.accionCerradoDerivoEjecutiva;
                vm.referenciaSeleccionada.ownerename = vm.ejecutivosSelect.nombre;
                vm.ejecutivosSelect = null;


            }

            if (vm.mailCliente2Send) {
                vm.referenciaSeleccionada.mailAccion = vm.mailCliente2Send;
                vm.referenciaSeleccionada.accionId = vm.accionCerradoEnviarMail;

            }

            if (vm.cerrarConExitoFlag) {
                vm.referenciaSeleccionada.accionId = vm.accionCerradoConExito;
                vm.referenciaSeleccionada.fechaterminoe = new Date();
                vm.cerrarConExitoFlag = false;

            }

            if (vm.cerrarSinExitoFlag) {
                vm.referenciaSeleccionada.accionId = vm.accionCerradoSinExito;
                vm.referenciaSeleccionada.fechaterminoe = new Date();
                vm.cerrarSinExitoFlag = false;

            }

            vm.referencias = new Referencias(vm.referenciaSeleccionada);

            vm.referencias.$update({},
                function (res) {
                    toastr.success("Referencias actualizadas con exito.", "Ok")

                    // 
                    //console.log(" ** referencias Ok ->");
                    //console.log(res);
                    vm.messageActividad.comentarios = '';

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar las referencias.", " " + err.status + " " + err.statusText)

                    //console.log(" ** referencias Err ->");
                    //console.log(err);

                });
        }

        //aqui quede


        vm.updateReferenciaReagendar = function (res, fecha, hora) {
            // debugger
            res.fechaAccion = new Date(fecha + " " + hora);

            res.$update({},
                function (res) {
                    toastr.success("Referencia actualizada con exito.", "Ok")
                    vm.listarActividades();
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar Referencia", " " + err.status + " " + err.statusText)

                });
        }

        vm.loadReferencia2Update = function (res, fecha, hora) {
            // debugger
            Referencias.get({
                "id": res.referenciaId.id
            },
                function (res) {
                    vm.updateReferenciaReagendar(res, fecha, hora);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar lista de actividades.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Bitacoras Err ->");
                    //console.log(err);

                });
        }

        vm.updateBitacora = function (res, fecha, hora) {
            // debugger
            res.$update({},
                function (res) {
                    toastr.success("Bitacora actualizada con exito.", "Ok")
                    vm.loadReferencia2Update(res, fecha, hora);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar Bitacora", " " + err.status + " " + err.statusText)
                });
        }

        vm.fijarBitacora2Cambio = function (bit) {
            vm.bit = bit;
        }

        vm.loadReferencia2DeleteComentarios = function (id) {
            // debugger
            Referencias.get({
                "id": id
            },
                function (res) {
                    vm.updateReferenciaDeleteComentarios(res);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar lista de actividades.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Bitacoras Err ->");
                    //console.log(err);

                });
        }


        vm.updateReferenciaDeleteComentarios = function (res) {
            // alert(JSON.stringify(res))
            // debugger
            res.fechaterminoe = null;

            res.$update({},
                function (res) {
                    toastr.success("Referencia actualizada con exito.", "Ok")
                    vm.listarActividades();
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar Referencia", " " + err.status + " " + err.statusText)

                });
        }

        vm.eliminarComentario = function (bit) {
            // alert(JSON.stringify(bit));

            var isConfirmed = confirm("Desea eliminar el comentario ?");
            if (!isConfirmed) {
                return;
            }

            Bitacoras.delete({
                "id": bit.id
            },
                function (res) {
                    toastr.success("Bitacora eliminada con exito.", "Ok")
                    vm.loadReferencia2DeleteComentarios(bit.referenciaId.id);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al eliminar la bitacora.", " " + err.status + " " + err.statusText)
                });

        }

        vm.editarBitacora = function (fecha, hora) {
            // debugger
            Bitacoras.get({
                "id": vm.bit.id
            },
                function (res) {
                    console.log("Obteniendo ..." + JSON.stringify(res));
                    res.comentarios = "REAGENDAR -> Actividad reagendada para : " + fecha + " " + hora;
                    vm.updateBitacora(res, fecha, hora);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar la bitacora.", " " + err.status + " " + err.statusText)
                });



        }


        vm.cerrarSinExito = function () {

            if (!vm.messageActividad.comentarios) {
                toastr.error("Debe especificar un comentario.", " ")
                return;
            }

            vm.cerrarSinExitoFlag = true;
            vm.messageActividad.comentarios = "SIN EXITO -> " + vm.messageActividad.comentarios;
            vm.enviarActividad(1);

        }

        vm.cerrarConExito = function () {

            if (!vm.messageActividad.comentarios) {
                toastr.error("Debe especificar un comentario.", " ")
                return;
            }

            vm.cerrarConExitoFlag = true;
            vm.messageActividad.comentarios = "VENTA EXITOSA -> " + vm.messageActividad.comentarios;
            vm.enviarActividad(1);

        }

        vm.cerrarMail2Cliente = function () {

            if (!vm.mailCliente2Send) {
                toastr.error("Debe debe especificar un correo electronico.", " ")
                return;

            }

            vm.messageActividad.comentarios = "Actividad cerrada, se debe enviar un mail al cliente  al correo -> " + vm.mailCliente2Send;
            vm.enviarActividad(1);

        }

        vm.cerrarDerivar = function () {

            if (!vm.ejecutivosSelect) {
                toastr.error("Debe debe selecciona un ejecutivo a derivar.", " ")
                return;

            }

            vm.ejecutivosSelect = JSON.parse(vm.ejecutivosSelect);
            vm.messageActividad.comentarios = "DERIVAR -> Actividad Derivada a ejecutivo : " + vm.ejecutivosSelect.nombre + ", sucursal : " + vm.ejecutivosSelect.sucursalId.nombre;
            // vm.enviarActividad(1);
            vm.derivarMasivoList = [];

            vm.derivarMasivoList.push(new Referencias({
                "IDEJECUTIVO": vm.ejecutivosSelect.id,
                "ID": vm.referenciaSeleccionada.id,
                "USUARIO": vm.usuario,
                "COMENTARIOS": "DERIVAR -> Actividad Derivada a ejecutivo : " + vm.ejecutivosSelect.nombre + ", sucursal : " + vm.ejecutivosSelect.sucursalId.nombre
            }));
			//console.log("deriva 2");
                        //console.log(JSON.stringify(vm.derivarMasivoList));


            $http({
                url: '/referidosrrvv/webresources/cl.cnsv.referidosrrvv.models.referencias/derivarmasivolist',
                method: "POST",
                data: vm.derivarMasivoList,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (res) {
                toastr.success("Referencia derivadas con exito.", "Ok")
                vm.listarActividades();
                vm.actualizarReferidoStatus();


            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al derivar la referencia.", " " + err.status + " " + err.statusText)


            });


        }

        vm.actualizarGraficoPorSucursal = function (noRunPanel) {

            if (vm.sucursalesSelectFiltroPanel != "undefined") {
                vm.sucursalesSelectFiltroPanel2 = "'" + JSON.parse(vm.sucursalesSelectFiltroPanel).id + "'";
            } else {
                vm.sucursalesSelectFiltroPanel2 = "undefined";

            }

            if (noRunPanel == undefined) {
                vm.cargarPanel();
            }
            $("#datatableReferidos").DataTable().draw();

        }

        vm.actualizarGraficoPorEjecutivo = function (noRunPanel) {
            if (vm.ejecutivosSelectFiltroPanel != undefined) {
                vm.usuarioSupervisaA2String = "'" + JSON.parse(vm.ejecutivosSelectFiltroPanel).codigo + "'";
            } else {
                vm.usuarioSupervisaA2String = undefined;

            }
            if (noRunPanel == undefined) {
                vm.cargarPanel();
            }


            $("#datatableReferidos").DataTable().draw();

        }

        vm.actualizarGraficoPorFecha = function (noRunPanel) {
            vm.anioParamUrlDataTable = (vm.mesAnioFiltroGraph != null && vm.mesAnioFiltroGraph != "") ? vm.mesAnioFiltroGraph.split('-')[0] : "undefined";



            if (vm.mesAnioFiltroGraph != null && vm.mesAnioFiltroGraph != "" && vm.mesAnioFiltroGraph.split('-')[1] == undefined) {
                vm.mesParamUrlDataTable = "undefined";
            } else {
                vm.mesParamUrlDataTable = (vm.mesAnioFiltroGraph != null && vm.mesAnioFiltroGraph != "") ? vm.mesAnioFiltroGraph.split('-')[1] : "undefined";

            }


            if (noRunPanel == undefined) {
                vm.cargarPanel();
            }
            $("#datatableReferidos").DataTable().draw();

        }

        $scope.enviarCapturarFechaReagendar = function (v) {
            vm.fechaReagendar = v;
        }

        $scope.enviarCapturarMesAnioFiltroGraph = function (v) {
            vm.mesAnioFiltroGraph = v;
        }

        $scope.enviarCapturarFechaNac = function (v) {
            vm.fechaNac = v;
        }

        $scope.reagendar = function (v) {
            vm.hora = v;
            vm.fechaHoraReagendar = new Date(vm.fechaReagendar + ' ' + vm.hora);
            vm.messageActividad.comentarios = "REAGENDAR -> Actividad reagendada para : " + vm.fechaReagendar + ' ' + vm.hora;
            vm.enviarActividad(1);
        }

        $scope.reagendar_reagendar = function (v) {
            // debugger
            vm.hora = v;
            vm.editarBitacora(vm.fechaReagendar, vm.hora);
        }

        vm.medirTam = function () {



        }



        vm.enviarActividad = function (dejarComent) {

            vm.messageActividad.version = "1";
            vm.messageActividad.fecha = new Date();
            vm.messageActividad.referenciaId = vm.referenciaSeleccionada;
            vm.messageActividad.usuario = vm.usuario;

            if (!vm.messageActividad.comentarios) {
                toastr.error("Debe ingresar al menos un comentario.", " ")
                return;

            }

            if (dejarComent == undefined) {
                vm.messageActividad.comentarios = "DEJAR COMENTARIO -> " + vm.messageActividad.comentarios;

            }

            vm.bitacora = new Bitacoras(vm.messageActividad);

            //            console.log(JSON.stringify(vm.bitacora));

            vm.bitacora.referenciaId.prioritario = (vm.bitacora.referenciaId.prioritario) ? true : false;
            vm.bitacora.referenciaId.referidoId.afp = (vm.bitacora.referenciaId.referidoId.afp) ? vm.bitacora.referenciaId.referidoId.afp : '';
            vm.bitacora.referenciaId.referidoId.prima = (vm.bitacora.referenciaId.referidoId.prima) ? vm.bitacora.referenciaId.referidoId.prima : '';

            vm.bitacora.$save({},
                function (res) {

                    toastr.success("Actividad actualizada con exito.", "Ok")


                    //console.log(" ** messageActividad Ok ->");
                    //console.log(res);
                    vm.listarActividades();
                    vm.actualizarReferidoStatus();


                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar la actividad.", " " + err.status + " " + err.statusText)

                    //console.log(" ** messageActividad Err ->");
                    //console.log(err);

                });



        }

        vm.listarReferenciasReagendadasTodoEje = function () {


            ReferenciasReagendadas.list({
                "id": vm.losAsistentesBuscaranInfPor,
                "tipo": "2"
            }, function (res) {

                //console.log(" ** ReferenciasReagendadas Ok ->");
                //console.log(res);
                // 

                vm.ahora = new Date();
                vm.ReferenciasReagendadasTodoEje = res;


            }, function (err) {
                vm.mostrarLoading = false;

                toastr.error("Ha ocurrido un problema al cargar las referencias reagendadas todo ejecutiva.", " " + err.status + " " + err.statusText)

                //console.log(" ** ReferenciasReagendadas Err ->");
                //console.log(err);

            });
        }

        vm.listarReferenciasReagendadas = function () {
            ReferenciasReagendadas.list({
                "id": vm.losAsistentesBuscaranInfPor,
                "tipo": "1"
            }, function (res) {

                //console.log(" ** ReferenciasReagendadas Ok ->");
                //console.log(res);
                // 

                vm.ahora = new Date();
                vm.ReferenciasReagendadas = res;
                vm.listarReferenciasReagendadasTodoEje();

                // var fecha = $filter('date')(vm.ReferenciasReagendadas[0].fechaAccion, 'yyyy-MM-dd');
                // var hoy = $filter('date')(new Date(), 'yyyy-MM-dd');

                if (vm.ReferenciasReagendadas.length > 0) {
                    vm.cargarAviso();

                }


            }, function (err) {
                vm.mostrarLoading = false;

                toastr.error("Ha ocurrido un problema al cargar las referencias reagendadas.", " " + err.status + " " + err.statusText)

                //console.log(" ** ReferenciasReagendadas Err ->");
                //console.log(err);

            });
        }

        vm.listarReferenciasReagendadasFull = function () {
            ReferenciasReagendadasFull.list({
                "id": vm.usuario
            }, function (res) {

                //console.log(" ** ReferenciasReagendadas Ok ->");
                //console.log(res);
                // 
                vm.ahora = new Date();
                vm.ReferenciasReagendadas = res;
                vm.activarTabla();
                //debugger


            }, function (err) {
                vm.mostrarLoading = false;
                toastr.error("Ha ocurrido un problema al cargar las referencias reagendadas.", " " + err.status + " " + err.statusText)

                //console.log(" ** ReferenciasReagendadas Err ->");
                //console.log(err);

            });
        }

        vm.listarReferenciasNoCerradasFinalizadas = function () {
            ReferenciasNoCerradas.list({
                "id": vm.usuario,
                "tipo": "2"
            }, function (res) {


                //console.log(" ** ReferenciasNoCerradas Ok ->");
                //console.log(res);

                vm.ReferenciasCerradas = res;


            }, function (err) {
                vm.mostrarLoading = false;
                //console.log(" ** ReferenciasNoCerradas Err ->");
                //console.log(err);

                toastr.error("Ha ocurrido un problema al cargar las referencias Finalizadas.", " " + err.status + " " + err.statusText)


            });
        }


        vm.cargarAviso = function () {
            swal("Are you sure?", {
                icon: 'warning',
                title: 'Atencion',
                text: 'Se han encontrado referencias reagendadas pendientes.',
                dangerMode: true,
            });
            vm.mostrarLoading = false;

        }



        vm.listarReferenciasNoCerradasTodasEje = function () {

            ReferenciasNoCerradas.list({
                "id": vm.losAsistentesBuscaranInfPor,
                "tipo": "4"
            }, function (res) {

                // console.log(res)
                // alert(JSON.stringify(res));


                //console.log(" ** ReferenciasNoCerradas Ok ->");
                //console.log(res);

                vm.ReferenciasTodoEje = res;

                angular.forEach(vm.ReferenciasTodoEje, function (v) {
                    v.cLike = v.referidoId.nombre + " " + $filter('date')(v.fecha, 'MMM d, yyyy') + " " + v.canalname + " " + v.accionId.nombre;
                });

                vm.loadingRecords = false;


            }, function (err) {
                vm.mostrarLoading = false;
                //console.log(" ** ReferenciasNoCerradas Err ->");
                //console.log(err);

                toastr.error("Ha ocurrido un problema al cargar las referencias Todo para la ejecutiva.", " " + err.status + " " + err.statusText)


            });


        }


        vm.listarReferenciasNoCerradasSinExito = function () {
            ReferenciasNoCerradas.list({
                "id": vm.usuario,
                "tipo": "3"
            }, function (res) {

                //console.log(" ** ReferenciasNoCerradas Ok ->");
                //console.log(res);

                vm.ReferenciasSinExito = res;
                vm.loadingRecords = false;


            }, function (err) {
                vm.mostrarLoading = false;
                //console.log(" ** ReferenciasNoCerradas Err ->");
                //console.log(err);

                toastr.error("Ha ocurrido un problema al cargar las referencias sin exito.", " " + err.status + " " + err.statusText)


            });
        }


        vm.listarReferenciasNoCerradasPendiente = function () {


            ReferenciasNoCerradas.list({
                "id": vm.losAsistentesBuscaranInfPor,
                "tipo": "1"
            }, function (res) {

                //console.log(" ** ReferenciasNoCerradas Ok ->");
                //console.log(res);


                vm.ReferenciasNoCerradas = res;

                angular.forEach(vm.ReferenciasNoCerradas, function (v) {
                    v.ordenEstado = (v.accionId.id == 5) ? 1 : 2;
                    v.cLike = v.referidoId.nombre + " " + $filter('date')(v.fecha, 'MMM d, yyyy') + " " + v.canalname + " " + v.accionId.nombre;
                });

                vm.ReferenciasNoCerradas = $filter('orderBy')(vm.ReferenciasNoCerradas, ['ordenEstado']);

                vm.listarReferenciasReagendadas();


            }, function (err) {
                vm.mostrarLoading = false;
                //console.log(" ** ReferenciasNoCerradas Err ->");
                //console.log(err);

                toastr.error("Ha ocurrido un problema al cargar las referencias pendientes.", " " + err.status + " " + err.statusText)


            });
        }


        vm.desbloquearReferencia = function () {
            vm.referenciaSeleccionada.blockedbycall = 0;
            vm.referenciaSeleccionada.fechablockedbycall = null;
            vm.referencias = new Referencias(vm.referenciaSeleccionada);

            vm.referencias.$update({},
                function (res) {
                    // 
                    //console.log(" ** referencias Ok ->");
                    //console.log(res);

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al desbloquear las referencias.", " " + err.status + " " + err.statusText)

                    //console.log(" ** referencias Err ->");
                    //console.log(err);

                });
        }

        vm.verificarBloqueo = function (c, deDondeVengo) {

            var param = {
                "id": (vm.rollUsuario == 'adm' || vm.rollUsuario == 'sup') ? c : c.id
            };
            Referencias.list(param,
                function (res) {


                    vm.mostrarActividadesContactos(res);

                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("Ha ocurrido un problema al verificar bloqueo las referencias.", " " + err.status + " " + err.statusText)
                    //console.log(" ** Referencias Err ->");
                    //console.log(err);

                });

        };

        vm.updateReferenciaPriodidad = function (res) {
            // debugger

            console.log(res);

            res.prioritario = (res.prioritario == 1) ? 0 : 1;

            res.$update({},
                function (res) {
                    toastr.success("Referencia actualizada con exito.", "Ok")
                    vm.cargarPerfilDeUsuario();
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al actualizar Referencia", " " + err.status + " " + err.statusText)

                });
        }


        vm.myPrioridad = function (c, deDondeVengo) {

            // alert("Se establecera como prioritario ..!!!" + c + " " + deDondeVengo);

            // debugger
            Referencias.get({
                "id": c
            },
                function (res) {
                    vm.updateReferenciaPriodidad(res);
                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al cargar lista de actividades.", " " + err.status + " " + err.statusText)

                    //console.log(" ** Bitacoras Err ->");
                    //console.log(err);

                });

        };


        $scope.verificarBloqueo = function (c) {
            vm.verificarBloqueo(c);
        }

        $scope.myPrioridad = function (c) {
            vm.myPrioridad(c);
        }

        vm.validarRut = function (rut) {

            var valor = rut;
            valor = valor.replace('-', '');

            if (valor.substr(0, 2) == "SR") {
                return true;
            }

            cuerpo = valor.slice(0, -1);
            dv = valor.slice(-1).toUpperCase();


            if (cuerpo.length < 7) {
                swal("Are you sure?", {
                    icon: 'warning',
                    title: 'Atencion',
                    text: 'El RUT ingresado -> ' + rut + ',  esta INCOMPLETO.',
                    dangerMode: true,
                });
                vm.mostrarLoading = false;
                return false;
            }

            suma = 0;
            multiplo = 2;

            for (i = 1; i <= cuerpo.length; i++) {

                index = multiplo * valor.charAt(cuerpo.length - i);

                suma = suma + index;

                if (multiplo < 7) {
                    multiplo = multiplo + 1;
                } else {
                    multiplo = 2;
                }

            }

            dvEsperado = 11 - (suma % 11);

            dv = (dv == 'K') ? 10 : dv;
            dv = (dv == 0) ? 11 : dv;

            if (dvEsperado != dv) {
                swal("Are you sure?", {
                    icon: 'warning',
                    title: 'Atencion',
                    text: 'El RUT ingresado -> ' + rut + ',  NO ES VALIDO.',
                    dangerMode: true,
                });
                vm.mostrarLoading = false;
                return false;
            }


            return true;

        }



        vm.activarGrafico = function () {


            var config = {
                type: 'pie',
                data: {
                    datasets: [{
                        data: [
                            vm.PanelTotales.tinactividad,
                            vm.PanelTotales.tinteresados,
                            vm.PanelTotales.tderivados,
                            vm.PanelTotales.treagendados,
                            vm.PanelTotales.tatendidos,
                            vm.PanelTotales.tsinexito,
                            vm.PanelTotales.tgenesis,
                            vm.PanelTotales.tcerrados,
                        ],
                        backgroundColor: [
                            vm.PanelTotales.colorbinactividad,
                            vm.PanelTotales.colorbinteresados,
                            vm.PanelTotales.colorbderivados,
                            vm.PanelTotales.colorbreagendados,
                            vm.PanelTotales.colorbatendidos,
                            vm.PanelTotales.colorbsinexito,
                            vm.PanelTotales.colorbgenesis,
                            vm.PanelTotales.colorbcerrados,
                        ],
                        label: 'Dataset 1'
                    }],
                    labels: [
                        'Iniciado',
                        'Interesado',
                        'Derivados',
                        'Reagendado',
                        'Atendido',
                        'Sin Exito',
                        'Genesis',
                        'Venta Exitosa'
                    ]
                },
                options: {
                    responsive: true
                }
            };

            var ctx = document.getElementById('lineChart').getContext('2d');
            new Chart(ctx, config);

        };



        vm.activarTabla = function () {

            vm.comboBox = '<div class="input-group" ><select style="width: 140px;">';

            angular.forEach(vm.ejecutivos, function (e) {
                vId = (e.id) ? e.id : "";
                vNombre = (e.nombre) ? e.nombre : "";
                vOption = '<option value="' + vId + '">' + vNombre + '</option>';
                vm.comboBox = vm.comboBox + vOption;

            });

            vm.comboBox = vm.comboBox + '</select></div>';

            vm.ReferenciasNoCerradas = null;
            vm.verTabla = true;
            vm.mostrarLoading = false;
            


        }



        vm.mostrarProspectos = function () {
            vm.verContactos.contactos = false;
            vm.verContactos.actividades = false;
            vm.verProspectos.contactos = true;
            vm.verProspectos.actividades = false;
        }


        vm.cargarPanel = function () {


            //                vm.mesParamUrlDataTable = (vm.mesParamUrlDataTable == undefined) ? "" : vm.mesParamUrlDataTable



            vm.usuarioSupervisaA2String = (vm.usuarioSupervisaA2String == undefined) ? 'undefined' : vm.usuarioSupervisaA2String;
            PanelTotales.list({
                "sup": vm.usuarioSupervisaA2String,
                "anio": vm.anioParamUrlDataTable,
                "mes": vm.mesParamUrlDataTable,
                "suc": vm.sucursalesSelectFiltroPanel2
            },
                function (res) {

                    //console.log(" ** PanelTotales Ok ->");
                    //console.log(res);

                    vm.PanelTotales = res;
                    vm.activarGrafico();
                    vm.activarCalendarFiltroGraph();


                },
                function (err) {
                    vm.mostrarLoading = false;

                    toastr.error("Ha ocurrido un problema al cargar PanelTotales.", " " + err.status + " " + err.statusText)

                    //console.log(" ** PanelTotales Err ->");
                    //console.log(err);

                });


        }


        vm.mostrarComentarios2 = function () {
            RolesUsuariosNombre.list({
                "id": vm.usuario
            },
                function (res) {

                    //console.log(" ** RolesUsuariosNombre Ok ->");
                    //console.log(res);
                    // vm.rollUsuario = res.rol;                   

                    if (vm.rollUsuario == "adm" || vm.rollUsuario == "sup") {
                        vm.cargarPanel();
                        vm.verTabla = false;
                        vm.mostrarLoading = false;
                    }

                    window.scrollTo(0, 0);
                    if (vm.SeTomoUnaReferencia) {
                    }
                    vm.estoyViendoUnaRef = false;
                    vm.mostrarCargarExcel = false;
                    vm.mostrarIngresarReferido = false;
                    vm.verContactos.contactos = true;
                    vm.verContactos.actividades = false;
                    vm.verProspectos.contactos = false;
                    vm.verProspectos.actividades = false;
                    // quitar esto

                    if (vm.rollUsuario == "adm" || vm.rollUsuario == "sup") {
                        vm.listarReferenciasReagendadasFull();
                    }
                    if (vm.rollUsuario == "usr" || vm.rollUsuario == "ast") {
                        vm.listarReferenciasNoCerradasPendiente();
                        // vm.listarReferenciasNoCerradasFinalizadas()
                        // vm.listarReferenciasNoCerradasSinExito();
                        vm.listarReferenciasNoCerradasTodasEje();
                    }

                },
                function (err) {
                    vm.mostrarLoading = false;


                    toastr.error("Hubo un problema al tratar de cargar el rol del usuario. ", "Atencion " + err.status + " " + err.statusText);
                    //console.log(" ** RolesUsuariosNombre Err ->");
                    //console.log(err);


                });
        }


        vm.volverDeEjecutiva = function () {

            // definir cuando sea sup de donde vengo de pantalla de eje o de pantalla adm

            if (vm.rollUsuario == "adm" || vm.rollUsuario == "sup") {
                vm.estoyViendoUnaRef = false;
                vm.mostrarCargarExcel = false;
                vm.mostrarIngresarReferido = false;
                vm.verContactos.contactos = true;
                vm.verContactos.actividades = false;
                vm.verProspectos.contactos = false;
                vm.verProspectos.actividades = false;
            } else {
                vm.mostrarContactos();
            }


        }

        vm.limpiarFiltroPrimero = function () {
            vm.mesAnioFiltroGraph = null;
            vm.anioParamUrlDataTable = 'undefined';
            vm.sucursalesSelectFiltroPanel = 'undefined';
            vm.ejecutivosSelectFiltroPanel = undefined;
            vm.usuarioSupervisaA2String = vm.ejecutivosSelectFiltroPanel;
            vm.actualizarGraficoPorFecha(false);
            vm.actualizarGraficoPorSucursal(false);
            vm.actualizarGraficoPorEjecutivo(false);
            vm.mostrarContactos();

        }


        vm.mostrarContactos = function () {
            vm.cargarAccionesEjecutivos();

        }

        vm.actuareComoEjecutivo = function () {
            vm.rollUsuario = 'usr';
            vm.mostrarContactos();
        }

        vm.actuareComoSupervisor = function () {
            vm.rollUsuario = 'sup';
            vm.mostrarContactos();
        }


        vm.mostrarActividadesProspectos = function (c) {
            vm.bitacoras = [];
            vm.messageActividad = new Bitacoras();
            window.scrollTo(0, 0);
            vm.referenciaSeleccionada = c;
            vm.verContactos.contactos = false;
            vm.verContactos.actividades = false;
            vm.verProspectos.contactos = false;
            vm.verProspectos.actividades = true;
            vm.listarActividades();
        }

        var capturarMesAnioFiltroGraph = function () {
            var value = document.getElementById('idMesAnioFiltroGraph').value;
            $scope.enviarCapturarMesAnioFiltroGraph(value);
        }

        var capturarFechaNac = function () {
            var value = document.getElementById('idfechaNac').value;
            $scope.enviarCapturarFechaNac(value);
        }

        var capturarFechaReagendar = function () {
            var value = document.getElementById('idfechaReagendar').value;
            $scope.enviarCapturarFechaReagendar(value);
        }

        var capturarFechaReagendar_reagendar = function () {
            var value = document.getElementById('idfechaReagendar_reagendar').value;
            $scope.enviarCapturarFechaReagendar(value);
        }

        var enviarReagendar = function () {
            var value = document.getElementById('idhora').value;
            $scope.reagendar(value);
        }

        var enviarReagendar_reagendar = function () {

            var isConfirmed = confirm("Desea reagendar ?");
            if (!isConfirmed) {
                return;
            }

            // debugger
            var value = document.getElementById('idhora_reagendar').value;
            $scope.reagendar_reagendar(value);
        }

        vm.activarCalendarFiltroGraph = function () {
            $('#data_4 .input-group.date').change(function () {
                capturarMesAnioFiltroGraph();
            });
            $('#data_4 .input-group.date').datepicker({
                minViewMode: 1,
                keyboardNavigation: false,
                forceParse: false,
                forceParse: false,
                autoclose: true,
                todayHighlight: true,
                format: 'yyyy-mm'
            });
        }

        vm.activarCalendarFicha = function () {
            $('#data_2 .input-group.date').change(function () {
                capturarFechaNac();
            });
            $('#data_2 .input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true,
                format: 'yyyy/mm/dd'
            });

        }

        vm.activarCalendarReloj = function () {
            $('#clockpicker').clockpicker({
                init: function () {
                    // //console.log("colorpicker initiated");
                },
                beforeShow: function () {
                    // //console.log("before show");
                },
                afterShow: function () {
                    // //console.log("after show");
                },
                beforeHide: function () {
                    // //console.log("before hide");
                },
                afterHide: function () {
                    // //console.log("after hide");
                },
                beforeHourSelect: function () {
                    // //console.log("before hour selected");
                },
                afterHourSelect: function () {
                    // //console.log("after hour selected");
                },
                beforeDone: function () {
                    // //console.log("before done");
                },
                afterDone: function () {
                    // debugger
                    enviarReagendar();
                }
            });
            $('#data_1 .input-group.date').change(function () {
                capturarFechaReagendar();
            });
            $('#data_1 .input-group.date').datepicker({
                setDate: new Date(),
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true,
                format: 'yyyy/mm/dd'
            });

            $('#data_1 .input-group.date').datepicker('update', new Date() + 1);

        }


        vm.activarCalendarReloj_reagendar = function () {
            $('#clockpicker_reagendar').clockpicker({
                init: function () {
                    // //console.log("colorpicker initiated");
                },
                beforeShow: function () {
                    // //console.log("before show");
                },
                afterShow: function () {
                    // //console.log("after show");
                },
                beforeHide: function () {
                    // //console.log("before hide");
                },
                afterHide: function () {
                    // //console.log("after hide");
                },
                beforeHourSelect: function () {
                    // //console.log("before hour selected");
                },
                afterHourSelect: function () {
                    // //console.log("after hour selected");
                },
                beforeDone: function () {
                    // //console.log("before done");
                },
                afterDone: function () {
                    // debugger
                    enviarReagendar_reagendar();
                }
            });

            $('#data_1_reagendar .input-group.date').change(function () {
                capturarFechaReagendar_reagendar();
            });
            $('#data_1_reagendar .input-group.date').datepicker({
                setDate: new Date(),
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true,
                format: 'yyyy/mm/dd'
            });

            $('#data_1_reagendar .input-group.date').datepicker('update', new Date() + 1);

        }


        vm.mostrarActividadesContactos = function (c) {
            vm.deshabilitadoOtros = true;
            vm.activarCalendarReloj();
            vm.activarCalendarReloj_reagendar();





            vm.referenciaSeleccionada = c;

            vm.referencias = new Referencias(vm.referenciaSeleccionada);

            vm.referencias.$update({},
                function (res) {

                    if (res.ownere != undefined) {
                        vm.ejecutivo = vm.getNombreEjecutiva(res.ownere);
                    }

                    vm.SeTomoUnaReferencia = true;
                    vm.cerrarSinExitoFlag = null
                    vm.cerrarConExitoFlag = null
                    vm.mailCliente2Send = null;
                    vm.mensajesPreEstablecidosSelect = null;
                    vm.ejecutivosSelect = null;
                    vm.hora = $filter('date')(new Date, 'H:mm');
                    vm.fechaReagendar = $filter('date')(new Date, 'yyyy-MM-dd');
                    vm.fechaHoraReagendar = null;
                    vm.bitacoras = [];
                    vm.messageActividad = new Bitacoras();
                    window.scrollTo(0, 0);
                    vm.verContactos.contactos = false;
                    vm.verContactos.actividades = true;
                    vm.verProspectos.contactos = false;
                    vm.verProspectos.actividades = false;
                    vm.listarActividades();

                },
                function (err) {
                    vm.mostrarLoading = false;
                    toastr.error("Ha ocurrido un problema al bloquear las referencias.", " " + err.status + " " + err.statusText)

                    //console.log(" ** referencias Err ->");
                    //console.log(err);

                });

        }


        //mostrarpestañas
        vm.fverEncuesta1 = function () {

            vm.verEncuesta1 = true;
            vm.verEncuesta2 = !vm.verEncuesta1;
            vm.fichaCanalDigitalPreguntas();
            //                vm.fichaCanalDigital();

        }

        vm.fverEncuesta2 = function () {
            vm.verEncuesta2 = true;
            vm.verEncuesta1 = !vm.verEncuesta2;
        }

        vm.cargarPerfilDeUsuario = function () {


            RolesUsuariosNombre.list({
                "id": vm.usuario
            },
                function (res) {

                    //console.log(" ** RolesUsuariosNombre Ok ->");
                    //console.log(res);
                    // vm.rollUsuario = res.rol;


                    // obtiene el roll
                    vm.rollUsuario = $filter('filter')(res, function (t) {
                        if (t.nombre.toUpperCase() == vm.usuario.toUpperCase()) {
                            return t;
                        }
                    });

                    vm.rollUsuario = vm.rollUsuario[0].rol;
                    vm.dobleRoll = (vm.rollUsuario == 'sup') ? true : false;

                    vm.mostrarPerfil = vm.usuario + " / " + ((vm.rollUsuario == 'adm') ? "Administrador" : (vm.rollUsuario == 'sup') ? "Supervisor" : (vm.rollUsuario == 'usr') ? "Ejecutivo" : "Asistente");

                    // obtiene a quien supervisa
                    vm.supervisa = $filter('filter')(res, function (t) {
                        if (t.estado == 'es_supervisor_de') {
                            return t;
                        }
                    });


                    // obtiene de quien es asistente
                    vm.esAsistenteDe = $filter('filter')(res, function (t) {
                        if (t.estado == 'es_asistente_de') {
                            return t;
                        }
                    });

                    // valida que sea sup el usuario entrante
                    if (vm.rollUsuario == 'sup') {
                        vm.usuarioSupervisaA = [];
                        vm.usuarioSupervisaA.push("'" + vm.usuario + "'");
                        angular.forEach(vm.supervisa, function (d) {
                            vm.usuarioSupervisaA.push("'" + d.nombre + "'");
                        });

                        vm.usuarioSupervisaA2String = vm.usuarioSupervisaA.toString();
                    }


                    // valida que sea asistente
                    if (vm.rollUsuario == 'ast') {
                        vm.usuarioEsAsistenteDe = [];
                        angular.forEach(vm.esAsistenteDe, function (d) {
                            vm.usuarioEsAsistenteDe.push(d.nombre);
                        });

                        vm.usuarioEsAsistenteDe2String = vm.usuarioEsAsistenteDe.toString();


                    }

                    vm.losAsistentesBuscaranInfPor = (vm.rollUsuario == 'ast') ? vm.usuarioEsAsistenteDe2String : vm.usuario;

                    // if (vm.losAsistentesBuscaranInfPor.length == 0) {
                    //     toastr.error("Este asistente no esta asociado a ninguna ejecutiva.", " ");
                    // }


                    //coxrestaurar descomentar
                    vm.dataTableOpt = {
                        "fnRowCallback": function (nRow, aData, iDisplayIndex) {

                            // console.log(aData);

                            if (aData[10].includes('Priorizado')) {
                                $('td', nRow).each(function () {
                                    $(this).addClass('bold');
                                });
                            }
                            return nRow;
                        },
                        "autoWidth": true,
                        "responsive": true,
                        "serverSide": true,
                        'ajax': {
                            'contentType': "application/json; charset=utf-8",
                            'url': "webresources/cl.cnsv.referidosrrvv.models.referencias/nocerrado/fullajax",
                            beforeSend: function (x, y) {
                                var sup = (vm.usuarioSupervisaA2String == undefined) ? "undefined" : vm.usuarioSupervisaA2String;
                                y.url = y.url + "&sup=" + sup + "&anio=" + vm.anioParamUrlDataTable + "&mes=" + vm.mesParamUrlDataTable + "&suc=" + vm.sucursalesSelectFiltroPanel2;
                                console.log("*************************")
                                console.log(y.url)
                                console.log("*************************")
                            },

                        },
                        "processing": true,
                        "lengthMenu": [10, 25, 50, 100],
                        "paginate": true,
                        "language": {
                            "decimal": "",
                            "emptyTable": "No hay información",
                            "info": "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
                            "infoEmpty": "Mostrando 0 to 0 of 0 Entradas",
                            "infoFiltered": "(Filtrado de _MAX_ total entradas)",
                            "infoPostFix": "",
                            "thousands": ",",
                            "lengthMenu": "Mostrar _MENU_ Entradas",
                            "loadingRecords": "Cargando...",
                            "processing": "Procesando...",
                            "search": "Buscar:",
                            "zeroRecords": "Sin resultados encontrados",
                            "paginate": {
                                "first": "Primero",
                                "last": "Ultimo",
                                "next": "Siguiente",
                                "previous": "Anterior"
                            }
                        },
                        'columnDefs': [{
                            'targets': 0,
                            'searchable': false,
                            'orderable': false,
                            'className': 'dt-body-center',
                            'render': function (data, type, full, meta) {
                                return vm.comboBox;
                            }
                        }],

                    };

                    if (vm.dobleRoll) {
                        // vm.actuareComoEjecutivo();
                        vm.actuareComoSupervisor()
                    } else {
                        vm.cargarAccionesEjecutivos();

                    }


                },
                function (err) {
                    vm.mostrarLoading = false;


                    toastr.error("Hubo un problema al tratar de cargar el rol del usuario. ", "Atencion " + err.status + " " + err.statusText);
                    //console.log(" ** RolesUsuariosNombre Err ->");
                    //console.log(err);


                });


        }

       // coxrestaurar
       Auth.loadUserInfo().success(function(userInfo) {
           vm.mailUsuario = userInfo.email;
           vm.usuario = userInfo.preferred_username;
           vm.usuarioname = userInfo.name;
           console.log("**** Ok -> keycloak.loadUserInfo");
           vm.cargarPerfilDeUsuario();

       }).error(function(err) {
           console.log("**** Err -> keycloak.loadUserInfo");


       });

        // coxrestaurar borrar
       //vm.cargarPerfilDeUsuario();



    });