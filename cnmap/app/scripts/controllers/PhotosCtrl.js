/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('ponmApp.photos', [
  'ui.bootstrap',
  'ui.map',
  'ui.router',
  'ponmApp',
  'xeditable',
  'duScroll'
])
//angular.module('photosApp')
  .config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {

      // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
      $urlRouterProvider

        // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
        // Here we are just setting up some convenience urls.
        .when('/photos/:userId', '/photos/:userId/all')

        // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
//                .otherwise('/yourphotos/all')
      ;

      //////////////////////////
      // State Configurations //
      //////////////////////////

      // Use $stateProvider to configure your states.
      $stateProvider

        //////////
        // Home //
        //////////

//                .state("home", {
//
//                    // Use a url of "/" to set a states as the "index".
//                    url: "/",
//
//                    // Example of an inline template string. By default, templates
//                    // will populate the ui-view within the parent state's template.
//                    // For top level states, like this one, the parent template is
//                    // the index.html file. So this template will be inserted into the
//                    // ui-view within index.html.
//                    template: '<p class="lead">Welcome to the UI-Router Demo</p>' +
//                        '<p>Use the menu above to navigate. ' +
//                        'Pay attention to the <code>$state</code> and <code>$stateParams</code> values below.</p>' +
//                        '<p>Click these links—<a href="#/c?id=1">Alice</a> or ' +
//                        '<a href="#/user/42">Bob</a>—to see a url redirect in action.</p>'
//
//                })
        .state('photos', {

          // With abstract set to true, that means this state can not be explicitly activated.
          // It can only be implicitly activated by activating one of it's children.
          abstract: true,

          // This abstract state will prepend '/contacts' onto the urls of all its children.
          url: '/photos/:userId',

          // Example of loading a template from a file. This is also a top level state,
          // so this template file will be loaded and then inserted into the ui-view
          // within index.html.
//                    templateUrl: 'views/photos.html',

          views: {

            // the main template will be placed here (relatively named)
            '': {
              templateUrl: 'views/photos.html',
              controller: 'PhotosCtrl'
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
          resolve: {},

          // You can pair a controller to your template. There *must* be a template to pair with.
          controller: "PhotosCtrl"
        })
        .state('photos.all', {
          url: '/all',
          templateUrl: 'views/photos.all.html',
          resolve: {},
          controller: "PhotosAllCtrl"
        })
        .state('photos.recent', {
          url: '/recent',
          templateUrl: 'views/photos.all.html',
          resolve: {},
          controller: "PhotosRecentCtrl"
        })
        .state('photos.albums', {
          url: '/albums',
          templateUrl: 'views/photos.albums.html',
          resolve: {},
          controller: "PhotosAlbumsCtrl"
        })
        .state('photos.album', {
          url: '/albums/:travelId',
          templateUrl: 'views/photos.album.html',
          resolve: {},
          controller: "PhotosAlbumCtrl"
        })
        .state('photos.trash', {
          url: '/trash',
          templateUrl: 'views/photos.trash.html',
          resolve: {},
          controller: "PhotosTrashCtrl"
        })
      ;
    }])
  .run(['editableOptions', function (editableOptions) {
    editableOptions.theme = 'bs3';
  }])
  .controller('PhotosCtrl',
  ['$window', '$location', '$document', '$rootScope', '$scope', 'PhotoService', 'UserService', '$modal',
    'ponmCtxConfig', '$log', '$state', '$stateParams', 'safeApply', 'jsUtils', 'HashStateManager', 'alertService',
    function ($window, $location, $document, $rootScope, $scope, PhotoService, UserService, $modal,
              ponmCtxConfig, $log, $state, $stateParams, safeApply, jsUtils, HashStateManager, alertService) {

      $scope.ctx = $window.ctx;
      $scope.staticCtx = ponmCtxConfig.staticCtx;
      $scope.apirest = $window.apirest;

      $scope.$state = $state;
      $scope.$location = $location;
      $scope.$stateParams = $stateParams;

      $scope.navbarFixedTop = false;

      $scope.hashStateManager = new HashStateManager($scope, $location);

      // copy a local message service
      $scope.alertService = angular.copy(alertService);
      $scope.alertService.options.alone = true;
      $scope.alertService.options.ttl = 200000;

      $log.debug($state);

      $scope.userId = $state.params.userId;
      if ($scope.userId == "yourphotos") {
        $scope.userId = ponmCtxConfig.userId;
      }
      if (!$scope.userId || $scope.userId != ponmCtxConfig.userId) {
        $state.go("maps.popular", {});
      }

      // 获取图片的用户信息
      UserService.getOpenInfo({userId: $scope.userId}, function (res) {
        if (res.status == "OK") {
          $scope.userOpenInfo = res.open_info;
        }
      });

      $scope.cancelSelect = function () {
        $scope.$broadcast("photosCancelSelect", {});
      };

      $scope.$on("photosCancelSelect", function () {
        $scope.selectable = false;
        angular.forEach($scope.selectedPhotos, function (value, key) {
          value.actionSelected = false;
        });
        $scope.selectedPhotos = [];
      });

      $scope.navbarType = ($state.current.name == "photos.trash" ? "trash" : "move");
      $scope.alertType = ($state.current.name == "photos.trash" ? "trash" : "move");
      $rootScope.$on('$stateChangeSuccess',
        function (event, toState, toParams, fromState, fromParams) {
          $scope.cancelSelect();
          $scope.navbarType = (toState.name == "photos.trash" ? "trash" : "move");
          $scope.alertType = (toState.name == "photos.trash" ? "trash" : "move");
          $document.scrollTop(0, 500);
        }
      );

      $scope.selectedPhotos = [];
      $scope.selectPhoto = function (e, photo, photos) {
        e && e.preventDefault();
        e && e.stopPropagation();
        photo.actionSelected = !photo.actionSelected;
        if (photo.actionSelected) {
          $scope.selectable = true;
          $scope.selectedPhotos.push(photo);
        } else {
          jsUtils.Array.removeItem($scope.selectedPhotos, "id", photo.id);
          if ($scope.selectedPhotos.length == 0) {
            $scope.selectable = false;
          }
        }
      };

      $scope.displayPhoto = function (photoId, state) {
        if (!state) {
          $scope.hashStateManager.set("d", 'd');
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
            travelId: function () {
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

      // 当地址栏photoid变化时，显示相应图片
      $scope.hashStateManager.watch("photoid", function (photoId) {
        $scope.photoDisplayModal && $scope.photoDisplayModal.dismiss('cancel');
        if (photoId) {
          $scope.displayPhoto(photoId, true);
        }
      });

      $scope.openMovePhoto = function () {

        var modalInstance = $modal.open({
          templateUrl: 'views/photos.move.html',
          controller: 'PhotosMoveCtrl',
          windowClass: 'move-photo-modal',
          resolve: {
            photos: function () {
              return $scope.selectedPhotos;
            },
            operateType: "move"
          }
        });

        modalInstance.result.then(function (selectedItem) {
          $log.debug("move photos success");
          removeSelectedPhotos();
        }, function () {
          $log.debug("move photos fail");
          $log.info('Modal dismissed at: ' + new Date());
//                    $scope.setPhotoId($scope.photoId);
        });
      };

      $scope.deletePhoto = function () {
        angular.forEach($scope.selectedPhotos, function (photo, key) {
          PhotoService.delete({photoId: photo.id}, function (res) {
            if (res.status == "OK") {
              $scope.selectPhoto(null, photo);
              $scope.$broadcast("removePhotoDo", photo.id);
              $scope.alertService.add("success", "删除成功");
            }
          })
        });
      };

      function removeSelectedPhotos() {
        var photos = angular.copy($scope.selectedPhotos);
        angular.forEach(photos, function (photo, key) {
          $scope.selectPhoto(null, photo);
          $scope.$broadcast("removePhotoDo", photo.id);
          $scope.alertService.add("success", "移动成功");
        });
      }

      $scope.cancelRecycle = function () {
        $scope.$broadcast("cancelRecycleDo", {});
      };
      $scope.removeRecycle = function () {
        $scope.$broadcast("removeRecycleDo", {});
      };
      $scope.selectAll = function () {
        $scope.$broadcast("selectAllDo", {});
      };
      $scope.emptyRecycleBin = function () {
        $scope.$broadcast("emptyRecycleBinDo", {});
      };

      $scope.photoWallMode = 'medium';

      $scope.backtop = function() {
        $document.scrollTop(0, 500);
      };
    }])
  .controller('PhotosAllCtrl',
  ['$window', '$location', '$rootScope', '$scope', '$q', '$timeout', 'UserPhoto', 'UserService', '$modal',
    'ponmCtxConfig', '$log', '$state', '$stateParams', 'jsUtils', 'safeApply',
    function ($window, $location, $rootScope, $scope, $q, $timeout, UserPhoto, UserService, $modal,
              ponmCtxConfig, $log, $state, $stateParams, jsUtils, safeApply) {

      // 用户图片分页属性
      $scope.photo = {
        pageSize: 30,
        totalItems: 0,
        numPages: 2,
        currentPage: 1,
        maxSize: 10
      };

      getOpenInfo();

      function getOpenInfo() {
        UserService.getOpenInfo({'userId': $scope.userId}, function (data) {
          if (data.status == "OK") {
            $scope.userOpenInfo = data.open_info;
            $scope.editable = ($scope.userOpenInfo.id == $scope.userId);
            $scope.photo.numPages = Math.ceil($scope.userOpenInfo.photo_count / $scope.photo.pageSize);
          }
        });
      }

      $scope.photos = [];
      function getUserPhotos() {
        var d = $q.defer();

        if ($scope.photo.currentPage > $scope.photo.numPages) {
          d.resolve();
          return d.promise;
        }

        $log.debug("load more photos");
        $scope.photoLoading = true;
        UserPhoto.get({userId: $scope.userId, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage},
          function (data) {
            if (data.status == "OK") {
              $scope.photos = $scope.photos.concat(data.photos);
              $scope.photos.sort(function (a, b) {
                return b.create_time - a.create_time;
              });
            }
            d.resolve();
            $scope.photoLoading = false;
          }, function (error) {
            $scope.photoLoading = false;
          });
        $scope.photo.currentPage += 1;

        return d.promise;
      }

      $scope.getUserPhotos = getUserPhotos;
      getUserPhotos();

      // timeline widget for photos in wall, reached event
      $scope.$on('waypointEvent', function (e, dir, id) {
        if (id.date) {
          safeApply($scope, function () {
            $scope.timelineDate = id.date;
          });
        }
      });

      // when a photo be removed
      $scope.$on('removePhotoDo', function (e, photoId) {
        jsUtils.Array.removeItem($scope.photos, "id", photoId);
      });

    }])
  .controller('PhotosRecentCtrl',
  ['$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService', '$modal',
    'jsUtils', '$log', 'safeApply', '$q',
    function ($window, $location, $rootScope, $scope, UserPhoto, UserService, $modal,
              jsUtils, $log, safeApply, $q) {

      // 用户图片分页属性
      $scope.photo = {
        pageSize: 30,
        totalItems: 0,
        numPages: 4,
        currentPage: 1,
        maxSize: 10
      };

      $scope.photos = [];
      function getUserPhotos() {
        var d = $q.defer();

        if ($scope.photo.currentPage > $scope.photo.numPages) {
          d.resolve();
          return d.promise;
        }

        UserPhoto.get({userId: $scope.userId, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage},
          function (data) {
            if (data.status == "OK") {
              if (data.photos.length < $scope.photo.pageSize) {
                $scope.photo.numPages = $scope.photo.currentPage - 1;
              }
              $scope.photos = $scope.photos.concat(data.photos);
              $scope.photos.sort(function (a, b) {
                return b.create_time - a.create_time;
              });
            }
            d.resolve();
          }, function (error) {
            $scope.photoLoading = false;
          });
        $scope.photo.currentPage += 1;

        return d.promise;
      }

      $scope.getUserPhotos = getUserPhotos;
      getUserPhotos();

      // timeline widget for photos in wall, reached event
      $scope.$on('waypointEvent', function (e, dir, id) {
        if (id.date) {
          safeApply($scope, function () {
            $scope.timelineDate = id.date;
          });
        }
      });

      // when a photo be removed
      $scope.$on('removePhotoDo', function (e, photoId) {
        jsUtils.Array.removeItem($scope.photos, "id", photoId);
      });

    }])
  .controller('PhotosAlbumsCtrl',
  ['$rootScope', '$scope', 'UserService', 'ponmCtxConfig', '$log', 'TravelService',
    function ($rootScope, $scope, UserService, ponmCtxConfig, $log, TravelService) {

      $scope.travels = [];
      UserService.getTravels({userId: $scope.userId}, function (res) {
        if (res.status == "OK") {
          $scope.travels = $scope.travels.concat(res.open_info.travels);
        }
      });

      TravelService.get({travelId: 0}, function (res) {
        if (res.status == "OK" && res.travel.spots && res.travel.spots[0].photos.length) {
          res.travel.title = "无旅行的图片";
          $scope.travels.splice(0, 0, res.travel);
        }
      });
    }])
  .controller('PhotosAlbumCtrl',
  ['$scope', 'TravelService', 'UserService', 'dialogService',
    'ponmCtxConfig', '$log', '$state', '$stateParams', 'TravelManager', 'jsUtils',
    function ($scope, TravelService, UserService, dialogService,
              ponmCtxConfig, $log, $state, $stateParams, TravelManager, jsUtils) {

      $scope.travelId = $stateParams.travelId;

      getTravel($scope.travelId);

      var travelManager;

      function getTravel(travelId) {
        if (!travelId) {
          return;
        }
        TravelService.getTravel({travelId: travelId}, function (res) {
          if (res.status == "OK") {
            $scope.travel = res.travel;
            travelManager = new TravelManager($scope.travel);
            travelManager.calculate();
            // 获取图片的用户信息
//                        UserService.getOpenInfo({'userId': $scope.travel.user.id}, function (data) {
//                            if (data.status == "OK") {
//                                $scope.userOpenInfo = data.open_info;
//                            }
//                        });
          }
        });
      }

      $scope.selectAll = function () {
        angular.forEach($scope.travel.spots, function (spot, key) {
          angular.forEach(spot.photos, function (photo, key) {
            $scope.selectPhoto(null, photo);
          });
        });
      };

      var delTravelModalOptions = {
        closeButtonText: '取消',
        actionButtonText: '删除旅行相册',
        headerText: '删除 ' + ' ' + '?',
        bodyText: '这么做会删除该相册及其中的照片。'
      };

      $scope.deleteTravel = function (travel) {
        delTravelModalOptions.headerText = '删除 ' + travel.title + '?';
        dialogService.showModal({}, delTravelModalOptions).then(function (result) {
          TravelService.delete({travelId: travel.id}, function (res) {
            if (res.status == "OK") {
              $state.go("photos.albums");
              $scope.alertService.add("success", "删除成功");
            }
          });
        });
      };

      $scope.$on('removePhotoDo', function (e, photoId) {
        angular.forEach($scope.travel.spots, function (spot, key) {
          jsUtils.Array.removeItem(spot.photos, "id", photoId);
        });
        // 删除图片后，发出重新布局事件
        $scope.$broadcast("ponm.photo.fluid.resize");
      });
    }])
  .controller('PhotosTrashCtrl',
  ['$location', '$rootScope', '$scope', 'RecycleService', '$log', 'jsUtils', 'TravelManager',
    function ($location, $rootScope, $scope, RecycleService, $log, jsUtils, TravelManager) {


      RecycleService.get({}, function (res) {
        if (res.status == "OK") {
          angular.forEach(res.recycles, function (recycle, key) {
            if (recycle.recy_type == "photo") {
              recycle.photo.recycle = recycle.id;
              if (recycle.photo.travel_id) {
                $scope.travels[recycle.photo.travel_id]
                  = $scope.travels[recycle.photo.travel_id] || {
                  id: recycle.photo.travel_id,
                  name: recycle.photo.travel_name,
                  user_id: recycle.photo.user_id,
                  photos: []
                };
                $scope.travels[recycle.photo.travel_id].photos.push(recycle.photo);
              } else {
                $scope.travels['noTravel'] = $scope.travels['noTravel'] ||
                {
                  user_id: recycle.photo.user_id,
                  name: "无旅行",
                  photos: []
                };
                $scope.travels['noTravel'].photos.push(recycle.photo);
              }
            } else if (recycle.recy_type == "travel") {
              recycle.travel.name = recycle.travel.title;
              recycle.travel.user_id = recycle.travel.user.id;
              recycle.travel.recycleId = recycle.id;
              var travelManager = new TravelManager(recycle.travel);
              $scope.travels[recycle.travel.id] = travelManager.travel;
            }
          });
        }
      });

      $scope.travels = {};
//            UserService.getRecycleBin({userId: $scope.userId}, function(res) {
//                if(res.status == "OK") {
//                    angular.forEach(res.recycles, function(recycle, key) {
//                        if(recycle.recy_type == "photo") {
//                            recycle.photo.recycle = recycle.id;
//                            if(recycle.photo.travel_id) {
//                                $scope.travels[recycle.photo.travel_id]
//                                    = $scope.travels[recycle.photo.travel_id] || {
//                                    id: recycle.photo.travel_id,
//                                    name: recycle.photo.travel_name,
//                                    user_id: recycle.photo.user_id,
//                                    photos: []
//                                };
//                                $scope.travels[recycle.photo.travel_id].photos.push(recycle.photo);
//                            }else {
//                                $scope.travels['noTravel'] = $scope.travels['noTravel'] ||
//                                                                {user_id: recycle.photo.user_id,
//                                                                    name: "无旅行",
//                                                                    photos:[]};
//                                $scope.travels['noTravel'].photos.push(recycle.photo);
//                            }
//                        }
//                    });
//                }
//            });

      $scope.$on('cancelRecycleDo', function (e) {
        angular.forEach($scope.selectedPhotos, function (photo, key) {
          RecycleService.cancel({id: photo.recycle},
            function (res) {
              if (res.status == "OK") {
                removePhoto(photo);
                $scope.alertService.add("success", "已恢复");
              }
            });
        });
      });

      $scope.$on('removeRecycleDo', function (e) {
        angular.forEach($scope.selectedPhotos, function (photo, key) {
          RecycleService.delete({id: photo.recycle},
            function (res) {
              if (res.status == "OK") {
                removePhoto(photo);
                $scope.alertService.add("success", "已彻底删除");
              }
            });
        });
      });

      function removePhoto(photo) {
        $scope.selectPhoto(null, photo);
        angular.forEach($scope.travels, function (travel, key) {
          jsUtils.Array.removeItem(travel.photos, "id", photo.id);
          if (travel.photos.length == 0) {
            delete $scope.travels[key];
          }
        });
      }

      $scope.$on('selectAllDo', function (e) {
        angular.forEach($scope.travels, function (travel, key) {
          angular.forEach(travel.photos, function (photo, key) {
            $scope.selectPhoto(null, photo);
          });
        });
      });

      $scope.$on('emptyRecycleBinDo', function (e) {
        RecycleService.delete({},
          function (res) {
            if (res.status == "OK") {
              delete $scope.travels;
              $scope.alertService.add("success", "已清空");
            }
          });
      });
    }])
  .controller('PhotosPhotoCtrl',
  ['$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modal',
    'ponmCtxConfig', '$log', '$state', '$stateParams',
    function ($window, $location, $rootScope, $scope, TravelService, UserService, $modal,
              ponmCtxConfig, $log, $state, $stateParams) {

      $scope.$watch('rectSelected', function (value) {
        if ((value && !$scope.photo.actionSelected) || (!value && $scope.photo.actionSelected)) {
          $scope.selectPhoto(null, $scope.photo);
        }
      });

      $scope.$on("photosCancelSelect", function () {
        $scope.rectSelected = false;
      });

    }])
  .controller('PhotosMoveCtrl',
  ['$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modalInstance',
    'ponmCtxConfig', '$log', '$q', 'jsUtils', 'operateType', 'photos',
    function ($window, $location, $rootScope, $scope, TravelService, UserService, $modalInstance,
              ponmCtxConfig, $log, $q, jsUtils, operateType, photos) {

      $scope.ctx = ponmCtxConfig.ctx;
      $scope.staticCtx = ponmCtxConfig.staticCtx;
      $scope.apirest = ponmCtxConfig.apirest;

      $scope.photos = photos;

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      UserService.getTravels({userId: ponmCtxConfig.userId}, function (res) {
        if (res.status == "OK") {
          $scope.travels = res.open_info.travels;
        }
      });

      $scope.newTravel = {};
      $scope.selectedTravel = {};
      $scope.select = function (travel, selected) {
        if (selected != undefined) {
          travel.selected = selected;
        } else {
          travel.selected = !travel.selected;
        }

        if ($scope.selectedTravel != travel) {
          $scope.selectedTravel.selected = false;
          $scope.selectedTravel = travel;
        }
      };
      $scope.selectNewTravel = function (name) {
        if (name) {
          $scope.select($scope.newTravel, true);
        } else {
          $scope.select($scope.newTravel, false);
        }
      };

      $scope.ok = function () {
        if ($scope.newTravel.selected) {
          if (!$scope.selectedTravel.name) {
            return;
          }
          createTravel($scope.selectedTravel.name, $scope.photos).then(function () {
            $modalInstance.close();
          }, function (reason) {

          });
        } else {
          addPhotosToTravel($scope.photos, $scope.selectedTravel).then(function () {
            $modalInstance.close();
          }, function (reason) {

          });
        }

      };

      function createTravel(name, photos) {
        var deferred = $q.defer();
        TravelService.create({}, jsUtils.param({travel: name}), function (res) {
          if (res.status == "OK") {
            addPhotosToTravel($scope.photos, res.travel)
              .then(function () {
                deferred.resolve();
              });
          }
        });
        return deferred.promise;
      }

      function addPhotosToTravel(photos, travel) {
        var photoIds = [];
        angular.forEach(photos, function (photo, key) {
          photoIds.push(photo.id);
        });

        var deferred = $q.defer();
        if (photoIds.length > 0) {
          TravelService.addPhoto({travelId: travel.id},
            jsUtils.param({photos: photoIds.join(",")}),
            function (res) {
              if (res.status == "OK") {
                deferred.resolve();
              } else {
                deferred.reject(res.info);
              }
            });
        } else {
          deferred.resolve();
        }

        return deferred.promise;
      }

      // 显示文本
      if (operateType == "move") {
        $scope.displayText = {
          ok: "移动",
          title1: "将",
          title2: "张图片移动到..."
        };
      } else {
        $scope.displayText = {
          ok: "移动",
          title1: "将",
          title2: "张图片到..."
        };
      }
    }])
  .controller('PhotosTravelMoveCtrl',
  ['$window', '$location', '$rootScope', '$scope', 'TravelService', 'UserService', '$modalInstance',
    'ponmCtxConfig', '$log', '$q', 'jsUtils', 'operateType', 'photos',
    function ($window, $location, $rootScope, $scope, TravelService, UserService, $modalInstance,
              ponmCtxConfig, $log, $q, jsUtils, operateType, photos) {

      $scope.ctx = $window.ctx;
      $scope.staticCtx = ponmCtxConfig.staticCtx;
      $scope.apirest = $window.apirest;

      $scope.photos = photos;

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      UserService.getTravels({userId: ponmCtxConfig.userId}, function (res) {
        if (res.status == "OK") {
          $scope.travels = res.open_info.travels;
        }
      });

      $scope.newTravel = {};
      $scope.selectedTravel = {};
      $scope.select = function (travel, selected) {
        if (selected != undefined) {
          travel.selected = selected;
        } else {
          travel.selected = !travel.selected;
        }

        if ($scope.selectedTravel != travel) {
          $scope.selectedTravel.selected = false;
          $scope.selectedTravel = travel;
        }
      };
      $scope.selectNewTravel = function (name) {
        if (name) {
          $scope.select($scope.newTravel, true);
        } else {
          $scope.select($scope.newTravel, false);
        }
      };

      $scope.ok = function () {
        addPhotosToTravel($scope.photos, $scope.selectedTravel).then(function () {
          $modalInstance.close();
        }, function (reason) {

        });
      };

      function addPhotosToTravel(photos, travel) {
        var photoIds = [];
        angular.forEach(photos, function (photo, key) {
          photoIds.push(photo.id);
        });

        var deferred = $q.defer();
        if (photoIds.length > 0) {
          TravelService.addPhoto({travelId: travel.id}, jsUtils.param({photos: photoIds.join(",")}), function (res) {
            if (res.status == "OK") {
              deferred.resolve();
            } else {
              deferred.reject(res.info);
            }
          });
        } else {
          deferred.resolve();
        }

        return deferred.promise;
      }

      // 显示文本
      if (operateType == "move") {
        $scope.displayText = {
          ok: "移动",
          title1: "将",
          title2: "张图片移动到..."
        };
      } else {
        $scope.displayText = {
          ok: "移动",
          title1: "将",
          title2: "张图片到..."
        };
      }
    }])
;
