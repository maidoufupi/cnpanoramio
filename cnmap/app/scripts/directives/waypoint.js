/**
 * Created by any on 2014/6/20.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('waypoint', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                setTimeout( function() {
                    $( element ).waypoint(function(direction) {
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
    });