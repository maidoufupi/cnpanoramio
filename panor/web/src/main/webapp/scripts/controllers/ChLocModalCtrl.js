/**
 * Created by any on 14-3-12.
 */
angular.module('cnmapApp')

    .controller('ChLocModalCtrl', ['$scope', '$modalInstance', 'files', 'GPSConvertService',
        function ($scope, $modalInstance, files, GPSConvertService) {

            angular.forEach(files, function (value, key) {
                if (!value.modal) {
                    var preview = cnmap.cloneCanvas(value.preview);
                    value.modal = {'preview': preview};
                }
            });

            $scope.files = files;

            $scope.file = $scope.files[0];

            $scope.ok = function () {
                angular.forEach(files, function (value, key) {
                    if (value.mapVendor) {
                        value.lat = value.mapVendor.lat;
                        value.lng = value.mapVendor.lng;
                        value.latPritty = value.mapVendor.latPritty;
                        value.lngPritty = value.mapVendor.lngPritty
                        value.address = value.mapVendor.address;
                        value.vendor = 'gaode';
                    }
                });
                $modalInstance.close($scope.file);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            if (!$scope.marker) {
                $scope.marker = new AMap.Marker({
                    draggable: true
                });
                AMap.event.addListener($scope.marker, "dragend", function (event) {
                    $scope.setPlace(event.lnglat.lat, event.lnglat.lng);
                });
            }

            $scope.$watch('$$childTail', function () {
                if ($scope.$$childTail) {
                    $scope.$$childTail.$watch('myMap', function () {
                        if ($scope.$$childTail.myMap) {
                            $scope.map = $scope.$$childTail.myMap;
                            cnmap.utils.gaode.map = $scope.$$childTail.myMap;
                            cnmap.utils.gaode.init();

                            $scope.activePhoto($scope.file);

                        }
                    });
                }
            })

            $scope.setMarker = function () {
                var latlng = $scope.map.getCenter();
                $scope.marker.setPosition(latlng);
                $scope.marker.setMap($scope.map);
                $scope.marker.show();
                $scope.setPlace(latlng.lat, latlng.lng);
            }

            $scope.hideMarker = function () {
                $scope.marker.hide();
            }

            $scope.updateMarker = function (lat, lng) {
                var point = new AMap.LngLat(lng, lat);
                $scope.marker.setPosition(point);
                $scope.marker.setMap($scope.map);
                $scope.marker.show();
                $scope.map.setCenter(point);
                $scope.setPlace(lat, lng);
            }

            $scope.setPlace = function (lat, lng, address) { // 此参数为非gps坐标

                $scope.file.mapVendor = $scope.file.mapVendor || {};
                $scope.file.mapVendor.lat = lat;
                $scope.file.mapVendor.lng = lng;
                $scope.file.mapVendor.latPritty = cnmap.GPS.convert(lat);
                $scope.file.mapVendor.lngPritty = cnmap.GPS.convert(lng);

                if (address) {
                    $scope.file.mapVendor.address = address;
                } else {
                    cnmap.utils.gaode.getAddress(lat, lng, function (res) {
                        $scope.file.mapVendor.address = "";
                        if (res.info == "OK") {
                            $scope.$apply(function () {
                                $scope.file.mapVendor.address = res.regeocode.formattedAddress;
                            })
                        }
                    })
                }
            }

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

                        $scope.updateMarker(latlng.lat, latlng.lng);
                    }
                })
            }

            $scope.clearPlace = function() {
                $scope.hideMarker();
                delete $scope.file.mapVendor;
            }

            $scope.activePhoto = function (file) {
                $scope.file.active = false;
                $scope.file = file;
                $scope.file.active = true;
                if ($scope.file.mapVendor) {
                    $scope.updateMarker($scope.file.mapVendor.lat, $scope.file.mapVendor.lng);
                } else {
                    if ($scope.file.lat && $scope.file.lng) {
                        if (!$scope.file.gpsConverted) {
                            GPSConvertService.convert({
                                'lat': $scope.file.lat,
                                'lng': $scope.file.lng
                            }, function (data) {
                                $scope.file.lat = data.lat;
                                $scope.file.lng = data.lng;
                                $scope.file.gpsConverted = true;
                                $scope.updateMarker($scope.file.lat, $scope.file.lng);
                            })
                        } else {
                            $scope.updateMarker($scope.file.lat, $scope.file.lng);
                        }
                    } else {
                        $scope.clearPlace();
                    }
                }
            }
        }])
    .service('GPSConvertService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/gps', {},
            {
                convert: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])

