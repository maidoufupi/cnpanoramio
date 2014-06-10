/**
 * Created by any on 2014/6/6.
 */

'use strict';

angular.module('ponmApp.controllers')
    .controller('PhotoModalCtrl', ['$window', '$scope', '$log', '$modalInstance', 'photoId', 'travelId', 'PhotoService',
        'CommentService', 'UserService', 'TravelService', '$q', '$modal',
        function ($window, $scope, $log, $modalInstance, photoId, travelId, PhotoService,
                  CommentService, UserService, TravelService, $q, $modal) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            $scope.photoId = photoId;
            $scope.userId = $window.userId;
            $scope.login = $window.login;

            $scope.photoDetailCollapsed = true;

            // comments configs
            $scope.comment = {
                pageSize: 10,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };

            $scope.setPhotoId = function(photoId) {

                $log.debug("photoId" + photoId);
                $scope.photoId = photoId;

                // 获取图片各种信息
                PhotoService.getPhoto({photoId: $scope.photoId}, function(data) {
                    $log.debug(data);
                    if(data.status == 'OK') {
                        $scope.photo = data.prop;
                        // 设置此photo是否可以被登录者编辑
                        $scope.photoEditable = ($scope.userId == $scope.photo.user_id);

                        // 评论
                        $scope.comment.totalItems = data.prop.comment_count;
                        $scope.comment.numPages = Math.ceil($scope.comment.totalItems / $scope.comment.pageSize);

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.photo["user_id"]}, function(data) {
                            if(data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });

                        // 旅行
                        if(!travelId && $scope.photo.travel_id) {
                            travelId = $scope.photo.travel_id;
                            getTravel(travelId);
                        }
                    }
                });

                PhotoService.getCameraInfo({photoId: photoId}, function(data) {
                    if(data.status == "OK") {
                        $scope.cameraInfo = data.camera_info;
                    }
                    $log.debug($scope.cameraInfo);
                });

                getComments(photoId);
            };

            /**
             * 获取详细评论(分页)
             */
            function getComments(photoId) {
                PhotoService.getComments({photoId: photoId, pageSize: $scope.comment.pageSize,
                    pageNo: $scope.comment.currentPage}, function (data) {
                    if(data.status == "OK") {
                        $scope.comments = data.comments;
                    }
                    }, function(error) {
                });
            }

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.travel = res.travel;
                        $scope.travel.photos = [];
                        $scope.travel.totalPhoto = 0;
                        $scope.travel.currentPhoto = 0;
                        var currentPhoto = 0;
                        angular.forEach($scope.travel.spots, function(spot, key) {
                            angular.forEach(spot.photos, function(photo, key) {
                                currentPhoto = currentPhoto + 1;
                                if(photo.id == photoId) {
                                    photo.active = true;
                                    $scope.travel.activePhoto = photo;
                                }
                                photo.sortCount = currentPhoto;
                                $scope.travel.photos.push(photo);
                            });
                        });
                        $scope.travel.totalPhoto = currentPhoto;
                    }
                });
            }
            if(travelId) {
                getTravel(travelId);
            }


            /**
             * 创建评论
             *
             * @param content
             */
            $scope.createComment = function(content) {
                var d = $q.defer();
                if(content) {
                    CommentService.save({photoId: $scope.photoId, content: content}, function(res) {
                        res = res || {};
                        if(res.status == "OK") {
                            $scope.comment.count = $scope.comment.count + 1;
                            $scope.comments.push(res.comment);
//                            $scope.comments.splice(0, 0, res.comment);
                            d.resolve(false);
                        }else {
                            d.resolve(res.info);
                        }
                    }, function(error) {
                        if(error.data) {
                            d.reject(error.data.info);
                        }else {
                            d.reject('Server error!');
                        }
                    })
                }
                return d.promise;
            };

            /**
             * 根据id删除评论, 当删除评论后调用此方法
             *
             * @param commentId
             */
            $scope.deletedComment = function(commentId) {
                angular.forEach($scope.comments, function(comment, key) {
                    if(comment.id == commentId) {
                        delete $scope.comments.splice(key, 1);
                    }
                });
            };

            /**
             * 更新图片属性
             *
             * @param photo
             * @param type
             * @param $data
             */
            $scope.updatePhoto = function(photo, type, $data) {
                var d = $q.defer();
                var params = {};
                params[type] = $data;
                PhotoService.updateProperties({photoId: photo.id}, params, function (res) {
                    res = res || {};
                    if(res.status === 'OK') { // {status: "OK"}
                        d.resolve();
                    } else { // {status: "error", msg: "Username should be `awesome`!"}
                        d.resolve(res.info);
                    }
                }, function(error) {
                    if(error.data) {
                        d.reject(error.data.info);
                    }else {
                        d.reject('Server error!');
                    }
                });
                return d.promise;
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.setTravelAlbum = function (travelAlbum) {
                $scope.travelAlbum = travelAlbum;
            };
            $scope.openTravelAlbum = function () {
                $log.debug("open travel album");
                $scope.travelAlbum.open();
            };
            $scope.closeTravelAlbum = function() {
                $scope.travelAlbum.close();
            };

            $scope.setRecommendAlbum = function (recommendAlbum) {
                $scope.recommendAlbum = recommendAlbum;
            };
            $scope.openRecommendAlbum = function () {
                $log.debug("open recommend album");
                $scope.recommendAlbum.open();
            };

            /**
             * 打开相册中一个指定的（单击）图片
             *
             * @param photo
             */
            $scope.openPhoto = function(photo) {
                $scope.travel.activePhoto && ($scope.travel.activePhoto.active = false);
                photo.active = true;
                $scope.travel.activePhoto = photo;
                $scope.closeTravelAlbum();
                $scope.setPhotoId(photo.id);
            };

            /**
             * 前一张图片
             * 如果已是第一张则打开相册
             *
             */
            $scope.previous = function() {

                var preIndex = $scope.travel.activePhoto.sortCount - 2;
                if(preIndex < 0) {
                    $scope.openTravelAlbum();
                }else {
                    $scope.travel.activePhoto && ($scope.travel.activePhoto.active = false);
                    var photo = $scope.travel.photos[preIndex];
                    photo.active = true;
                    $scope.travel.activePhoto = photo;
                    $scope.setPhotoId(photo.id);
                }
            };

            /**
             * 下一张图片，如果已是最后一张则打开推荐相册
             *
             */
            $scope.next = function() {
                var preIndex = $scope.travel.activePhoto.sortCount;
                if(preIndex >= $scope.travel.photos.length ) {
                    $scope.openRecommendAlbum();
                }else {
                    $scope.travel.activePhoto && ($scope.travel.activePhoto.active = false);
                    var photo = $scope.travel.photos[preIndex];
                    photo.active = true;
                    $scope.travel.activePhoto = photo;
                    $scope.setPhotoId(photo.id);
                }
            };

            $scope.setPhotoId(photoId);
        }])
    .directive("photoTravelAlbum",
    ['$rootScope', '$animate', '$log', function( $rootScope, $animate, $log ) {
        return({
            restrict: "A",
            link: function(scope, element, attrs) {
                var background = element.find(".travel-album-background"),
                    travelAlbum = element.find(".travel-album");

                background.on("click", function(e) {
                    openClose.close();
                });
                var openClose = {
                    open: function() {
                        $animate.addClass(background, "show");
                        $animate.addClass(travelAlbum, "show");
                    },
                    close: function() {
                        $animate.removeClass(background, "show");
                        $animate.removeClass(travelAlbum, "show");
                    }
                };
                scope.setTravelAlbum(openClose);

            }
        });
    }])

    .directive("photoRecommendAlbum",
    ['$rootScope', '$animate', '$log', function( $rootScope, $animate, $log ) {
        return({
            restrict: "A",
            link: function(scope, element, attrs) {
                var background = element.find(".recommend-album-background"),
                    travelAlbum = element.find(".recommend-album");

                background.on("click", function(e) {
                    openClose.close();
                });
                var openClose = {
                    open: function() {
                        $animate.addClass(background, "show");
                        $animate.addClass(travelAlbum, "show");
                    },
                    close: function() {
                        $animate.removeClass(background, "show");
                        $animate.removeClass(travelAlbum, "show");
                    }
                };
                scope.setRecommendAlbum(openClose);
            }
        });
    }])
;