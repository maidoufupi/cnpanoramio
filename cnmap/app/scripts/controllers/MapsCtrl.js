/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('mapsApp', [
    'ui.bootstrap',
    'ui.map',
    'ui.router',
    'ponmApp',
    'xeditable',
    'fileuploadApp'
])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
                .when('/', '/popular')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
                .otherwise('/popular');
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////
                .state('maps', {

                    // With abstract set to true, that means this state can not be explicitly activated.
                    // It can only be implicitly activated by activating one of it's children.
                    abstract: true,

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
//                    templateUrl: 'views/photos.html',

                    views: {

                        // the main template will be placed here (relatively named)
                        '': { templateUrl: 'views/maps.html',
                              controller: 'MapsCtrl'},

                        // for column two, we'll define a separate controller
                        'alerts@maps': {
                            templateUrl: 'views/photos.alerts.html',
                            controller: 'PhotosAlertsCtrl'
                        }
                    },

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    }
                })
                .state('maps.popular', {
                    url: '/popular',
                    templateUrl: 'views/maps.popular.html',
                    resolve: {
                    },
                    controller: "MapsPopularCtrl"
                })
                .state('maps.recent', {
                    url: '/recent',
                    templateUrl: 'views/maps.popular.html',
                    resolve: {
                    },
                    controller: "MapsRecentCtrl"
                })
                .state('maps.travel', {
                    url: '/travel/:travelId',
                    templateUrl: 'views/maps.travel.html',
                    resolve: {
                    },
                    controller: "MapsTravelCtrl"
                })
                .state('maps.photo', {
                    url: '/photo',
                    templateUrl: 'views/maps.popular.html',
                    resolve: {
                    },
                    controller: "MapsRecentCtrl"
                })
                .state('maps.camera', {
                    url: '/camera',
                    templateUrl: 'views/maps.popular.html',
                    resolve: {
                    },
                    controller: "MapsRecentCtrl"
                })
                .state('maps.user', {
                    url: '/user',
                    templateUrl: 'views/maps.popular.html',
                    resolve: {
                    },
                    controller: "MapsUserCtrl"
                })
                .state('maps.travels', {
                    url: '/travels',
                    templateUrl: 'views/maps.travels.html',
                    resolve: {
                    },
                    controller: "MapsTravelsCtrl"
                })
                .state('maps.upload', {
                    url: '/upload',
                    templateUrl: 'views/upload.html',
                    resolve: {
                    },
                    controller: "MapsPhotoUploadCtrl"
                });
        }])
    .run(['$rootScope', '$window', 'editableOptions', function ($rootScope, $window, editableOptions) {
        editableOptions.theme = 'bs3';
        $rootScope.user = {
            id: $window.userId
        }
    }])
    .controller('MapsCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q', 'PhotoService', 'UserService', 'PanoramioService',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        function ($window, $location, $rootScope, $scope, $q, PhotoService, UserService, PanoramioService,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;
            $scope.userId = ponmCtxConfig.userId;
            $scope.login = !!ponmCtxConfig.userId;

            $scope.$state = $state;
            $scope.$location = $location;
            $scope.$stateParams = $stateParams;

            // 编辑视图
            $scope.editableView = true;

            $scope.$watch("editableView", function(editableView) {
                $scope.$broadcast("editableViewChanged", editableView);
            });

            $scope.changeEditableView = function() {
                $scope.editableView = !$scope.editableView;
                $scope.$broadcast("editableViewChanged", $scope.editableView);
            };

            $scope.navbarFixedTop = false;

            $log.debug($state);

            $scope.$on('waypointEvent', function (e, direction, elmId) {
                $log.debug("waypointSpot: " + direction + " = " + elmId);
                if (elmId == "navbar") {
                    if (direction == "down") {
                        safeApply($scope, function () {
                            $scope.navbarFixedTop = true;
                        });
                    } else {
                        safeApply($scope, function () {
                            $scope.navbarFixedTop = false;
                        });
                    }
                }
            });

            $scope.stateStatus = {
                popular: $state.current.url == '/popular',
                recent: $state.current.url == '/recent',
                travel: $state.current.name == 'maps.travel',
                upload: $state.current.name == 'maps.upload',
                camera: $state.current.url == '/camera',
                user: $state.current.url == '/user',
                travels: $state.current.url == '/travels'
            };

            $rootScope.$on('$stateChangeSuccess',
                function (event, toState, toParams, fromState, fromParams) {
                    $scope.stateStatus = {
                        popular: toState.url == '/popular',
                        recent: toState.url == '/recent',
                        travel: toState.name == 'maps.travel',
                        upload: toState.name == 'maps.upload',
                        camera: toState.url == '/camera',
                        user: toState.url == '/user',
                        travels: toState.url == '/travels'
                    };

                    $scope.clearSearch();
                }
            );

            $scope.contentLayouts = ['left-full', 'left-fixed', 'right-full', 'right-fixed'];
            $scope.contentLayout = 3;
            $scope.changeContentLayout = function() {
                $scope.contentLayout = ($scope.contentLayout + 1) % 4;
            };

            // 设置侧边栏显示的用户
            $scope.user = {
                login: !!ponmCtxConfig.userId,
                id: ponmCtxConfig.userId
            };
            if(ponmCtxConfig.userId) {
                $scope.user.name = "您";
            }

            $scope.mapOptions = {
                // map plugin config
//            toolbar: true,
                scrollzoom: true,
                maptype: true, //'SATELLITE',
                overview: true,
//                locatecity: true,
                // map-self config
                rotateEnable: true,
                resizeEnable: true,
                scaleControl: true
//            panControl: false,
//            zoomControl: false
                // ui map config
//            uiMapCache: false
            };

            $scope.mapEventListener = $window.cnmap.MapEventListener.factory();
            $scope.mapService = $window.cnmap.MapService.factory();

            var panoramioLayer = new cnmap.PanoramioLayer(
                {suppressInfoWindows: false,
                    mapVendor: $window.mapVendor || "gaode"}
//                    auto: false
            );
            $scope.panoramioLayer = panoramioLayer;
            panoramioLayer.initEnv($window.ctx, $scope.staticCtx );
            $(panoramioLayer).bind("data_changed", function (e, data) {
                if(!$scope.stateStatus.user) {
                    $scope.$apply(function (scope) {
                        $scope.photos = data;
                    });
                }
            });

            $scope.$watch('myMap', function (mapObj) {
                if (!$scope.map && mapObj) {
                    $scope.map = mapObj;
                    // 当地图初始化完成后，发出mapChanged事件
                    $scope.$broadcast("mapChanged", mapObj);
                }
            });

            $scope.$on("mapChanged", function(e, mapObj) {
                panoramioLayer.setMap(mapObj);
                $scope.mapEventListener.addToolBar(mapObj);
                $scope.mapService.init(mapObj);
                locationHash(mapObj);
            });

            $scope.displayPhoto = function(photoId) {
                hashObj.photoid = photoId;
                updateState();
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
                    delete hashObj.photoid;
                    updateState();
//                    $scope.selected = selectedItem;
                }, function () {
                    delete hashObj.photoid;
                    updateState();
                });
            };

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
                delete hashObj.recent;
                delete hashObj.favorite;
                panoramioLayer.setAuto(true);
                switch (type) {
                    case "user":
                        panoramioLayer.setFavorite(false);
                        panoramioLayer.setUserId($scope.user.id);
                        hashObj.userid = $scope.user.id;
                        break;
                    case "popular":
                        panoramioLayer.setUserId(undefined);
                        break;
                    case "recent":
                        panoramioLayer.setLatest(true);
                        break;
                    case "favorite":
                        panoramioLayer.setFavorite(true);
                        hashObj.favorite = true;
                        panoramioLayer.setUserId($scope.user.id);
                        hashObj.userid = $scope.user.id;
                        break;
                    case "manual":
                        panoramioLayer.setAuto(false);
                        panoramioLayer.clearMap();
                        break;
                    case "auto":
                        panoramioLayer.setAuto(true);
                        break;
                }
                panoramioLayer.trigger("changed")
            };

            /**
             *  change the url hash params
             * ***********************************************************************************/
            var hashObj = {}; //jQuery.deparam($location.hash());
            $scope.hashObj = hashObj;
            // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
            // 高德地图有setCenter后取getCenter不完全一致问题
            var changeState = false;

            function locationHash(map) {

                $scope.mapEventListener.addLocationHashListener(map, function(lat ,lng, zoom) {
                    updateState(lat ,lng, zoom);
                });

                $scope.$watch(function () {
                    return $location.hash();
                }, function (hash) {
                    if (changeState) {
                        return;
                    }
                    var stateObj = jsUtils.deparam(hash);
                    splitPoundSign(stateObj, "photoid");
                    splitPoundSign(stateObj, "zoom");
                    splitPoundSign(stateObj, "lat");
                    splitPoundSign(stateObj, "lng");

                    if (stateObj.lat && stateObj.lng) {
                        if (hashObj.lat != stateObj.lat ||
                            hashObj.lng != stateObj.lng ||
                            hashObj.zoom != stateObj.zoom) {
                            if ($scope.setState) {
                                $timeout.cancel($scope.setState);
                            }
                            changeState = true;
                            $scope.mapEventListener.setCenter(map, stateObj.lat, stateObj.lng);
                            if (stateObj.zoom) {
                                $scope.mapEventListener.setZoom(map, stateObj.zoom);
                            }
                            $scope.setState = $timeout(function () {
                                changeState = false;
                            }, 500);

                            if(stateObj.bounds) {
                                var boundPoints = stateObj.bounds.split(",");
                                if(4 == boundPoints.lenght) {
                                    $scope.mapEventListener.setBounds(map, {lat: boundPoints[0], lng: boundPoints[1]},
                                        {lat: boundPoints[2], lng:boundPoints[3]});
                                }

                            }
                        }
                    }

                    if(stateObj.search != hashObj.search) {
                        $scope.setSearch(stateObj.search);
//                        $scope.$broadcast("searchChanged", stateObj.search);
                    }

                    angular.copy(stateObj, hashObj);

                    if(stateObj.userid) {
                        if(stateObj.favorite) {
                            $scope.setPanormaioType("favorite", stateObj.userid);
                        }else {
                            $scope.setPanormaioType("user", stateObj.userid);
                        }
                    }

                    if(stateObj.photoid) {
                        $scope.displayPhoto(stateObj.photoid);
                    }

//                    if(stateObj.search) {
//                        var url = $state.current.url;
//                        url = url.replace("/", "");
//                        $scope.search(stateObj.search, url);
//                    }
                })
            }

            function updateState(lat ,lng, zoom) {
                if (changeState) {
                    return;
                }

                var stateObj = jsUtils.deparam($location.hash());
                splitPoundSign(stateObj, "photoid");

                if (lat && lng && zoom) {
                    hashObj['lat'] = lat;
                    hashObj['lng'] = lng;
                    hashObj['zoom'] = zoom;
                }
                if (hashObj.lat != stateObj.lat ||
                    hashObj.lng != stateObj.lng ||
                    hashObj.zoom != stateObj.zoom ||
                    hashObj.latest != stateObj.latest ||
                    hashObj.userid != stateObj.userid ||
                    hashObj.favorite != stateObj.favorite ||
                    hashObj.photoid != stateObj.photoid
                    ) {
                    if ($scope.setState) {
                        $timeout.cancel($scope.setState);
                    }
                    changeState = true;
                    $location.hash(jsUtils.param(hashObj));
                    $scope.setState = $timeout(function () {
                        changeState = false;
                    }, 500);
                }
            }
            $scope.updateState = updateState;

            /**
             * 去掉可能有的非法#号
             * @param value
             */
            function splitPoundSign(object, propName) {
                if(object[propName]) {
                    var values = object[propName].split("#");
                    object[propName] = values[0];
                }
            }

            $scope.search = function(val, type) {

                var deferred = $q.defer();

                var bounds = panoramioLayer.getBounds(),
                    level = panoramioLayer.getLevel(),
                    size = panoramioLayer.getSize();

                PanoramioService.search(
                    {swlat:bounds.sw.lat, swlng:bounds.sw.lng, nelat:bounds.ne.lat, nelng:bounds.ne.lng,
                        level:level, width:size.width, height:size.height, type: type||'', term: val},
                    function(res) {
                        if(res.status == "OK") {
                            $scope.photos = res.photos;
                            panoramioLayer.createPhotosMarker($scope.photos);
                            $log.debug("search res size: " + $scope.photos.length);
                            deferred.resolve(res.photos);
                        }
                    });

                return deferred.promise;
            };

            $scope.setSearch = function(search) {
                if(search) {
                    $scope.setPanormaioType("manual");
                }else {
                    $scope.setPanormaioType("auto");
                }
                $scope.searchValObject = {
                    val: search
                };
                hashObj.search = search;
                updateState();
//                if(!$scope.stateStatus.popular && !$scope.stateStatus.recent) {
//                    $state.go("maps.popular");
//                }
                $scope.$broadcast("searchChanged", search);
            };

            $scope.clearSearch = function() {
                panoramioLayer.setAuto(true);
                $scope.searchValObject = "";
                delete hashObj.search;
                updateState();
            };

//            PanoramioService.getLatest({swlat:23, swlng:120, nelat:30, nelng:130, level:3, width:500, height:500, vendor:'gaode'},
//                function(res) {
//                    if(res.status == "OK") {
//                        $scope.photos = res.photos;
//                        panoramioLayer.createPhotosMarker($scope.photos);
//                    }
//                });
        }])
    .controller('PhotosAlertsCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

        }])
    .controller('MapsSearchCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', '$q',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, $q) {

            $scope.selected = undefined;
            $scope.preconfigured = [{
                preconfig: true,
                type: "photo",
                typeDesc: "图片"
            }];

            $scope.$on("searchChanged", function(e, val) {
                $scope.asyncSelected = val;
            });

            // Any function returning a promise object can be used to load values asynchronously
            $scope.getLocation = function (val) {
                var d = $q.defer();
                $scope.mapService.getLocPois(val, function(res) {
                    d.resolve(res);
                });
                return d.promise.then(function(res) {
                    var preconfigured = angular.copy($scope.preconfigured);
                    angular.forEach(preconfigured, function(value, key) {
                        value.address = val;
                        value.val = val;
                    });
                    return preconfigured.concat(res);
                });
            };

            $scope.goLocation = function (search) {
                if(angular.isString(search)) {
                    $scope.setSearch(search);
                }
                if(search.preconfig) {
                    $scope.setSearch(search.val);
//                    $scope.search(address.val, address.type);
                }else {
                    var location = search.location;
                    if (location) {
                        $scope.mapEventListener.setCenter($scope.map, location.lat, location.lng);
                        $scope.mapEventListener.setZoom($scope.map, search.zoom);
                    }
                }
            };

            $scope.onSelect = function($item, $model, $label) {

                $scope.goLocation($item);
            };

        }])
    .controller('MapsPopularCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;

            $scope.setPanormaioType('popular');

            $scope.$on("searchChanged", function(e, val) {
                $scope.search(val, "photo").then(function(photos) {
                    $scope.photos = photos;
                })
            });
        }])
    .controller('MapsRecentCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;

            $scope.setPanormaioType('recent');

            $scope.$on("searchChanged", function(e, val) {
                $scope.search(val, "photo").then(function(photos) {
                    $scope.photos = photos;
                })
            });
        }])
    .controller('MapsTravelCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils',
        function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, jsUtils) {

            $scope.panoramioLayer.setAuto(false);

            $scope.setPanormaioType('travel');

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.stateStatus.name = res.travel.title;
                        $scope.setTravel(res.travel);

                        if($scope.travel.spots[0]) {
//                            $scope.activeSpot($scope.travel.spots[0]);
                        }

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.travel.user_id}, function (data) {
                            if (data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });
                    }
                });
            }

            $scope.setTravel = function(travel) {
                $scope.travel = travel;

                // 设置此travel是否可以被登录者编辑
                $scope.travelEnedit = ($scope.userId == $scope.travel.user_id) && $scope.editableView;

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
                $scope.travelLayer.clearMap();
                $scope.travelLayer.setTravel($scope.travel);
                $scope.travelLayer.initMap();
            };

            $scope.$on("travelChanged", function(e, travelId) {
                getTravel(travelId);
            });

            $scope.$on("mapChanged", function(e) {
                initTravelLayer($scope.map)
            });
            $scope.$on("editableViewChanged", function(e, editableView) {
                // 设置此travel是否可以被登录者编辑
                if($scope.travel) {
                    $scope.travelEnedit = ($scope.userId == $scope.travel.user_id) && editableView;
                }
            });

            function initTravelLayer(map) {
                $scope.travelLayer = new $window.cnmap.TravelLayer({
                    ctx: $scope.ctx,
                    staticCtx: $scope.staticCtx,
                    travel: $scope.travel,
                    clickable: true
                });

                jQuery($scope.travelLayer).bind("data_clicked", function (e, photoId) {
                    $scope.displayPhoto(photoId);
                });

                $scope.travelLayer.setMap(map);
            }

            function clearTravelLayer() {
                $scope.travelLayer.clearMap();
            }

            $scope.$on("$destroy", function() {
                clearTravelLayer();
                delete $scope.hashObj.travelid;
                $scope.updateState();
            });

            $scope.activePhoto = function(photo) {
                if($scope.photo && $scope.photo.id == photo.id) {
                    $scope.displayPhoto(photo.id);
                }else {
                    $scope.photo = photo;
                    $scope.travelLayer.activePhoto(photo);
                }
            };

            $scope.activeSpot = function(spot) {
                var spotId = 0;
                if(angular.isObject(spot)) {
                    spotId = spot.id;
                }else {
                    spotId = spot;
                }
                if($scope.activedSpot && $scope.activedSpot.id == spotId) {
                    $scope.travelLayer.activeSpot(spot);
                    return;
                }
                angular.forEach($scope.travel.spots, function(spot, key) {
                    if(spot.id == spotId) {
                        spot.active = true;
                        $scope.activedSpot = spot;
                    }else {
                        spot.active = false;
                    }
                });
                $scope.travelLayer.activeSpot(spot);
            };

            $scope.showSpotAddress = function(spot) {
                return spot.address || "添加地址";
            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            var today = new Date();
            // Disable weekend selection
            $scope.datepickerDisabled = function(date, mode) {
                return ( mode === 'day' && ( date > today ) );
            };

            if($scope.map) {
                initTravelLayer($scope.map)
            }

            if($stateParams.travelId) {
                getTravel($stateParams.travelId);
            }
        }])
    .controller('MapsTravelSpotCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils',
        function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, jsUtils) {

            $scope.$watch('spot.timeStart', function(timeStart) {
                if(timeStart) {
                    var promise = $scope.updateSpot($scope.spot, "time_start", $filter('date')(timeStart, "yyyy/MM/dd"));
                    promise.then(function() {
                        $scope.spot.time_start = timeStart;
                        $scope.travelLayer.calcSpotTime();
                    }, function() {
//                    $scope.travelLayer.calcSpotTime();
                    });
                }
            });

            $scope.updateSpot = function(spot, type, $data) {
                var d = $q.defer();
                var params = {
                    title: spot.title,
                    description: spot.description,
                    address: spot.address
                };
                params[type] = $data;
                TravelService.changeSpot({travelId: spot.travel_id, typeId: spot.id}, param(params), function(res) {
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

            $scope.createSpot = function(photo) {
                TravelService.createSpot({travelId: $scope.spot.travel_id}, param({}), function(res) {
                    if(res.status == "OK") {
                        $scope.addSpotPhoto(photo, res.spot);
                    }
                });
            };

            $scope.addSpotPhoto = function(photo, spot) {
                var photos = {photos: photo.id};
                TravelService.addSpotPhoto({travelId: spot.travel_id, typeId: spot.id},
                    param(photos), function(res) {
                        if(res.status == "OK") {
                            $scope.setTravel(res.travel);
                        }
                    });
            };

            $scope.removePhoto = function(photo) {
                $scope.$emit('photoDeleteEvent', photo.id);
            };

            $scope.deleteSpot = function(spot) {
                TravelService.deleteSpot({travelId: spot.travel_id, typeId: spot.id}, function(res) {
                    if(res.status == "OK") {
                        $scope.setTravel(res.travel);
                    }
                });
            };

        }])
    .controller('MapsUserCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;

            $scope.setPanormaioType('user');

            // 用户图片分页属性
            $scope.photo = {
                pageSize: 40,
                currentPage: 1,
                maxSize: 10
            };

            $($scope.panoramioLayer).bind("map_changed", function (e, bounds, level, size) {
                $log.debug(bounds);
                $log.debug(level);
                $log.debug(size);

                UserPhoto.get({userId: $scope.user.id, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage,
                        swlat:bounds.sw.lat, swlng:bounds.sw.lng, nelat:bounds.ne.lat, nelng:bounds.ne.lng},
                    function(data) {
                        if(data.status == "OK") {
                            $scope.photos = data.photos;
                        }
                    })
            });

            $scope.$on("$destroy", function() {
                delete $scope.hashObj.userid;
                $scope.updateState();
            });
        }])
    .controller('MapsTravelsCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', 'TravelService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, TravelService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

//            $scope.selectable = false;
            $scope.setPanormaioType('manual');

            $scope.goTravel = function(travel) {
                $state.go("maps.travel", {travelId: travel.id});
            };

            $scope.$on("searchChanged", function(e, val) {

                $scope.travels = [];

                $scope.search(val, "travel").then(function(photos) {
                    var travels = {};
                    angular.forEach(photos, function(photo, key) {
                        if(photo.travel_id && !travels[photo.travel_id]) {
                            travels[photo.travel_id] = photo.travel_id;

                            TravelService.getTravel({travelId: photo.travel_id}, function(res) {
                                if(res.status == "OK") {
                                    res.travel.photos = [];
                                    angular.forEach(res.travel.spots, function(spot, key) {
                                        res.travel.photos = res.travel.photos.concat(spot.photos);
                                    });
                                    $scope.travels.push(res.travel);
                                }
                            });
                        }
                    });
                });
            });
        }])
    .controller('MapsPhotoUploadCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', 'TravelService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, TravelService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.setPanormaioType('manual');

        }])
;