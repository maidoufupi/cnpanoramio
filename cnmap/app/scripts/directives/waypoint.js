/**
 * Created by any on 2014/6/20.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('waypoint', ['$log', function ($log) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $log.debug("waypoint...");

                setTimeout( function() {
                    $log.debug("set waypoint");
                    $( element ).waypoint(function(direction) {
                        $log.debug("waypoint - waypointEvent");
                        scope.$emit('waypointEvent', direction, attrs.waypoint);
                    }, {
                        context: '.waypoint-scrollable',
                        continuous: true,
                        offset: attrs.waypointOffset || "10%"
                    });
                }, 1000);

                attrs.$observe('waypointRefresh', function ( data ) {
                    $.waypoints('refresh');
                });
                attrs.$observe('waypointEnable', function ( data ) {
                    if(data && data == "true") {
                        $( element ).waypoint('enable');
                    }else {
                        $( element ).waypoint('disable');
                    }
                });
            }
        }
    }]);