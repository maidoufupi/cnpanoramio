/**
 * Created by any on 14-4-1.
 */
'use strict';

angular.module('userSettingsApp', [
        'ngResource',
        'ui.bootstrap',
        'angularFileUpload',
        'cnmapApp'])
    .controller('UserSettingsCtrl', ['$window', '$log', '$location', '$rootScope', '$scope', '$modal', 'UserPhoto', 'UserService',
        function ($window, $log, $location, $rootScope, $scope, $modal, UserPhoto, UserService) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            $scope.changeAvatar = function (files) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/changeUserAvatar.html',
                    controller: "ChUserAvatarCtrl",
                    resolve: {
                    }
                });

                modalInstance.result.then(function (selectedItem) {

                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            }

        }]);