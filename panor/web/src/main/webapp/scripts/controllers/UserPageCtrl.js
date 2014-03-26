/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('userPageApp', [
        'ngResource',
        'ui.bootstrap',
        'ui.mapgaode',
        'cnmapApp'])
    .controller('UserCtrl', ['$window', '$location', '$rootScope', '$scope', 'UserPhoto', 'UserService',
        function ($window, $location, $rootScope, $scope, UserPhoto, UserService) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            var url = $location.absUrl();
            var userm = url.match(/\/user\/[0-9]+/g);
            if(userm) {
                $scope.userId = userm[0].match(/[0-9]+/g)[0];
            }else {
                $scope.userId = 3;
            }

            // 用户图片分页属性
            $scope.photo = {
                pageSize: 20,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };
            $scope.photos = [];

            $scope.$watch('photo.currentPage', function() {
                getPhotos();
            });

            getPhotos();

            function getPhotos() {
                $scope.photos = UserPhoto.query({userId: $scope.userId, pageSize: $scope.photo.pageSize, pageNo: $scope.photo.currentPage}, function(data) {
//                console.log(data)
                }, function(error) {
                    console.log(error)
                })
            }

            UserService.getOpenInfo({'userId': $scope.userId}, function(openInfo) {
                $scope.userOpenInfo = openInfo;
                $scope.photo.totalItems = openInfo.photoCount || 0;
            })

        }]);