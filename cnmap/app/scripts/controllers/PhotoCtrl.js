/**
 * Created by any on 14-3-14.
 */
'use strict';

angular.module('photoApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ui.bootstrap',
    'ui.map',
    'ponmApp',
    'ponmApp.services',
    'ponmApp.directives'])
    .controller('PhotoCtrl', ['$window', '$location', '$log', '$rootScope', '$scope', '$cookieStore', '$cookies',
        'PhotoService', 'CommentService', 'UserService', 'UserPhoto',
        function ($window, $location, $log, $rootScope, $scope, $cookieStore, $cookies,
                  PhotoService, CommentService, UserService, UserPhoto) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            var mapEventListener = $window.cnmap.MapEventListener.factory();

            var url = $location.absUrl();
            var photo = url.match(/\/photo\/[0-9]+/g);
            if(photo) {
                $scope.photoId = photo[0].match(/[0-9]+/g)[0];
            }else {
                // for test
                $scope.photoId = 8;
            }

            $scope.photo = {};
            // comments vars
            $scope.comment = {
                pageSize: 10,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };
            $scope.comments = [];

            $scope.$watch('comment.currentPage', function() {
                getComments();
            });

            // 用户属性
            $scope.user = {};
            $scope.user.userId = $window.userId;
            $scope.user.username = $cookies['username'];
            $scope.user.login = $window.login;
            $scope.photo = {};
            
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
                    UserPhoto.get({userId: $scope.photo["user_id"], pageSize: $scope.photoId},
                        function(data) {
                            if(data.status == "OK") {
                                $scope.user_photos = data.photos.slice(0, 6);
                            }
                        });
                }
            });

            PhotoService.getGPSInfo({photoId: $scope.photoId, 'vendor':'gaode'}, function(data) {
                if(data.status == "OK" && data.gps.length) {
                    $scope.gpsInfo = data.gps[0];
                    $log.debug($scope.gpsInfo);
	                $scope.gpsInfo.lat = cnmap.GPS.convert($scope.gpsInfo.gps.lat);
	                $scope.gpsInfo.lng = cnmap.GPS.convert($scope.gpsInfo.gps.lng);

                    mapEventListener.setCenter($scope.minimap1, $scope.gpsInfo.gps.lat, $scope.gpsInfo.gps.lng);
                    mapEventListener.addMarker($scope.minimap1, $scope.gpsInfo.gps.lat, $scope.gpsInfo.gps.lng);
                }
                
            });
            PhotoService.getCameraInfo({photoId: $scope.photoId}, function(data) {
                if(data.status == "OK") {
                    $scope.cameraInfo = data.camera_info;
                }
                $log.debug($scope.cameraInfo);
            });

//            获取评论总数
//            CommentService.getPhotos({photoId: $scope.photoId}, function (data) {
//                $scope.comment.totalItems = data.count;
//                $scope.comment.numPages = Math.round($scope.bigTotalItems / $scope.comment.pageSize);
//            }, function(error) {
//                console.log(error)
//            });

            /**
             * 获取详细评论(分页)
             */
            function getComments() {
                PhotoService.getComments({photoId: $scope.photoId, pageSize: $scope.comment.pageSize,
                    pageNo: $scope.comment.currentPage}, function (data) {
                    if(data.status == "OK") {
                        $scope.comments = data.comments;
                    }
                }, function(error) {
                });
            }

            //
            getComments();

            if($scope.user.login && $scope.user.userId) {
                // 获取图片的用户信息
                UserService.getOpenInfo({'userId': $scope.user.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.user.open_info = data.open_info;
                    }
                })
            }

            $scope.setEditable = function(who) {
                if($scope.user.login == 'true' &&
                    $scope.userOpenInfo.name == $scope.user.username) {
                    $scope.editable = who;
                }
            };

            $scope.deletePhoto = function () {
                $window.alert("您确定要删除这张照片？");
            };

            /**
             * 创建评论
             *
             * @param content
             */
            $scope.createComment = function(content) {
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

            $scope.favorite = function() {
                if($scope.user.login) {

                    if($scope.photo.favorite) {
                        PhotoService.removeFavorite({photoId: $scope.photoId}, function (data) {
                            if(data.status == "OK") {
                                $scope.photo.favorite = false;
                            }
                        })
                    }else {
                        PhotoService.favorite({photoId: $scope.photoId}, {}, function (data) {
                            if(data.status == "OK") {
                                $scope.photo.favorite = true;
                            }
                        })
                    }
                }
            };

            $scope.mapOptions = {
                // map plugin config
                toolbar: true,
                scrollzoom: false,
                maptype: 'SATELLITE',
//                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                panControl: false,
                level: 13,
                // ui map config
                uiMapCache: false
            };

            if($window.mapVendor == "qq") {
                $window.mapVendor = "gaode";
            }
            var panoramioLayer = new cnmap.PanoramioLayer(
                {suppressInfoWindows: false,
                    mapVendor: $window.mapVendor || "gaode"});
            panoramioLayer.initEnv($window.ctx);
            $(panoramioLayer).bind("data_changed", function (e, data) {
                $scope.$apply(function (scope) {

                    // 更新图片
                    $scope.update_nearby_photos(data);
                });
            });
            $scope.$watch('minimap1', function () {
                if (!$scope.map) {
                    $scope.map = $scope.minimap1;
                    var mapObj = $scope.minimap1;
                    panoramioLayer.setMap(mapObj);
                }
            });

            $scope.update_nearby_photos = function (photos) {
                $scope.nearby_photos = photos && photos.slice(0,5);
            };
        }])
;