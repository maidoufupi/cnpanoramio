/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('photosApp', [
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
                .when('/', '/yourphotos/all')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
                .otherwise('/yourphotos/all');
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////

                .state("home", {

                    // Use a url of "/" to set a states as the "index".
                    url: "/",

                    // Example of an inline template string. By default, templates
                    // will populate the ui-view within the parent state's template.
                    // For top level states, like this one, the parent template is
                    // the index.html file. So this template will be inserted into the
                    // ui-view within index.html.
                    template: '<p class="lead">Welcome to the UI-Router Demo</p>' +
                        '<p>Use the menu above to navigate. ' +
                        'Pay attention to the <code>$state</code> and <code>$stateParams</code> values below.</p>' +
                        '<p>Click these links—<a href="#/c?id=1">Alice</a> or ' +
                        '<a href="#/user/42">Bob</a>—to see a url redirect in action.</p>'

                })
                .state('photos', {

                    // With abstract set to true, that means this state can not be explicitly activated.
                    // It can only be implicitly activated by activating one of it's children.
                    abstract: true,

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/:userId',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
                    templateUrl: 'views/photos.html',

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    },

                    // You can pair a controller to your template. There *must* be a template to pair with.
                    controller: "PhotosCtrl"
                })
                .state('photos.all', {

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/all',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
                    templateUrl: 'views/photos.all.html',

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    },

                    // You can pair a controller to your template. There *must* be a template to pair with.
                    controller: "PhotosAllCtrl"
                })
                .state('photos.albums', {

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/albums',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
                    templateUrl: 'views/photos.albums.html',

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    },

                    // You can pair a controller to your template. There *must* be a template to pair with.
                    controller: "PhotosAlbumsCtrl"
                })
                .state('photos.album', {

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/albums/:travelId',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
                    templateUrl: 'views/photos.album.html',

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    },

                    // You can pair a controller to your template. There *must* be a template to pair with.
                    controller: "PhotosAlbumCtrl"
                })

        }])
    .run(['editableOptions', function (editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('PhotosCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'safeApply',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, safeApply) {

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

            $scope.userId = $state.params.userId;
            if($scope.userId == "yourphotos") {
                $scope.userId = $window.userId;
            }

            $scope.cancelSelect = function() {
                $scope.selectable = false;
                angular.forEach($scope.selectedPhotos, function(value, key) {
                    value.actionSelected = false;
                });
                $scope.selectedPhotos = [];
                $scope.$broadcast("photosCancelSelect", {});
            };

            $scope.selectedPhotos = [];
            $scope.selectPhoto = function(e, photo, photos) {
                e && e.preventDefault();
                e && e.stopPropagation();
                photo.actionSelected=!photo.actionSelected;
                if(photo.actionSelected) {
                    $scope.selectable = true;
                    $scope.selectedPhotos.push(photo);
                }else {
                    angular.forEach($scope.selectedPhotos, function(value, key) {
                        if(value.id == photo.id) {
                            delete $scope.selectedPhotos.splice(key, 1);
                        }
                    });
                    if($scope.selectedPhotos.length == 0) {
                        $scope.selectable = false;
                    }
                }
            };

            $scope.setWaypointRefresh = function(waypointRefresh) {
                $scope.waypointRefresh = waypointRefresh;
                $scope.photoLoading = waypointRefresh;
            };

            $scope.openMovePhoto = function () {

                var modalInstance = $modal.open({
                    templateUrl: 'views/photos.move.html',
                    controller: 'PhotosMoveCtrl',
                    windowClass: 'map-photo-modal',
                    resolve: {
                        photos: function () {
                            return $scope.selectedPhotos;
                        },
                        operateType: "move"
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $scope.selected = selectedItem;
//                    $scope.setPhotoId($scope.photoId);
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
//                    $scope.setPhotoId($scope.photoId);
                });
            };
        }])
    .controller('PhotosAllCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;

            // 用户图片分页属性
            $scope.photo = {
                pageSize: 10,
                totalItems: 0,
                numPages: 2,
                currentPage: 1,
                maxSize: 10
            };

            getOpenInfo();

            function getOpenInfo() {
                UserService.getOpenInfo({'userId': $scope.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.userOpenInfo = data.open_info;
                        $scope.editable = ($scope.userOpenInfo.id == $scope.userId);
                        $scope.photo.numPages = Math.ceil($scope.userOpenInfo.photo_count/$scope.photo.pageSize);
                    }
                });
            }

            $scope.photos = [];
            function getUserPhotos() {
                if($scope.photo.currentPage > $scope.photo.numPages) {
                    return;
                }

                $scope.setWaypointRefresh(true);
                UserPhoto.get({userId: $scope.userId, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage},
                    function(data) {
                        $scope.setWaypointRefresh(false);
                        if(data.status == "OK") {
                            $scope.photo.currentPage += 1;
                            $scope.photos = $scope.photos.concat(data.photos);
                        }
                    },function(error){$scope.photoLoading = false;});
            }

            getUserPhotos();

            $scope.$on('waypointEvent', function (e, direction, elmId) {
                $log.debug("waypointSpot: " + direction + " = " + elmId);
                if (elmId == "loadMore") {
                    e.preventDefault();
                    e.stopPropagation();
                    if (direction == "down") {
                        getUserPhotos();
                    } else {
//                        getUserPhotos();
                    }
                }
            });
        }])
    .controller('PhotosAlbumsCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;

            $scope.$state = $state;
            $scope.$location = $location;
            $scope.$stateParams = $stateParams;

            UserService.getTravels({userId: $scope.userId}, function (res) {
                if (res.status == "OK") {
                    $scope.travels = res.open_info.travels;
                }
            });
        }])
    .controller('PhotosAlbumCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;

            $scope.$state = $state;
            $scope.$location = $location;
            $scope.$stateParams = $stateParams;

            $scope.travelId = $stateParams.travelId;

            getTravel($scope.travelId);

            function getTravel(travelId) {
                if(!travelId) {
                    return;
                }
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.travel = res.travel;
//                        $scope.setTravel(res.travel);

                        // 获取图片的用户信息
                        UserService.getOpenInfo({'userId': $scope.travel.user_id}, function (data) {
                            if (data.status == "OK") {
                                $scope.userOpenInfo = data.open_info;
                            }
                        });
                    }
                });
            }

        }])
    .controller('PhotosPhotoCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams',
        function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams) {

            $scope.ctx = $window.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = $window.apirest;

            $scope.$watch('rectSelected', function(value) {
                if((value && !$scope.photo.actionSelected) || (!value && $scope.photo.actionSelected)) {
                    $scope.selectPhoto(null, $scope.photo);
                }
            });

            $scope.$on("photosCancelSelect", function() {
                $scope.rectSelected = false;
            });

        }])
    .controller('PhotosMoveCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modalInstance',
             'ponmCtxConfig', '$log', 'operateType', 'photos',
    function ($window,   $location,   $rootScope,   $scope,   TravelService,   UserService,   $modalInstance,
              ponmCtxConfig,   $log,   operateType,   photos) {

        $scope.ctx = $window.ctx;
        $scope.staticCtx = ponmCtxConfig.staticCtx;
        $scope.apirest = $window.apirest;

        $scope.photos = photos;

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        UserService.getTravels({userId: ponmCtxConfig.userId}, function (res) {
            if (res.status == "OK") {
                $scope.travels = res.open_info.travels;
            }
        });

        $scope.newTravel = {};
        $scope.selectedTravel = {};
        $scope.select = function(travel, selected) {
            if(selected != undefined) {
                travel.selected = selected;
            }else {
                travel.selected = !travel.selected;
            }

            if($scope.selectedTravel != travel) {
                $scope.selectedTravel.selected = false;
                $scope.selectedTravel = travel;
            }
        };
        $scope.selectNewTravel = function(name) {
            if(name) {
                $scope.select($scope.newTravel, true);
            }else {
                $scope.select($scope.newTravel, false);
            }
        };

        // 显示文本
        if(operateType == "move") {
            $scope.displayText = {
                ok: "移动",
                title1: "将",
                title2: "张图片移动到..."
            };
        }else {
            $scope.displayText = {
                ok: "移动",
                title1: "将",
                title2: "张图片到..."
            };
        }
    }])
;