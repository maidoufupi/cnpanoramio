/**
 * Created by any on 2014/8/15.
 */
'use strict';

angular.module('adminApp', [
    'ui.bootstrap',
    'ui.router'
])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
                .when('/admin', '/admin/manager')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
//                .otherwise('/popular')
            ;
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////
                .state('admin', {

                    // With abstract set to true, that means this state can not be explicitly activated.
                    // It can only be implicitly activated by activating one of it's children.
                    abstract: true,

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/admin',

                    // Example of loading a template from a file. This is also a top level state,
                    // so this template file will be loaded and then inserted into the ui-view
                    // within index.html.
//                    templateUrl: 'views/photos.html',

                    views: {

                        // the main template will be placed here (relatively named)
                        '': { templateUrl: 'views/admin.html',
                            controller: 'AdminCtrl'},
                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                        ,'alert': {
                            templateUrl: 'views/ponm.alert.html',
                            controller: 'AlertsCtrl'
                        }
                    },

                    // Use `resolve` to resolve any asynchronous controller dependencies
                    // *before* the controller is instantiated. In this case, since contacts
                    // returns a promise, the controller will wait until contacts.all() is
                    // resolved before instantiation. Non-promise return values are considered
                    // to be resolved immediately.
                    resolve: {
                    }
                })
                .state('admin.manager', {
                    url: '/manager',
                    templateUrl: 'views/admin.manager.html',
                    resolve: {
                    },
                    controller: "AdminManagerCtrl"
                })
                ;
        }])
    .run(['$rootScope', '$window', 'editableOptions', function ($rootScope, $window, editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    .controller('AdminCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q','$modal', 'ponmCtxConfig', '$log', '$state',
        '$stateParams', '$timeout', 'safeApply', 'jsUtils', 'HashStateManager',
        function ($window, $location, $rootScope, $scope, $q, $modal, ponmCtxConfig, $log, $state,
                  $stateParams, $timeout, safeApply, jsUtils, HashStateManager) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.userId = ponmCtxConfig.userId;
            $scope.login = ponmCtxConfig.login;
            $scope.ponmCtxConfig = ponmCtxConfig;
        }])
    .controller('AdminManagerCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q','$modal', 'ponmCtxConfig', '$log', '$state',
        '$stateParams', '$timeout', 'safeApply', 'alertService', 'PanoramioService',
        function ($window, $location, $rootScope, $scope, $q, $modal, ponmCtxConfig, $log, $state,
                  $stateParams, $timeout, safeApply, alertService, PanoramioService) {

            $scope.updatePanoramioIndex = function() {
                PanoramioService.get(function(res) {
                    $log.debug(res);
                    alertService.clear();
                    alertService.add("success", "更新成功", 2000);
                });
            };
            $scope.updatePanoramioLatest = function() {
                PanoramioService.get({action: 'latest'}, function(res) {
                    $log.debug(res)
                    alertService.clear();
                    alertService.add("success", "更新成功", 2000);
                });
            };
        }])
;