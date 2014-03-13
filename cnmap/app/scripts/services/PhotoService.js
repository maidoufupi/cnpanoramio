/**
 * Created by any on 14-3-13.
 */
'use strict';

angular.module('cnmapApp')
    .factory('PhotoService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/photo/:photoId/:type',
            {photoId: "@id"},
            {
                getCmeraInfo: {
                    method: 'GET',
                    params: {'type': 'camerainfo'}
                },
                delete: {
                    method: 'DELETE'
                }
            });
    }])