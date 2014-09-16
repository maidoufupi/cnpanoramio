/**
 * Created by any on 2014/9/3.
 */
angular.module('ponmApp.directives')
    .directive('ponmWhoToFollow', ['$window', '$animate', '$log', 'UserService', '$q', 'ponmCtxConfig',
        function ($window, $animate, $log, UserService, $q, ponmCtxConfig) {

            return {
                restrict: 'A',
                scope: {
                    user: "=user"
                },
                templateUrl: "views/ponm-who-to-follow.html",
                link: {
                    pre: function preLink(scope, iElement, iAttrs, controller) {
                        scope.staticCtx = ponmCtxConfig.staticCtx;
                    },
                    post: function postLink(scope, iElement, attrs, controller) {

                        scope.$watch("user.id", function(userId) {
                            if(userId) {
                                getFollowSuggested(userId);
                            }
                        });

                        function getFollowSuggested(userId) {
                            UserService.getFollowSuggested({userId: userId}, function(res) {
                                if(res.status == "OK") {
                                    scope.follows = res.follow;
                                }
                            });
                        }

                        scope.following = function(account) {
                            scope.followDoing = true;
                            $log.debug(scope.user);
                            UserService.following({userId: scope.user.id, value: account.id}, {}, function(res) {
                                scope.followDoing = false;
                                if(res.status == "OK") {
                                    account.follow = true;
                                }
                            }, function(error) {
                                scope.followDoing = false;
                            });
                        };

                        scope.unFollowing = function(account) {
                            UserService.unFollowing({userId: scope.user.id, value: account.id}, function(res) {
                                scope.followDoing = false;
                                if(res.status == "OK") {
                                    account.follow = false;
                                }
                            }, function(error) {
                                scope.followDoing = false;
                            });
                        };

                        scope.removeFollow = function(user) {
                            scope.follows.splice(scope.follows.indexOf(user), 1);
                        };

                        scope.refresh = function() {
                            getFollowSuggested(scope.user.id);
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