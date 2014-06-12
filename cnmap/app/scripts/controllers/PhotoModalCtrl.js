/**
 * Created by any on 2014/6/6.
 */

'use strict';

angular.module('ponmApp.controllers')
    .controller('PhotoModalCtrl', ['$window', '$scope', '$log', '$modalInstance', 'photoId', 'travelId', 'PhotoService',
        'CommentService', 'UserService', 'TravelService', '$q', '$modal',
        function ($window, $scope, $log, $modalInstance, photoId, travelId, PhotoService, CommentService, UserService,
                  TravelService, $q, $modal) {

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

            $scope.setPhotoId = function (photoId) {

                $log.debug("photoId" + photoId);
                $scope.photoId = photoId;

                // 获取图片各种信息
                PhotoService.getPhoto({photoId: $scope.photoId}, function (data) {
                    $log.debug(data);
                    if (data.status == 'OK') {
                        $scope.photo = data.prop;
                        // 设置此photo是否可以被登录者编辑
                        $scope.photoEditable = ($scope.userId == $scope.photo.user_id);

                        // 评论
                        $scope.comment.totalItems = data.prop.comment_count;
                        $scope.comment.numPages = Math.ceil($scope.comment.totalItems / $scope.comment.pageSize);

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.photo["user_id"]}, function (data) {
                            if (data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });

                        // 旅行
                        if (!travelId) {
                            if ($scope.photo.travel_id) {
                                travelId = $scope.photo.travel_id;
                                getTravel(travelId);
                            } else {
                                $scope.travel = {
//                                    photos: [
//                                        $scope.photo
//                                    ]
                                };
                            }
                        }
                    }
                });

                PhotoService.getCameraInfo({photoId: photoId}, function (data) {
                    if (data.status == "OK") {
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
                    if (data.status == "OK") {
                        $scope.comments = data.comments;
                    }
                }, function (error) {
                });
            }

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function (res) {
                    if (res.status == "OK") {
                        $scope.travel = res.travel;
                        $scope.travel.photos = [];
                        $scope.travel.totalPhoto = 0;
                        $scope.travel.currentPhoto = 0;
                        var currentPhoto = 0;
                        angular.forEach($scope.travel.spots, function (spot, key) {
                            angular.forEach(spot.photos, function (photo, key) {
                                currentPhoto = currentPhoto + 1;
                                if (photo.id == photoId) {
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

            if (travelId) {
                getTravel(travelId);
            }

            /**
             * 创建评论
             *
             * @param content
             */
            $scope.createComment = function (content) {
                var d = $q.defer();
                if (content) {
                    CommentService.save({photoId: $scope.photoId, content: content}, function (res) {
                        res = res || {};
                        if (res.status == "OK") {
                            $scope.comment.count = $scope.comment.count + 1;
                            $scope.comments.push(res.comment);
//                            $scope.comments.splice(0, 0, res.comment);
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
                    })
                }
                return d.promise;
            };

            /**
             * 根据id删除评论, 当删除评论后调用此方法
             *
             * @param commentId
             */
            $scope.deletedComment = function (commentId) {
                angular.forEach($scope.comments, function (comment, key) {
                    if (comment.id == commentId) {
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
            $scope.updatePhoto = function (photo, type, $data) {
                var d = $q.defer();
                var params = {};
                params[type] = $data;
                PhotoService.updateProperties({photoId: photo.id}, params, function (res) {
                    res = res || {};
                    if (res.status === 'OK') { // {status: "OK"}
                        d.resolve();
                    } else { // {status: "error", msg: "Username should be `awesome`!"}
                        d.resolve(res.info);
                    }
                }, function (error) {
                    if (error.data) {
                        d.reject(error.data.info);
                    } else {
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
            $scope.closeTravelAlbum = function () {
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
            $scope.openPhoto = function (photo) {
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
            $scope.previous = function () {
                var preIndex = 0;
                if ($scope.travel.activePhoto && (preIndex = $scope.travel.activePhoto.sortCount - 2) >= 0) {
                    $scope.travel.activePhoto.active = false;
                    var photo = $scope.travel.photos[preIndex];
                    photo.active = true;
                    $scope.travel.activePhoto = photo;
                    $scope.setPhotoId(photo.id);
                } else {
                    $scope.openTravelAlbum();
                }
            };

            /**
             * 下一张图片，如果已是最后一张则打开推荐相册
             *
             */
            $scope.next = function () {
                var preIndex = 0;
                if ($scope.travel.activePhoto &&
                    (preIndex = $scope.travel.activePhoto.sortCount) < $scope.travel.photos.length) {
                    $scope.travel.activePhoto.active = false;
                    var photo = $scope.travel.photos[preIndex];
                    photo.active = true;
                    $scope.travel.activePhoto = photo;
                    $scope.setPhotoId(photo.id);
                } else {
                    $scope.openRecommendAlbum();
                }
            };

            $scope.setPhotoId(photoId);

            $scope.openMapPhoto = function () {

                var modalInstance = $modal.open({
                    templateUrl: 'views/mapPhoto.html',
                    controller: 'MapPhotoCtrl2',
                    windowClass: 'map-photo-modal',
                    resolve: {
                        photoId: function () {
                            return $scope.photoId;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $scope.selected = selectedItem;
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
        }])
    .directive("photoTravelAlbum",
    ['$rootScope', '$animate', '$log', function ($rootScope, $animate, $log) {
        return({
            restrict: "A",
            link: function (scope, element, attrs) {
                var background = element.find(".travel-album-background"),
                    travelAlbum = element.find(".travel-album");

                background.on("click", function (e) {
                    openClose.close();
                });
                var openClose = {
                    open: function () {
                        $animate.addClass(background, "show");
                        $animate.addClass(travelAlbum, "show");
                    },
                    close: function () {
                        $animate.removeClass(background, "show");
                        $animate.removeClass(travelAlbum, "show");
                    }
                };
                scope.setTravelAlbum(openClose);

            }
        });
    }])

    .directive("photoRecommendAlbum",
    ['$rootScope', '$animate', '$log', function ($rootScope, $animate, $log) {
        return({
            restrict: "A",
            link: function (scope, element, attrs) {
                var background = element.find(".recommend-album-background"),
                    travelAlbum = element.find(".recommend-album");

                background.on("click", function (e) {
                    openClose.close();
                });
                var openClose = {
                    open: function () {
                        $animate.addClass(background, "show");
                        $animate.addClass(travelAlbum, "show");
                    },
                    close: function () {
                        $animate.removeClass(background, "show");
                        $animate.removeClass(travelAlbum, "show");
                    }
                };
                scope.setRecommendAlbum(openClose);
            }
        });
    }])
    .controller('MapPhotoCtrl2', ['$window', '$log', '$timeout', '$scope', '$modalInstance', 'PhotoService',
        'GeocodeService', 'photoId',
        function ($window, $log, $timeout, $scope, $modalInstance, PhotoService, GeocodeService, photoId) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            $scope.userId = $window.userId;

            var mapEventListener = $window.cnmap.MapEventListener.factory();
            var mapService = $window.cnmap.MapService.factory();
            $scope.mapEventListener = mapEventListener;
            $scope.mapService = mapService;

            $scope.photoId = photoId;

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.ok = function () {

                PhotoService.updateProperties({photoId: $scope.photoId}, {
                    'point': {
                        'lat': $scope.file.mapVendor.lat,
                        'lng': $scope.file.mapVendor.lng,
                        'address': $scope.file.mapVendor.address
                    },
                    'vendor': 'gaode',
                    'is360': $scope.file.is360
                }, function(data) {
                    if(data.status == "OK") {
                        $log.debug("properties update successful");
                        $scope.addAlert({type: "success", msg: "保存成功!"});
                    }else {
                        $scope.addAlert({type: "danger", msg: "保存失败 " + data.status});
                    }
                }, function(error) {
                    $scope.addAlert({type: "danger", msg: "保存失败 " + (error.data && error.data.status)});
                });

            };

            $scope.$watch('$$childTail.myMap', function (map) {
                $scope.map = map;
                mapService.init(map);
                addMapClickEvent($scope.map);
                updatePhotoIdListener();
            });

            function updatePhotoIdListener() {
                if($scope.file && $scope.file.mapVendor && $scope.file.mapVendor.marker) {
                    mapEventListener.setMap($scope.file.mapVendor.marker, null);
                }
                $scope.file = {
                    photoId: $scope.photoId
                };

                PhotoService.getPhoto({photoId: $scope.photoId}, function(data) {
                    if (data.status == 'OK') {
                        $scope.file.is360 = data.prop.is360;
                    }
                });

                PhotoService.getGPSInfo({photoId: $scope.photoId}, function(res) {
                    if(res.status == "OK") {
                        addOrUpdateMarker($scope.file, res.gps[0].gps.lat, res.gps[0].gps.lng);
                        mapEventListener.setCenter($scope.map, res.gps[0].gps.lat, res.gps[0].gps.lng);
                        $scope.setPlace($scope.file, res.gps[0].gps.lat, res.gps[0].gps.lng, res.gps[0].gps.address);
                    }
                })
            }

            function createMarker(file, lat, lng) {
                // create marker
                file.mapVendor = file.mapVendor || {};
                if(lat && lng) {
                    file.mapVendor.lat = lat;
                    file.mapVendor.lng = lng;
                }else {
                    if(!file.lat && !file.lng) {
                        return;
                    }else {
                        file.mapVendor.lat = file.lat;
                        file.mapVendor.lng = file.lng;
                    }
                }

                file.mapVendor.marker = mapEventListener.createDraggableMarker(
                    $scope.map, file.mapVendor.lat, file.mapVendor.lng
                );
                file.mapVendor.marker.photo_file = file;
                mapEventListener.setMap(file.mapVendor.marker, $scope.map);

                mapEventListener.addDragendListener(file.mapVendor.marker, function (lat, lng) {
                    $scope.setPlace(this.photo_file, lat, lng);
                })

            }

            function addMapClickEvent(map) {
                mapEventListener.addMapClickListener(map, function (lat, lng) {
                    addOrUpdateMarker($scope.file, lat, lng);
                    $scope.setPlace($scope.file, lat, lng);
                })
            }

            function addOrUpdateMarker(file, lat, lng) {
                file.mapVendor = file.mapVendor || {};
                if (!file.mapVendor.marker) {
                    createMarker(file, lat, lng);
                    mapEventListener.activeMarker(file.mapVendor.marker);
                }else {
                    mapEventListener.setPosition(file.mapVendor.marker, lat, lng);
                    mapEventListener.setMap(file.mapVendor.marker, $scope.map);
                }
            }

            $scope.addOrUpdateMarker = addOrUpdateMarker;

            $scope.setPlace = function (file, lat, lng, address) { // 此参数为非gps坐标

                $scope.file.mapVendor = $scope.file.mapVendor || {};
                $scope.file.mapVendor.lat = lat;
                $scope.file.mapVendor.lng = lng;
                $scope.file.mapVendor.latPritty = cnmap.GPS.convert(lat);
                $scope.file.mapVendor.lngPritty = cnmap.GPS.convert(lng);

                GeocodeService.regeoAddresses(lat, lng).then(function(res) {
                    if(!address) {
                        $scope.file.mapVendor.address = res.address;
                    }
                    $scope.file.mapVendor.addresses = res.addresses;
                });

                if (address) {
                    $scope.file.mapVendor.address = address;
                }
            };


            $scope.clearPlace = function () {
                //$scope.hideMarker();
                //delete $scope.file.mapVendor;
            };

            var alertPromise = null;
            $scope.addAlert = function(msg) {
                $scope.alerts = [];
                $scope.alerts.push(msg);
                if(alertPromise) {
                    $timeout.cancel(alertPromise);
                }
                alertPromise = $timeout(function() {
                    $scope.alerts = [];
                }, 3000);
            };

            $scope.closeAlert = function (index) {
                if($scope.alerts) {
                    $scope.alerts.splice(index, 1);
                }
            };

            $scope.mapOptions = {
                // map plugin config
                toolbar: true,
                scrollzoom: true,
                maptype: true, //'SATELLITE',
                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true
                // ui map config
//            uiMapCache: false
            }
        }])
;