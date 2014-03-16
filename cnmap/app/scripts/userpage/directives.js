'use strict';

/* Directives */
var userPageDirectives = angular.module('userPageDirectives', []);
userPageDirectives.directive('masonry', function ($parse, $timeout) {
    return {
        restrict: 'AC',
        link: function (scope, elem, attrs) {
            elem.masonry({
                itemSelector: '.masonry-item'
//                isFitWidth: true
            });
        },
        controller : function($scope, $element){
            var bricks = [];
            this.appendBrick = function(child, brickId, waitForImage){
                function addBrick() {
                    $element.masonry('prepended', child, true);

                    // If we don't have any bricks then we're going to want to
                    // resize when we add one.
                    if (bricks.length === 0) {
                        // Timeout here to allow for a potential
                        // masonary timeout when appending (when animating
                        // from the bottom)
                        $timeout(function(){
                            $element.masonry('resize');
                        });
                    }

                    // Store the brick id
                    var index = bricks.indexOf(brickId);
                    if (index === -1) {
                        bricks.push(brickId);
                    }
                }

                if (waitForImage) {
                    child.imagesLoaded(addBrick);
                } else {
                    addBrick();
                }
            };

            // Removed bricks - we only want to call masonry.reload() once
            // if a whole batch of bricks have been removed though so push this
            // async.
            var willReload = false;
            function hasRemovedBrick() {
                if (!willReload) {
                    willReload = true;
                    $scope.$evalAsync(function(){
                        willReload = false;
                        $element.masonry("reloadItems");
                        $element.masonry("layout");
                    });
                }
            }

            this.removeBrick = function(brickId){
                hasRemovedBrick();
                var index = bricks.indexOf(brickId);
                if (index != -1) {
                    bricks.splice(index, 1);
                }
            };
        }

    };
});

userPageDirectives.directive('masonryItem', function ($compile) {
    return {
        restrict: 'AC',
        require : '^masonry',
        link: function (scope, elem, attrs, MasonryCtrl) {

            MasonryCtrl.appendBrick(elem, scope.$id, true);

            scope.$on("$destroy", function() {
                MasonryCtrl.removeBrick(scope.$id);
            });
        }
    };
});