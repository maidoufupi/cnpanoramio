/**
 * Created by any on 2014/8/23.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('userCard', ['$log', '$document', '$http', '$compile', '$timeout', 'safeApply', 'UserService',
        function ($log, $document, $http, $compile, $timeout, safeApply, UserService) {

            var tpl = "views/user-card.html",
                defaults = {
                    delay: 500,
                    removeDelay: 100
                };

            return {
                restrict: 'A',
                scope: {
//                    user: "=user",
                    userId: "=userId",
                    owner: "=owner"
                },
                link: function (scope, element, attrs) {

                    var opts, options;

                    options = scope.$eval(attrs.userCard || "{}");

                    opts = angular.extend({}, defaults, options);

                    var userCardElm,
                        userCardTpl;
                    $http.get(tpl)
                        .then(function(response){
                            userCardTpl = response.data;
                        });

                    var moveLeft = 10;
                    var moveDown = 10;

                    var timeoutHander,
                        mouseenter,
                        userCardEnter,
                        removeCardHander;
                    element.on("mouseenter", function(e) {
                        mouseenter = true;
                        if(removeCardHander) {
                            $timeout.cancel(removeCardHander);
                            removeCardHander = null;
                        }
                    });
                    element.on("mousemove", function(e) {
                        if(timeoutHander) {
                            $timeout.cancel(timeoutHander);
                            timeoutHander = null;
                        }
                        if(mouseenter&&!userCardElm) {
                            timeoutHander = $timeout(function() {

                                if(!scope.user&&scope.userId) {
                                    getUserOpenInfo(scope.userId);
                                }

                                userCardElm = $compile(userCardTpl)(scope);
                                $document.find("body").append(userCardElm);
                                userCardElm.css('top', e.clientY + moveDown).css('left', e.clientX + moveLeft);
                                userCardElm.on("mouseenter", function(e) {
                                    userCardEnter = true;
                                    if(removeCardHander) {
                                        $timeout.cancel(removeCardHander);
                                        removeCardHander = null;
                                    }
                                });
                                userCardElm.on("mouseleave", function(e) {
//                                    $log.debug("mouseleave");
                                    userCardEnter = false;
//                                    $log.debug(removeCardHander);
                                    if(!removeCardHander) {
//                                        $log.debug("mouseleave removeCardHander");
                                        removeCardHander = $timeout(function() {
//                                            $log.debug("mouseleave removeCardHander remove");
                                            userCardElm.remove();
                                            userCardElm = null;
                                        }, opts.removeDelay);
                                        removeCardHander.then(function() {
                                            removeCardHander = null;
                                        });
                                    }
                                });
                            }, opts.delay);
                            timeoutHander.then(function() {
                                timeoutHander = null;
                            });
                        }
                    });
                    element.on("mouseout", function(e) {
//                        $log.debug("mouseout");
//                        $log.debug("hide");
                        if(userCardElm&&!userCardEnter) {
                            if(!removeCardHander) {
                                removeCardHander = $timeout(function() {
                                    userCardElm.remove();
                                    userCardElm = null;
                                }, opts.removeDelay);
                                removeCardHander.then(function() {
                                    removeCardHander = null;
                                });
                            }
                        }
                        if(timeoutHander) {
                            $timeout.cancel(timeoutHander);
                            timeoutHander = null;
                        }
                        mouseenter = false;
                    });

                    /**
                     * 获取图片的用户信息
                     *
                     * @param userId
                     */
                    function getUserOpenInfo(userId) {
                        UserService.getOpenInfo({userId: userId}, function (res) {
                            if (res.status == "OK") {
                                scope.user = res.open_info;
                            }
                        });
                    }
                }
            };


        }])
;