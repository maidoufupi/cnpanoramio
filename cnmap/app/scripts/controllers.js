'use strict';
angular.module('ponmApp.controllers', ['ponmApp', 'xeditable', 'ui.map'])
    .run(['editableOptions', function(editableOptions) {
        editableOptions.theme = 'bs3';
    }])
    ;
