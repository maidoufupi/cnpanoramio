/**
 * Created by any on 2014/9/3.
 */
/**
 * @ngdoc overview
 * @name ponmApp.dynamic
 *
 * @description
 * user's dynamic message page
 *
 */
angular.module('ponmApp.user', [
    'ui.bootstrap',
    'ngAnimate',
    'ui.router',
    'ponmApp',
    'xeditable',
    'perfect_scrollbar'
])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
                .when('/user/{userId:[0-9]{1,10}}', '/user/{userId:[0-9]{1,10}}/dynamic')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
//                .otherwise('/popular')
            ;
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////
                .state('user', {

                    // With abstract set to true, that means this state can not be explicitly activated.
                    // It can only be implicitly activated by activating one of it's children.
                    abstract: true,

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/user/{userId:[0-9]{1,10}}',

                    views: {

                        // the main template will be placed here (relatively named)
                        '': { templateUrl: 'views/user.html',
                            controller: 'UserCtrl'},
                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                        ,'alert': {
                            templateUrl: 'views/ponm.alert.html',
                            controller: 'AlertsCtrl'
                        }
                    }
                })
                .state('user.dynamic', {
                    url: '/dynamic',
                    templateUrl: 'views/user.dynamic.html',
                    resolve: {
                    },
                    controller: "UserDynamicCtrl"
                })
                .state('user.message', {
                    url: '/message/{messageId:[0-9]{1,10}}',
                    templateUrl: 'views/user.message.html',
                    resolve: {
                    },
                    controller: "UserMessageCtrl"
                })
                .state('user.following', {
                    url: '/following',
                    templateUrl: 'views/user.following.html',
                    resolve: {
                    },
                    controller: "UserFollowingCtrl"
                })
                .state('user.followers', {
                    url: '/followers',
                    templateUrl: 'views/user.followers.html',
                    resolve: {
                    },
                    controller: "UserFollowersCtrl"
                })
                .state('user.likes', {
                    url: '/likes',
                    templateUrl: 'views/user.likes.html',
                    resolve: {
                    },
                    controller: "UserLikesCtrl"
                })
            ;
        }])
    .run(['$rootScope', '$window', 'editableOptions',
        function ($rootScope, $window, editableOptions) {
            editableOptions.theme = 'bs3';
        }])
    .factory('MyMessageManager', ['$q', 'UserService', function ($q, UserService) {

        function MessageManager(userId) {

            this.userId = userId;

            this.pageSize = 10;
            this.pageNo = 1;
            this.ended = false;

            this.getMoreMessages = function() {
                var deferred = $q.defer();
                if(this.loading) {
                    deferred.reject("loading");
                }
                if(this.ended) {
                    deferred.reject("ended");
                }
                this.loading = true;
                var that = this;
                UserService.getMyMessages({userId: this.userId, value: this.pageSize, action: this.pageNo},
                    function(res) {
                        that.loading = false;
//                    $log.debug(res);
                        if(res.status == "OK") {
                            that.pageNo++;
                            if(res.messages.length < that.pageSize) {
                                that.ended = true;
                            }
                            deferred.resolve(res.messages);
                        }else {
                            deferred.reject(res.status);
                        }
                    }, function(error) {
                        that.loading = false;
                        deferred.reject(error);
                    });
                return deferred.promise;
            }
        }

        return MessageManager;
    }])
    .controller('UserCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'UserService',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  UserService) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.login = ponmCtxConfig.login;
            $scope.ponmCtxConfig = ponmCtxConfig;
            $scope.$state = $state;

            $scope.userId = $stateParams.userId;

            // 获取图片的用户信息
            UserService.getOpenInfo({userId: $scope.userId}, function (data) {
                if (data.status == "OK") {
                    $scope.user = data.open_info;
                }
            });

//            UserService.getFollowSuggested({userId: $scope.userId}, function(res) {
//                $log.debug(res);
//                if(res.status == "OK") {
//                    $scope.followSuggested = res.follow;
//                }
//            });

            $scope.trendItems = [
                "室内", "全景", "上海"
            ];

            $scope.displayPhoto = function(photoId) {

                $scope.photoDisplayModal = $modal.open({
                    templateUrl: 'views/photo.html',
                    controller: 'PhotoModalCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                        photoId: function () {
                            return photoId;
                        },
                        travelId: function() {
                            return '';
                        }
                    }
                });

                $scope.photoDisplayModal.result.then(function (selectedItem) {
                    $scope.photoDisplayModal = null;
                }, function () {
                    $scope.photoDisplayModal = null;
                });
            };

            $scope.$on('photo.click', function(e, photoId) {
                $scope.displayPhoto(photoId);
            });
        }])
    .controller('UserDynamicCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'MyMessageManager',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  MyMessageManager) {

            var messageManager = new MyMessageManager($scope.userId);
            $scope.messageManager = messageManager;

            $scope.messages = [];

            $scope.onLoadWaypoint = function(element, direction) {
                $log.debug("load more " + direction);

                messageManager.getMoreMessages().then(function (messages) {
//                    $scope.messages = $scope.messages.concat(messages);
                    $scope.$broadcast("message.add", messages);
                });
            };

            $scope.onLoadWaypoint();

            $scope.$on("message.shared", function(e, message) {
                if(message.user.id == ponmCtxConfig.id) {
                    $scope.$broadcast("message.insert", message);
//                    $scope.messages.splice(0, 0, message);
                }
            });
        }])
    .controller('UserFollowingCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'UserService',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  UserService) {

            $scope.followings = [];

            UserService.getCircles({userId: $scope.userId}, function(res) {
                if (res.status == "OK") {
                    angular.forEach(res.circles, function(circle, key) {
                        angular.forEach(circle.users, function(user, key) {
                            user.follow = true;
                        });
                        $scope.followings = $scope.followings.concat(circle.users);
                    });
                }
            });
        }])
    .controller('UserFollowersCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'UserService',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  UserService) {

            // 获取图片的用户信息
            UserService.getFollowers({userId: $scope.userId}, function (data) {
                if (data.status == "OK") {
                    $scope.followers = data.followers;
                }
            });
        }])
    .controller('UserLikesCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'HashStateManager',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  HashStateManager) {
        }])
    .controller('UserMessageCtrl',
    [   '$scope', '$q', 'ponmCtxConfig', '$log', '$state', '$stateParams', 'MessageService',
        function ($scope, $q, ponmCtxConfig, $log, $state, $stateParams, MessageService) {

            $scope.messageId = $stateParams.messageId;
            $log.debug("message id : " + $scope.messageId);

            $scope.messages = [];
            MessageService.get({id: $scope.messageId}, function(res) {
                if(res.status == "OK") {
                    $scope.messages.push(res.message);
                }
            });
        }])
;
