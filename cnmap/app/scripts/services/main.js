'use strict';

angular.module('cnmapApp', ['ngCookies',
        'ngResource'])
    .factory('CommentService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/comment/photo/:photoId/:pageSize/:pageNo',
            {'photoId': "@photoId"},
            {
                getPhotos: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('UserPhoto', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/photos/:pageSize/:pageNo',
            {'userId': "@id"}, {
                query: {
                    method: 'GET',
                    isArray: true
                }
            });
    }])
    .factory('UserService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/:type',
            {'userId': "@id"}, {
                getOpenInfo: {
                    method: 'GET',
                    params: {'type': 'openinfo'}
                }
            });
    }])
    .factory('PhotoService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/photo/:photoId/:type',
            {'photoId': "@id"},
            {
                getCameraInfo: {
                    method: 'GET',
                    params: {'type': 'camerainfo'}
                },
                delete: {
                    method: 'DELETE'
                },
                tag: {
                    method: 'POST',
                    params: {'type': 'tag'}
                },
                updateProperties: {
                    method: 'POST',
                    params: {'type': 'properties'}
                },
                getPhoto: {
                    method: 'GET'
                },
                getGPSInfo: {
                    method: 'GET',
                    params: {'type': 'gps'}
                }
            });
    }])