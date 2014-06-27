/**
 * Created by any on 2014/6/6.
 */
'use strict';
angular.module('ponmApp.directives')
    .directive('photoFluidContainer', ['$window', '$parse', '$animate', '$log', '$timeout',
        function ($window, $parse, $animate, $log, $timeout) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs, controller) {
                    var maxHeight = 0, minHeight = 0;
                    maxHeight = scope.$eval(attrs.fluidLineMaxHeight) || 200;
                    minHeight = scope.$eval(attrs.fluidLineMinHeight) || 100;
                    var itemSelector = attrs.itemSelector || ".fluid-brick";

                    $($window).resize(function (e) {
                        $log.debug("window resize");
                        calculateHeight();
                    });

                    scope.$on('ponmPhotoFluidResize', function (e) {
                        $log.debug("ponmPhotoFluidResize");
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
                        scope.updatePromise = $timeout(function () {
                            scope.updateFluid();
                            scope.updatePromise = null;
                        }, 500);

                    });

                    function calculateHeight() {

                        var containerWidth = element.innerWidth() - 10;
//                        $log.debug("containerWidth: " + containerWidth);

                        var items = element.find(itemSelector);
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(items, function (item, key) {
                            item = angular.element(item);
                            var itemWidth = item.outerWidth(),
                                itemHeight = item.outerHeight();
//                            $log.debug("itemWidth: " + itemWidth + "itemHeight: " + itemHeight);
                            var itemLineWidthTemp = itemLineWidth + itemWidth * minHeight / itemHeight;
                            if (itemLineWidthTemp > containerWidth) {
//                                $log.debug("itemLineWidth: " + itemLineWidth);
                                setWidthHeight(itemLine, containerWidth / itemLineWidth * minHeight);
                                itemLine = [item];
                                itemLineWidth = minHeight * itemWidth / itemHeight;
                            } else {
                                itemLine.push(item);
                                itemLineWidth = itemLineWidthTemp;
                            }
                        });
                        if (itemLine.length) {
                            $log.debug("itemLineWidth: " + itemLineWidth);
                            setWidthHeight(itemLine, containerWidth / itemLineWidth * minHeight);
                        }
                    }

                    function setWidthHeight(items, height) {
//                        $log.debug("setWidthHeight: " + items.length);
//                        $log.debug("height: " + height);
                        if (height > maxHeight) {
                            height = maxHeight;
                        }
                        angular.forEach(items, function (item, key) {
                            var img = item.find("img");
                            item.css("height", height + "px");
                            img.css("height", (height - 4) + "px");
                        });
                    }

                    scope.updateFluid = function() {
                        $log.debug("updateFluid");
                        if ($window.imagesLoaded) {
//                            $log.debug(element.find(itemSelector).length);
                            var imgLoad = $window.imagesLoaded && $window.imagesLoaded(element);
                            // bind with .on()
                            imgLoad.on('always', function (e) {
                                $log.debug("imagesLoaded");
                                calculateHeight();
                            });
                        }
                    };

                    scope.$watch(attrs.photoFluidContainer, function() {
                        $log.debug("photoFluidContainer changed");
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
                        scope.updatePromise = $timeout(function () {
                            scope.updateFluid();
                            scope.updatePromise = null;
                        }, 1000);
                    });
                }
            };
        }])
//    .directive('ponmPhoto', ['$window', '$parse', '$animate', '$log',
//        function ($window, $parse, $animate, $log) {
//            return {
//                restrict: 'A',
//                link: function (scope, element, attrs, ngModel) {
//                    var photoAction = element.find(".action");
//                    element.on('mouseenter', function (e) {
//                        var photoAction = element.find(".action");
//                        $animate.addClass(photoAction, "ponm-show");
//                    });
//                    element.on('mouseleave', function (e) {
//                        var photoAction = element.find(".action");
//                        $animate.removeClass(photoAction, "ponm-show");
//                    });
//
//                    var photoRemoveButton = element.find(".action.ponm-photo-remove");
//                    photoRemoveButton.on("click", function(e) {
//                        e.preventDefault();
//                        e.stopPropagation();
//                        scope.$emit('photoDeleteEvent', scope.photo.id);
//                    });
//                }
//            };
//        }])
    .directive('ponmHover', ['$window', '$parse', '$animate', '$log',
        function ($window, $parse, $animate, $log) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs, ngModel) {
                    var photoAction = element.find("> .action");
                    element.on('mouseenter', function (e) {
                        var photoAction = element.find("> .action");
                        $animate.addClass(photoAction, "ponm-show");
                    });
                    element.on('mouseleave', function (e) {
                        var photoAction = element.find("> .action");
                        $animate.removeClass(photoAction, "ponm-show");
                    });
                }
            };
        }])
;