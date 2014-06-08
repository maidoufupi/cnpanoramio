/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('userPageApp', [
        'ui.bootstrap',
        'ui.map',
        'ponmApp',
        'ponmApp.services',
        'ponmApp.directives',
        'ponmApp.controllers',
        'xeditable'
    ])
    .run(['editableOptions', function(editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('UserCtrl', ['$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            var url = $location.absUrl();
            var userm = url.match(/\/user\/[0-9]+/g);
            if(userm) {
                $scope.userId = userm[0].match(/[0-9]+/g)[0];
            }else {
                $scope.userId = 3;
            }

            // 用户图片分页属性
            $scope.photo = {
                pageSize: 40,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };
            $scope.photos = [];

            $scope.tag = '';

            $scope.$watch('photo.currentPage', function() {
                getPhotos($scope.tag);
            });

            var searchPart = $location.search();
            if(searchPart) {
                if(searchPart.with_photo_id) {
                    photo_with_photo_id(searchPart.with_photo_id);
                    getOpenInfo();
                }else if (searchPart.tag){
                    $scope.tag = searchPart.tag;
                    photo_tag(searchPart.tag);
                    getOpenInfo();
                }else {
                    photos();
                }
            }

            $scope.$watch(function () {
                return $location.search();
            }, function (searchPart) {
                if(searchPart) {
                    if(searchPart.with_photo_id) {
                        photo_with_photo_id(searchPart.with_photo_id);
                    }else if (searchPart.tag){
                        $scope.tag = searchPart.tag;
                        photo_tag(searchPart.tag);
                    }else {
                        photos();
                    }
                }
            });

            function getPhotos(tag) {
                if(tag) {
                    UserPhoto.getByTag({userId: $scope.userId, tag: tag, pageSize: $scope.photo.pageSize,
                            pageNo: $scope.photo.currentPage},
                        function(data) {
                            if(data.status == "OK") {
                                $scope.photos = data.photos;
                            }
                        })
                }else {
                    UserPhoto.get({userId: $scope.userId, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage},
                        function(data) {
                            if(data.status == "OK") {
                                $scope.photos = data.photos;
                            }
                        })
                }
            }

            $scope.$watch('photos', function() {
                angular.forEach($scope.photos, function(photo, key) {
                    photo.backgroundColor = "grey";
                })
            });

            function getOpenInfo() {
                UserService.getOpenInfo({'userId': $scope.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.userOpenInfo = data.open_info;
                    }

                })
            }

            function photo_with_photo_id(photo_id) {
                UserPhoto.get({userId: $scope.userId, pageSize: photo_id},
                    function(data) {
                        if(data.status == "OK") {
                            var num = data.photo_info.photo_num;
                            $scope.photo.totalItems = data.photo_info.photo_count;
                            $scope.photo.currentPage = Math.ceil(num / $scope.photo.pageSize);
                            $scope.photo.numPages = Math.round($scope.photo.totalItems / $scope.photo.pageSize);
                            getPhotos();
                        }
                    })
            }

            function photo_tag(tag) {
                UserPhoto.getByTag({userId: $scope.userId, tag: tag},
                    function(data) {
                        if(data.status == "OK") {
                            $scope.photo.totalItems = data.photo_info.photo_count || 0;
                            $scope.photo.numPages = Math.round($scope.photo.totalItems / $scope.photo.pageSize);
                            getPhotos(tag);
                        }
                    })
            }

            function photos() {
                UserService.getOpenInfo({'userId': $scope.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.userOpenInfo = data.open_info;
                        $scope.photo.totalItems = data.open_info.photo_count || 0;
                        $scope.photo.numPages = Math.round($scope.photo.totalItems / $scope.photo.pageSize);
                        getPhotos();
                    }

                })
            }

            $scope.activePhoto = function(photo) {
                var modalInstance = $modal.open({
                    templateUrl: '../views/photo.html',
                    controller: 'PhotoModalCtrl',
                    resolve: {
                        photoId: function () {
                            return photo.id;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $scope.selected = selectedItem;
                }, function () {
//                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            // 用户页面中的地图
            $scope.mapOptions = {
                // map plugin config
                toolbar: true,
                scrollzoom: false,
                maptype: 'SATELLITE',
//                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                level: 3,
                // ui map config
                uiMapCache: false
            };

            if($window.mapVendor == "qq") {
                $window.mapVendor = "gaode";
            }
            var panoramioLayer = new cnmap.PanoramioLayer({
                                            suppressInfoWindows: false,
                                            mapVendor: $window.mapVendor || "gaode"});
            panoramioLayer.initEnv($window.ctx);
            panoramioLayer.setUserId($scope.userId);
            $scope.$watch('minimap', function () {
                if (!$scope.map) {
                    $scope.map = $scope.minimap;
                    var mapObj = $scope.minimap;
                    panoramioLayer.setMap(mapObj);
                }
            });

        }])
;