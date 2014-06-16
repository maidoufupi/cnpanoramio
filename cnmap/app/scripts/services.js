/**
 * Created by any on 2014/4/26.
 */
'use strict';

angular.module('ponmApp.services', [
    'ngResource'])
    .factory('CommentService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/comment/:commentId',
            {commentId: "@id"},
            {
                modify: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
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
        return $resource($window.apirest + '/user/:userId/:type/:value',
            {'userId': "@id"}, {
                getMe: {
                    method: 'GET'
                },
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
                },
                getTravels: {
                    method: 'GET',
                    params: {'type': 'travel'}
                },
                getTags: {
                    method: 'GET',
                    params: {'type': 'tag'}
                },
                createTag: {
                    method: 'GET',
                    params: {'type': 'tag'}
                }
            });
    }])
    .factory('PhotoService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/photo/:photoId/:type/:pageSize/:pageNo',
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
                ,getComments: {
                    method: 'GET',
                    params: {'type': 'comment'}
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
    }])
    .factory('TravelService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/travel/:travelId/:type/:typeId',
            {'travelId': '@id'},
            {
                create: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                addPhoto: {
                    method: 'POST',
                    params: {'type': 'photo'},
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                getTravel: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                },
                changeTravel: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                getSpot: {
                    method: 'GET',
                    params: {'type': 'spot'}
                },
                changeSpot: {
                    method: 'POST',
                    params: {'type': 'spot'},
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                deletePhoto: {
                    method: 'DELETE',
                    params: {'type': 'photo'},
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .service('LoginUserService', ['$window', '$resource', '$rootScope',
        function ($window, $resource, $rootScope) {
            this.getUserId = function() {
                return $rootScope.user.id;
            };
        }])

;