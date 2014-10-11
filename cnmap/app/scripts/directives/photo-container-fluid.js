/**
 * Created by any on 2014/10/11.
 */
'use strict';
angular.module('ponmApp.directives')
    .directive('photoContainerFluid', ['$window', '$parse', '$animate', '$log', '$timeout',
        function ($window, $parse, $animate, $log, $timeout) {
            return {
                restrict: 'EA',
                scope: {
                    photos: "=photos"
                },
                link: function (scope, element, attrs, controller) {

                    var options = {
                            size: "maxi",
                            lineMaxHeight: 200,
                            lineMinHeight: 100,
                            itemSelector: ".fluid-brick",
                            itemMargin: 0,
                            itemBorder: 0 },

                        opt;

                    opt = scope.$eval(attrs.photoContainerFluid || "{}");

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

                    scope.$on('ponmPhotoFluidResize', function (e) {
                        $log.debug("ponmPhotoFluidResize");
                        scope.updateFluid();
                    });

                    scope.updateFluid = function() {
                        $log.debug("updateFluid");
                        if(scope.updatePromise) {
                            $timeout.cancel(scope.updatePromise);
                        }
                        scope.updatePromise = $timeout(function () {
                            calculateHeight();
                            $timeout(function () {
                                scope.$apply(function(){});
                            },500);
                        }, 500);
                    };

                    function calculateHeight() {

                        var containerWidth = element.innerWidth() - 5;

                        if(options.size == "maxi") {
                            maximumAlgorithm(scope.photos, containerWidth);
                        }else if(options.size == "mini") {
                            minimizationAlgorithm(scope.photos, containerWidth);
                        }

                        scope.$emit("ponm-photo-fluid-resized");
                    }

                    function setWidthHeight(photos, height, containerWidth) {
                        if (height > options.lineMaxHeight) {
                            height = options.lineMaxHeight;
                        }

                        var border = Number(options.itemBorder);
                        var itemMargin = Number(options.itemMargin);
                        height -= 2*itemMargin;
                        var lineWidth = 0;
                        angular.forEach(photos, function (photo, key) {
                            photo.fluid = {
                                height: height,
                                width: Math.ceil(height * (photo.width/photo.height))
                            };
                            lineWidth += (photo.fluid.width+2*itemMargin);
                            photo.fluid.lineWidth = lineWidth;
                        });
                        if(lineWidth < containerWidth) {
                            $log.debug("containerWidth="+containerWidth+"; lineWidth="+lineWidth);
                            $log.debug("photo id=" + photos[photos.length-1].id + " width="+photos[photos.length-1].width);
                            $log.debug("photo height="+photos[photos.length-1].height);
                            photos[photos.length-1].fluid.width += (containerWidth - lineWidth);
                            $log.debug("photo width="+photos[photos.length-1].fluid.width);
                        }
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

                    function minimizationAlgorithm(photos, containerWidth) {
                        var itemLine = [],
                            itemLineWidth = 0;
                        angular.forEach(photos, function (photo, key) {
                            var itemWidth = photo.width + 2*options.itemMargin,
                                itemHeight = photo.height + 2*options.itemMargin;

                            var itemLineWidthTemp = itemLineWidth + (itemWidth * options.lineMinHeight / itemHeight);
                            if (itemLineWidthTemp > containerWidth) {
//                                $log.debug("itemLineWidth: " + itemLineWidth);
                                setWidthHeight(itemLine, Math.ceil(containerWidth / itemLineWidth * options.lineMinHeight),
                                    containerWidth);
                                itemLine = [photo];
                                itemLineWidth = (itemWidth * options.lineMinHeight / itemHeight);
                            } else {
                                itemLine.push(photo);
                                itemLineWidth = itemLineWidthTemp;
                            }
                        });
                        if (itemLine.length) {
//                            $log.debug("itemLineWidth: " + itemLineWidth);
                            setWidthHeight(itemLine, Math.ceil(containerWidth / itemLineWidth * options.lineMinHeight),
                                containerWidth);
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
                                setWidthHeight(itemLine, Math.round(containerWidth * (options.lineMaxHeight / itemLineWidth)));
                                itemLine = [];
                                itemLineWidth = 0;
                            }
                        });
                        if (itemLine.length) {
                            setWidthHeight(itemLine, Math.round(containerWidth * (options.lineMaxHeight / itemLineWidth)));
                        }
                    }
                }
            };
        }])
;