/**
 * Created by any on 2014/5/10.
 */
angular.module('LocationHashApp', [
    'ngResource',
    'ui.bootstrap'
])
    .config(function () {
    })
    .run(['$log', function($log) {
        $log.info("test app runing");
    }])
    .controller('LocationHashCtrl', ['$scope', '$http', '$log', '$location',
        function($scope, $http, $log, $location) {

            $scope.setUserId = function(userid) {
                var hashObj = {
                    userid: userid
                };
                $location.hash(jQuery.param(hashObj));
            }
        }])
;