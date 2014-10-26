/**
 * Created by any on 2014/8/25.
 */
'use strict';

angular.module('ponmApp.directives')
.directive('dynamicMessage', ['ponmCtxConfig', 'MessageService', 'alertService',
function (ponmCtxConfig, MessageService, alertService) {
    return {
        restrict: 'A',
        scope: {
            message: "=message",
            owner: "=owner"
        },
        templateUrl: "views/dynamic-message.html",
        link: function (scope, element, attr) {
            scope.ctx = ponmCtxConfig.ctx;
            scope.staticCtx = ponmCtxConfig.staticCtx;
            scope.ponmCtxConfig = ponmCtxConfig;

            switch (scope.message.type) {
                case 'photo':
                    scope.message.point = scope.message.photo.point;
                    break;
                case 'travel':
                    if(scope.message.travel.album_cover) {
                        scope.message.point = scope.message.travel.album_cover.point;
                    }
                    break;
                case 'message':
                    var shareMesasage = scope.message.share_message;
                    if(shareMesasage) {
                        scope.message.tags = shareMesasage.tags;
                        scope.message.comments = shareMesasage.comments;
                        scope.message.like_count = shareMesasage.like_count;
                        scope.message.like = shareMesasage.like
                        switch (shareMesasage.type) {
                            case 'photo':
                                scope.message.point = shareMesasage.photo.point;
                                break;
                            case 'travel':
                                if(shareMesasage.travel.album_cover) {
                                    scope.message.point = shareMesasage.travel.album_cover.point;
                                }
                                break;
                        }
                    }
                    break;
            }

            scope.photoClick = function(photoId) {
                scope.$emit('photo.click', photoId);
            };

            scope.pointClick = function() {
                if(scope.message.point) {
                    scope.$emit('message.point.click', scope.message);
                }
            };

            scope.getPhotoSrc = function(photo) {
                if(photo.file_type == "gif") {
                    return scope.staticCtx + '/' + photo.oss_key;
                }else {
                    return scope.staticCtx + '/' + photo.oss_key + '@!photo-preview-big';
                }
            };

            scope.onWaypoint = function(element, direction) {
                if(direction == "down") {
                    element.addClass("shown");
                    if(!scope.active) {
                        scope.$emit("message.actived", angular.copy(scope.message));
                        scope.active = true;
                    }

                }
            };

            scope.like = function(like) {
                if(scope.message.like) {
                    MessageService.unLike({id: scope.message.id}, function(res) {
                        if(res.status == "OK") {
                            scope.message.like = false;

                            scope.message.like_count = scope.message.like_count | 1;
                            scope.message.like_count--;
                        }
                    });
                }else {
                    MessageService.like({id: scope.message.id}, function(res) {
                        if(res.status == "OK") {
                            scope.message.like = true;
                            scope.message.like_count = scope.message.like_count | 0;
                            scope.message.like_count++;
                        }
                    });
                }

            };

            scope.share = function(message) {
                MessageService.share({id: message.id}, {content: scope.shareContent}, function(res) {
                   if(res.status == "OK") {
                       scope.flipx=false;
                       scope.sharing=false;
                       alertService.add("success", "分享成功!", {alone: true});
                       scope.$emit("message.shared", res.message);
                   }else {
                       alertService.add("danger", "分享失败", {ttl: 2000});
                   }
                }, function(error) {
                    alertService.add("danger", "分享失败", {ttl: 2000});
                });
            };

            scope.delete = function(message) {
                MessageService.delete({id: message.id}, function(res) {
                    if(res.status == "OK") {
                        alertService.add("success", "删除成功!", {alone: true, ttl: 1000});
                        scope.$emit("message.deleted", message);
                    }else {
                        alertService.add("danger", "删除失败", {ttl: 1000});
                    }
                }, function(error) {
                    alertService.add("danger", "删除失败", {ttl: 1000});
                });
            };
        }
    }
}]);