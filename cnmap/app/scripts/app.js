'use strict';

angular.module('cnmapApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
  })
    .run(function() {
        window.ctx = "http://localhost:8080/panor-web";
        window.apirest = "http://localhost:8080/panor-web/api/rest";
    });
