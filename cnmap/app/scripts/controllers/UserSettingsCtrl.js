/**
 * Created by any on 14-4-1.
 */
'use strict';

angular.module('userSettingsApp', [
        'ngResource',
        'ui.bootstrap',
        'ponmApp'])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $stateProvider to configure your states.
            $stateProvider
                .state("settings", {
//                    abstract: true,
                    // Use a url of "/" to set a states as the "index".
                    url: "/settings",
                    views: {

                        // the main template will be placed here (relatively named)
                        '': { templateUrl: 'views/settings.html',
                            controller: 'UserSettingsCtrl'},

                        // for column two, we'll define a separate controller
                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                    }
                })
            ;
        }])
    .controller('UserSettingsCtrl',
    ['$window', '$log', '$location', '$rootScope', '$scope', '$modal', '$state', 'UserPhoto', 'UserService',
        'ponmCtxConfig', 'AuthService',
        function ($window, $log, $location, $rootScope, $scope, $modal, $state, UserPhoto, UserService,
                  ponmCtxConfig, AuthService) {
            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.ponmCtxConfig = ponmCtxConfig;

            AuthService.checkLogin().then(function(){
                $scope.userId = ponmCtxConfig.userId;
                getSettings();
            }, function(){
                $state.go("login", {});
            });

//            $scope.avatar = $scope.userId = $window.userId;

            $scope.changeAvatar = function () {
//                $scope.avatar = 1;

                var modalInstance = $modal.open({
                    templateUrl: 'views/changeUserAvatar.html',
                    controller: "ChUserAvatarCtrl",
                    windowClass: 'map-photo-modal',
                    resolve: {
                    }
                });

                modalInstance.result.then(function (avatar) {
                    $scope.ponmCtxConfig.avatar = avatar;
                }, function (avatar) {
                    $log.debug("dismissed avatar is " + avatar);
                    if(avatar) {
                        $scope.ponmCtxConfig.avatar = avatar;
                    }
                });
            };

            function getSettings() {
                UserService.getSettings({userId: $scope.userId}, function(data) {
                    if(data.status == "OK") {
                        $scope.settings = data.settings;
                    }
                })
            }

            $scope.submit = function() {
                UserService.updateSettings({userId: $scope.userId}, $scope.settings, function(data) {
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