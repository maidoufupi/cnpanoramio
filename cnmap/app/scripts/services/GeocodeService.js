/**
 * Created by any on 2014/5/26.
 */
'use strict';
angular.module('ponmApp.services')
    .factory('GeocodeService', ['$window', '$http', function ($window, $http) {

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
                        key: "53f7e239ddb8ea62ba552742a233ed1f"
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
                        key: "53f7e239ddb8ea62ba552742a233ed1f"
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
            }

        };
    }])
;