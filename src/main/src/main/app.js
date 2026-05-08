'use strict';

// var app = angular.module("todo", ['ngMaterial', 'ngMessages',  'ngCookies', 'ngResource', 'angular-js-xlsx',  'ui.utils']);

(function() {
 

    var app = angular.module("todo", ['ngMaterial', 'ngMessages', 'ngCookies', 'ngResource', 'angular-js-xlsx', 'ui.utils']);

    function initializeKeycloak() {

        var keycloak = Keycloak('keycloak.json');
        //coxrestaurar
        // var keycloak = Keycloak('keycloak_OLD.json');

        keycloak.init({
            onLoad: 'login-required'
        }).success(function() {
            console.log("**** Ok -> keycloak.init");

            app.factory('Auth', function() {

                console.log("**** Ok -> entro a return auth ");

                return keycloak;

            });

            console.log("**** Ok -> angular.bootstrap");
            angular.bootstrap(document, ["todo"]);
            console.log("**** Ok -> paso angular.bootstrap");


        }).error(function() {
            // debugger;
            console.log("**** Err -> keycloak.init");
        });

 
    }

    angular.element(document).ready(function($http) {

        //coxrestaurar
        initializeKeycloak();
        //coxrestaurar eliminar
        //angular.bootstrap(document, ["todo"]); 

 
    }); 


}());