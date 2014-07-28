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
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
                        scope.updatePromise = $timeout(function () {
                            scope.updateFluid();
                            scope.updatePromise = null;
                        }, 500);
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

                        var containerWidth = element.innerWidth(),
                            lingItemMargin = attrs.fluidLineItemMargin || 0;
                        $log.debug("lingItemMargin: " + lingItemMargin);

                        var items = element.find(itemSelector);
                        minimizationAlgorithm(items, containerWidth, lingItemMargin);
                    }

                    function setWidthHeight(items, height) {
                        if (height > maxHeight) {
                            height = maxHeight;
                        }
                        var border = Number(attrs.fluidLineBorder);
                        var itemMargin = Number(attrs.fluidLineItemMargin);
                        height -= 4*itemMargin;
                        height = Math.round(height);
                        angular.forEach(items, function (item, key) {
                            var img = item.find("img");
                            item.css("height", height + "px");
                            img.css("height", (height - 2*border) + "px");
                        });
                    }

                    scope.updateFluid = function() {
                        $log.debug("updateFluid");
                        if ($window.imagesLoaded) {
//                            $log.debug(element.find(itemSelector).length);
                            var imgLoad = $window.imagesLoaded && $window.imagesLoaded(element);
                            // bind with .on()
                            imgLoad.on('always', function (e) {
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

                    function maximumAlgorithm(items, containerWidth, lingItemMargin) {
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(items, function (item, key) {
                            item = angular.element(item);
                            var itemWidth = item.outerWidth() + 2*lingItemMargin,
                                itemHeight = item.outerHeight() + 2*lingItemMargin;
//                            $log.debug("itemWidth: " + itemWidth + "itemHeight: " + itemHeight);
                            var itemLineWidthTemp = itemLineWidth + Math.round(itemWidth * minHeight / itemHeight);
                            if (itemLineWidthTemp > containerWidth) {
//                                $log.debug("itemLineWidth: " + itemLineWidth);
                                setWidthHeight(itemLine, Math.round(containerWidth / itemLineWidth * minHeight));
                                itemLine = [item];
                                itemLineWidth = Math.round(minHeight * itemWidth / itemHeight);
                            } else {
                                itemLine.push(item);
                                itemLineWidth = itemLineWidthTemp;
                            }
                        });
                        if (itemLine.length) {
//                            $log.debug("itemLineWidth: " + itemLineWidth);
                            setWidthHeight(itemLine, Math.round(containerWidth / itemLineWidth * minHeight));
                        }
                    }

                    function minimizationAlgorithm(items, containerWidth, lingItemMargin) {
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(items, function (item, key) {
                            item = angular.element(item);
                            var itemWidth = item.outerWidth() + 2*lingItemMargin,
                                itemHeight = item.outerHeight() + 2*lingItemMargin;
//                            $log.debug("itemWidth: " + itemWidth + "itemHeight: " + itemHeight);
                            itemLine.push(item);
                            itemLineWidth = itemLineWidth + Math.round((itemWidth / itemHeight) * maxHeight);
                            if (itemLineWidth >= containerWidth) {
//                                $log.debug("itemLineWidth: " + itemLineWidth);
                                setWidthHeight(itemLine, Math.round(containerWidth * (maxHeight / itemLineWidth)));
                                itemLine = [];
                                itemLineWidth = 0;
                            }
                        });
                        if (itemLine.length) {
                            setWidthHeight(itemLine, Math.round((containerWidth / itemLineWidth) * maxHeight));
                        }
                    }
                }
            };
        }])
    .directive('ponmHover', ['$window', '$parse', '$animate', '$log',
        function ($window, $parse, $animate, $log) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs, ngModel) {

                    element.on('mouseenter', function (e) {
                        var photoAction = element.find(attrs.ponmHover);
                        $animate.addClass(photoAction, "ponm-show");
                    });

                    element.on('mouseleave', function (e) {
                        var photoAction = element.find(attrs.ponmHover);
                        $animate.removeClass(photoAction, "ponm-show");
                    });
                }
            };
        }])
;