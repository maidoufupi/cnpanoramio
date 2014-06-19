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
  .config(['$routeProvider', '$logProvider', '$sceDelegateProvider',
  function ($routeProvider, $logProvider, $sceDelegateProvider) {
        // enable log debug level
        $logProvider.debugEnabled = true;

      $sceDelegateProvider.resourceUrlWhitelist([
          // Allow same origin resource loads.
          'self',
          // Allow loading from our assets domain.  Notice the difference between * and **.
          'http://bdimg.share.baidu.com/**']);
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
