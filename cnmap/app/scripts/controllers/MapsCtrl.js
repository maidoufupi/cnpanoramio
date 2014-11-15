/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('ponmApp.maps', [
//    'ngAnimate',
    'ui.bootstrap',
    'ui.map',
    'ui.router',
    'xeditable',
    'fileuploadApp',
    'ngDragDrop',
    'ui.bootstrap.datetimepicker'
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
                })
                .state('maps.dynamic', {
                    url: '/dynamic',
                    templateUrl: 'views/maps.dynamic.html',
                    resolve: {
                    },
                    controller: "MapsDynamicCtrl"
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
                'HashStateManager', 'alertService', 'matchmedia',
        function ($window, $location, $rootScope, $scope, $q, PhotoService, UserService, PanoramioService,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  HashStateManager, alertService, matchmedia) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.userId = ponmCtxConfig.userId;
            $scope.login = ponmCtxConfig.login;
            $scope.ponmCtxConfig = ponmCtxConfig;
            // 设置二级菜单显示的用户
            $scope.user = {
            };

            $scope.$watch(function() {
                return ponmCtxConfig.userId;
            }, function(userId) {
                $scope.userId = userId;
                $scope.login = ponmCtxConfig.login;
                if(!$scope.user.id && userId) {
                    $scope.setMenu("user", {id: userId, name: "我"})
                }
            });

            $scope.$state = $state;
            $scope.$location = $location;
            $scope.$stateParams = $stateParams;

            // copy a local message service
            $scope.alertService = angular.copy(alertService);
            $scope.alertService.options.alone = true;
            $scope.alertService.options.ttl = 1000;

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

            /**
             * map config
             *
             */
            $scope.mapOptions = {
                centerPoint: {
                    lat: 35,
                    lng: 116
                },
                zoom: 8,
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
            panoramioLayer.initEnv(ponmCtxConfig.ctx, $scope.staticCtx );
            jQuery(panoramioLayer).bind("data_changed", function (e, data) {
                if(!$scope.stateStatus.user) {
                    $scope.$apply(function (scope) {
                        $scope.photos = jsUtils.Array.mergeRightist($scope.photos || [], data, "id");
//                        $scope.photos = data;
                        $scope.$broadcast("ponm.photo.fluid.resize", {});
                    });
                }
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
                    if($scope.login && userid == $scope.userId) {
                        $scope.setMenu("user", {id: userid, name: "我"});
                    }else {
                        $scope.setMenu("user", {id: userid, name: ""});
                        UserService.getOpenInfo({userId: userid}, function(res) {
                            if(res.status == "OK") {
                                res.open_info && $scope.setMenu("user", res.open_info);
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

            /**
             * layout manager
              */
            $scope.layoutManager = {
                index: 0,
                layouts: ['maps-right-fixed', 'maps-left-fixed', 'maps-right-full', 'maps-left-full'],
                rightFixed: 0,
                leftFixed: 1,
                rightFull: 2,
                leftFull: 3,
                getLayout: function() {
                    return this.layouts[this.index];
                }
                ,setLayout: function(index) {
                    this.index = index
                }
                ,small: function() {
                    switch(this.index) {
                        case 0:
                            this.index = 1;
                            break;
                        case 1:
                            this.index = 2;
                            break;
                        case 2:
                            break;
                        case 3:
                            this.index = 0;
                            break;
                        default:
                            this.index = 0;
                    }
                }
                ,largen: function() {
                    switch(this.index) {
                        case 0:
                            this.index = 3;
                            break;
                        case 1:
                            this.index = 0;
                            break;
                        case 2:
                            this.index = 1
                            break;
                        case 3:
                            break;
                        default:
                            this.index = 0;
                    }
                }
                ,full: function() {
                    this.index = this.leftFull;
                }
            };

            $scope.$on('$viewContentLoaded',
                function(event){
                    var unRegisterOnPhone = matchmedia.onPhone( function(mediaQueryList){

                        if(mediaQueryList.matches) {
                            if($scope.layoutManager.index === $scope.layoutManager.leftFixed) {
                                $scope.layoutManager.setLayout($scope.layoutManager.leftFull);
                            }else if($scope.layoutManager.index === $scope.layoutManager.rightFixed) {
                                $scope.layoutManager.setLayout($scope.layoutManager.rightFull);
                            }
                        }

                    }, $scope);
                });

            $scope.navbar =[
                {
                    name: "popular",
                    link: "#/maps/popular",
                    text: "受欢迎",
                    more: false
                }
                ,{
                    name: "recent",
                    link: "#/maps/recent",
                    text: "最新",
                    more: false
                }
                ,{
                    name: "upload",
                    link: "#/maps/upload",
                    text: "上传",
                    more: false
                }
                ,{
                    name: "user",
                    link: "#/maps/user/",
                    text: "我的照片",
                    more: true
                }
                ,{
                    name: "dynamic",
                    link: "#/maps/dynamic",
                    text: "好友动态",
                    more: true
                }
                ,{
                    name: "travels",
                    link: "#/maps/travels",
                    text: "热门旅行",
                    more: true
                }
                ,{
                    name: "travel",
                    link: "#/maps/travel",
                    text: "旅行",
                    more: true,
                    hidden: true
                }
                ,{
                    name: "news",
                    link: "#/maps/news",
                    text: "热门新闻",
                    more: true
                }
            ];

            $scope.setMenu = function(type, object) {
                if(type == "user") {
                    var user = object;
                    $scope.user.id = user.id;
                    $scope.user.name = user.name;
                    angular.forEach($scope.navbar, function(nav, key) {
                        if(nav.name == "user") {
                            nav.link = "#/maps/user/"+user.id,
                                nav.text = user.name+"的照片";
                        }
                    });
                }else if(type == "travel") {
                    angular.forEach($scope.navbar, function(nav, key) {
                        if(nav.name == "travel") {
                            nav.link = "#/maps/travel/"+object.id,
                            nav.text = object.name;
                        }
                    });
                }

            };
            if(ponmCtxConfig.userId) {
                $scope.setMenu("user", {id: ponmCtxConfig.userId, name: "我"});
            }

            /**
             * state status manager
             *
             * @param toState
             */
            function setStateStatus(toState) {
                var stateName = toState.name;
                stateName = stateName.replace("maps.", "");
                $scope.stateStatus = {};
                $scope.stateStatus[stateName] = true;

              if(matchmedia.isPhone()) {
                if($scope.stateStatus.dynamic || $scope.stateStatus.upload) {
                  $scope.layoutManager.setLayout($scope.layoutManager.rightFull);
                  $scope.setPanormaioType('manual');
                }else {
                  $scope.layoutManager.setLayout($scope.layoutManager.leftFull);
                }

              }else {
                if($scope.stateStatus.dynamic || $scope.stateStatus.upload) {
                  $scope.layoutManager.setLayout($scope.layoutManager.leftFixed);
                  $scope.setPanormaioType('manual');
                }else {
                  $scope.layoutManager.setLayout($scope.layoutManager.rightFixed);
                }

              }

                angular.forEach($scope.navbar, function(nav, key) {
                    nav.active = false;
                    if(nav.name == stateName) {
                        nav.active = true;
                    }
                });

                $scope.$broadcast("dynamic.menu");
            }
            setStateStatus($state.current);
            $rootScope.$on('$stateChangeSuccess',
                function (event, toState, toParams, fromState, fromParams) {
                    setStateStatus(toState);
                }
            );

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
                if(!$scope.hashStateManager.get("lat")) {
                    getCurrentPostion(mapObj);
                }
            });

            function getCurrentPostion(map) {
                // Try HTML5 geolocation
                if(navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function(position) {
                        $scope.mapEventListener.setCenter(map, position.coords.latitude,
                            position.coords.longitude);
                    }, function(e) {
                        $log.error('Error: Your browser doesn\'t support geolocation.');
                        $log.debug(e);
//                        handleNoGeolocation(true);
                    });
                } else {
                    // Browser doesn't support Geolocation
                    $log.error('Error: Your browser doesn\'t support geolocation.');
//                    handleNoGeolocation(false);
                }
            }

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

            $scope.$on('photo.click', function(e, photoId) {
                $scope.displayPhoto(photoId);
            });

            // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
            // 高德地图有setCenter后取getCenter不完全一致问题
            var changeState = false,
                listeners = [];

            function locationHash(map) {

                listeners = $scope.mapEventListener.addLocationHashListener(map,
                    function(lat ,lng, zoom) {
//                        $log.debug("locationHashListener");

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

                    // 由hashstate导致地图状态变化时 重新搜索
                    if($scope.searchVal) {
                        $timeout(function () {
                            $scope.$broadcast("searchChanged", $scope.searchVal);
                        }, 500);
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

            $scope.hashStateManager.watch("search", function(search) {
//                $scope.setSearch(search, true);
                if(search) {
                    panoramioLayer.clearMap();
                    panoramioLayer.setAuto(false);
//                    $scope.setPanormaioType("manual");
                }else {
                    panoramioLayer.clearMap();
                    panoramioLayer.setAuto(true);
//                    $scope.setPanormaioType("auto");
                }
                $scope.searchVal = search;
                $scope.$broadcast("searchChanged", search);
            });

            $scope.setSearch = function(search, state) {
                if(search) {
                    $scope.setPanormaioType("manual");
                }else {
                    $scope.setPanormaioType("auto");
                }
                $scope.searchVal = search;
                if(!state) {
                    $scope.hashStateManager.set("search", search);
                }

                $scope.$broadcast("searchChanged", search);
            };

            $scope.clearSearch = function() {
                panoramioLayer.setAuto(true);
                $scope.searchVal = "";
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

            /**
             * on photo drop on the map
             * @type {Array}
             */
            $scope.markers = [];
            $scope.onPhotoDrop = function(e, ui) {
                if($scope.markers[$scope.markers.length-1]) {
                    $scope.$broadcast("onDrop", {event: e, ui: ui, id: $scope.markers[$scope.markers.length-1]});
                }
            };

            $scope.onPhotoOver = function(e, ui) {

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
                        search.zoom && $scope.mapEventListener.setZoom($scope.map, search.zoom);
                        search.bounds && $scope.mapEventListener.setBounds($scope.map, search.bounds.sw, search.bounds.ne);
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
    [        '$window', '$scope', 'TravelService', 'UserService', '$modal', 'MessageService',
        'ponmCtxConfig', '$log', '$state', '$stateParams', '$q', 'jsUtils', '$timeout',
        function ($window, $scope, TravelService, UserService, $modal, MessageService,
                  ponmCtxConfig, $log, $state, $stateParams, $q, jsUtils, $timeout) {

            $scope.clearSearch();
            $scope.setPanormaioType('manual');

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.setMenu("travel", {id: travelId, name: res.travel.title});
                        $scope.setTravel(res.travel);

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.travel.user_id}, function (data) {
                            if (data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });

                        if(res.travel.message_id) {
                            MessageService.get({id: res.travel.message_id}, function(res) {
                                if(res.status == "OK") {
                                    $scope.message = res.message;
                                }
                            });
                        }

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
                    // 新线程中做地图图形绘制
                    $timeout(function() {
                        $scope.travelLayer.clearMap();
                        $scope.travelLayer.setEditable($scope.travelEnedit);
                        $scope.travelLayer.setTravel($scope.travel);
                        $scope.travelLayer.initMap();
                        $scope.activeSpot($scope.travel.spots[0]);
                    }, 1000);
                }
            };

            $scope.updateTravel = function(travel, propName, $data) {
                var d = $q.defer();
                var t = {
                    id: travel.id
                };
                t[propName] = $data;

                TravelService.changeTravel({travelId: travel.id}, t,
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

            $scope.$on("travelChanged", function(e, travelId) {
                getTravel(travelId);
            });

            $scope.$on("mapChanged", function(e) {
                if(!$scope.travelLayer) {
                    initTravelLayer($scope.map)
                }
            });

            $scope.$on("editableViewChanged", function(e, editableView) {
                // 设置此travel是否可以被登录者编辑
                if($scope.travel) {
                    $scope.travelEnedit = ($scope.userId == $scope.travel.user_id) && editableView;
                    $scope.travelLayer && $scope.travelLayer.setEditable($scope.travelEnedit);
                }
            });

            $scope.$on('photoDeleteEvent', function(e, photoId) {
                $log.debug("photo delete event: photoId = " + photoId);
                e.preventDefault();
                e.stopPropagation();

            });

            function initTravelLayer(map) {
                $scope.travelLayer = new $window.cnmap.TravelLayer({
                    ctx: $scope.ctx,
                    staticCtx: $scope.staticCtx,
//                    travel: $scope.travel,
                    clickable: true,
                    editable: !!$scope.travelEnedit
                });

                jQuery($scope.travelLayer).bind("data_clicked", function (e, photoId) {
                    $scope.displayPhoto(photoId);
                });

                $scope.travelLayer.setMap(map);
                if($scope.travel) {
                    $scope.travelLayer.setTravel($scope.travel);
                    $scope.travelLayer.initMap();
                    if($scope.travel.spots[0]) {
                        $scope.activeSpot($scope.travel.spots[0]);
                    }
                }

                jQuery($scope.travelLayer).bind("spot.edited", function (e, spotId) {
                    $scope.$broadcast("spot.edited."+spotId);
                });
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

            /**
             * 激活spot在地图上显示
             *
             * @param spot
             */
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

            /**
             * 将photo移动到spot
             *
             * @param photo
             * @param spot
             */
            $scope.addSpotPhoto = function(photo, spot) {
                var photos = {photos: photo.id};
                TravelService.addSpotPhoto({travelId: spot.travel_id, typeId: spot.id},
                    jsUtils.param(photos), function(res) {
                        if(res.status == "OK") {
                            $scope.travelLayer.removePhoto(photo);
                            $scope.travelLayer.addPhoto(spot, photo);
                            $scope.$broadcast('ponm.photo.fluid.resize');
                            $scope.alertService.add("success", "更新成功");
                        }
                    });
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

            $scope.$on("ponm.photo.fluid.resized", function() {
                $scope.$broadcast("waypoint-refresh");
            });

            $scope.$on("$destroy", function() {
                if($scope.travelLayer) {
                    $scope.travelLayer.clearMap();
                }
                $scope.clearState();
            });
        }])
    .controller('MapsTravelSpotCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q', 'TravelService', 'UserService', 'PhotoService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', '$filter', 'jsUtils',
        function ($window, $location, $rootScope, $scope, $q, TravelService, UserService, PhotoService, $modal,
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

            /**
             * 更新景点信息
             *
             * @param spot
             * @param type
             * @param $data
             * @returns {*}
             */
            $scope.updateSpot = function(spot, type, $data) {
                var d = $q.defer();
                var travelSpot = {
                    id: spot.id,
                    title: spot.title,
                    description: spot.description,
                    address: spot.address
                };
                travelSpot[type] = $data;
                TravelService.changeSpot({travelId: spot.travel_id, typeId: spot.id},
                    travelSpot,
                    function(res) {
                        res = res || {};
                        if(res.status === 'OK') { // {status: "OK"}
                            $scope.alertService.add("success", "更新成功");
                            d.resolve()
                        } else { // {status: "error", msg: "Username should be `awesome`!"}
                            $scope.alertService.add("danger", "更新失败 " + res.info, {ttl: 2000});
                            d.resolve(res.info);
                        }
                    }, function(error) {
                        if(error.data) {
                            $scope.alertService.add("danger", "更新失败 " + error.data.info, {ttl: 2000});
                            d.reject(error.data.info);
                        }else {
                            $scope.alertService.add("danger", "更新失败", {ttl: 2000});
                            d.reject('Server error!');
                        }
                    });
                return d.promise;
            };

            /**
             * 设为相册封面
             *
             * @param photo
             */
            $scope.setAlbumCover = function(photo) {
                $scope.updateTravel($scope.travel, "album_cover", {id: photo.id})
                    .then(function() {
                        $scope.alertService.add("success", "更新成功");
                    }, function(error) {
                        $scope.alertService.add("danger", "更新失败 " + error, {ttl: 3000});
                    });
            };

            /**
             * 把这张图片创建新的景点
             *
             * @param photo
             */
            $scope.createSpot = function(photo) {
                TravelService.createSpot({travelId: $scope.spot.travel_id}, jsUtils.param({}), function(res) {
                    if(res.status == "OK") {
                        $scope.travelLayer.addSpot(res.spot);
                        $scope.addSpotPhoto(photo, res.spot);
                        $scope.alertService.add("success", "创建成功");
                        $scope.$broadcast('ponm.photo.fluid.resize');
                    }
                });
            };

            /**
             * 从相册中删除图片
             *
             * @param photo
             */
            $scope.removePhoto = function(photo) {
                $scope.$emit('photoDeleteEvent', photo.id);
                // remove travel photo on server
                TravelService.deletePhoto({travelId: $scope.spot.travel_id, typeId: photo.id}, function(res) {
                    if(res.status == "OK") {
                        $scope.travelLayer.removePhoto(photo);
                        $scope.alertService.add("success", "删除成功");
                        $scope.$broadcast('ponm.photo.fluid.resize');
                    }
                });
            };

            /**
             * 删除景点
             *
             * @param spot
             */
            $scope.deleteSpot = function(spot) {
                TravelService.deleteSpot({travelId: spot.travel_id, typeId: spot.id}, function(res) {
                    if(res.status == "OK") {
                        $scope.travelLayer.removeSpot(spot);
                        $scope.alertService.add("success", "删除成功");
                        $scope.$emit("ponm.photo.fluid.resized");
                    }
                });
            };

            $scope.saveSpotPhotoPoint = function(spot) {
                var deferred = $q.defer();
                var spotPosition = {
                    id: spot.id,
                    photos: []
                };
                angular.forEach(spot.photos, function(photo, key) {
                    var p = {id: photo.id};
                    // 如果旧值存在，则说明有改动； 成功后删除旧值
                    if(photo.oPoint) {
                        p.point = angular.copy(photo.point);
                        p.vendor = ponmCtxConfig.getCoordSS();
                    }
                    if(photo.oDate_time) {
                        p.date_time = photo.date_time;
                    }
                    spotPosition.photos.push(p);
                });
                TravelService.changeSpot({travelId: spot.travel_id, typeId: spot.id},
                    spotPosition, function(res) {
                        if(res.status == "OK") {
                            // 保存后删除备份的point， 没有oPoint则说明本地与服务器point属性一致
                            angular.forEach(spot.photos, function(photo, key) {
                                delete photo.oPoint;
                                delete photo.oDate_time;
                            });
                            deferred.resolve();
                        }else {
                            deferred.reject();
                        }
                    },function(error) {
                        deferred.reject();

                    });
                return deferred.promise;
            };

            $scope.editAndSave = function(spotLine) {
                if(spotLine.edit) {
                    if(spotLine.save) {
                        $scope.saveSpotPhotoPoint($scope.spot).then(function() {

                            $scope.alertService.add("success", "更新成功");
                            spotLine.save = false;
                        }, function() {
                            $scope.alertService.add("danger", "更新失败", {ttl: 3000});
                        });

                    }else {
                        spotLine.edit = false;
                        spotLine.save = false;
                    }
                }else {
                    spotLine.edit = true;
                    spotLine.save = false;
                }
                if($scope.travelLayer) {
                    $scope.travelLayer.setSpotEditable($scope.spot, spotLine.edit);
                }
            };

            $scope.cancelEdit = function(spotLine) {
                angular.forEach($scope.spot.photos, function(photo, key) {
                    if(photo.oDate_time) {
                        photo.date_time = photo.oDate_time;
                    }
                    delete photo.oDate_time;
                });
                if($scope.travelLayer) {
                    // 图片重新排序
                    $scope.travelLayer.calcSpotTime();
                    // 线路重新绘制
                    $scope.travelLayer.updateSpotLine($scope.spot);
                    $scope.travelLayer.cancelSpotEdit($scope.spot);
                    // 图片布局重新计算
                    $scope.$emit('ponm.photo.fluid.resize');
                }
                spotLine.save = false;
            };

            $scope.$on("spot.edited."+$scope.spot.id, function(e) {
                $scope.spotLine.save = true;
            });

            // 默认显示旅行的线
            $scope.spotLine = {disp: true};
            $scope.$watch("spotLine.disp", function(displayLine) {
                if($scope.travelLayer) {
                    $scope.travelLayer.toggleSpotLine($scope.spot, !!displayLine);
                }
            });
        }])
    .controller('MapsTravelSpotPhotoCtrl',
    [        '$scope', '$q',
        function($scope, $q) {

            $scope.setPhotoDateTime = function(nDateTime, oDateTime) {
                if(oDateTime == nDateTime) {
                    return;
                }
                // 设置新时间
                $scope.photo.oDate_time = oDateTime;
                $scope.photo.date_time = nDateTime;
                // 图片重新排序
                $scope.travelLayer.calcSpotTime();
                // 线路重新绘制
                $scope.travelLayer.updateSpotLine($scope.spot);
                // 图片布局重新计算
                $scope.$emit('ponm.photo.fluid.resize');
                //
                $scope.spotLine.edit = true;
                $scope.spotLine.save = true;
                if($scope.travelLayer) {
                    $scope.travelLayer.setSpotEditable($scope.spot, true);
                }
            };

        }])
    .controller('MapsUserCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, jsUtils) {

            $scope.selectable = false;

            var userId = $stateParams.userId;

            // 获取图片的用户信息
            UserService.getOpenInfo({userId: userId}, function (data) {
                if (data.status == "OK") {
                    $scope.user = data.open_info;
                }
            });

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
                UserPhoto.get({userId: userId,
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
                            $scope.$broadcast("ponm.photo.fluid.resize", {});
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
            $scope.setPanormaioType('user', userId);

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

            AuthService.checkLogin().then(function(){

            }, function(){
                $state.go("login", {});
            });

            var mapEventListener = $scope.mapEventListener;
            var mapService = $scope.mapService;

            /* 此Ctrl中的所有file即photo object */

            var photos = {};
            $scope.$on("photoAdd", function(e, photo) {
                $log.debug("add marker of photo : ");
                $log.debug(photo);
                photo.mapVendor = photo.mapVendor || {};
                photos[photo.uuid] = photos[photo.uuid] || photo;
                photo = photos[photo.uuid];

                if(photo.point) {
                    addOrUpdateMarker(photo, photo.point.lat, photo.point.lng);
                }

            });

            $scope.$on("photoDelete", function(e, photo) {
                $log.debug("photo delete: " + photo.uuid);
                photo = photos[photo.uuid];
                if(photo) {
                    if(photo.mapVendor.marker) {
                        mapEventListener.removeMarker(photo.mapVendor.marker);
                    }
                    delete photos[photo.uuid];
                }
            });

            $scope.$on("photoActive", function(e, photo) {
                $log.debug("active: " + photo.uuid);
                photo = photos[photo.uuid];
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
//                    $scope.activePhoto(this.photo_file);
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
//                file.mapVendor.latPritty = cnmap.GPS.convert(lat);
//                file.mapVendor.lngPritty = cnmap.GPS.convert(lng);

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
                $log.debug("on drop left = " );
                $log.debug(drop.event);
                var photo;
                if(photos[drop.id]) {
                    photo = photos[drop.id];
                }else {
                    photo = {
                        id: drop.id,
                        uuid: drop.id,
                        mapVendor: {}
                    };
                    photos[photo.uuid] = photo;
                }
                var point = mapEventListener.pixelToPoint($scope.map, {x: (drop.event.pageX ), y: (drop.event.clientY - 102)});
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
    .controller("MapsDynamicCtrl", ['$scope', '$log', '$state', '$document', 'MessageManager', 'ponmCtxConfig',
    function($scope, $log, $state, $document, MessageManager, ponmCtxConfig) {

        $scope.$on("user.login", function(e) {
            if(!$scope.userId || ($scope.userId != ponmCtxConfig.userId)) {
                $scope.userId = userId;
                $scope.messageManager = new MessageManager($scope.userId);
                $scope.onLoadWaypoint();
            }
        });

        $scope.onLoadWaypoint = function(element, direction) {

            if($scope.messageManager) {
                $scope.messageManager.getMoreMessages().then(function(messages) {
                    $scope.$broadcast("message.add", messages);
                });

            }

        };

        if($scope.userId) {
            $scope.messageManager = new MessageManager($scope.userId);
            $scope.onLoadWaypoint();
        }

        $scope.$on("message.actived", function(e, message) {
            if(message.point) {
                $scope.$broadcast('message.point.click', message);
            }
        });

        var messagePoints = [],
            currentPoint;
        $scope.$on('message.point.click', function(e, message) {

            var messagePoint = messagePoints[message.id],
                point = message.point;
            if(!messagePoint) {
                messagePoint = {id: message.id};
                messagePoint.marker = $scope.mapEventListener.addMarker($scope.map, point.lat, point.lng);
                messagePoint.marker.message = message;
                messagePoints[message.id] = messagePoint;

                $scope.mapEventListener.addMarkerActiveListener(messagePoint.marker, function() {
                    $scope.$broadcast("message.active", this.message);
                });
            }
            $scope.mapEventListener.setCenter($scope.map, point.lat, point.lng);
            if(currentPoint == message.id) {
                $scope.mapEventListener.zoomIn($scope.map);
            }else {
                currentPoint = message.id;
            }
        });

        $scope.$on("message.active", function(e, message) {
            // scroll to photo's element when photo is clicked
            var messageCard = angular.element(document.getElementById('message-' + message.id));
            $document.scrollToElementAnimated(messageCard, 120);
        });

        $scope.$on("$destroy", function() {
            angular.forEach(messagePoints, function(messagePoint, key) {
                if(messagePoint.marker) {
                    $scope.mapEventListener.removeMarker(messagePoint.marker);
                }
            });
            $scope.mapEventListener.clearMap();
            $scope.clearState();
        });

    }])
;
