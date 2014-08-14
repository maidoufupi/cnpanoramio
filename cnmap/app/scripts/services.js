/**
 * Created by any on 2014/4/26.
 */
'use strict';

angular.module('ponmApp.services', [
    'ngResource'])
    .factory('AuthService', ['$window', '$resource', '$cookies', '$log', '$q', 'ponmCtxConfig',
        function ($window, $resource, $cookies, $log, $q, ponmCtxConfig) {

            var loginService = $resource($window.apirest + '/auth/login');

        function login(user) {
            var deferred = $q.defer();
            loginService.save({username: user.username, password: user.password},
                function(res) {
                    $log.debug(res);
                    if(res.status == "OK") {
                        $log.debug("login success")
                        ponmCtxConfig.userId = res.user.id;
                        ponmCtxConfig.username = res.user.username;
                        ponmCtxConfig.name = res.user.name;
                        ponmCtxConfig.avatar = res.user.avatar;
                        ponmCtxConfig.login = true;

                        $log.debug($cookies.JSESSIONID);
                        $log.debug($cookies);
                        deferred.resolve();
                    }else {
                        deferred.reject(1);
                    }
                });
            return deferred.promise;
        }

        function checkLogin() {
            var deferred = $q.defer();
            if(ponmCtxConfig.login && ponmCtxConfig.userId) {
                deferred.resolve();
            }else {
                loginService.get({}, function(res) {
                    if(res.status == "OK") {
                        ponmCtxConfig.userId = res.user.id;
                        ponmCtxConfig.username = res.user.username;
                        ponmCtxConfig.name = res.user.name;
                        ponmCtxConfig.avatar = res.user.avatar;
                        ponmCtxConfig.login = true;

                        $log.debug(res.user);
                        deferred.resolve();
                    }else if(res.status == "NO_AUTHORIZE") {
                        deferred.reject(1);
                    }
                });
            }

            return deferred.promise;
        }

        return {
            login: login,
            checkLogin: checkLogin
        };
    }])
    .factory('CommentService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/comment/:commentId/:type',
            {commentId: "@id"},
            {
                modify: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
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
                },
                getBounds: {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('UserService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/:type/:value/:action',
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
                },
                getRecycleBin: {
                    method: 'GET',
                    params: {'type': 'recycle'}
                },
                emptyRecycleBin: {
                    method: 'DELETE',
                    params: {'type': 'recycle'}
                },
                removeRecycle: {
                    method: 'DELETE',
                    params: {'type': 'recycle'}
                },
                cancelRecycle: {
                    method: 'GET',
                    params: {'type': 'recycle', action: 'cancel'}
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
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
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
        return $resource($window.apirest + '/travel/:travelId/:type/:typeId/:action',
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
                createSpot: {
                    method: 'POST',
                    params: {
                        'type': 'spot'
                    }
                },
                addSpotPhoto: {
                    method: 'POST',
                    params: {
                        'type': 'spot',
                        'action': 'photo'
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                deleteSpot: {
                    method: 'DELETE',
                    params: {
                        'type': 'spot'
                    }
                },
                deletePhoto: {
                    method: 'DELETE',
                    params: {'type': 'photo'},
                    headers: {
                        'Accept': 'application/json'
                    }
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
                }
            });
    }])
    .factory('PanoramioService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/panoramio/:action',
            {'id': '@id'},
            {
                getLatest: {
                    method: 'GET',
                    params: {
                        action: 'photo',
                        latest: true
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                search: {
                    method: 'GET',
                    params: {
                        action: 'search',
                        type: 'all'
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
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
    .factory('safeApply', [function($rootScope) {
        return function($scope, fn) {
            var phase = $scope.$root && $scope.$root.$$phase;
            if(phase == '$apply' || phase == '$digest') {
                if (fn) {
                    $scope.$eval(fn);
                }
            } else {
                if (fn) {
                    $scope.$apply(fn);
                } else {
                    $scope.$apply();
                }
            }
        }
    }])
    .factory('ponmCtxConfig', ['$window', '$resource', function ($window, $resource) {
        return {
            ctx: $window.ctx,
            staticCtx: $window.staticCtx || "http://static.photoshows.cn",
            corsproxyCtx: $window.corsproxyCtx || "http://www.corsproxy.com/static.photoshows.cn",
            apirest: $window.apirest
            ,userId: $window.user && $window.user.id
            ,name: $window.user && $window.user.name
            ,login: $window.login
        }
    }])
;