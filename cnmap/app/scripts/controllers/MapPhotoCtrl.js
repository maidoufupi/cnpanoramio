/**
 * Created by any on 14-4-13.
 */
'use strict';

angular.module('mapPhotoApp', ['ponmApp', 'ui.bootstrap', 'ui.map'])
    .controller('MapPhotoCtrl', ['$window', '$location', '$log', '$scope', 'PhotoService',
        function ($window, $location, $log, $scope, PhotoService) {
        $scope.ctx = $window.ctx;
        $scope.apirest = $window.apirest;

        var mapEventListener = $window.cnmap.MapEventListener.factory();
        var mapService = $window.cnmap.MapService.factory();
        $scope.mapEventListener = mapEventListener;
        $scope.mapService = mapService;

        $scope.$watch(function () {
            return $location.hash();
        }, function (hash) {
            var stateObj = jQuery.deparam(hash);

            if(stateObj && stateObj.id) {
                $scope.photoId = stateObj.id;
                updatePhotoIdListener($scope.photoId);

            }
        });

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
            })
        };

        $scope.cancel = function () {
            $window.history.back();
        };

        $scope.$watch('myMap', function (map) {
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

            if (address) {
                $scope.file.mapVendor.address = address;
            } else {
                mapService.getAddress(lat, lng, function (res) {
                    $scope.$apply(function () {
                        $scope.file.mapVendor.address = res;
                    })
                })
            }
        }


        $scope.clearPlace = function () {
            //$scope.hideMarker();
            //delete $scope.file.mapVendor;
        }

            $scope.addAlert = function(msg) {
                $scope.alerts = [];
                $scope.alerts.push(msg);
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
    .controller('TypeaheadCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.selected = undefined;
        $scope.states = [];
        // Any function returning a promise object can be used to load values asynchronously
        $scope.getLocation = function (val) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function (res) {
                    var addresses = [];
                    angular.forEach(res.data.results, function (item) {
                        addresses.push(item);
                    });
                    return addresses;
                });
        };

        $scope.goLocation = function (address) {
            var geometry = address.geometry;
            if (geometry) {
                $scope.mapEventListener.setCenter($scope.map, geometry.location.lat, geometry.location.lng);
                $scope.addOrUpdateMarker($scope.file, geometry.location.lat, geometry.location.lng);
                $scope.setPlace($scope.file,
                    geometry.location.lat,
                    geometry.location.lng,
                    address.formatted_address);

                $scope.mapEventListener.setBounds($scope.map,
                    geometry.viewport.southwest,
                    geometry.viewport.northeast);
            }
        }
    }])
;