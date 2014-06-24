/**
 * Created by any on 2014/6/19.
 */
'use strict';

angular.module('ponmApp.login', ['ponmApp'])
    .controller('LoginCtrl', ['$window', '$scope', '$log', '$q', 'param', '$location', '$cookieStore',
        function ($window, $scope, $log, $q, param, $location, $cookieStore) {

            $scope.ctx = $window.ctx;

            $scope.credentials = {};

            $scope.login = function(e, user) {
//                $log.debug(user);
                if(!$scope.credentials.username) {
                    e.preventDefault();
                    e.stopPropagation();
                    $scope.userForm.j_username.$dirty = true;
                }
                if(!$scope.credentials.password) {
                    e.preventDefault();
                    e.stopPropagation();
                    $scope.userForm.j_password.$dirty = true;
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

        }])
;