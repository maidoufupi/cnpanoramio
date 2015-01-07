/**
 * @license AngularJS
 * (c) 2010-2014 Google, Inc. http://angularjs.org
 * License: MIT
 */
'use strict';

angular.module('ponmApp.directives')
/**
 * 地图控件
 */
.directive('ponmMapPanoramio',
          ['$window', '$animate', '$log', '$q',
  function ($window,   $animate,   $log,   $q) {
    return {
      restrict: 'EA',
      scope: {
        panoramioLayer: "=panoramioLayer"
      },
      templateUrl: "views/ponmMapPanoramio.html",
      link: function (scope, element, attrs) {


      }
    };
  }])
;
