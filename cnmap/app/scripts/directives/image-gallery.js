/**
 * Created by any on 2014/8/14.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('imageGallery', ['$parse', '$log', 'jsUtils',
        function($parse, $log, jsUtils) {

            // this directive default options
            var defaults = {
            };

            // blueimp gallery default options
            var galleryDefaults = {
                fullScreen: true
            };

            return {
                restrict: 'EA',
                templateUrl: 'views/imageGallery.html',
                link: function (scope, element, attrs) {

                    // set options
                    var options;
                    var opt = scope.$eval(attrs.imageGallery || attrs.options || "{}");
                    options = angular.extend({}, defaults, opt);

                    // set gallery container
                    var container = element.find(".blueimp-gallery");
                    galleryDefaults.container = container;

                    // get gallery images data
//                    var data = scope.$eval(options.galleryData);

                    if(options.galleryCallback) {
                        scope.$eval(options.galleryCallback)(function(data, opts) {
                            blueimp.Gallery(data, angular.extend({}, galleryDefaults, opts));
                        });
                    }

                    // display gallery when event fired
                    scope.$on("image-gallery", function(e, opts) {
                        // get gallery images data
                        var data = scope.$eval(options.galleryData || 'galleryData');
                        blueimp.Gallery(data, angular.extend({}, galleryDefaults, opts));
                    });

                }
            };
        }])
;