/**
 * Created by any on 2014/9/3.
 */
angular.module('ponmApp.directives')
    .directive('ponmProfileCard', ['$window', '$animate', '$log', 'UserService', '$q', 'ponmCtxConfig',
        function ($window, $animate, $log, UserService, $q, ponmCtxConfig) {

            return {
                restrict: 'A',
                scope: {
                    user: "=user",
                    owner: "=owner"
                },
                templateUrl: "views/ponm-profile-card.html",
                link: {
                    pre: function preLink(scope, iElement, iAttrs, controller) {
                        scope.staticCtx = ponmCtxConfig.staticCtx;
                    },
                    post: function postLink(scope, iElement, iAttrs, controller) {
//                        $log.debug(scope.user);
                        scope.following = function(user) {
                            scope.followDoing = true;
                            $log.debug(scope.owner);
                            UserService.following({userId: scope.owner.id, value: user.id}, {}, function(res) {
                                scope.followDoing = false;
                               if(res.status == "OK") {
                                   user.follow = true;
                               }
                            }, function(error) {
                                scope.followDoing = false;
                            });
                        };

                        scope.unFollowing = function(user) {
                            UserService.unFollowing({userId: scope.owner.id, value: user.id}, function(res) {
                                scope.followDoing = false;
                               if(res.status == "OK") {
                                   user.follow = false;
                               }
                            }, function(error) {
                                scope.followDoing = false;
                            });
                        };
                    }
                }
//                link: function (scope, element, attrs) {
//
//                    scope.staticCtx = ponmCtxConfig.staticCtx;
//                    $log.debug(scope.staticCtx);
//
//                }
            }
        }])
;