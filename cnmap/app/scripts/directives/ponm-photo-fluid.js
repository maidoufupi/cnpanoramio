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

                    var options = {
                            size: "maxi",
                            lineMaxHeight: 200,
                            lineMinHeight: 100,
                            itemSelector: ".fluid-brick",
                            itemMargin: 0,
                            itemBorder: 0 },

                        opt;

                    opt = scope.$eval(attrs.photoFluidContainer || "{}");

                    angular.extend(options, opt);

                    scope.$watch(function() {
                        return element.innerWidth && element.innerWidth();
                    }, function() {
                        $log.debug("container width changed");
                        scope.updateFluid();
                    });

                    angular.element($window).bind("resize", function (e) {
                        scope.updateFluid();
                    });

                    scope.$on('ponm.photo.fluid.resize', function (e) {
                        $log.debug("ponm.photo.fluid.resize");
                        scope.updateFluid();
                    });

                    scope.updateFluid = function() {
                        $log.debug("updateFluid");
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
//                        scope.updatePromise = $timeout(function () {
//                            if ($window.imagesLoaded) {
////                            $log.debug(element.find(options.itemSelector).length);
//                                var imgLoad = $window.imagesLoaded && $window.imagesLoaded(element);
//                                // bind with .on()
//                                imgLoad.on('always', function (e) {
//                                    calculateHeight();
//                                    $timeout(function () {
//                                        scope.$apply(function(){});
//                                    },500);
//                                });
//                            }
//                        }, 500);
                        scope.updatePromise = $timeout(function () {
                            calculateHeight();
                            $timeout(function () {
                                scope.$apply(function(){});
                            },500);
                        }, 500);
                    };

                    function calculateHeight() {

                        var containerWidth = element.innerWidth();

                        var items = element.find(options.itemSelector);
                        if(options.size == "maxi") {
                            maximumAlgorithm(items, containerWidth);
                        }else if(options.size == "mini") {
                            minimizationAlgorithm(items, containerWidth);
                        }

                        scope.$emit("ponm-photo-fluid-resized");
                    }

                    function setWidthHeight(items, height) {
                        if (height > options.lineMaxHeight) {
                            height = options.lineMaxHeight;
                        }
                        var border = Number(options.itemBorder);
                        var itemMargin = Number(options.itemMargin);
                        height -= 2*itemMargin;
//                        height = Math.floor(height) - 1;
                        angular.forEach(items, function (item, key) {
                            var img = item.find("img");
                            item.css("height", height + "px");
//                            item.css("opacity", 1);
//                            item.css("display", "inline-block");
                            img.css("height", (height - 2*border) + "px");
//                            img.css("opacity", 1);
//                            img.css("display", "inline-block");
                        });
                    }

                    scope.$watch(attrs.photoFluidContainer, function() {
//                        $log.debug("photoFluidContainer changed");
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
                        scope.updatePromise = $timeout(function () {
                            scope.updateFluid();
                            scope.updatePromise = null;
                        }, 1000);
                    });

                    function minimizationAlgorithm(items, containerWidth) {
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(items, function (item, key) {
                            item = angular.element(item);

                            if(item.scope && item.scope() &&
                                item.scope().photo &&
                                item.scope().photo.width &&
                                item.scope().photo.height) {

                                var itemWidth = item.scope().photo.width + 2 * options.itemMargin,
                                    itemHeight = item.scope().photo.height + 2 * options.itemMargin;
                            }else {
                                var itemWidth = item.outerWidth() + 2*options.itemMargin,
                                    itemHeight = item.outerHeight() + 2*options.itemMargin;
                            }

                            var itemLineWidthTemp = itemLineWidth + (itemWidth * options.lineMinHeight / itemHeight);
                            if (itemLineWidthTemp > containerWidth) {
//                                $log.debug("itemLineWidth: " + itemLineWidth);
//                                setWidthHeight(itemLine, Math.floor(containerWidth / itemLineWidth * options.lineMinHeight));
                                setWidthHeight(itemLine, (containerWidth / itemLineWidth * options.lineMinHeight));
                                itemLine = [item];
//                                itemLineWidth = Math.floor(options.lineMinHeight * itemWidth / itemHeight);
                                itemLineWidth = (itemWidth * options.lineMinHeight / itemHeight);
                            } else {
                                itemLine.push(item);
                                itemLineWidth = itemLineWidthTemp;
                            }
                        });
                        if (itemLine.length) {

//                            setWidthHeight(itemLine, Math.floor(containerWidth / itemLineWidth * options.lineMinHeight));
                            setWidthHeight(itemLine, (containerWidth / itemLineWidth * options.lineMinHeight));
                        }
                    }

                    function maximumAlgorithm(items, containerWidth) {
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(items, function (item, key) {
                            item = angular.element(item);
                            if(item.scope && item.scope() &&
                                item.scope().photo &&
                                item.scope().photo.width &&
                                item.scope().photo.height) {

                                var itemWidth = item.scope().photo.width + 2 * options.itemMargin,
                                    itemHeight = item.scope().photo.height + 2 * options.itemMargin;
                            }else {
                                var itemWidth = item.outerWidth() + 2*options.itemMargin,
                                    itemHeight = item.outerHeight() + 2*options.itemMargin;
                            }

                            itemLine.push(item);
                            itemLineWidth = itemLineWidth + ((itemWidth / itemHeight) * options.lineMaxHeight);
                            if (itemLineWidth >= containerWidth) {
                                setWidthHeight(itemLine, Math.floor(containerWidth * (options.lineMaxHeight / itemLineWidth)));
                                itemLine = [];
                                itemLineWidth = 0;
                            }
                        });
                        if (itemLine.length) {
                            setWidthHeight(itemLine, Math.floor(containerWidth * (options.lineMaxHeight / itemLineWidth)));
                        }
                    }
                }
            };
        }])
;