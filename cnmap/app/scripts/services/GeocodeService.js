/**
 * Created by any on 2014/5/26.
 */
'use strict';
angular.module('ponmApp.services')
//    .factory('GeocodeService', ['$window', '$http', '$q', function ($window, $http, $q) {
//
//        var levelMap = {
//            "国家": 3,
//            "省": 7,
//            "市": 9,
//            "区县": 11,
//            "乡镇": 13,
//            "村庄": 16,
//            "热点商圈": 16,
//            "小区": 18,
//            "兴趣点": 18,
//            "门牌号": 19,
//            "道路": 17,
//            "道路交叉路口": 18,
//            "公交站台、地铁站": 18
//        };
//
//        return {
//            levelMap: levelMap,
//            geo: function(address, callback) {
//                return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
//                    params: {
//                        address: address,
//                        sensor : false
//                    }
//            }).then(function(res){
//                    if(res.status == "OK") {
//                        angular.forEach(res.results, function(geocode, key) {
//                            geocode.zoom = 10;
//                        });
//                        callback.apply(undefined, [res.data.geocodes, address]);
//                    }
//                });
//            },
//
//            regeo: function(point, callback, extensions) {
//                var location = point.lat + "," + point.lng;
//
//                $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
//                    params: {
//                        latlng: location,
//                        sensor : false
//                    }
//                }).then(function(res){
//                    if(res.status == "OK") {
//                       allback.apply(undefined, [res.results]);
//                    }
//                });
//            },
//
//            regeoAddresses: function(lat, lng) {
//                var d = $q.defer();
//                this.regeo({lat: lat, lng: lng}, function(regeocode) {
//                    var addresses = {};
//                    var baseAddr = regeocode.addressComponent.province + regeocode.addressComponent.district
//                        + regeocode.addressComponent.city + regeocode.addressComponent.township;
//                    angular.forEach(regeocode.pois, function(poi, key) {
//                        addresses[baseAddr + poi.name] = {
//                            poiweight: poi.poiweight,
//                            location: poi.location
//                        };
//                    });
//                    d.resolve({
//                        address: regeocode.formatted_address,
//                        addresses: addresses
//                    });
//                }, "all");
//                return d.promise;
//            }
//        };
//    }])
    .factory('GeocodeService', ['$window', '$http', '$q', function ($window, $http, $q) {

        var levelMap = {
            "国家": 3,
            "省": 7,
            "市": 9,
            "区县": 11,
            "乡镇": 13,
            "村庄": 16,
            "热点商圈": 16,
            "小区": 18,
            "兴趣点": 18,
            "门牌号": 19,
            "道路": 17,
            "道路交叉路口": 18,
            "公交站台、地铁站": 18
        };

        return {
            levelMap: levelMap,
            geo: function(address, callback) {
                return $http.get('http://restapi.amap.com/v3/geocode/geo', {
                    params: {
                        address: address,
                        key: "feb4691f59f30a28499c2a6197ba2773"
                    },
                    headers:{
                        'Access-Control-Allow-Headers': 'Content-Type, X-Requested-With',
                        'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
                        'Access-Control-Allow-Origin': '*'
                    }
            }).then(function(res){
                    if(res.status == "200") {
                        if(res.data && res.data.info == "OK") {
                            angular.forEach(res.data.geocodes, function(geocode, key) {
                                if(levelMap[geocode.level]) {
                                    geocode.zoom = levelMap[geocode.level];
                                }
                            });
                            callback.apply(undefined, [res.data.geocodes, address]);
                        }
                    }
                });
            },

            regeo: function(point, callback, extensions) {
                var location = "",
                    batch    = false;
                if(angular.isArray(point)) {
                    batch = true;
                    angular.forEach(point, function(p, key) {
                        var loc = p.lng + "," + p.lat;
                        if(location) {
                            location = location + "|" + loc;
                        }else {
                            location = loc;
                        }
                    });
                }else if(angular.isObject(point)) {
                    location = point.lng + "," + point.lat;
                }
                $http.get('http://restapi.amap.com/v3/geocode/regeo', {
                    params: {
                        location: location,
                        batch: batch,
                        extensions: extensions ? "all" : "base",
                        key: "feb4691f59f30a28499c2a6197ba2773"
                    },
                    headers:{
                        'Access-Control-Allow-Headers': 'Content-Type, X-Requested-With',
                        'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
                        'Access-Control-Allow-Origin': '*'
                    }
                }).then(function(res){
                    if(res.status == "200") {
                        if(res.data && res.data.info == "OK") {
//                            angular.forEach(res.data.geocodes, function(geocode, key) {
//                                if(levelMap[geocode.level]) {
//                                    geocode.zoom = levelMap[geocode.level];
//                                }
//                            });
                            if(batch) {
                                callback.apply(undefined, [res.data.regeocodes]);
                            }else {
                                callback.apply(undefined, [res.data.regeocode]);
                            }

                        }
                    }
                });
            },

            regeoAddresses: function(lat, lng) {
                var d = $q.defer();
                this.regeo({lat: lat, lng: lng}, function(regeocode) {
                    var addresses = {};
                    var baseAddr = regeocode.addressComponent.province + regeocode.addressComponent.district
                        + regeocode.addressComponent.city + regeocode.addressComponent.township;
                    angular.forEach(regeocode.pois, function(poi, key) {
                        addresses[baseAddr + poi.name] = {
                            poiweight: poi.poiweight,
                            location: poi.location
                        };
                    });
                    d.resolve({
                        address: regeocode.formatted_address,
                        addresses: addresses
                    });
                }, "all");
                return d.promise;
            }
        };
    }])
;