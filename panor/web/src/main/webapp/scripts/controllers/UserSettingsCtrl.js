/**
 * Created by any on 14-4-1.
 */
'use strict';

angular.module('userSettingsApp', [
        'ngResource',
        'ui.bootstrap',
        'cnmapApp'])
    .controller('UserSettingsCtrl', ['$window', '$log', '$location', '$rootScope', '$scope', '$modal', 'UserPhoto', 'UserService',
        function ($window, $log, $location, $rootScope, $scope, $modal, UserPhoto, UserService) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            $scope.avatar = $scope.userId = $window.userId;

            $scope.changeAvatar = function () {
                $scope.avatar = 1;

                var modalInstance = $modal.open({
                    templateUrl: 'views/changeUserAvatar.html',
                    controller: "ChUserAvatarCtrl",
                    resolve: {
                    }
                });

                modalInstance.result.then(function () {
                    $scope.avatar = $scope.userId;
                }, function () {
                    $scope.avatar = $scope.userId;
                    $log.info('Modal dismissed at: ' + new Date());
                });
            }

            getSettings();

            function getSettings() {
                UserService.getSettings({'userId': $scope.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.settings = data.settings;
                    }
                })
            }

            $scope.submit = function() {
                UserService.updateSettings({'userId': $scope.userId}, $scope.settings, function(data) {
                    if(data.status == "OK") {
                        $scope.addAlert({type: "success", msg: "保存成功!"});
                    }else {
                        $scope.addAlert({type: "danger", msg: "保存失败!"});
                    }
                }, function(data) {
                    $scope.addAlert({type: "danger", msg: "保存失败!"});
                })
            }

            $scope.addAlert = function(msg) {
                $scope.alerts = [];
                $scope.alerts.push(msg);
            };

            $scope.closeAlert = function (index) {
                if($scope.alerts) {
                    $scope.alerts.splice(index, 1);
                }
            };

        }]);