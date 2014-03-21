/**
 * Created by any on 14-3-15.
 */
'use strict';

angular.module('exploreWorldApp', ['cnmapApp', 'ui.mapgaode'])
    .controller('ExploreWorldCtrl', ['$window', '$location', '$scope', function ($window, $location, $scope) {
        $scope.ctx = $window.ctx;
        $scope.apirest = $window.apirest;
        $scope.login = $window.login;

        $scope.photos = [];
        $scope.allPhotos = [];
        $scope.slice = 0;
        $scope.photoStart = true;
        $scope.photoEnd = true;

        $scope.mapOptions = {
            // map plugin config
//            toolbar: true,
            scrollzoom: true,
            maptype: true, //'SATELLITE',
            overview: true,
//                locatecity: true,
            // map-self config
            resizeEnable: true,
            // ui map config
//            uiMapCache: false
        }

        $scope.tabs = {map: true};

        var photoSize = 4;

        var panoramioLayer = new cnmap.PanoramioLayer(
            {suppressInfoWindows: true,
                mapVendor: window.mapVendor || "gaode"});
        panoramioLayer.initEnv(ctx);
        $(panoramioLayer).bind("data_changed", function (e, data) {
            $scope.$apply(function (scope) {
                $scope.updatePhoto(data);
            });

        })

        $scope.setPanormaioType = function (type) {
            $scope.tabs = {};
            $scope.tabs[type] = true;
            switch (type) {
                case "user":
                    panoramioLayer.setFavorite(false);
                    if ($scope.login) {
                        if ($window.userId) {
                            panoramioLayer.setUserId($window.userId);
                        }
                    }
                    break;
                case "map":
                    panoramioLayer.setUserId(undefined);
                    break;
                case "favorite":
                    if ($scope.login) {
                        if ($window.userId) {
                            panoramioLayer.setUserId($window.userId);
                            panoramioLayer.setFavorite(true);
                        }
                    }
                    break;
            }
            panoramioLayer.trigger("changed")
        }

        $scope.updatePhoto = function (photos) {
            $scope.allPhotos = photos;
            $scope.photos = photos.slice(0, photoSize);
            $scope.slice = 0;
            setPhotoStartEnd();
        }

        $scope.nextPhoto = function () {
            if ($scope.slice <= $scope.allPhotos.length) {
                $scope.slice = $scope.slice + photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }
        }

        $scope.prePhoto = function () {
            if ($scope.slice >= photoSize) {
                $scope.slice = $scope.slice - photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }

        }

        function setPhotoStartEnd() {
            if ($scope.slice + photoSize <= $scope.allPhotos.length) {
                $scope.photoEnd = false;
            } else {
                $scope.photoEnd = true;
            }

            if ($scope.slice >= photoSize) {
                $scope.photoStart = false;
            } else {
                $scope.photoStart = true;
            }
        }

        $scope.$watch('myMap', function () {
            if (!$scope.map) {
                $scope.map = $scope.myMap;
                var mapObj = $scope.myMap;
                panoramioLayer.setMap(mapObj);
                locationHash(mapObj);
                mapObj.plugin(["AMap.ToolBar"], function () {
                    //加载工具条
                    var tool = new AMap.ToolBar();
                    mapObj.addControl(tool);
                });
                //jQuery.explore(mapObj);
                //$($window).trigger('hashchange');

//                var stateObj = jQuery.parseParams($location.hash());
//
//                $window.cnmap.setCenter(stateObj.lat, stateObj.lng, mapObj);
//                if (stateObj.zoom) {
//                    $window.cnmap.setZoom(stateObj.zoom, mapObj);
//                }
            }
        });

        function locationHash(map) {
            AMap.event.addListener(
                map,
                'zoomend',
                setState);

            AMap.event.addListener(
                map,
                'moveend',
                setState
            );

            var hashObj = {};
            // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
            // 高德地图有setCenter后取getCenter不完全一致问题
            var changeState = false;

                function setState() {
                var stateObj = {};
                var latlng = this.getCenter(),
                    zoom = this.getZoom();

                if (latlng) {
                    stateObj['lat'] = latlng.lat;
                    stateObj['lng'] = latlng.lng;
                    stateObj['zoom'] = zoom;
                    changeState = true;
                    hashObj = stateObj;
                    //$location.hash(jQuery.param(stateObj));
                }
            }


//            $($window).bind( 'hashchange', function(e) {
//                // Get the hash (fragment) as a string, with any leading # removed. Note that
//                // in jQuery 1.4, you should use e.fragment instead of $.param.fragment().
//                if(changeState) {
//                    return;
//                }
//                //var lat = $location.search("lat");
//                var stateObj = jQuery.parseParams($location.hash());
//
//                if(stateObj.lat && stateObj.lng) {
//                    if(hashObj.lat != stateObj.lat ||
//                        hashObj.lng != stateObj.lng ||
//                        hashObj.zoom != stateObj.zoom ) {
//                        if(this.setState) {
//                            clearTimeout(this.setState);
//                        }
//                        changeState = true;
//                        hashObj = stateObj;
//                        $window.cnmap.setCenter(stateObj.lat, stateObj.lng, map);
//                        if(stateObj.zoom) {
//                            $window.cnmap.setZoom(stateObj.zoom, map);
//                        }
//                        this.setState = setTimeout(function() {
//                            changeState = false;
//                        }, 500);
//                    }
//                }
//            })
        }

        $scope.$watch(function () {
            return $location.hash();
        }, function (hash) {
            var stateObj = jQuery.parseParams(hash);
            if(stateObj.lat && stateObj.lng) {
                $window.cnmap.setCenter(stateObj.lat, stateObj.lng, $scope.map);
                if (stateObj.zoom) {
                    $window.cnmap.setZoom(stateObj.zoom, $scope.map);
                }
            }


        })
    }])

