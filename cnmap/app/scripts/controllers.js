'use strict';
angular.module('ponmApp.controllers',
    ['ngAnimate', 'ponmApp', 'xeditable', 'ui.map', 'ui.bootstrap'])
    .run(['editableOptions', 'editableThemes', function(editableOptions, editableThemes) {
        editableOptions.theme = 'bs3';
        // overwrite submit button template
        editableThemes['bs3'].submitTpl = '<button type="submit" class="btn btn-info">提交</button>';
        editableThemes['bs3'].cancelTpl = '<button type="button" class="btn btn-default" ng-click="$form.$cancel()">取消</button>';
    }])
    ;
