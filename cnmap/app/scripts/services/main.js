'use strict';

angular.module('cnmapApp')
    .factory('Comment', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/comment/photo/:photoId/:pageSize/:pageNo',
            {photoId: "@photoId"},
            {
                getPhotos: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('UserPhoto', ['$window', '$resource', 'TokenService', function ($window, $resource, TokenService) {
        return $resource($window.apirest + '/user/:userId/photos/:pageSize/:pageNo',
            {userId: "@id"}, {
                query: {
                    method: 'GET',
                    isArray: true
                }
            });
    }])
    .factory('UserService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId',
            {userId: "@id"}, {});
    }])
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