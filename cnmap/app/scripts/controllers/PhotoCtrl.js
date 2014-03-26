/**
 * Created by any on 14-3-14.
 */
'use strict';

angular.module('photoApp', ['ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute',
        'ui.bootstrap',
        'ui.mapgaode',
        'cnmapApp'])
    .controller('PhotoCtrl', ['$window', '$location', '$log', '$rootScope', '$scope', '$cookieStore', '$cookies', 'PhotoService', 'CommentService', 'UserService',
        function ($window, $location, $log, $rootScope, $scope, $cookieStore, $cookies, PhotoService, CommentService, UserService) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            var url = $location.absUrl();
            var photo = url.match(/\/photo\/[0-9]+/g);
            if(photo) {
                $scope.photoId = photo[0].match(/[0-9]+/g)[0];
            }else {
                $scope.photoId = 9;
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
            $scope.user.username = $cookies['username'];
            $scope.user.login = $window.login;
            $scope.photo = {};
            PhotoService.getPhoto({photoId: $scope.photoId}, function(data) {
                $log.debug(data);
                if(data.status == 'OK') {
                    $scope.photo = data.prop;
                    UserService.getOpenInfo({'userId': $scope.photo["user_id"]}, function(openInfo) {
                        $scope.userOpenInfo = openInfo;
                    })

                    $scope.photo.save = function() {
                        $scope.editable = '';
                        PhotoService.updateProperties({photoId: $scope.photoId}, {
                            'title': $scope.photo.title,
                            'description': $scope.photo.description
                        }, function() {

                        })
                    }
                }
            })

            PhotoService.getGPSInfo({photoId: $scope.photoId, 'vendor':'gaode'}, function(data) {
                if(data.status == "OK") {
                    $scope.gpsInfo = data.gps[0];
                }
                $log.debug($scope.gpsInfo);
                $scope.gpsInfo.lat = cnmap.GPS.convert($scope.gpsInfo.gps.lat);
                $scope.gpsInfo.lng = cnmap.GPS.convert($scope.gpsInfo.gps.lng);
                var point = new AMap.LngLat($scope.gpsInfo.gps.lng, $scope.gpsInfo.gps.lat);
                $scope.minimap1.setCenter(point);
                new AMap.Marker({
                    map: $scope.minimap1,
                    position: point
                })
            })
            PhotoService.getCameraInfo({photoId: $scope.photoId}, function(data) {
                if(data.status == "OK") {
                    $scope.cameraInfo = data.camera_info;
                }
                $log.debug($scope.cameraInfo);
            })

            CommentService.getPhotos({photoId: $scope.photoId}, function (data) {
                $scope.comment.totalItems = data.count;
                $scope.comment.numPages = Math.round($scope.bigTotalItems / $scope.comment.pageSize);
            }, function(error) {
                console.log(error)
            })

            /**
             * 获取评论
             */
            function getComments() {
                CommentService.getPhotos({photoId: $scope.photoId, pageSize: $scope.comment.pageSize, pageNo: $scope.comment.currentPage}, function (data) {
                    $scope.comments = data.comments;
                }, function(error) {
                });
            }

            //
            getComments();

            $scope.setEditable = function(who) {
                if($scope.user.login == 'true' &&
                    $scope.userOpenInfo.name == $scope.user.username) {
                    $scope.editable = who;
                }
            }

            $scope.deletePhoto = function () {
                $window.alert("您确定要删除这张照片？");
            };

            $scope.save = function(content) {
                if(content) {
                    CommentService.save({photoId: $scope.photoId, content: content}, function(data) {
                        $scope.comment.content = "";
                        $scope.comment.count = $scope.comment.count + 1;
                        $scope.comments.push(data);
                    })
                }

            }

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
            }

            $scope.mapOptions = {
                // map plugin config
                toolbar: true,
                scrollzoom: false,
                maptype: 'SATELLITE',
//                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                level: 13,
                // ui map config
                uiMapCache: false
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
            })
            $scope.$watch('minimap1', function () {
                if (!$scope.map) {
                    $scope.map = $scope.minimap1;
                    var mapObj = $scope.minimap1;
                    panoramioLayer.setMap(mapObj);

                    mapObj.plugin(["AMap.ToolBar"], function () {
                        //加载工具条
                        var tool = new AMap.ToolBar();
                        mapObj.addControl(tool);
                    });
                }
            });

            $scope.update_nearby_photos = function (photos) {
                $scope.nearby_photos = photos && photos.slice(0,5);
            }
        }]);