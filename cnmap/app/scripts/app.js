'use strict';

angular.module('ponmApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ponmApp.services',
    'ponmApp.directives',
    'ponmApp.controllers'
  ])
  .config(['$routeProvider', '$logProvider', function ($routeProvider, $logProvider) {
        // enable log debug level
        $logProvider.debugEnabled = true;
  }])
    .run(['$rootScope', '$window', function($rootScope, $window) {
        if($window.login) {
            $rootScope.user = {
                id: $window.userId,
                loggedIn: true
            }
        }else {
            $rootScope.user = {
                loggedIn: false
            }
        }

    }]);
