/**
 * Created by any on 2014/4/23.
 */
angular.module('contentEditableApp', [
    'ngResource',
    'ui.bootstrap',

    'ponmApp.directives'
])
    .config(function () {
    })
    .run(['$log', function($log) {
        $log.info("test app runing");
    }])
    .controller('ContentEditableCtrl', ['$scope', '$http', '$log',
        function($scope, $http, $log) {
            $scope.name = "image.jpg";

            $scope.$watch('description2', function(newValue) {
                $log.debug(newValue);
            })

        }])
;