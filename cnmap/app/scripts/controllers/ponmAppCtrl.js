/**
 * Created by any on 2014/8/1.
 */
'use strict';

angular.module('ponmApp.Index', ['ponmApp', 'photosApp', 'mapsApp', 'userSettingsApp',
    'ui.router',
    'ui.map',
    'ui.bootstrap'])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
// Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
//                .when('/', '/yourphotos/all')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
                .otherwise('/');
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////

                .state("home", {
//                    abstract: true,
                    // Use a url of "/" to set a states as the "index".
                    url: "/",
                    views: {

                        // the main template will be placed here (relatively named)
                        '': { template: '<div>home view</div>'},

                        // for column two, we'll define a separate controller
                        'navbar@': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                    }

                    // Example of an inline template string. By default, templates
                    // will populate the ui-view within the parent state's template.
                    // For top level states, like this one, the parent template is
                    // the index.html file. So this template will be inserted into the
                    // ui-view within index.html.
//                    template: '<p class="lead">Welcome to the UI-Router Demo</p>' +
//                        '<p>Use the menu above to navigate. ' +
//                        'Pay attention to the <code>$state</code> and <code>$stateParams</code> values below.</p>' +
//                        '<p>Click these links—<a href="#/c?id=1">Alice</a> or ' +
//                        '<a href="#/user/42">Bob</a>—to see a url redirect in action.</p>'

                })
                ;
        }])

    .controller('IndexCtrl',
    [        '$window', '$location', '$rootScope', '$scope', 'PhotoService', 'UserService', '$modal',
        'ponmCtxConfig', '$log', '$state', '$stateParams', 'safeApply', 'jsUtils', 'HashStateManager', 'AuthService',
        function ($window, $location, $rootScope, $scope, PhotoService, UserService, $modal,
                  ponmCtxConfig, $log, $state, $stateParams, safeApply, jsUtils, HashStateManager, AuthService) {

            $scope.checkLogin = function() {
                AuthService.checkLogin();
            };

            $scope.checkLogin();

            $scope.displayLogin = function(photoId) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/ponm.login.html',
                    controller: 'LoginCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                    }
                });

                modalInstance.result.then(function () {
//                    $scope.$broadcast("login");
                }, function () {
//                    $scope.hashStateManager.set("photoid", "");
                });
            };
        }])
;