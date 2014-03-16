/**
 * Created by any on 14-3-15.
 */
'use strict';

angular.module('cnmapApp')
    .controller('ExploreWorldCtrl', ['$window', '$scope', function ($window, $scope) {
        $scope.ctx = $window.ctx;
        $scope.apirest = $window.apirest;

        $scope.photos = [];
        $scope.allPhotos = [];
        $scope.slice = 0;
        $scope.photoStart = true;
        $scope.photoEnd = true;
        var photoSize = 10;

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

