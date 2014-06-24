/**
 * Created by any on 2014/6/10.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmComment', ['$window', '$animate', '$log', 'CommentService', 'param', '$q', 'ponmCtxConfig',
        function ($window, $animate, $log, CommentService, param, $q, ponmCtxConfig) {

            return {
                restrict: 'A',
                scope: {
                    comment: "=",
                    userId: "="
                },
                templateUrl: "views/ponmComment.html",
                link: function (scope, element, attrs) {

                    scope.ctx = $window.ctx;
                    scope.apirest = $window.apirest;
                    scope.staticCtx = ponmCtxConfig.staticCtx;

                    // 设置comment是否是本人的评论
                    scope.comment.editable = (scope.comment.user_id == scope.userId);

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
                                scope.$emit('deletedCommentEvent', comment.id);
//                                scope.deletedComment && scope.deletedComment(scope)()(comment.id);
                            }
                        });
                    };

                    scope.likeComment = function(comment) {

                        if(comment.like) {
                            CommentService.unLike({commentId: comment.id}, function (res) {
                                res = res || {};
                                if (res.status === 'OK') { // {status: "OK"}
                                    comment.like = false;
                                    comment.like_count -= 1;
                                }
                            }, function (error) {
                                if (error.data) {
                                } else {
                                }
                            });
                        }else {
                            CommentService.like({commentId: comment.id}, function (res) {
                                res = res || {};
                                if (res.status === 'OK') { // {status: "OK"}
                                    comment.like = true;
                                    comment.like_count = comment.like_count || 0;
                                    comment.like_count += 1;
                                }
                            }, function (error) {
                                if (error.data) {
                                } else {
                                }
                            });
                        }
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

                    scope.replyComment = function(comment) {
                        scope.$emit('replyCommentEvent', {id: comment.user_id, name: comment.username});
                    }
                }
            };
        }])
;