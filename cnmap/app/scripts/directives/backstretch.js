/**
 * Created by any on 2014/6/20.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('backstretch', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                jQuery(element).backstretch(attrs.backgroundUrl);
                attrs.$observe("backgroundUrl", function(url) {
                    jQuery(element).backstretch(url);
                });
            }
        }
    });