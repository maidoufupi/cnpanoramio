/**
 * Created by any on 2014/6/23.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmMapControls', ['$window', '$animate', '$log', 'param', '$q',
        function ($window, $animate, $log, param, $q) {
            return {
                restrict: 'EA',
                scope: {
                    map: "=ponmMap",
                    mapService: "=ponmMapService",
                    mapEventListener: "=ponmMapEventListener"
                },
                templateUrl: "views/ponmMapControls.html",
                link: function (scope, element, attrs) {

                    scope.selected = undefined;
                    scope.states = [];
                    // Any function returning a promise object can be used to load values asynchronously
                    scope.getLocation = function (val) {
                        var d = $q.defer();
                        scope.mapService.getLocPois(val, function(res) {
                            d.resolve(res);
                        });
                        return d.promise.then(function(res) {
                            return res;
                        });
                    };

                    scope.goLocation = function (address) {

                        var location = address.location;
                        if (location) {
                            scope.mapEventListener.setCenter(scope.map, location.lat, location.lng);
                            scope.mapEventListener.setZoom(scope.map, address.zoom);
                        }
                    }
                }
            };
        }])
;