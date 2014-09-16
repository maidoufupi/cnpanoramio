'use strict';

angular.module('ponmApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngAnimate',
    'LocalStorageModule',
    'xeditable',
    'ui.router',
    'ui.bootstrap',
    'ponmApp.services',
    'ponmApp.directives',
    'ponmApp.controllers'
  ])
  .config(['$logProvider', '$sceDelegateProvider',
  function ($logProvider, $sceDelegateProvider) {
        // enable log debug level
        $logProvider.debugEnabled = false;

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
