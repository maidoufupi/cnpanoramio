/**
 * Created by any on 2014/6/7.
 */
angular.module('ponmPhotoApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ponmApp',
    'ponmApp.services',
    'ponmApp.controllers',
    'ponmApp.directives',
    'ui.bootstrap',
    'xeditable'
]).run(function(editableOptions) {
    editableOptions.theme = 'bs3';
})
    .controller('PhotoMCtrl',
    ['$window', '$log', '$rootScope', '$scope', '$modal',
        function ($window, $log, $rootScope, $scope, $modal) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;

            $scope.open = function (size) {

                var modalInstance = $modal.open({
                    templateUrl: 'views/photo.html',
                    controller: 'PhotoModalCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                        photoId: function () {
                            return 134;
                        },
                        travelId: function() {
                            return 11;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    $scope.selected = selectedItem;
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.open();
        }]);