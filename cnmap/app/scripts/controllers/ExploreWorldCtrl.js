/**
 * Created by any on 14-3-15.
 */
'use strict';

angular.module('exploreWorldApp', ['ponmApp', 'ui.map', 'ui.bootstrap'])
//    .config(['$locationProvider', function ($locationProvider) {
//        $locationProvider.html5Mode(true)
//                        .hashPrefix('');
//    }])
    .controller('ExploreWorldCtrl', ['$window', '$location', '$scope', 'UserService', '$modal', 'deparam', 'param',
    function ($window, $location, $scope, UserService, $modal, deparam, param) {

        $scope.ctx = $window.ctx;
        $scope.apirest = $window.apirest;
        $scope.login = $window.login;
        $scope.userId = $window.userId;

            // 设置侧边栏显示的用户
            $scope.user = {};
        if($scope.login && $scope.userId) {
            $scope.user = {
                id: $scope.userId,
                name: "您"
            }
        }

        // 默认为map tab页
        $scope.tabs = {map: true};

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
            scaleControl: true
//            panControl: false,
//            zoomControl: false
            // ui map config
//            uiMapCache: false
        };

        var photoSize = 20;
        if($window.mapVendor == "qq") {
            $window.mapVendor = "gaode";
        }
        var panoramioLayer = new cnmap.PanoramioLayer(
            {suppressInfoWindows: false,
                mapVendor: $window.mapVendor || "gaode"});
        panoramioLayer.initEnv($window.ctx);
        $(panoramioLayer).bind("data_changed", function (e, data) {
            $scope.$apply(function (scope) {
                // 计算可显示图片行数
                var thumbArea = jQuery("div#thumbinnerarea");
                var width = thumbArea.innerWidth();
                var height = thumbArea.innerHeight();
                var vc = Math.floor((width - 40)/116);
                photoSize = vc * Math.floor((height-50)/116);

                // 更新图片
                $scope.updatePhoto(data);
            });
        });

        jQuery(panoramioLayer).bind("data_clicked", function (e, photoId) {
            $scope.displayPhoto(photoId);
        });

            /**
             * 设置浏览类型
             *
             * @param type
             * @param userid
             */
        $scope.setPanormaioType = function (type, userid) {
            $scope.tabs = {};
            $scope.tabs[type] = true;

            // 设置侧边栏显示的用户名
            if(userid) {
                $scope.user.id = userid;
                if($scope.login && userid == $scope.userId) {
                    $scope.user.name = "您";
                }else {
                    UserService.getOpenInfo({userId: userid}, function(res) {
                        if(res.status == "OK") {
                            $scope.user.name = res.open_info && res.open_info.name;
                        }
                    })
                }
            }

            panoramioLayer.setFavorite(false);
            panoramioLayer.setUserId('');
            panoramioLayer.setLatest(false);
            delete hashObj.userid;
            delete hashObj.latest;
            delete hashObj.favorite;
            switch (type) {
                case "user":
                    panoramioLayer.setFavorite(false);
                    panoramioLayer.setUserId($scope.user.id);
                    hashObj.userid = $scope.user.id;
                    break;
                case "map":
                    panoramioLayer.setUserId(undefined);
                    break;
                case "latest":
                    panoramioLayer.setLatest(true);
                    hashObj.latest = true;
                    break;
                case "favorite":
                    panoramioLayer.setFavorite(true);
                    hashObj.favorite = true;
                    panoramioLayer.setUserId($scope.user.id);
                    hashObj.userid = $scope.user.id;
                    break;
            }
            panoramioLayer.trigger("changed")
        };

        $scope.updatePhoto = function (photos) {
            $scope.allPhotos = photos;
            $scope.photos = photos.slice(0, photoSize);
            $scope.slice = 0;
            setPhotoStartEnd();
        };

        $scope.nextPhoto = function () {
            if ($scope.slice <= $scope.allPhotos.length) {
                $scope.slice = $scope.slice + photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }
        };

        $scope.prePhoto = function () {
            if ($scope.slice >= photoSize) {
                $scope.slice = $scope.slice - photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }
        };

        /**
         *
         */
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

        var mapEventListener = $window.cnmap.MapEventListener.factory();

        $scope.$watch('myMap', function () {
            if (!$scope.map) {
                $scope.map = $scope.myMap;
                var mapObj = $scope.myMap;
                panoramioLayer.setMap(mapObj);
                locationHash(mapObj);
                mapEventListener.addToolBar(mapObj);
                jQuery.bind("");
                //$($window).trigger('hashchange');

//                var stateObj = jQuery.parseParams($location.hash());
//
//                $window.cnmap.setCenter(stateObj.lat, stateObj.lng, mapObj);
//                if (stateObj.zoom) {
//                    $window.cnmap.setZoom(stateObj.zoom, mapObj);
//                }
            }
        });

        /**
        *  change the url hash params
        * ***********************************************************************************/
        var hashObj = {}; //jQuery.deparam($location.hash());
        // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
        // 高德地图有setCenter后取getCenter不完全一致问题
        var changeState = false;

        function locationHash(map) {

            mapEventListener.addLocationHashListener(map, function(lat ,lng, zoom) {
                if (changeState) {
                    return;
                }

                var stateObj = deparam($location.hash());

                if (lat && lng) {
                    hashObj['lat'] = lat;
                    hashObj['lng'] = lng;
                    hashObj['zoom'] = zoom;
                    if (hashObj.lat != stateObj.lat ||
                        hashObj.lng != stateObj.lng ||
                        hashObj.zoom != stateObj.zoom ||
                        hashObj.latest != stateObj.latest ||
                        hashObj.userid != stateObj.userid ||
                        hashObj.favorite != stateObj.favorite
                        ) {
                        if (this.setState) {
                            clearTimeout(this.setState);
                        }
                        changeState = true;
//                    angular.copy(hashObj, stateObj);
                        $location.hash(param(hashObj));
//                    jQuery.bbq.pushState(stateObj);
                        this.setState = setTimeout(function () {
                            changeState = false;
                        }, 500);

                    }
                }
            });

            $scope.$watch(function () {
                return $location.hash();
            }, function (hash) {
                if (changeState) {
                    return;
                }
                var stateObj = deparam(hash);

                if (stateObj.lat && stateObj.lng) {
                    if (hashObj.lat != stateObj.lat ||
                        hashObj.lng != stateObj.lng ||
                        hashObj.zoom != stateObj.zoom) {
                        if (this.setState) {
                            clearTimeout(this.setState);
                        }
                        changeState = true;
//                        angular.copy(stateObj, hashObj);
                        mapEventListener.setCenter(map, stateObj.lat, stateObj.lng);
                        if (stateObj.zoom) {
                            mapEventListener.setZoom(map, stateObj.zoom);
                        }
                        this.setState = setTimeout(function () {
                            changeState = false;
                        }, 500);

                        if(stateObj.bounds) {
                            var boundPoints = stateObj.bounds.split(",");
                            if(boundPoints.lenght = 4) {
                                mapEventListener.setBounds(map, {lat: boundPoints[0], lng: boundPoints[1]},
                                    {lat: boundPoints[2], lng:boundPoints[3]});
                            }

                        }
                    }
                }
                angular.copy(stateObj, hashObj);
                if(!!stateObj.latest) {
                    $scope.setPanormaioType("latest")
                }else {
                    if(stateObj.userid) {
                        if(stateObj.favorite) {
                            $scope.setPanormaioType("favorite", stateObj.userid);
                        }else {
                            $scope.setPanormaioType("user", stateObj.userid);
                        }
                    }
                }
            })
        }

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
                            return '';
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
