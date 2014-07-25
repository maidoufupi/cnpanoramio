/**
 * Created by any on 14-3-12.
 */
'use strict';
/**
angular.module('map.AMap', ['ngRoute', 'ngResource', 'ui.bootstrap'])
//    .constant('mapObj', window.mapAMap)
//    .config(['mapObj', function(mapObj) {
//        var auto;
//        //加载输入提示插件
//        mapObj.plugin(["AMap. Autocomplete"], function() {
//            var autoOptions = {
//                city: "010" //城市，默认全国
//            };
//            auto = new AMap. Autocomplete(autoOptions);
//            //查询成功时返回查询结果
//            AMap.event.addListener(auto, "complete", function(result) {
//                console.log(result)
//            });
//            //输入关键字“苏州”，提示匹配信息
//            auto.search("苏州");
//        })
//    }])
    .factory('AutoComplete', function () {
        return function () {
            this.auto;
            this.geolocation;
            this.init = function (mapObj) {

            }

            this.search = function (request) {
            }

            this.addListener = function (callback) {
            }
        }
    })
    .factory('amapService', ['$log', 'AutoComplete', function ($log, AutoComplete) {

        function AMapAutoComplete() {

            this.init = function (mapObj, callback) {
                if (!mapObj) {
                    return this.auto;
                }
                var that = this;

                mapObj.plugin(["AMap.Autocomplete"], function () {
                    var autoOptions = {
                        city: "010" //城市，默认全国
                    };
                    that.auto = new AMap.Autocomplete(autoOptions);
                    //查询成功时返回查询结果
                    AMap.event.addListener(that.auto, "complete");
                    callback.apply(that, [])
                })
            }

            this.search = function (name, callback) {
                if (this.auto) {
                    if (callback) {
                        if (this.listener) {
                            AMap.event.removeListener(this.auto, this.listener);
                        }
                        this.listener = AMap.event.addListener(this.auto, "complete", callback);
                    }

                    this.auto.search(name);
                }
            }

            this.addAutoListener = function (geolocationResult, errorResult) {
                AMap.event.addListener(this.geolocation, 'complete', geolocationResult);
                AMap.event.addListener(this.geolocation, 'error', errorResult);
            }
        }

        function AMapGeolocation() {

            this.init = function (mapObj, callback) {
                if (!mapObj) {
                    return this.geolocation;
                }
                var that = this;

                mapObj.plugin(["AMap.Geolocation"], function () {
                    var geoOptions = {
                        enableHighAccuracy: true, //是否使用高精度
                        timeout: 3000, // 超时毫秒数
                        maximumAge: 1000 //定位结果缓存毫秒数
                    };
                    that.geolocation = new AMap.Geolocation(geoOptions);
                    callback.apply(that, [])
                });
            }

            this.addGeolocationListener = function (geolocationResult, errorResult) {
                AMap.event.addListener(this.geolocation, 'complete', geolocationResult);
                AMap.event.addListener(this.geolocation, 'error', errorResult);
            }

            this.getCurrentPosition = function () {
                this.geolocation.getCurrentPosition();
            }
        }

        function AMapGeocoder() {

            this.init = function (mapObj, geocoder_CallBack) {
                if (!mapObj) {
                    return this.geocoder;
                }
                var that = this;

                //加载地理编码插件
                mapObj.plugin(["AMap.Geocoder"], function () { //加载地理编码插件
                    that.geocoder = new AMap.Geocoder({
                        radius: 1000, //以已知坐标为中心点，radius为半径，返回范围内兴趣点和道路信息
                        extensions: "all" //返回地址描述以及附近兴趣点和道路信息，默认“base”
                    });
                    //返回地理编码结果
                    that.listener = AMap.event.addListener(that.geocoder, "complete", geocoder_CallBack);
                });
            }

            this.getAddress = function (lat, lng, callback) {
                if (this.geocoder) {
                    if (callback) {
                        if (this.listener) {
                            AMap.event.removeListener(this.geocoder, this.listener);
                        }
                        this.listener = AMap.event.addListener(this.geocoder, "complete", callback);
                    }

                    //逆地理编码
                    this.geocoder.getAddress(new AMap.LngLat(lng, lat));
                }
            }

            this.addListener = function (result, errorResult) {
                AMap.event.addListener(this.geocoder, 'complete', result);
                AMap.event.addListener(this.geocoder, 'error', errorResult);
            }
        }

        var geocoder;

        return {
            autoComplete: new AMapAutoComplete(),
            geolocation: new AMapGeolocation(),
            geocoder: new AMapGeocoder()
        };
    }

    ])
    .
    controller('AMapAutoCompleteCtrl', ['$scope', '$log', '$http', 'amapService', function ($scope, $log, $http, amapService) {

        $scope.$watch('myMap', function () {
            if ($scope.myMap) {
                amapService.geocoder.init($scope.myMap, function (res) {
                    console.log(res)
                })
                amapService.autoComplete.init($scope.myMap, function (res) {
                    console.log(res)
                })
            }

        })
        $scope.getAddress = function (lat, lng) {
            amapService.geocoder.getAddress(lat, lng, function (res) {
                if (res.info == "OK" && res.type == "complete") {
                    var regeocode = res.regeocode;
                    $scope.$apply(function () {
                        $scope.address = regeocode.formattedAddress;
                    })

                }
            });
        }
        $scope.search = function (address) {
            amapService.autoComplete.search(address, function (res) {
                if (res.info == "OK" && res.type == "complete") {
                    $log.debug(res);
                    var regeocode = res.regeocode;
                    $scope.$apply(function () {
                        $scope.address = regeocode.formattedAddress;
                    })

                }
            });
        }

        $scope.getLocation = function(val) {

            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(res){
                    var addresses = [];
                    $scope.addresses = {};
                    angular.forEach(res.data.results, function(item){
                        $log.debug(item);
                        $scope.addresses[item.formatted_address] = item;
                        addresses.push(item);
                    });
                    return addresses;
                });
        }

        $scope.update = function(address) {
            var geometry = address.geometry;
            if(geometry) {
                $scope.myMap.setCenter(new AMap.LngLat(geometry.location.lng, geometry.location.lat))
                $scope.myMap.setBounds(new AMap.Bounds(
                    new AMap.LngLat(geometry.bounds.southwest.lng, geometry.bounds.southwest.lat),
                    new AMap.LngLat(geometry.bounds.northeast.lng, geometry.bounds.northeast.lat)
                    ))
            }

        }
    }])

    .controller('TypeaheadCtrl', ['$scope', '$log', '$http', function ($scope, $log, $http) {
        $scope.getLocation = function(val) {

            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(res){
                    var addresses = [];
                    $scope.addresses = {};
                    angular.forEach(res.data.results, function(item){
                        $log.debug(item);
                        $scope.addresses[item.formatted_address] = item;
                        addresses.push(item);
                    });
                    return addresses;
                });
        }

        $scope.update = function(address) {
            var geometry = address.geometry;
            if(geometry) {
                $scope.myMap.setCenter(new AMap.LngLat(geometry.location.lng, geometry.location.lat))
                $scope.myMap.setBounds(new AMap.Bounds(
                    new AMap.LngLat(geometry.bounds.southwest.lng, geometry.bounds.southwest.lat),
                    new AMap.LngLat(geometry.bounds.northeast.lng, geometry.bounds.northeast.lat)
                ))
            }

        }
    }]);
 */