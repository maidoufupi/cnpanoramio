/**
 * Created by any on 2014/6/20.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('waypoint', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                setTimeout( function() {
                    $( element ).waypoint(function() {
                        scope.$emit('waypointEvent', attr.waypoint);
                    }, {
                        context: '.waypoint-scrollable',
                        continuous: false,
                        offset: "10%"
                    });
                }, 2000);
            }
        }
    });