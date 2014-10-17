/**
 * Created by any on 2014/10/17.
 */
'use strict';

angular.module('ponmApp.directives')
.directive('ponmDynamicMenu', ['$log', function($log) {
    // Runs during compile
    return {
        name: 'ponmDynamicMenu',
        scope: {
            navbar: '='
        },
        restrict: 'EA', // E = Element, A = Attribute, C = Class, M = Comment
        templateUrl: 'views/ponm-dynamic-menu.html',
        replace: true,
        link: function(scope, element, attrs) {

            scope.more = {
                more: true,
                active: false,
                once: false,
                hidden: false
            };
            scope.normal = {
                more: false,
                hidden: false
            };
            scope.once = {
                once: true
            };

            scope.$on("dynamic.menu", function() {
                calcNavbar();
            });

            function calcNavbar() {
                var newOnce = false;
                angular.forEach(scope.navbar, function(nav, key) {
                    if(nav.more&&nav.active) {
                        nav.once = true;
                        newOnce = true;
                    }
                    if(!nav.hidden) {
                        nav.hidden = false;
                    }
                });
                angular.forEach(scope.navbar, function(nav, key) {
                    if(nav.more&&!nav.active) {
                        if(newOnce) {
                            nav.once = false;
                        }else if(!nav.once) {
                            nav.once = false;
                        }

                    }
                });
            }

            calcNavbar();
        }
    };
}]);