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
  .config(['$logProvider', '$sceDelegateProvider', 'uiMapLoadParamsProvider',
  function ($logProvider, $sceDelegateProvider, uiMapLoadParamsProvider) {
      // enable log debug level
      $logProvider.debugEnabled = false;

      $sceDelegateProvider.resourceUrlWhitelist([
          // Allow same origin resource loads.
          'self',
          // Allow loading from our assets domain.  Notice the difference between * and **.
          'http://bdimg.share.baidu.com/**']);

      if(window.mapVendor == "baidu") {
          uiMapLoadParamsProvider.setParams({
              v: '2.0',
              ak:'kp3ODQt4pkpHMW2Yskl2Lwee'
          });
      }else if(window.mapVendor == "qq") {
          uiMapLoadParamsProvider.setParams({
              v: '2.0',
              key:'ZYZBZ-WCCHU-ETAVP-4UZUB-RGLDJ-QDF57'
          });
      }else if(window.mapVendor == "google") {
          uiMapLoadParamsProvider.setParams({
              key:'AIzaSyA9e11ZRebw8fHsd1ZIi0FLTXsrSQTc490'
          });
      }else {
          uiMapLoadParamsProvider.setParams({
              v: '1.3',
              key:'53f7e239ddb8ea62ba552742a233ed1f'
          });
      }
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
