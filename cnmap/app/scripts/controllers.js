'use strict';
angular.module('ponmApp.controllers', ['ponmApp', 'xeditable'])
    .run(['editableOptions', function(editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    ;
