/**
 * Created by any on 14-3-12.
 */
angular.module('cnmapApp')

    .controller('ChLocModalCtrl', ['$scope', '$log', '$modalInstance', 'files', 'GPSConvertService',
        function ($scope, $log, $modalInstance, files, GPSConvertService) {

            angular.forEach(files, function (file, key) {
                file.active = false;
                delete file.mapVendor;
                if (!file.modal) {
                    var preview = cnmap.cloneCanvas(file.preview);
                    file.modal = {'preview': preview};
                }
                if (!file.loc_preview && !!loadImage) {
                    // load image data preview
                    loadImage(
                        file,
                        function (img) {
                            file.loc_preview = file.loc_preview || {};
                            file.loc_preview.preview = img;
                        },
                        {
                            maxWidth: 600,
                            maxHeight: 300,
                            minWidth: 100,
                            minHeight: 50,
                            canvas: true
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
                        if(file.lat != file.mapVendor.lat
                            || file.lng != file.mapVendor.lng) {
                            file.lat = file.mapVendor.lat;
                            file.lng = file.mapVendor.lng;
                            file.latPritty = file.mapVendor.latPritty;
                            file.lngPritty = file.mapVendor.lngPritty
                            file.address = file.mapVendor.address;
                            file.vendor = 'gaode';
                            res.push(file);
                        }
                    }
                });
                $modalInstance.close(res);
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
                            addMapClickEvent($scope.map);
                            $scope.activePhoto($scope.file);

                        }
                    });
                }
            })

            function addMapClickEvent(map) {
                AMap.event.addListener(map, "click", function (event) {
                    $log.debug(event);
                    var lnglat = event.lnglat;
                    addOrUpdateMarker($scope.file, lnglat);
                    $scope.setPlace(lnglat.lat, lnglat.lng);
                });
            }

            function addOrUpdateMarker(file, lnglat) {
                file.mapVendor = file.mapVendor || {};
                if (!file.mapVendor.marker) {
                    file.mapVendor.marker = new AMap.Marker({
                        draggable: true
                    });
                    file.mapVendor.marker.photo_file = file;

                    AMap.event.addListener(file.mapVendor.marker, "dragend", function (event) {
                        updatePhoto(this.photo_file);
                        $scope.setPlace(event.lnglat.lat, event.lnglat.lng);
                    });
                }
                file.mapVendor.marker.setPosition(lnglat);
                file.mapVendor.marker.setMap($scope.map);
                file.mapVendor.marker.show();
            }

//            $scope.setMarker = function () {
//                var latlng = $scope.map.getCenter();
//                $scope.marker.setPosition(latlng);
//                $scope.marker.setMap($scope.map);
//                $scope.marker.show();
//                $scope.setPlace(latlng.lat, latlng.lng);
//            }

            $scope.hideMarker = function () {
                $scope.marker.hide();
            }

//            $scope.updateMarker = function (lat, lng) {
//                var point = new AMap.LngLat(lng, lat);
//                $scope.file.mapVendor = $scope.file.mapVendor || {};
//                if(!$scope.file.mapVendor.marker) {
//
//                }
//                $scope.file.mapVendor.marker.setPosition(point);
//                $scope.file.mapVendor.marker.setMap($scope.map);
//                $scope.file.mapVendor.marker.show();
//                $scope.map.setCenter(point);
//                $scope.setPlace(lat, lng);
//            }

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

                        addOrUpdateMarker($scope.file, lnglat);
                    }
                })
            }

            $scope.clearPlace = function () {
                $scope.hideMarker();
                delete $scope.file.mapVendor;
            }

            function updatePhoto(file) {
                $scope.file.active = false;
                $scope.file = file;
                $scope.file.active = true;
            }

            $scope.activePhoto = function (file) {
                $scope.file.active = false;
                $scope.file = file;
                $scope.file.active = true;

                if ($scope.file.mapVendor &&
                    ($scope.file.mapVendor.lat || $scope.file.mapVendor.lng)) {
                    var point = new AMap.LngLat($scope.file.mapVendor.lng, $scope.file.mapVendor.lat);
                    addOrUpdateMarker($scope.file, point);
                    $scope.map.setCenter(point);
//                    $scope.updateMarker($scope.file.mapVendor.lat, $scope.file.mapVendor.lng);
                } else {
                    if ($scope.file.lat || $scope.file.lng) {
                        $scope.file.mapVendor = $scope.file.mapVendor || {};
                        $scope.file.mapVendor.lat = $scope.file.lat;
                        $scope.file.mapVendor.lng = $scope.file.lng;

                        addOrUpdateMarker($scope.file, new AMap.LngLat($scope.file.lng, $scope.file.lat));
                        $scope.map.setCenter(new AMap.LngLat($scope.file.lng, $scope.file.lat));
                        $scope.setPlace($scope.file.lat, $scope.file.lng);
                    } else {
                        $scope.clearPlace();
                    }
                }
            }
        }])
    .controller('PhotoListScrollCtrl', ['$scope', '$location', '$anchorScroll',
        function ($scope, $location, $anchorScroll) {

        }])
;

