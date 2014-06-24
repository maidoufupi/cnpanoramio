/**
 * Created by any on 2014/4/17.
 */

'use strict';
angular.module('aTravelApp', ['ponmApp', 'ui.map', 'ui.bootstrap', 'xeditable'])
    .run(['editableOptions', function(editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('ATravelCtrl', ['$window', '$scope', '$log', '$http', '$location', 'TravelService',
        'UserService', '$modal', '$q', 'param', 'ponmCtxConfig',
        function ($window, $scope, $log, $http, $location, TravelService, UserService, $modal, $q, param,
                  ponmCtxConfig) {
            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;
            $scope.userId = $window.userId;

            var mapEventListener = $window.cnmap.MapEventListener.factory();
            $scope.mapEventListener = mapEventListener;
            $scope.mapService = $window.cnmap.MapService.factory();

            var travelLayer = new $window.cnmap.TravelLayer({
                ctx: $scope.ctx,
                staticCtx: ponmCtxConfig.staticCtx,
                travel: $scope.travel,
                clickable: true
            });

            jQuery(travelLayer).bind("data_clicked", function (e, photoId) {
                $scope.displayPhoto(photoId);
            });

            var stateObject = {};
            $scope.$watch(function () {
                return $location.search();
            }, function (searchObject) {
                if(searchObject.id && searchObject.id != stateObject.id) {
                    stateObject.id = searchObject.id;
                    if(searchObject.id != $scope.travelId) {
                        $scope.travelId = searchObject.id;
                        getTravel(searchObject.id);
                    }
                }else {
//                    $location.search({id: $scope.travelId || '0'});
                }
                if(searchObject.photoid && searchObject.photoid != stateObject.photoid) {
                    stateObject.photoid = searchObject.photoid;
                    $scope.displayPhoto(searchObject.photoid);
                }
            });

            function updateState() {
                $location.search(param(stateObject));
            }

            $scope.activePhoto = function(photo) {
                if($scope.photo && $scope.photo.id == photo.id) {
                    $scope.displayPhoto(photo.id);
                }else {
                    $scope.photo = photo;
                    travelLayer.activePhoto(photo);
                }
            };

            $scope.activeSpot = function(spot) {
                var spotId = 0;
                if(angular.isObject(spot)) {
                    spotId = spot.id;
                }else {
                    spotId = spot;
                }
                if($scope.activedSpot && $scope.activedSpot.id == spotId) {
                    return;
                }
                angular.forEach($scope.travel.spots, function(spot, key) {
                    if(spot.id == spotId) {
                        spot.active = true;
                        $scope.activedSpot = spot;
                    }else {
                        spot.active = false;
                    }
                });
                travelLayer.activeSpot(spot);
            };

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.travel = res.travel;

                        // 设置此travel是否可以被登录者编辑
                        $scope.travelEnedit = ($scope.userId == $scope.travel.user_id);

                        if(!angular.isArray($scope.travel.spots)) {
                            $scope.travel.spots = [];
                        }
                        if($scope.travel.spot) {
                            $scope.travel.spots.push($scope.travel.spot);
                        }

                        angular.forEach($scope.travel.spots, function(spot, key) {
                            spot.addresses = {};
                            angular.forEach(spot.photos, function(photo, key) {
                                if(photo.point.address) {
                                    spot.addresses[photo.point.address] = photo.point;
                                }
                            });
                        });
                        travelLayer.clearMap();
                        travelLayer.setTravel($scope.travel);
                        travelLayer.initMap();
                        if($scope.travel.spots[0]) {
                            $scope.activeSpot($scope.travel.spots[0]);
                        }

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.travel.user_id}, function (data) {
                            if (data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });
                    }
                });
            }

            $scope.updateTravel = function(travel, $data) {
                var d = $q.defer();
                TravelService.changeTravel({travelId: travel.id}, param({description: $data}), function(res) {
                    res = res || {};
                    if(res.status === 'OK') { // {status: "OK"}
                        d.resolve()
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

            $scope.updateSpot = function(travel, spot, type, $data) {
                var d = $q.defer();
                var params = {
                    title: spot.title,
                    description: spot.description,
                    address: spot.address
                };
                params[type] = $data;
                TravelService.changeSpot({travelId: travel.id, typeId: spot.id}, param(params), function(res) {
                    res = res || {};
                    if(res.status === 'OK') { // {status: "OK"}
                        d.resolve()
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

            $scope.showSpotAddress = function(spot) {
               return spot.address || "添加地址";
            };

            $scope.$on('photoDeleteEvent', function(e, data) {
                $log.debug("photo delete event: photoId = " + data);
                e.preventDefault();
                e.stopPropagation();

                // remove travel photo on server
                TravelService.deletePhoto({travelId: $scope.travel.id, typeId: data}, function(res) {
                    if(res.status == "OK") {
                        angular.forEach($scope.travel.spots, function (spot, key) {
                            angular.forEach(spot.photos, function (photo, key) {
                                if (photo.id == data) {
                                    delete spot.photos.splice(key, 1);
                                }
                            });
                            if(!spot.photos.length) {
                                delete $scope.travel.spots.splice(key, 1);
                            }
                        });

                        $scope.$broadcast('ponmPhotoFluidResize');
                    }
                });
            });

            $scope.mapOptions = {
                // map plugin config
//            toolbar: true,
                scrollzoom: true,
                maptype: true, //'SATELLITE',
                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                scaleControl: true
//            panControl: false,
//            zoomControl: false
            };

            $scope.$watch('myMap', function (map) {
//                $scope.map = map;
                var mapObj = $scope.myMap;
                $scope.mapService.init(mapObj);
                mapEventListener.addToolBar(mapObj);
                travelLayer.setMap(mapObj);
            });

            //
            $scope.$on('waypointEvent', function(e, id) {
//                $log.debug("waypointSpot: " + id);
//                $log.debug("waypointSpot: " + angular.isObject(id));
//                $scope.activeSpot(id);
            });

            $scope.displayPhoto = function(photoId) {
                stateObject.photoid = photoId;
                updateState();
                var modalInstance = $modal.open({
                    templateUrl: 'views/photo.html',
                    controller: 'PhotoModalCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                        photoId: function () {
                            return photoId;
                        },
                        travelId: function() {
                            return $scope.travel && $scope.travel.id || '';
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    delete stateObject.photoid;
                    updateState();
                }, function () {
                    delete stateObject.photoid;
                    updateState();
                });
            };
        }])
;