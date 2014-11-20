/**
 * Created by any on 2014/11/3.
 */
'use strict';

angular.module('ponmApp', [
  'ponmApp.directives',
  'ponmApp.services'
])
    .config(['$logProvider',
        function ($logProvider ) {
            // enable log debug level
            $logProvider.debugEnabled = false;
        }])
;
