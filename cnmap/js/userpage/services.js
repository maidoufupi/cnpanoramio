'use strict';

/* Services */

var userPageServices = angular.module('userPageServices', ['ngResource']);

userPageServices.factory('UserPhoto', ['$resource',
  function($resource){
    return $resource('api/rest/photos/user/:userId.json', {}, {
      query: {method:'GET', params:{userId:'userId'}, isArray:true}
    });
  }]);
