/**
 * Created by any on 14-3-12.
 */
'use strict';
angular.module('ponmApp.controllers')
    .controller('ChLocModalCtrl', ['$window', '$scope', '$log', '$modalInstance', 'files', 'GPSConvertService',
        'GeocodeService',
        function ($window, $scope, $log, $modalInstance, files, GPSConvertService, GeocodeService) {

            var mapEventListener = $window.cnmap.MapEventListener.factory();
            var mapService = $window.cnmap.MapService.factory();
            $scope.mapEventListener = mapEventListener;
            $scope.mapService = mapService;

            angular.forEach(files, function (file, key) {
                file.active = false;
                delete file.mapVendor;
                if (!file.modal && file.preview) {
                    var preview = cnmap.cloneCanvas(file.preview);
                    file.modal = {'preview': preview};
                }
                file.mapVendor = file.mapVendor || {};
                file.mapVendor.is360 = file.is360;
            });

            angular.forEach(files, function (file, key) {
                if (!file.loc_preview && !!loadImage) {
                    // load image data preview
                    loadImage(
                        file,
                        function (img) {
                            file.loc_preview = file.loc_preview || {};
                            jQuery(img).removeAttr('width').removeAttr('height');
//                            delete img.width;
                            file.loc_preview.preview = img;
                        },
                        {
                            maxWidth: 600,
                            maxHeight: 300,
                            minWidth: 100,
                            minHeight: 50,
                            canvas: false
                        }
                    );
                }
            });

            $scope.files = files;

            $scope.file = $scope.files[0];

            $scope.ok = function () {
                var res = [];
                angular.forEach(files, function (file, key) {
                    if (file.mapVendor) {
                        file.address = file.mapVendor.address;
                        file.is360 = file.mapVendor.is360;
//                        if (file.lat != file.mapVendor.lat
//                            || file.lng != file.mapVendor.lng) {
                            file.lat = file.mapVendor.lat;
                            file.lng = file.mapVendor.lng;
                            file.latPritty = file.mapVendor.latPritty;
                            file.lngPritty = file.mapVendor.lngPritty;
                            file.vendor = 'gaode';
//                        }
                        res.push(file);
                    }
                });
                $modalInstance.close(res);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.$watch('$$childTail', function () {
                if ($scope.$$childTail) {
                    $scope.$$childTail.$watch('myMap', function () {
                        if ($scope.$$childTail.myMap) {
                            $scope.map = $scope.$$childTail.myMap;

                            angular.forEach(files, function (file, key) {
                                createMarker(file);
                            });

                            mapService.init($scope.map);

                            addMapClickEvent($scope.map);
                            $scope.activePhoto($scope.file);

                        }
                    });
                }
            });

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
//                mapEventListener.setPosition(file.mapVendor.marker, file.lat, file.lng);
                mapEventListener.setMap(file.mapVendor.marker, $scope.map);
                mapEventListener.addMarkerActiveListener(file.mapVendor.marker, function () {
                    $scope.activePhoto(this.photo_file);
                });

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

                mapService.getAddrPois(lat, lng, function(addresses, addr) {
                    if(!address) {
                        $scope.file.mapVendor.address = addr;
                    }
                    $scope.file.mapVendor.addresses = addresses;
                });

//                GeocodeService.regeoAddresses(lat, lng).then(function(res) {
//                    if(!address) {
//                        $scope.file.mapVendor.address = res.address;
//                    }
//                    $scope.file.mapVendor.addresses = res.addresses;
//                });

                if (address) {
                    $scope.file.mapVendor.address = address;
                } else {
//                    GeocodeService.regeo({lat: lat, lng: lng}, function(regeocode) {
//                        $scope.file.mapVendor.address = regeocode.formatted_address;
//                    });
//                    mapService.getAddress(lat, lng, function (res) {
//                        $scope.$apply(function () {
//                            $scope.file.mapVendor.address = res;
//                        })
//                    });
//                    cnmap.utils.gaode.getAddress(lat, lng, function (res) {
//                        $scope.file.mapVendor.address = "";
//                        if (res.info == "OK") {
//
//                        }
//                    })
                }
            };

            /*
             $scope.getLocation = function (address) {
             // 将地址解析结果显示在地图上,并调整地图视野
             cnmap.utils.gaode.getLocation(address, function (res) {
             if (res.info == "OK") {
             var latlng = res.geocodes[0].location;
             $scope.map.setCenter(latlng);
             switch (res.geocodes[0].level) {
             case '省':
             $scope.map.setZoom(6);
             break;
             case '市':
             $scope.map.setZoom(8);
             break;
             case '区县':
             $scope.map.setZoom(11);
             break;
             case '乡镇':
             $scope.map.setZoom(12);
             break;
             case '村庄':
             $scope.map.setZoom(13);
             break;
             case '道路':
             $scope.map.setZoom(14);
             break;
             case '兴趣点':
             $scope.map.setZoom(15);
             }

             addOrUpdateMarker($scope.file, lnglat);
             }
             })
             }
             */

            $scope.clearPlace = function () {
                //$scope.hideMarker();
                //delete $scope.file.mapVendor;
            };

            /**
             * 激活图片
             *
             * @param file
             */
            $scope.activePhoto = function (file) {
                $scope.file.active = false;
                if ($scope.file.mapVendor) {
                    mapEventListener.deactiveMarker($scope.file.mapVendor.marker);
                }
                $scope.file = file;
                $scope.file.active = true;
                if ($scope.file.mapVendor) {
                    if ($scope.file.mapVendor.lat) {
                        mapEventListener.activeMarker($scope.file.mapVendor.marker);
                        if(!mapEventListener
                            .inMapView($scope.file.mapVendor.lat, $scope.file.mapVendor.lng, $scope.map)) {
                            mapEventListener.setCenter($scope.map, $scope.file.mapVendor.lat, $scope.file.mapVendor.lng);
                        }
                        $scope.setPlace($scope.file, $scope.file.mapVendor.lat, $scope.file.mapVendor.lng);
                    } else {
                        $scope.clearPlace();
                    }
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
    .controller('TypeaheadCtrl', ['$scope', '$http', 'GeocodeService', '$q',
    function ($scope, $http, GeocodeService, $q) {
        $scope.selected = undefined;
        $scope.states = [];
        // Any function returning a promise object can be used to load values asynchronously
        $scope.getLocation = function (val) {
//            return GeocodeService.geo(val, function(geocodes, address) {
//                var addresses = [];
//                angular.forEach(geocodes, function (item) {
//                    addresses.push(item);
//                });
//                return addresses;
//            });
            var d = $q.defer();
            $scope.mapService.getLocation(val, function(res) {
//                var addresses = [];
//                if(res.info == "OK") {
//                    angular.forEach(res.geocodes, function(geocode, key) {
//                        if(GeocodeService.levelMap[geocode.level]) {
//                            geocode.zoom = GeocodeService.levelMap[geocode.level];
//                        }
//                        addresses.push(geocode);
//                    });
//                }
                d.resolve(res);
            });
            return d.promise.then(function(res) {
                var addresses = [];
                if(res.info == "OK") {
                    angular.forEach(res.geocodes, function(geocode, key) {
                        if(GeocodeService.levelMap[geocode.level]) {
                            geocode.zoom = GeocodeService.levelMap[geocode.level];
                        }
                        addresses.push(geocode);
                    });
                }
                return addresses;
            });
//            return $http.get('http://restapi.amap.com/v3/geocode/geo', {
//                params: {
//                    address: val,
//                    key: "53f7e239ddb8ea62ba552742a233ed1f"
//                }
//            }).then(function(res){
//                var addresses = [];
//                if(res.status == "200") {
//                    if(res.data && res.data.info == "OK") {
//                        angular.forEach(res.data.geocodes, function(geocode, key) {
//                            if(GeocodeService.levelMap[geocode.level]) {
//                                geocode.zoom = GeocodeService.levelMap[geocode.level];
//                            }
//                            addresses.push(geocode);
//                        });
//                    }
//                }
//                return addresses;
//            });
//            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
//                params: {
//                    address: val,
//                    sensor: false
//                }
//            }).then(function (res) {
//                    var addresses = [];
//                    angular.forEach(res.data.results, function (item) {
//                        addresses.push(item);
//                    });
//                    return addresses;
//                });
        };

        $scope.goLocation = function (address) {

            var location = address.location;
            if (location) {
//                location = location.split(",");
//                location = {
//                    lat: location[1],
//                    lng: location[0]
//                };
                $scope.mapEventListener.setCenter($scope.map, location.lat, location.lng);
//                $scope.addOrUpdateMarker($scope.file, location.lat, location.lng);
//                $scope.setPlace($scope.file,
//                    location.lat,
//                    location.lng,
//                    address.formatted_address);

                $scope.mapEventListener.setZoom($scope.map, address.zoom);

//                $scope.mapEventListener.setBounds($scope.map,
//                    geometry.viewport.southwest,
//                    geometry.viewport.northeast);
            }
        }
    }])
;

