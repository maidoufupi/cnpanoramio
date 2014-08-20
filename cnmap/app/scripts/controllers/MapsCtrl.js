/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('mapsApp', [
//    'ngAnimate',
    'ui.bootstrap',
    'ui.map',
    'ui.router',
//    'ponmApp',
    'xeditable',
    'fileuploadApp',
    'ngDragDrop'
])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
                .when('/maps', '/maps/popular')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
//                .otherwise('/popular')
                ;
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
                    url: '/maps',

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
                        },
                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
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
                    url: '/user/:userId',
                    templateUrl: 'views/maps.user.html',
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
                'HashStateManager',
        function ($window, $location, $rootScope, $scope, $q, PhotoService, UserService, PanoramioService,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  HashStateManager) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.userId = ponmCtxConfig.userId;
            $scope.login = ponmCtxConfig.login;
            $scope.ponmCtxConfig = ponmCtxConfig;
            // 设置二级菜单显示的用户
            $scope.user = {
                login: $scope.login,
                id: ponmCtxConfig.userId
            };
            if(ponmCtxConfig.userId) {
                $scope.user.name = "您";
            }
            $scope.$watch(function() {
                return ponmCtxConfig.userId;
            }, function(userId) {
                $scope.userId = userId;
                $scope.login = ponmCtxConfig.login;
                if(!$scope.user.name && userId) {
                    $scope.user.id = userId;
                    $scope.user.name = "您";
                }
            });

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

            $scope.hashStateManager = new HashStateManager($scope, $location);

            $scope.stateStatus = {
                popular: $state.current.url == '/popular',
                recent: $state.current.url == '/recent',
                travel: $state.current.name == 'maps.travel',
                upload: $state.current.name == 'maps.upload',
                camera: $state.current.url == '/camera',
                user: $state.current.name == 'maps.user',
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
                        user: toState.name == 'maps.user',
                        travels: toState.url == '/travels'
                    };
                }
            );

            $scope.contentLayouts = ['right-fixed', 'left-fixed', 'right-full', 'left-full'];
            $scope.contentLayout = 0;
            $scope.changeContentLayout = function() {
                $scope.contentLayout = ($scope.contentLayout + 1) % 4;
            };

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
                {suppressInfoWindows: false
//                    ,mapVendor: $window.mapVendor || "gaode"
                }
//                    auto: false
            );
            $scope.panoramioLayer = panoramioLayer;
            panoramioLayer.initEnv($window.ctx, $scope.staticCtx );
            jQuery(panoramioLayer).bind("data_changed", function (e, data) {
                if(!$scope.stateStatus.user) {
                    $scope.$apply(function (scope) {
                        $scope.photos = jsUtils.Array.mergeRightist($scope.photos || [], data, "id");
//                        $scope.photos = data;
                        $scope.$broadcast("ponmPhotoFluidResize", {});
                    });
                }
            });
            jQuery(panoramioLayer).bind("data_clicked", function (e, photoId) {
                $scope.displayPhoto(photoId);
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

            /**
             * state 参数说明打开图片是由地址栏photoid状态变化引起，不需要重新设置为photoid状态值
             *
             * @param photoId
             * @param state
             */
            $scope.displayPhoto = function(photoId, state) {
                if(!state) {
                    $scope.hashStateManager.set("photoid", photoId);
                }

                $scope.photoDisplayModal = $modal.open({
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

                $scope.photoDisplayModal.result.then(function (selectedItem) {
                    $scope.hashStateManager.set("photoid", "");
                    $scope.photoDisplayModal = null;
                }, function () {
                    $scope.hashStateManager.set("photoid", "");
                    $scope.photoDisplayModal = null;
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
                panoramioLayer.setAuto(true);
                panoramioLayer.clearMap();
                switch (type) {
                    case "user":
                        panoramioLayer.setFavorite(false);
                        panoramioLayer.setUserId($scope.user.id);
                        break;
                    case "popular":
                        panoramioLayer.setUserId(undefined);
                        break;
                    case "recent":
                        panoramioLayer.setLatest(true);
                        break;
                    case "favorite":
                        panoramioLayer.setFavorite(true);
                        panoramioLayer.setUserId($scope.user.id);
                        break;
                    case "manual":
                        panoramioLayer.setAuto(false);
//                        panoramioLayer.clearMap();
                        break;
                    case "auto":
                        panoramioLayer.setAuto(true);
                        break;
                }
                panoramioLayer.trigger("changed")
            };

            // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
            // 高德地图有setCenter后取getCenter不完全一致问题
            var changeState = false,
                listeners = [];

            function locationHash(map) {

                listeners = $scope.mapEventListener.addLocationHashListener(map,
                    function(lat ,lng, zoom) {
                        $log.debug("locationHashListener");
//                        if (changeState) {
//                            return;
//                        }

                        if ($scope.setState) {
                            $timeout.cancel($scope.setState);
                        }
                        changeState = true;
                        $scope.hashStateManager.set({
                            lat: lat,
                            lng: lng,
                            zoom: zoom
                        });
                        $scope.setState = $timeout(function () {
                            changeState = false;
                        }, 500);

                });

                $scope.hashStateManager.bindingWatch("lat lng zoom bounds", function(lat, lng, zoom, bounds) {

                    $log.debug("watch lat lng zoom");
                    if ($scope.setState) {
                        $timeout.cancel($scope.setState);
                    }
                    changeState = true;
                    if(zoom && lat && lng) {
                        $log.debug("setZoomAndCenter");
                        $scope.mapEventListener.setZoomAndCenter(map, zoom, lat, lng);
                    }else {
                        if(lat && lng) {
                            $log.debug("setCenter");
                            $scope.mapEventListener.setCenter(map, lat, lng);
                        }
                        if (zoom) {
                            $log.debug("setZoom");
                            $scope.mapEventListener.setZoom(map, zoom);
                        }
                    }

                    $scope.setState = $timeout(function () {
                        changeState = false;
                    }, 500);

                    if(bounds) {
                        var boundPoints = bounds.split(",");
                        if(4 == boundPoints.lenght) {
                            $scope.mapEventListener.setBounds(map, {lat: boundPoints[0], lng: boundPoints[1]},
                                {lat: boundPoints[2], lng:boundPoints[3]});
                        }

                    }
                });
            }

            // 当地址栏photoid变化时，显示相应图片
            $scope.hashStateManager.watch("photoid", function(photoId) {
                $scope.photoDisplayModal && $scope.photoDisplayModal.dismiss('cancel');
                if(photoId) {
                    $scope.displayPhoto(photoId, true);
                }
            });

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
                $scope.hashStateManager.set("search", search);

                $scope.$broadcast("searchChanged", search);
            };

            $scope.clearSearch = function() {
                panoramioLayer.setAuto(true);
                $scope.searchValObject = "";
                $scope.hashStateManager.set("search", "");
            };

            $scope.loadMoreDisabled = true;

            $scope.$on("$destroy", function() {
                $scope.mapEventListener.removeListener(listeners);
                $scope.hashStateManager.clear();
                $scope.hashStateManager = null;
            });

            $scope.clearState = function() {
                $scope.hashStateManager && $scope.hashStateManager.set("photoid", "");
                $scope.photoDisplayModal && $scope.photoDisplayModal.dismiss('cancel');
//                panoramioLayer.clearMap();
            };

            $scope.markers = [];

            $scope.onPhotoDrop = function(e, ui) {
                $log.debug(e);
                $log.debug(e.offsetX);
                $log.debug(e.offsetY);

                $log.debug(ui);
                $log.debug(ui.position);
                $log.debug(ui.offset);
                $log.debug($scope.markers[$scope.markers.length-1].photoId);
                $scope.$broadcast("onDrop", {event: e, ui: ui, item: $scope.markers[$scope.markers.length-1]});
            };

            $scope.onPhotoOver = function(e, ui) {
                $log.debug(e);
//                $log.debug(ui.helper.addClass('drag-marker'));
            };
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
                $scope.mapService.getLocPois(val).then(function(res) {
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
            $scope.clearSearch();

            $scope.setPanormaioType('popular');

            $scope.$on("searchChanged", function(e, val) {
                $scope.search(val, "photo").then(function(photos) {
                    $scope.photos = photos;
                })
            });

            $scope.$on("$destroy", function() {
                $scope.clearState();
            });
        }])
    .controller('MapsRecentCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;
            $scope.clearSearch();

            $scope.setPanormaioType('recent');

            $scope.$on("searchChanged", function(e, val) {
                $scope.search(val, "photo").then(function(photos) {
                    $scope.photos = photos;
                })
            });

            $scope.$on("$destroy", function() {
                $scope.clearState();
            });
        }])
    .controller('MapsTravelCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils',
        function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, jsUtils) {

            $scope.clearSearch();
            $scope.setPanormaioType('manual');
//            $scope.panoramioLayer.setAuto(false);

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
                if($scope.travelLayer) {
                    $scope.travelLayer.clearMap();
                    $scope.travelLayer.setTravel($scope.travel);
                    $scope.travelLayer.initMap();
                }

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
                if($scope.travel) {
                    $scope.travelLayer.setTravel($scope.travel);
                    $scope.travelLayer.initMap();
                }
            }

            function clearTravelLayer() {
                $scope.travelLayer.clearMap();
            }

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

            $scope.$on("$destroy", function() {
                if($scope.travelLayer) {
                    $scope.travelLayer.clearMap();
                }
                $scope.clearState();
            });
        }])
    .controller('MapsTravelSpotCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', '$filter', 'jsUtils',
        function ($window, $location, $rootScope, $scope, $q, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, $filter, jsUtils) {

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
                TravelService.changeSpot({travelId: spot.travel_id, typeId: spot.id},
                    jsUtils.param(params),
                    function(res) {
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
                TravelService.createSpot({travelId: $scope.spot.travel_id}, jsUtils.param({}), function(res) {
                    if(res.status == "OK") {
                        $scope.addSpotPhoto(photo, res.spot);
                    }
                });
            };

            $scope.addSpotPhoto = function(photo, spot) {
                var photos = {photos: photo.id};
                TravelService.addSpotPhoto({travelId: spot.travel_id, typeId: spot.id},
                    jsUtils.param(photos), function(res) {
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
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, jsUtils) {

            $scope.selectable = false;

            $scope.userId = $stateParams.userId;

            // 用户图片分页属性
            $scope.photo = {
                pageSize: 20,
                currentPage: 1,
                maxSize: 10
            };

            $scope.photos = [];
            $scope.loadMoreDisabled = false;
            $scope.loadBusy = false;

            function loadUserPhotos() {
                if($scope.loadBusy) {
                    return;
                }
                $scope.loadBusy = true;
                UserPhoto.get({userId: $scope.userId,
                        pageSize: $scope.photo.pageSize,
                        pageNo: $scope.photo.currentPage,
                        swlat:$scope.bounds.sw.lat, swlng:$scope.bounds.sw.lng,
                        nelat:$scope.bounds.ne.lat, nelng:$scope.bounds.ne.lng},
                    function(data) {
                        if(data.status == "OK") {
//                            $scope.photos = data.photos;
                            if(data.photos.length < $scope.photo.pageSize) {
                                $scope.loadMoreDisabled = true;
                            }
                            if($scope.photo.currentPage === 1) {
                                $scope.photos = jsUtils.Array.mergeRightist($scope.photos || [], data.photos, "id");
                            }else {
                                $scope.photos = jsUtils.Array.append($scope.photos || [], data.photos, "id");
                            }
                            $scope.photo.currentPage++;
                            $scope.$broadcast("ponmPhotoFluidResize", {});
                            $scope.loadBusy = false;
                        }
                    });


            }

            $($scope.panoramioLayer).bind("map_changed", function (e, bounds, level, size) {
//                $log.debug(e);
//                $log.debug(bounds);
                $log.debug(level);
//                $log.debug(size);
                $scope.bounds = bounds;
                $scope.level = level;
                $scope.size = size;
                $scope.photo.currentPage = 1;
                $scope.loadMoreDisabled = false;
                loadUserPhotos();
            });

            // 要在bind map_changed event 之后，才能有初始化图片的动作
            $scope.setPanormaioType('user', $scope.userId);

            $scope.loadMorePhotos = function() {
                loadUserPhotos();
            };

            $scope.$on("$destroy", function() {
                $($scope.panoramioLayer).unbind("map_changed");
                $scope.clearState();
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

            $scope.$on("$destroy", function() {
                $scope.clearState();
            });
        }])
    .controller('MapsPhotoUploadCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', 'TravelService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'AuthService',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, TravelService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, AuthService) {

            $scope.setPanormaioType('manual');

            AuthService.checkLogin().then(function(){

            }, function(){
                $state.go("login", {});
            });

            var mapEventListener = $scope.mapEventListener;
            var mapService = $scope.mapService;

            /* 此Ctrl中的所有file即photo object */

            var photos = {};
            $scope.$on("photoAdd", function(e, photo) {
                $log.debug(photo.id);
                photo.mapVendor = photo.mapVendor || {};
                photos[photo.id] = photos[photo.id] || photo;
                photo = photos[photo.id];

                if(photo.point) {
                    addOrUpdateMarker(photo, photo.point.lat, photo.point.lng);
                }

            });

            $scope.$on("photoDelete", function(e, photo) {
                $log.debug("photo delete: " + photo.id);
                photo = photos[photo.id];
                if(photo) {
                    if(photo.mapVendor.marker) {
                        mapEventListener.removeMarker(photo.mapVendor.marker);
                    }
                    delete photos[photo.id];
                }
            });

            $scope.$on("photoActive", function(e, photo) {
                $log.debug("active: " + photo.id);
                photo = photos[photo.id];
                $scope.activePhoto(photo);
            });

            if($scope.map) {
//                addMapClickEvent($scope.map);
            }
            $scope.$on("mapChanged", function(e, map) {
//                addMapClickEvent($scope.map);
            });

            function addOrUpdateMarker(photo, lat, lng) {
//                photo.mapVendor = photo.mapVendor || {};
                if (!photo.mapVendor.marker) {
                    createMarker(photo, lat, lng);
//                    mapEventListener.activeMarker(photo.mapVendor.marker);
//                    $scope.activePhoto(photo);
                }else {
                    mapEventListener.setPosition(photo.mapVendor.marker, lat, lng);
                    mapEventListener.setMap(photo.mapVendor.marker, $scope.map);
                }

                $scope.setPlace(photo, lat, lng);
            }

            function createMarker(file, lat, lng) {
                // create marker
//                file.mapVendor = file.mapVendor || {};
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
                    if($scope.file) {
                        addOrUpdateMarker($scope.file, lat, lng);
                        mapEventListener.activeMarker($scope.file.mapVendor.marker);
//                        $scope.setPlace($scope.file, lat, lng);
                    }
                });
            }

            /**
             * 激活图片
             *
             * @param file
             */
            $scope.activePhoto = function (file) {
                if($scope.file) {
                    $scope.file.active = false;
                    if ($scope.file.mapVendor) {
                        mapEventListener.deactiveMarker($scope.file.mapVendor.marker);
                    }
                    $scope.file = null;
                }
                if(!file) {
                    return;
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
//                        $scope.setPlace($scope.file, $scope.file.mapVendor.lat, $scope.file.mapVendor.lng);
                    } else {
//                        $scope.clearPlace();
                    }
                }

                $scope.$broadcast("photoReverseActive", file);
            };

            $scope.setPlace = function (file, lat, lng, address) { // 此参数为非gps坐标

                file.mapVendor.lat = lat;
                file.mapVendor.lng = lng;
                file.mapVendor.latPritty = cnmap.GPS.convert(lat);
                file.mapVendor.lngPritty = cnmap.GPS.convert(lng);

                if (address) {
                    file.mapVendor.address = address;
                }

                $log.debug("setPlace for photo: " + file.id);

                // 加载gps地点可选的地址列表
                mapService.getAddrPois(lat, lng).then(function(addresses, addr) {
                    if(!address) {
                        file.mapVendor.address = addr;
                    }
                    file.mapVendor.addresses = addresses;
                    $log.debug("getAddrPois for photo: " + file.id);
                    $scope.$broadcast("photoReverseAddress", file);
                });
            };

            // 当拖动放到地图上时
            $scope.$on("onDrop", function(e, drop) {
                $log.debug(drop.item.id);
                var photo;
                if(photos[drop.item.id]) {
                    photo = photos[drop.item.id];
                }else {
                    photo = {
                        id: drop.item.id,
                        mapVendor: {}
                    };
                    photos[photo.id] = photo;
                }
                var point = mapEventListener.pixelToPoint($scope.map, {x: (drop.ui.offset.left + drop.event.offsetX), y: (drop.event.clientY - 103)});
                addOrUpdateMarker(photo, point.lat, point.lng);
//                $scope.activePhoto(photo);
//                mapEventListener.activeMarker(photo.mapVendor.marker);
            });

            $scope.$on("$destroy", function() {
                angular.forEach(photos, function(photo, key) {
                    if(photo.mapVendor && photo.mapVendor.marker) {
                        mapEventListener.removeMarker(photo.mapVendor.marker);
                    }
                });
                mapEventListener.clearMap();
                $scope.clearState();
            });
        }])
;