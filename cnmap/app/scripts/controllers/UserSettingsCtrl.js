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
                        ,'alert': {
                            templateUrl: 'views/ponm.alert.html',
                            controller: 'AlertsCtrl'
                        }
                    }
                })
            ;
        }])
    .controller('UserSettingsCtrl',
    ['$window', '$log', '$location', '$rootScope', '$scope', '$modal', '$state', 'UserPhoto', 'UserService',
        'ponmCtxConfig', 'AuthService', 'alertService',
        function ($window, $log, $location, $rootScope, $scope, $modal, $state, UserPhoto, UserService,
                  ponmCtxConfig, AuthService, alertService) {
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
                        alertService.add("success",  "保存成功!", 5000);
                    }else {
                        alertService.add("danger", "保存失败!", 5000);
                    }
                }, function(data) {
                    alertService.add("danger", "保存失败!", 5000);
                })
            };
        }]);