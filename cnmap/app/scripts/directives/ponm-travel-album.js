/**
 * Created by any on 2014/9/9.
 */
angular.module('ponmApp.directives')
    .directive('ponmTravelAlbum', ['$window', '$log', '$q', 'ponmCtxConfig', 'TravelManager',
        function ($window, $log, $q, ponmCtxConfig, TravelManager) {

            return {
                restrict: 'EA',
                scope: {
                    travel: "=travel"
                },
                templateUrl: "views/ponm-travel-album.html",
                link: {
                    pre: function preLink(scope, element, attrs, controller) {
                        scope.staticCtx = ponmCtxConfig.staticCtx;
                        var travelManager = new TravelManager(scope.travel);
                    },
                    post: function postLink(scope, element, attrs) {
                        scope.photoClick = function(photoId) {
                            scope.$emit('photo.click', photoId);
                        };
                    }
                }
            };
        }])
;