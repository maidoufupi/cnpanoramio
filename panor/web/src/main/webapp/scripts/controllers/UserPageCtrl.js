/**
 * Created by any on 14-3-16.
 */
'use strict';

angular.module('userPageApp', [
    'ngResource',
    'ui.mapgaode',
    'cnmapApp'])
    .controller('UserCtrl', ['$window', '$location', '$rootScope', '$scope', 'UserPhoto',
        function ($window, $location, $rootScope, $scope, UserPhoto) {

            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            var url = $location.absUrl();
            var userm = url.match(/\/user\/[0-9]+/g);
            if(userm) {
                $scope.userId = userm[0].match(/[0-9]+/g)[0];
            }else {
                $scope.userId = 1;
            }

            $scope.photos = [];

            $scope.photos = UserPhoto.query({userId: $scope.userId, pageSize: 10, pageNo: 1}, function(data) {
                console.log(data)
            }, function(error) {
                console.log(error)
            })
        }])