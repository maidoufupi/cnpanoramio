/**
 * Created by any on 2014/6/6.
 */

'use strict';

angular.module('ponmApp.controllers')
  .controller('PhotoModalCtrl', ['$window', '$scope', '$log', '$modalInstance', 'photoId', 'travelId', 'PhotoService',
    'CommentService', 'UserService', 'TravelService', '$q', '$modal', '$filter', '$location',
    'ponmCtxConfig', 'jsUtils',
    function ($window, $scope, $log, $modalInstance, photoId, travelId, PhotoService, CommentService, UserService,
              TravelService, $q, $modal, $filter, $location, ponmCtxConfig, jsUtils) {

      $scope.ctx = $window.ctx;
      $scope.staticCtx = ponmCtxConfig.staticCtx;
      $scope.corsproxyCtx = ponmCtxConfig.corsproxyCtx;
      $scope.apirest = $window.apirest;
      $scope.photoId = photoId;
      $scope.userId = $window.userId;
      $scope.login = ponmCtxConfig.login;
      $scope.ponmCtxConfig = ponmCtxConfig;

      $scope.photoDetailCollapsed = true;

      // comments configs
      $scope.comment = {
        pageSize: 10,
        totalItems: 0,
        numPages: 0,
        currentPage: 1,
        maxSize: 10
      };

      $scope.setPhotoId = function (photoId) {

        $log.debug("$scope id " + $scope.$id + " photoId" + photoId);
        $scope.photoId = photoId;

        // 获取图片各种信息
        PhotoService.getPhoto({photoId: $scope.photoId}, function (data) {
//                    $log.debug(data);
          if (data.status == 'OK') {
            $scope.photo = data.prop;
            // 设置此photo是否可以被登录者编辑
            $scope.photoEditable = ($scope.ponmCtxConfig.userId == $scope.photo.user_id);

            // 评论
            $scope.comment.totalItems = data.prop.comment_count;
            $scope.comment.numPages = Math.ceil($scope.comment.totalItems / $scope.comment.pageSize);

            // 获取图片的用户信息
            UserService.getOpenInfo({userId: $scope.photo.user_id}, function (data) {
              if (data.status == "OK") {
                $scope.userOpenInfo = data.open_info;
              }
            });

            $scope.setGalleryImages($scope.photo);

            // 旅行
            if (!travelId) {
              if ($scope.photo.travel_id) {
                travelId = $scope.photo.travel_id;
                getTravel(travelId);
              } else {
                $scope.travel = {
//                                    photos: [
//                                        $scope.photo
//                                    ]
                };
              }
            }
          }
        });

        getCameraInfo(photoId);

        getComments(photoId);
      };

      function getCameraInfo(photoId) {
        PhotoService.getCameraInfo({photoId: photoId}, function (data) {
          if (data.status == "OK") {
            $scope.cameraInfo = data.camera_info;
          }
//                    $log.debug($scope.cameraInfo);
        });
      }

      /**
       * 获取详细评论(分页)
       */
      function getComments(photoId) {
        PhotoService.getComments({photoId: photoId}, function (data) {
          if (data.status == "OK") {
            $scope.comments = data.comments;
          }
        }, function (error) {
        });
      }

      function getTravel(travelId) {
        TravelService.getTravel({travelId: travelId}, function (res) {
          if (res.status == "OK") {
            $scope.travel = res.travel;
            $scope.travel.photos = [];
            $scope.travel.totalPhoto = 0;
            $scope.travel.currentPhoto = 0;
            var currentPhoto = 0;
            angular.forEach($scope.travel.spots, function (spot, key) {
              angular.forEach(spot.photos, function (photo, key) {
                currentPhoto = currentPhoto + 1;
                if (photo.id == photoId) {
                  photo.active = true;
                  $scope.travel.activePhoto = photo;
                }
                photo.sortCount = currentPhoto;
                $scope.travel.photos.push(photo);
              });
            });
            $scope.travel.totalPhoto = currentPhoto;

            $scope.setGalleryImages($scope.travel.photos);
          }
        });
      }

      if (travelId) {
        getTravel(travelId);
      }

      $scope.getPhotoSrc = function (photo) {
        if (!photo) {
          return "";
        }
        if (photo.file_type == "gif") {
          return $scope.corsproxyCtx + '/' + photo.oss_key;
        } else {
          var extension = "@";
//                    if(photo.width > 2000 || photo.height > 2000) {
          extension = extension + "!photo-preview-lg";
//                    }
//                    if(photo.file_size > 1024*1024*4) {
//                        extension = extension + "_1o_50Q.jpg";
//                    }else if(photo.file_size > 1024*1024*2) {
//                        extension = extension + "_1o_80Q.jpg";
//                    }else if(photo.file_size > 1024*1024) {
//                        extension = extension + "_1o_90Q.jpg";
//                    }

          return $scope.corsproxyCtx + '/' + photo.oss_key + extension;
        }

      };

      /**
       * 更新图片属性
       *
       * @param photo
       * @param type
       * @param $data
       */
      $scope.updatePhoto = function (photo, type, $data) {
        var d = $q.defer();
        var params = {};
        params[type] = $data;
        PhotoService.updateProperties({photoId: photo.id}, params, function (res) {
          res = res || {};
          if (res.status === 'OK') { // {status: "OK"}
            d.resolve();
          } else { // {status: "error", msg: "Username should be `awesome`!"}
            d.resolve(res.info);
          }
        }, function (error) {
          if (error.data) {
            d.reject(error.data.info);
          } else {
            d.reject('Server error!');
          }
        });
        return d.promise;
      };

      $scope.addTravel = function (data) {
        var d = $q.defer();
        TravelService.addPhoto({travelId: data},
          jsUtils.param({photos: $scope.photoId}), function (res) {
            if (res.status == "OK") {
              d.resolve();
            } else { // {status: "error", msg: "Username should be `awesome`!"}
              d.resolve(res.info);
            }
          }, function (error) {
            if (error.data) {
              d.reject(error.data.info);
            } else {
              d.reject('Server error!');
            }
          });
        return d.promise;
      };

      $scope.showTravel = function () {
        var selected = [];
        if ($scope.userOpenInfo) {
          selected = $filter('filter')($scope.userOpenInfo.travels, {id: $scope.photo.travel_id});
        }

        return ($scope.photo.travel_id && selected.length) ? selected[0].title : '添加到旅行';
      };

      /**
       * 根据id删除评论, 当删除评论后调用此方法
       */
      $scope.$on('deletedCommentEvent', function (e, id) {
        e.preventDefault();
        e.stopPropagation();
        angular.forEach($scope.comments, function (comment, key) {
          if (comment.id == id) {
            delete $scope.comments.splice(key, 1);
            $scope.photo.comment_count -= 1;
          }
        });
      });

      /**
       * 回复评论人的事件响应
       */
      $scope.$on('replyCommentEvent', function (e, user) {
        e.preventDefault();
        e.stopPropagation();
        $scope.comment.placeholder = "回复 " + user.name;
        $scope.commentContent = "@" + user.name + " ";
      });

      $scope.cancelComment = function () {
        $scope.comment.placeholder = "";
        $scope.commentContent = "";
      };

      $scope.like = function () {
        if ($scope.photo.like) {
          PhotoService.unLike({photoId: $scope.photoId}, function (res) {
            res = res || {};
            if (res.status === 'OK') { // {status: "OK"}
              $scope.photo.like = false;
              $scope.photo.like_count -= 1;
            }
          }, function (error) {
            if (error.data) {
            } else {
            }
          });
        } else {
          PhotoService.like({photoId: $scope.photoId}, function (res) {
            res = res || {};
            if (res.status === 'OK') { // {status: "OK"}
              $scope.photo.like = true;
              $scope.photo.like_count += 1;
            }
          }, function (error) {
            if (error.data) {
            } else {
            }
          });
        }
      };

//            $scope.$watch('cameraInfo.date_time_original', function(photoCameraTime) {
//                $scope.photoCameraTime = photoCameraTime;
//            });
//            $scope.$watch('photoCameraTime', function(photoCameraTime) {
//                if($scope.photoCameraTime != $scope.cameraInfo.date_time_original) {
//
//                }
//            });

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      $scope.openTravelAlbum = function () {
        $scope.$broadcast("travelAlbum.open");
      };
      $scope.closeTravelAlbum = function () {
        $scope.$broadcast("travelAlbum.close");
      };

      $scope.openRecommendAlbum = function () {
        $scope.$broadcast("photoRecommendAlbum.open");
      };

      /**
       * 打开相册中一个指定的（单击）图片
       *
       * @param photo
       */
      $scope.openPhoto = function (photo) {
        $scope.travel.activePhoto && ($scope.travel.activePhoto.active = false);
        photo.active = true;
        $scope.travel.activePhoto = photo;
        $scope.closeTravelAlbum();
        $scope.setPhotoId(photo.id);
      };

      /**
       * 前一张图片
       * 如果已是第一张则打开相册
       *
       */
      $scope.previous = function () {
        var preIndex = 0;
        if ($scope.travel.activePhoto && (preIndex = $scope.travel.activePhoto.sortCount - 2) >= 0) {
          $scope.travel.activePhoto.active = false;
          var photo = $scope.travel.photos[preIndex];
          photo.active = true;
          $scope.travel.activePhoto = photo;
          $scope.setPhotoId(photo.id);
        } else {
          $scope.openTravelAlbum();
        }
      };

      /**
       * 下一张图片，如果已是最后一张则打开推荐相册
       *
       */
      $scope.next = function () {
        var preIndex = 0;
        if ($scope.travel.activePhoto &&
          (preIndex = $scope.travel.activePhoto.sortCount) < $scope.travel.photos.length) {
          $scope.travel.activePhoto.active = false;
          var photo = $scope.travel.photos[preIndex];
          photo.active = true;
          $scope.travel.activePhoto = photo;
          $scope.setPhotoId(photo.id);
        } else {
          $scope.openRecommendAlbum();
        }
      };

      $scope.setPhotoId(photoId);

      $scope.openMapPhoto = function () {

        var modalInstance = $modal.open({
          templateUrl: 'views/mapPhoto.html',
          controller: 'MapPhotoCtrl2',
          windowClass: 'map-photo-modal',
          resolve: {
            photo: function () {
              return $scope.photo;
            }
          }
        });

        modalInstance.result.then(function (selectedItem) {
          $scope.selected = selectedItem;
          $scope.setPhotoId($scope.photoId);
        }, function () {
          $log.info('Modal dismissed at: ' + new Date());
          $scope.setPhotoId($scope.photoId);
        });
      };

      $scope.displayGallery = function () {
        var index = 0;
        angular.forEach($scope.galleryData, function (photo, key) {
          if (photo.photoId == $scope.photoId) {
            index = key;
          }
        });
        $scope.$broadcast("image-gallery", {index: index});
      };

      $scope.galleryData = [];
      $scope.setGalleryImages = function (photos) {
        if (angular.isArray(photos)) {
          $scope.galleryData = [];
          angular.forEach(photos, function (photo, key) {
            $scope.galleryData.push({
              photoId: photo.id,
              title: photo.title,
              href: $scope.getPhotoSrc(photo),
              type: 'image/jpeg',
              thumbnail: getGalleryThumbnail(photo)
            });
          });
        } else if (angular.isObject(photos)) {
          $scope.galleryData.push({
            photoId: photos.id,
            title: photos.title,
            href: $scope.getPhotoSrc(photos),
            type: 'image/jpeg',
            thumbnail: getGalleryThumbnail(photos)
          });
        }
      };
      function getGalleryThumbnail(photo) {
        return $scope.staticCtx + '/' + photo.oss_key + '@1e_20w_20h_1c.jpg';
      }

      $scope.share = function () {
      };

      /**
       * 点击图片事件 打开下一张
       */
      $scope.$on('image.click', function (e, photo) {
        e.stopPropagation();
        e.preventDefault();
        $log.debug('image.click ' + $scope.photo.id);
        $scope.next();
      });
    }])
  .directive("photoTravelAlbum",
  ['$rootScope', '$animate', '$log', function ($rootScope, $animate, $log) {
    return ({
      restrict: "A",
      link: function (scope, element, attrs) {
        scope.$on("travelAlbum.open", function () {
          scope.show = true;
        });
        scope.$on("travelAlbum.close", function () {
          scope.show = false;
        });
      }
    });
  }])

  .directive("photoRecommendAlbum",
  ['$rootScope', '$animate', '$log', function ($rootScope, $animate, $log) {
    return ({
      restrict: "A",
      link: function (scope, element, attrs) {

        scope.$on("photoRecommendAlbum.open", function () {
          scope.recommendAlbumShow = true;
        });
        scope.$on("photoRecommendAlbum.close", function () {
          scope.recommendAlbumShow = false;
        });
      }
    });
  }])
  .controller('MapPhotoCtrl2', ['$window', '$log', '$timeout', '$scope', '$modalInstance', 'PhotoService',
    'GeocodeService', 'photo', 'ponmCtxConfig', 'alertService',
    function ($window, $log, $timeout, $scope, $modalInstance, PhotoService, GeocodeService, photo,
              ponmCtxConfig, alertService) {
      $scope.ctx = $window.ctx;
      $scope.staticCtx = ponmCtxConfig.staticCtx;
      $scope.apirest = $window.apirest;
      $scope.userId = $window.userId;

      var mapEventListener = $window.cnmap.MapEventListener.factory();
      var mapService = $window.cnmap.MapService.factory();
      $scope.mapEventListener = mapEventListener;
      $scope.mapService = mapService;

      $scope.photo = photo;
      $scope.photoId = photo.id;

      $scope.alertService = angular.copy(alertService);

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      $scope.ok = function () {

        PhotoService.updateProperties({photoId: $scope.photoId}, {
          'point': {
            'lat': $scope.file.mapVendor.lat,
            'lng': $scope.file.mapVendor.lng,
            'address': $scope.file.mapVendor.address
          },
          'vendor': 'gaode',
          'is360': $scope.file.is360
        }, function (data) {
          if (data.status == "OK") {
            $log.debug("properties update successful");
            $scope.alertService.clear();
            $scope.alertService.add("success", "保存成功!", {ttl: 2000});
            $timeout(function () {
              $scope.cancel();
            }, 2000);

          } else {
            $scope.alertService.clear();
            $scope.alertService.add("danger", "保存失败 " + data.status, {ttl: 2000});
          }
        }, function (error) {
          $scope.alertService.clear();
          $scope.alertService.add("danger", "保存失败 " + (error.data && error.data.status), {ttl: 2000});
        });

      };

      $scope.$watch('$$childTail.myMap', function (map) {
        if (map) {
          $scope.map = map;
          mapService.init(map);
          addMapClickEvent($scope.map);
          updatePhotoIdListener();
        }
      });

      function updatePhotoIdListener() {
        if ($scope.file && $scope.file.mapVendor && $scope.file.mapVendor.marker) {
          mapEventListener.setMap($scope.file.mapVendor.marker, null);
        }
        $scope.file = {
          photoId: $scope.photoId
        };

        PhotoService.getPhoto({photoId: $scope.photoId}, function (data) {
          if (data.status == 'OK') {
            $scope.photo = data.prop;
            $scope.file.is360 = data.prop.is360;
          }
        });

        PhotoService.getGPSInfo({photoId: $scope.photoId}, function (res) {
          if (res.status == "OK") {
            var gpsInfo = res.gps;
            if (gpsInfo) {
              addOrUpdateMarker($scope.file, gpsInfo.point.lat, gpsInfo.point.lng);
              mapEventListener.setCenter($scope.map, gpsInfo.point.lat, gpsInfo.point.lng);
              $scope.setPlace($scope.file, gpsInfo.point.lat, gpsInfo.point.lng, gpsInfo.point.address);
            }
          }
        })
      }

      function createMarker(file, lat, lng) {
        // create marker
        file.mapVendor = file.mapVendor || {};
        if (lat && lng) {
          file.mapVendor.lat = lat;
          file.mapVendor.lng = lng;
        } else {
          if (!file.lat && !file.lng) {
            return;
          } else {
            file.mapVendor.lat = file.lat;
            file.mapVendor.lng = file.lng;
          }
        }

        file.mapVendor.marker = mapEventListener.createDraggableMarker(
          $scope.map, file.mapVendor.lat, file.mapVendor.lng
        );
        file.mapVendor.marker.photo_file = file;
        mapEventListener.setMap(file.mapVendor.marker, $scope.map);

        mapEventListener.addDragendListener(file.mapVendor.marker, function (lat, lng) {
          $scope.setPlace(this.photo_file, lat, lng);
        })

      }

      function addMapClickEvent(map) {
        mapEventListener.addMapClickListener(map, function (lat, lng) {
          addOrUpdateMarker($scope.file, lat, lng);
          $scope.setPlace($scope.file, lat, lng);
        })
      }

      function addOrUpdateMarker(file, lat, lng) {
        file.mapVendor = file.mapVendor || {};
        if (!file.mapVendor.marker) {
          createMarker(file, lat, lng);
          mapEventListener.activeMarker(file.mapVendor.marker);
        } else {
          mapEventListener.setPosition(file.mapVendor.marker, lat, lng);
          mapEventListener.setMap(file.mapVendor.marker, $scope.map);
        }
      }

      $scope.addOrUpdateMarker = addOrUpdateMarker;

      $scope.setPlace = function (file, lat, lng, address) { // 此参数为非gps坐标

        $scope.file.mapVendor = $scope.file.mapVendor || {};
        $scope.file.mapVendor.lat = lat;
        $scope.file.mapVendor.lng = lng;
        $scope.file.mapVendor.latPritty = cnmap.GPS.convert(lat);
        $scope.file.mapVendor.lngPritty = cnmap.GPS.convert(lng);

        if (address) {
          $scope.file.mapVendor.address = address;
        }

        // 加载gps地点可选的地址列表
        mapService.getAddrPois(lat, lng).then(function (addresses, addr) {
          if (!address) {
            $scope.file.mapVendor.address = addr;
          }
          $scope.file.mapVendor.addresses = addresses;
        });
      };


      $scope.clearPlace = function () {
        //$scope.hideMarker();
        //delete $scope.file.mapVendor;
      };

      $scope.mapOptions = {
        zoom: 5
      }
      if (photo.point) {
        $scope.mapOptions.centerPoint = photo.point;
      }
    }])
  .controller('TypeaheadCtrl', ['$scope', '$http', 'GeocodeService', '$q',
    function ($scope, $http, GeocodeService, $q) {
      $scope.selected = undefined;
      $scope.states = [];
      // Any function returning a promise object can be used to load values asynchronously
      $scope.getLocation = function (val) {
        var d = $q.defer();
//                $scope.mapService.getLocPois(val, function(res) {
//                    d.resolve(res);
//                });
        $scope.mapService.getLocPois(val).then(function (res) {
          d.resolve(res);
        });
        return d.promise.then(function (res) {
          return res;
        });
      };

      $scope.goLocation = function (address) {

        var location = address.location;
        if (location) {
//                location = location.split(",");
//                location = {
//                    lat: location[1],
//                    lng: location[0]
//                };
          $scope.mapEventListener.setCenter($scope.map, location.lat, location.lng);
//                $scope.addOrUpdateMarker($scope.file, location.lat, location.lng);
//                $scope.setPlace($scope.file,
//                    location.lat,
//                    location.lng,
//                    address.formatted_address);
          if (address.bounds) {
            $scope.mapEventListener.setBounds($scope.map,
              address.bounds.sw,
              address.bounds.ne);
          } else if (address.zoom) {
            $scope.mapEventListener.setZoom($scope.map, address.zoom);
          }
        }
      };

      $scope.onSelect = function ($item, $model, $label) {

        $scope.goLocation($item);
      };
    }])
;
