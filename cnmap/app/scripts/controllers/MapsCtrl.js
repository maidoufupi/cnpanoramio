/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('mapsApp', [
    'ui.bootstrap',
    'ui.map',
    'ui.router',
    'ponmApp',
    'xeditable'
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
                    url: '/travel',
                    templateUrl: 'views/maps.popular.html',
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
                });
        }])
    .run(['editableOptions', function (editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('MapsCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'PhotoService', 'UserService', 'PanoramioService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        function ($window, $location, $rootScope, $scope, PhotoService, UserService, PanoramioService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;

            $scope.$state = $state;
            $scope.$location = $location;
            $scope.$stateParams = $stateParams;

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
                travel: $state.current.url == '/travel',
                camera: $state.current.url == '/camera',
                user: $state.current.url == '/user'
            };

            $rootScope.$on('$stateChangeSuccess',
                function (event, toState, toParams, fromState, fromParams) {
                    $scope.stateStatus = {
                        popular: toState.url == '/popular',
                        recent: toState.url == '/recent',
                        travel: toState.url == '/travel',
                        camera: toState.url == '/camera',
                        user: toState.url == '/user'
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
                if(!$scope.stateStatus) {
                    $scope.$apply(function (scope) {
                        $scope.photos = data;
                    });
                }
            });

            $scope.$watch('myMap', function (mapObj) {
                if (!$scope.map && mapObj) {
                    $scope.map = mapObj;
                    panoramioLayer.setMap(mapObj);
                    $scope.mapEventListener.addToolBar(mapObj);
                    $scope.mapService.init(mapObj);
                    locationHash(mapObj);
                }
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

                    if(stateObj.search) {
                        var url = $state.current.url;
                        url = url.replace("/", "");
                        $scope.search(stateObj.search, url);
                    }
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
                        }
                    });
            };

            $scope.setSearch = function(search) {
                panoramioLayer.setAuto(false);
                $scope.searchValObject = search;
                hashObj.search = search.val;
                updateState();
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
            }, {
                preconfig: true,
                type: "travel",
                typeDesc: "旅行"
            }, {
                preconfig: true,
                type: "camera",
                typeDesc: "相机"
            }];

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

            $scope.goLocation = function (address) {
                if(address.preconfig) {
                    $scope.setSearch(address);
                    $scope.search(address.val, address.type);
                }else {var location = address.location;
                    if (location) {
                        $scope.mapEventListener.setCenter($scope.map, location.lat, location.lng);
                        $scope.mapEventListener.setZoom($scope.map, address.zoom);
                    }
                }
            };

            $scope.onSelect = function($item, $model, $label) {
                $scope.setSearch($item);
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
//            if($scope.search) {
//                $scope.search($scope.search, 'popular');
//            }
        }])
    .controller('MapsRecentCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;

            $scope.setPanormaioType('recent');
//            if($scope.search) {
//                $scope.search($scope.search, 'recent');
//            }
        }])
    .controller('MapsTravelCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.selectable = false;

            $scope.setPanormaioType('travel');
//            if($scope.search) {
//                $scope.search($scope.search, 'travel');
//            }
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
        }])
;