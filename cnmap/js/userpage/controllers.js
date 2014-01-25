'use strict';

/* Controllers */

var userPageControllers = angular.module('userPageControllers', []);

userPageControllers.controller('UserPhotoCtrl', ['$scope', 'UserPhoto',
    function ($scope, UserPhoto) {

        $scope.orderProp = 'size';

        $scope.photos = [];

        UserPhoto.get({userId: 2}, function (data) {
            $scope.photos = data.photos;
        });

        $scope.getPhotos = function() {
            $scope.photos = UserPhoto.get({userId: $scope.userId, pageNo: 1}, function (data) {
                $scope.photos = data.photos;
            });
        }

        $scope.reload = function() {
            jQuery($scope.msnry).masonry();
        }
    }]);