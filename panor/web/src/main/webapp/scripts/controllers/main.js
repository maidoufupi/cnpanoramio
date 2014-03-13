'use strict';

angular.module('cnmapApp')
  .controller('ExploreWorldCtrl', ['$scope', function ($scope) {
        $scope.photos = [];
        $scope.allPhotos = [];
        $scope.slice = 0;
        $scope.photoStart = true;
        $scope.photoEnd = true;
        var photoSize = 4;


        $scope.updatePhoto = function(photos) {
            $scope.allPhotos = photos;
            $scope.photos = photos.slice(0, photoSize);
            $scope.slice = 0;
            setPhotoStartEnd();
        }

        $scope.nextPhoto = function() {
            if($scope.slice <= $scope.allPhotos.length) {
                $scope.slice = $scope.slice + photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }
        }

        $scope.prePhoto = function() {
            if($scope.slice >= photoSize) {
                $scope.slice = $scope.slice - photoSize;
                $scope.photos = $scope.allPhotos.slice($scope.slice, $scope.slice + photoSize);
                setPhotoStartEnd();
            }

        }

        function setPhotoStartEnd() {
            if($scope.slice + photoSize <= $scope.allPhotos.length) {
                $scope.photoEnd = false;
            }else {
                $scope.photoEnd = true;
            }

            if($scope.slice >= photoSize) {
                $scope.photoStart = false;
            }else {
                $scope.photoStart = true;
            }
        }
  }])
    .controller('PhotoCtrl', ['$window', '$location', '$rootScope', '$scope', '$cookieStore', 'PhotoService', 'Comment',
        function ($window, $location, $rootScope, $scope, $cookieStore, PhotoService, Comment) {

            var url = $location.absUrl();
            var photo = url.match(/\/photo\/[0-9]+/g);
            if(photo) {
                $scope.photoId = photo[0].match(/[0-9]+/g)[0];
            }else {
                $scope.photoId = 1;
            }

            $scope.ctx = $window.apirest + "/photo";

            $scope.photo = {};
            $scope.comment = {};
            $scope.comments = [];

            $scope.user = {};
            var user = $cookieStore.get('user');
            console.log(user)
            if(user) {
                $scope.user = user;
            }

            $scope.photo = PhotoService.getCmeraInfo({photoId: $scope.photoId}, function() {

            })

            Comment.getPhotos({photoId: $scope.photoId}, function (data) {
                $scope.comment.count = data.count;
                console.log(data);
            }, function(error) {
                console.log(error)
            })

            Comment.getPhotos({photoId: $scope.photoId, pageSize: 10, pageNo: 1}, function (data) {
//                console.log(photoComments);
                $scope.comments = data.comments;

            }, function(error) {
            });

            $scope.deletePhoto = function () {
                $window.alert("您确定要删除这张照片？");
            };

            $scope.save = function(content) {
                Comment.save({photoId: $scope.photoId, content: content}, function(data) {
                    console.log(data);
                    $scope.comment.content = "";
                    $scope.comment.count = $scope.comment.count + 1;
                    $scope.comments.push(data);
                })
            }
        }])
