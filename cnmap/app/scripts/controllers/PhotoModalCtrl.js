/**
 * Created by any on 2014/6/6.
 */

'use strict';

angular.module('ponmApp.controllers')
    .controller('PhotoModalCtrl', ['$window', '$scope', '$log', '$modalInstance', 'photoId', 'PhotoService',
        'CommentService', 'UserService',
        function ($window, $scope, $log, $modalInstance, photoId, PhotoService, CommentService, UserService) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            $scope.photoId = photoId;

            $log.debug(photoId);
            $scope.photoDetailCollapsed = true;

            // comments configs
            $scope.comment = {
                pageSize: 10,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };

            $scope.comments = [
                {
                    userId: 2,
                    username: "user",
                    content: "asdfqaef",
                    createTime: 1401901233320
                }
                ,{
                    userId: 2,
                    username: "user",
                    content: "aidufhqwiejfaosidjfow",
                    createTime: 1401921233320
                }
                ,{
                    userId: 2,
                    username: "user",
                    content: "asdfqaef",
                    createTime: 1401901233320
                }
                ,{
                    userId: 2,
                    username: "user",
                    content: "aidufhqwiejfaosidjfow",
                    createTime: 1401921233320
                }
            ];

            $scope.detail = {

            };

            /**
             * 获取详细评论(分页)
             */
            function getComments() {
                PhotoService.getComments({photoId: photoId, pageSize: $scope.comment.pageSize,
                    pageNo: $scope.comment.currentPage}, function (data) {
                    if(data.status == "OK") {
                        $scope.comments = data.comments;
                    }
                }, function(error) {
                });
            }

            // 获取图片各种信息
            PhotoService.getPhoto({photoId: $scope.photoId}, function(data) {
                $log.debug(data);
                if(data.status == 'OK') {
                    $scope.photo = data.prop;
                    // 评论
                    $scope.comment.totalItems = data.prop.comment_count;
                    $scope.comment.numPages = Math.ceil($scope.comment.totalItems / $scope.comment.pageSize);

                    // 设置图片属性保存函数
                    $scope.photo.save = function() {
                        $scope.editable = '';
                        PhotoService.updateProperties({photoId: $scope.photoId}, {
                            'title': $scope.photo.title,
                            'description': $scope.photo.description
                        }, function() {

                        })
                    };

                    // 获取图片的用户信息
                    UserService.getOpenInfo({'userId': $scope.photo["user_id"]}, function(data) {
                        if(data.status == "OK") {
                            $scope.userOpenInfo = data.open_info;
                        }
                    });

                    // 获取图片主人的图片
//                    UserPhoto.get({userId: $scope.photo["user_id"], pageSize: $scope.photoId},
//                        function(data) {
//                            if(data.status == "OK") {
//                                $scope.user_photos = data.photos.slice(0, 6);
//                            }
//                        });
                }
            });

            getComments();

            PhotoService.getCameraInfo({photoId: photoId}, function(data) {
                if(data.status == "OK") {
                    $scope.cameraInfo = data.camera_info;
                }
                $log.debug($scope.cameraInfo);
            });

            /**
             * 创建评论
             *
             * @param content
             */
            $scope.createComment = function() {
                var content = $scope.comment.content;
                if(content) {
                    CommentService.save({photoId: $scope.photoId, content: content}, function(data) {
                        if(data.status == "OK") {
                            $scope.comment.content = "";
                            $scope.comment.count = $scope.comment.count + 1;
                            $scope.comments.splice(0, 0, data.comment);
                        }
                    })
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }])
;