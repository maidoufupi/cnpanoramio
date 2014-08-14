/**
 * Created by any on 2014/6/20.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('waypoint', ['$log', '$timeout', function ($log, $timeout) {

        var defaults = {
            stuckClass: 'stuck',
            direction: 'down right'
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $log.debug("waypoint...");

                var options;
                var opt = scope.$eval(attrs.waypoint || "{}");
                options = angular.extend({}, $.fn.waypoint.defaults, defaults, opt);

                $timeout( function() {
                    $log.debug("set waypoint");

                    $( element ).waypoint(function(direction) {
                        scope.$emit('waypointEvent', direction, opt.id);
                    }, options);
                }, 500);

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
    }])
    .directive('waypointSticky', ['$log', '$timeout', function ($log, $timeout) {

        var defaults;

        defaults = {
            stuckClass: 'stuck',
            direction: 'down right'
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $log.debug("waypoint sticky...");

                var options, originalHandler, opt, $sticky;

                opt = scope.$eval(attrs.waypointSticky || "{}");

                options = $.extend({}, $.fn.waypoint.defaults, defaults, opt);
                originalHandler = options.handler;
                $sticky = $(element);
                options.handler = function(direction) {
                    var shouldBeStuck;

                    shouldBeStuck = options.direction.indexOf(direction) !== -1;
                    $sticky.toggleClass(options.stuckClass, shouldBeStuck);

//                    $log.debug(direction);
                    if (originalHandler != null) {
                        return originalHandler.call(element, direction);
                    }
                };

                $timeout(function() {
                    $sticky.waypoint(options);
                    $sticky.data('stuckClass', options.stuckClass);
                }, 500);

                attrs.$observe('waypointEnable', function ( data ) {
                    if(data && data == "true") {

                    }else {
                        $sticky.waypoint('destroy');
                        $sticky.removeClass($sticky.data('stuckClass'));
                    }
                });

            }
        }
    }])
    .directive('whenScrolled', function() {
        return function(scope, elm, attr) {
            var raw = elm[0];

            elm.bind('scroll', function() {
                if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
                    scope.$apply(attr.whenScrolled);
                }
            });
        };
    })
    .directive('waypointInfinite', ['$log', '$timeout', function ($log, $timeout) {

        var defaults;

        defaults = {
            container: 'auto',
            offset: 'bottom-in-view',
            loadingClass: 'infinite-loading',
            onBeforePageLoad: $.noop
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $log.debug("waypoint infinite...");

                var $container, opts, options;

                options = scope.$eval(attrs.waypointInfinite || "{}");

                opts = $.extend({}, $.fn.waypoint.defaults, defaults, options);

                $container = opts.container === 'auto' ? element : angular.element(opts.container);
                opts.handler = function(direction) {

                    if (direction === 'down' || direction === 'right') {

                        scope[opts.onBeforePageLoad]().then(function() {
                            $container.removeClass(opts.loadingClass);

                            $.waypoints('refresh');

//                            $(element).waypoint('destroy');
//                            $timeout(function() {
//                                $(element).waypoint(opts);
//
//                            }, 100);

//                            $(element).waypoint('disable');
//                            $(element).waypoint('enable');
                        });
                        $container.addClass(opts.loadingClass);
                    }else {
                        $.waypoints('refresh');
                        $.waypoints('viewportHeight');
                    }
                };
                $(element).waypoint(opts);

                attrs.$observe('waypointDisable', function ( data ) {
                    if(data && data == "true") {
                        $(element).waypoint('destroy');
                    }
                });
            }
        }
    }])
;