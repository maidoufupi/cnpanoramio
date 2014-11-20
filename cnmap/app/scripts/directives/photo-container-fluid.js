/**
 * Created by any on 2014/10/11.
 */
'use strict';
angular.module('ponmApp.directives')
  .directive('photoContainerFluid', ['$window', '$animate', '$log', '$timeout',
    function ($window, $animate, $log, $timeout) {

      var defaultOptions = {
        // the ideal height you want your images to be
        'targetHeight': 400,
        // how quickly you want images to fade in once ready can be in ms, "slow" or "fast"
        'fadeSpeed': "fast",
        // how the resized block should be displayed. inline-block by default so that it doesn't break the row
        'display': "inline-block",
        // which effect you want to use for revealing the images (note CSS3 browsers only),
        'effect': 'default',
        // effect delays can either be applied per row to give the impression of descending appearance
        // or horizontally, so more like a flock of birds changing direction
        'direction': 'vertical',
        // Sometimes there is just one image on the last row and it gets blown up to a huge size to fit the
        // parent div width. To stop this behaviour, set this to true
        'allowPartialLastRow': false,
        // 是否使用imagesLoaded插件
        'imagesLoaded': false,
        // 延迟计算事件（等待ng-repeat完成）
        'delayTime': 500
      };

      return {
        restrict: 'EA',
        link: function (scope, element, attrs, controller) {

          var options = {},
            opt;

          opt = scope.$eval(attrs.photoContainerFluid || "{}");

          angular.extend(options, defaultOptions, opt);

          // 图片容器大小变化时更新布局
          scope.$watch(function () {
            return element.innerWidth && element.innerWidth();
          }, function () {
            //$log.debug("container width changed");
            scope.updateFluid();
          });

          // 浏览器窗口大小变化时更新布局
          angular.element($window).bind("resize", function (e) {
            scope.updateFluid();
          });

          // 收到指定事件时更新布局
          scope.$on('ponm.photo.fluid.resize', function (e) {
            //$log.debug("ponm.photo.fluid.resize");
            scope.updateFluid();
          });

          attrs.$observe('photoContainerFluidTargetHeight', function (height) {
            if(height && options.targetHeight != height) {
              options.targetHeight = height;
              scope.updateFluid();
            }
          });

          scope.updateFluid = function () {
            $log.debug("collage images");

            scope.updatePromise = $timeout(function () {
              if (options.imagesLoaded && $window.imagesLoaded) {
                var imgLoad = $window.imagesLoaded && $window.imagesLoaded(element);
                imgLoad.on('always', function (e) {
                  collage();
                  $timeout(function () {
                    scope.$apply(function () {
                    });
                  }, options.delayTime);
                });
              } else {
                collage();
              }
            }, options.delayTime);
          };

          // Here we apply the actual CollagePlus plugin
          function collage() {
            element.removeWhitespace().collagePlus(options);
            scope.$emit("ponm.photo.fluid.resized");
          };
        }
      };
    }])
;
