/**
 * Created by any on 2014/6/19.
 */
'use strict';

angular.module('ponmApp.login', ['ponmApp'])
    .controller('LoginCtrl', ['$window', '$scope', '$log', '$q', 'param', '$location', '$cookieStore',
        function ($window, $scope, $log, $q, param, $location, $cookieStore) {

            $scope.ctx = $window.ctx;

            $scope.credentials = {
            };

            $scope.login = function(e, user) {
//                $log.debug(user);
                if(!$scope.credentials.username || !$scope.credentials.password) {
                    e.preventDefault();
                    e.stopPropagation();
                }
            };

//            if ($cookieStore.get("username") != null && $cookieStore.get("username") != "") {
//                $scope.credentials.username = $cookieStore.get("username");
//            } else {
//
//            }

            function saveUsername(theForm) {
                $cookies("username",theForm.j_username.value, { expires: 30, path: "/panor-web/"});
            }

            function validateForm(form) {
                var valid = validateRequired(form);
                if (valid == false) {
                    $(".control-group").addClass('error');
                }
                return valid;
            }

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