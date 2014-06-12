/**
 * Created by any on 2014/4/17.
 */

'use strict';
angular.module('aTravelApp', ['ponmApp', 'ui.map', 'ui.bootstrap',
    'xeditable'])
    .run(['editableOptions', function(editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('ATravelCtrl', ['$window', '$scope', '$log', '$http', '$location', 'TravelService', '$modal', '$q',
        'param',
        function ($window, $scope, $log, $http, $location, TravelService, $modal, $q, param) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            $scope.userId = $window.userId;

            var mapEventListener = $window.cnmap.MapEventListener.factory();

            var travelLayer = new $window.cnmap.TravelLayer({
                ctx: $scope.ctx,
                travel: $scope.travel,
                clickable: true
            });

            jQuery(travelLayer).bind("data_clicked", function (e, photoId) {
                $scope.displayPhoto(photoId);
            });

            $scope.$watch(function () {
                return $location.search();
            }, function (searchObject) {
                $log.debug(searchObject);
                if(searchObject.id) {
                    if(searchObject.id != $scope.travelId) {
                        $scope.travelId = searchObject.id;
                        getTravel(searchObject.id);
                    }
                }else {
                    $location.search({id: $scope.travelId || '0'});
                }
            });

            $scope.activePhoto = function(photo) {
                if($scope.photo && $scope.photo.id == photo.id) {
                    $scope.displayPhoto(photo.id);
                }else {
                    $scope.photo = photo;
                    travelLayer.activePhoto(photo);
                }
            };

            $scope.activeSpot = function(spot) {
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
                var params = {};
                params[type] = $data;
                TravelService.changeSpot({travelId: travel.id, spotId: spot.id}, param(params), function(res) {
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

            $scope.$watch('myMap', function () {
                var mapObj = $scope.myMap;
                mapEventListener.addToolBar(mapObj);
                travelLayer.setMap(mapObj);
            });

            //
            $scope.scrollCallback = function(e, spot) {
//                $log.debug("scrollCallback: " + spot);
            };

            $scope.displayPhoto = function(photoId) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/photo.html',
                    controller: 'PhotoModalCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                        photoId: function () {
                            return photoId;
                        },
                        travelId: function() {
                            return $scope.travel.id;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
//                    $scope.selected = selectedItem;
                }, function () {
//                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
        }])
;