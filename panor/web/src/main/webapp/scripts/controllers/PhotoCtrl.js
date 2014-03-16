/**
 * Created by any on 14-3-14.
 */
'use strict';

angular.module('photoApp', ['ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute',
        'ui.mapgaode',
        'cnmapApp'])
    .controller('PhotoCtrl', ['$window', '$location', '$log', '$rootScope', '$scope', '$cookieStore', '$cookies', 'PhotoService', 'CommentService', 'UserService',
        function ($window, $location, $log, $rootScope, $scope, $cookieStore, $cookies, PhotoService, CommentService, UserService) {

            $scope.ctx = $window.ctx;

            var url = $location.absUrl();
            var photo = url.match(/\/photo\/[0-9]+/g);
            if(photo) {
                $scope.photoId = photo[0].match(/[0-9]+/g)[0];
            }else {
                $scope.photoId = 6;
            }

            $scope.apirest = $window.apirest + "/photo";

            $scope.photo = {};
            $scope.comment = {};
            $scope.comments = [];

            // 用户属性
            $scope.user = {};
            $scope.user.username = $cookies['username'];
            $scope.user.login = $window.login;

            $scope.photo = PhotoService.getPhoto({photoId: $scope.photoId}, function() {
                $log.debug($scope.photo);
                UserService.getOpenInfo({'userId': $scope.photo["userId"]}, function(openInfo) {
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
            })

            $scope.gpsInfo = PhotoService.getGPSInfo({photoId: $scope.photoId, 'vendor':'gaode'}, function() {
                $scope.gpsInfo.lat = cnmap.GPS.convert($scope.gpsInfo.gps.lat);
                $scope.gpsInfo.lng = cnmap.GPS.convert($scope.gpsInfo.gps.lng);
                $log.debug($scope.gpsInfo);
                var point = new AMap.LngLat($scope.gpsInfo.gps.lng, $scope.gpsInfo.gps.lat);
                $scope.minimap1.setCenter(point);
                new AMap.Marker({
                    map: $scope.minimap1,
                    position: point
                })
            })
            $scope.cameraInfo = PhotoService.getCameraInfo({photoId: $scope.photoId}, function() {
                $log.debug($scope.cameraInfo);
            })

            CommentService.getPhotos({photoId: $scope.photoId}, function (data) {
                $scope.comment.count = data.count;
                console.log(data);
            }, function(error) {
                console.log(error)
            })

            CommentService.getPhotos({photoId: $scope.photoId, pageSize: 10, pageNo: 1}, function (data) {
//                console.log(photoComments);
                $scope.comments = data.comments;

            }, function(error) {
            });

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
                CommentService.save({photoId: $scope.photoId, content: content}, function(data) {
                    $scope.comment.content = "";
                    $scope.comment.count = $scope.comment.count + 1;
                    $scope.comments.push(data);
                })
            }

            $scope.mapOptions = {
                // map plugin config
                toolbar: true,
                scrollzoom: true,
                maptype: 'SATELLITE',
//                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                // ui map config
                uiMapCache: false
            }
        }])