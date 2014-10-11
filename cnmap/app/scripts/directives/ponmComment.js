/**
 * Created by any on 2014/6/10.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmComment', ['$window', '$animate', '$log', 'CommentService', 'jsUtils', '$q', 'ponmCtxConfig',
        function ($window, $animate, $log, CommentService, jsUtils, $q, ponmCtxConfig) {

            return {
                restrict: 'A',
                scope: {
                    comment: "=",
                    userId: "="
                },
                templateUrl: "views/ponm-comment.html",
                link: function (scope, element, attrs) {

                    scope.ctx = ponmCtxConfig.ctx;
//                    scope.apirest = ponmCtxConfig.apirest;
                    scope.staticCtx = ponmCtxConfig.staticCtx;
                    scope.ponmCtxConfig = ponmCtxConfig;

                    // 设置comment是否是本人的评论
                    scope.comment.editable = (scope.comment.user_id == scope.userId);

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
                            CommentService.modify({commentId: comment.id}, jsUtils.param({content: content}),
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
    .directive('ponmComments', ['$window', '$animate', '$log', '$q', 'ponmCtxConfig', 'CommentService',
        function ($window, $animate, $log, $q, ponmCtxConfig, CommentService) {

            var defaults = {
                type: "photo"
            };

            return {
                restrict: 'A',
                scope: {
                    entityId: "&ponmCommentTo",
                    comments: "=ponmCommentComments"
                },
                templateUrl: 'views/ponm-comments.html',
                link: function (scope, element, attrs) {

                    scope.ctx = ponmCtxConfig.ctx;
//                    scope.apirest = ponmCtxConfig.apirest;
                    scope.staticCtx = ponmCtxConfig.staticCtx;
                    scope.ponmCtxConfig = ponmCtxConfig;

                    var opts, options;

                    options = scope.$eval(attrs.ponmComments || "{}");

                    opts = angular.extend({}, defaults, options);

                    scope.comment = {
                        commentLimit: opts.commentLimit,
                        limit: opts.commentLimit || (scope.comments&&scope.comments.length)
                    };

                    scope.$watch('comments', function(comments) {
                        scope.comment.limit = scope.comment.commentLimit || (comments&&comments.length);
                    });

                    function updateLimit() {
                        if(!scope.comment.commentLimit) {
                            scope.comment.limit = scope.comments&&scope.comments.length;
                        }
                    }

                    /**
                     * 回复评论人的事件响应
                     */
                    scope.$on('replyCommentEvent', function(e, user) {
                        e.preventDefault();
                        e.stopPropagation();
                        scope.comment = scope.comment || {};
                        scope.comment.placeholder = "回复 " + user.name;
                        scope.commentContent = "@" + user.name + " ";
                    });

                    /**
                     * 创建评论
                     *
                     * @param content
                     */
                    scope.createComment = function (content) {
                        var d = $q.defer();
                        if (content) {
                            CommentService.save({type: opts.type, entity_id: scope.entityId(scope), content: content}, function (res) {
                                res = res || {};
                                if (res.status == "OK") {
                                    scope.comments.splice(0, 0, res.comment);
                                    updateLimit();
                                    d.resolve(false);
                                } else {
                                    d.resolve(res.info);
                                }
                            }, function (error) {
                                if (error.data) {
                                    d.reject(error.data.info);
                                } else {
                                    d.reject('Server error!');
                                }
                            });
                        }else {
                            d.resolve(false);
                        }
                        return d.promise;
                    };
                }
            };
        }])
;