/**
 * Created by any on 2014/6/19.
 */
'use strict';

angular.module('ponmApp.controllers')
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $stateProvider
                // state: login
                .state('login', {
                    url: '/login',
                    views: {
                        '': { templateUrl: 'views/ponm.login.html',
                            controller: 'LoginCtrl'},

                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                    },
                    resolve: {
                    }
                })
                // state: signup
                .state('signup', {
                    url: '/signup',
                    views: {
                        '': { templateUrl: 'views/ponm.signup.html',
                            controller: 'SignupCtrl'},

                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                    },
                    resolve: {
                    }
                })
        }])
    .controller('LoginCtrl',
    ['$scope', '$log', '$q', 'jsUtils', '$location', '$state', 'AuthService',
        'ponmCtxConfig',
        function ($scope, $log, $q, jsUtils, $location, $state, AuthService,
                  ponmCtxConfig) {

            $scope.ponmCtxConfig = ponmCtxConfig;
            $scope.$log = $log;

            AuthService.checkLogin().then(function() {
                $state.go("maps.popular", {});
            });

            $scope.$watch(function() {
                return $location.search().login_error;
            }, function(login_error) {
                if(login_error) {
                    $scope.loginError = true;
                }else {
                    $scope.loginError = false;
                }
            });

//            $scope.$watch('userForm.$dirty', function($dirty) {
//                if($dirty) {
//                    $scope.loginError = false;
//                }
//            });

            $scope.credentials = {};

            $scope.login = function(e, user) {
//                if(!$scope.credentials.username) {
//                    e.preventDefault();
//                    e.stopPropagation();
//                    $scope.userForm.j_username.$dirty = true;
//                }
//                if(!$scope.credentials.password) {
//                    e.preventDefault();
//                    e.stopPropagation();
//                    $scope.userForm.j_password.$dirty = true;
//                }

                if($scope.userForm.$valid) {
                    AuthService.loginCheck(user).then(function() {

                    }, function() {
                        $scope.loginError = true;
                    });
                }else {
                }
            };

            $scope.loginSubmit = function(e, user) {
                if($scope.userForm.$valid) {
                    AuthService.loginCheck(user).then(function() {
                        $scope.loginError = false;
                        return true;
//                        AuthService.login(user);
                    }, function() {
                        $scope.loginError = true;
                        $log.debug(e);
                        e.originalEvent.preventDefault();
                        e.originalEvent.stopPropagation();
                        e.preventDefault();
                        e.stopPropagation();
                        return true;
                    });
                }else {
//                    e.preventDefault();
//                    e.stopPropagation();
                    return false;
                }
            };

            $scope.passwordHint = function() {
                if(!$scope.credentials.username) {
                    alert("用户名 为必填项。");
                    e.preventDefault();
                    e.stopPropagation();
                }else {
                    $location.$$absUrl=ctx+"/passwordHint?username=" + $scope.credentials.username;
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
            $scope.ok = function () {
                $modalInstance.close();
            };

        }])
    .controller('SignupCtrl',
    ['$window', '$scope', '$log', '$q', 'jsUtils', '$location', '$cookieStore', 'AuthService',
        'ponmCtxConfig',
        function ($window, $scope, $log, $q, jsUtils, $location, $cookieStore, AuthService,
                  ponmCtxConfig) {

            $scope.ponmCtxConfig = ponmCtxConfig;

            $scope.user = {};

//            $scope.$watch('user', function(passwordConfirm) {
//                $log.debug(passwordConfirm);
//            });

        }])
;