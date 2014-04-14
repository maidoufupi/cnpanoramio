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
        return $resource($window.apirest + '/user/:userId/photos/:type/:tag/:pageSize/:pageNo',
            {'userId': "@id"}, {
                query: {
                    method: 'GET',
                    isArray: true
                },
                getByTag: {
                    method: 'GET',
                    params: {
                        'type': 'tag'
                    }
                }
            });
    }])
    .factory('UserService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/:type',
            {'userId': "@id"}, {
                getOpenInfo: {
                    method: 'GET',
                    params: {'type': 'openinfo'}
                },
                getSettings: {
                    method: 'GET',
                    params: {'type': 'settings'}
                },
                updateSettings: {
                    method: 'POST',
                    params: {'type': 'settings'}
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
                },
                favorite: {
                    method: 'PUT',
                    params: {'type': 'favorite'}
                },
                removeFavorite: {
                    method: 'DELETE',
                    params: {'type': 'favorite'}
                }
            });
    }])
    .service('GPSConvertService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/gps', {},
            {
                convert: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .service('AvatarService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/avatar', {},
            {
                upload: {
                    method: 'POST',
                    headers: {
                        'Content-Type': false,
                        'Accept': 'application/json'
                    }
                }
            });
    }]);