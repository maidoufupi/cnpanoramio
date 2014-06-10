/**
 * Created by any on 2014/6/10.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmComment', ['$window', '$animate', '$log', 'CommentService', 'param', '$q',
        function ($window, $animate, $log, CommentService, param, $q) {

            return {
                restrict: 'A',
                scope: {
                    comment: "=",
                    userId: "=",
                    deletedComment: "&"
                },
                templateUrl: "views/ponmComment.html",
                link: function (scope, element, attrs) {

                    scope.apirest = $window.apirest;
                    // 设置comment是否是本人的评论
                    scope.comment.editable = (scope.comment.userId == scope.userId);

                    // 赞的按钮
                    var thumbsUp = element.find(".thumbs-up"),
                        action = element.find(".action");

                    if(scope.userId) {
                        element.on("mouseenter", function(e) {
                            scope.$apply(function() {
                                scope.mouseEnter = true;
                            });
                        });
                        element.on("mouseleave", function(e) {
                            scope.$apply(function() {
                                scope.mouseEnter = false;
                            });
                        });
                    }

                    scope.deleteCommment = function(comment) {

                        CommentService.delete({commentId: comment.id}, function(res) {
                            if(res.status == "OK") {
                                scope.deletedComment && scope.deletedComment(scope)()(comment.id);
                            }
                        });
                    };

                    scope.modifyComment = function(comment, content) {
                        var d = $q.defer();
                        if(content) {
                            CommentService.modify({commentId: comment.id}, param({content: content}),
                                function(res) {
                                    if(res.status == "OK") {
                                        d.resolve();
                                    }else {
                                        d.resolve(res.info);
                                    }
                                }, function(error) {
                                    if(error.data) {
                                        d.reject(error.data.info);
                                    }else {
                                        d.reject('Server error!');
                                    }
                                });
                        }
                        return d.promise;
                    };
                }
            };
        }])
;