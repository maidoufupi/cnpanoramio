/**
 * Created by any on 2014/4/23.
 */
'use strict';

angular.module('ponmApp.directives', [
    'ngResource'])
    .directive('passwordMatch', [function () {
        return {
            require: 'ngModel',
            link: function (scope, elem , attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {
                    //get the value of the other password
                    var e2 = scope.$eval(attrs.passwordMatch);
                    //set the form control to valid if both
                    //passwords are the same, else invalid
                    ctrl.$setValidity("unique", viewValue == e2);
                    return viewValue;
                });

                scope.$watch(attrs.passwordMatch, function(e2) {
                    var e1 = ctrl.$viewValue;
                    ctrl.$setValidity("unique", e1 == e2);
                })
            }
        };
    }])
    .directive('passwordStrength', [function () {
        return {
            replace: false,
            restrict: 'A',
            link: function (scope, elem, attrs) {

                var strength = {
                    colors: ['#F00', '#F90', '#FF0', '#9F0', '#0F0'],
                    mesureStrength: function (p) {

                        var _force = 0;
                        var _regex = /[$-/:-?{-~!"^_`\[\]]/g;

                        var _lowerLetters = /[a-z]+/.test(p);
                        var _upperLetters = /[A-Z]+/.test(p);
                        var _numbers = /[0-9]+/.test(p);
                        var _symbols = _regex.test(p);

                        var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];
                        var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;

                        _force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
                        _force += _passedMatches * 10;

                        // penality (short password)
                        _force = (p.length <= 6) ? Math.min(_force, 10) : _force;

                        // penality (poor variety of characters)
                        _force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
                        _force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
                        _force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;

                        return _force;

                    },
                    getColor: function (s) {

                        var idx = 0;
                        if (s <= 10) { idx = 0; }
                        else if (s <= 20) { idx = 1; }
                        else if (s <= 30) { idx = 2; }
                        else if (s <= 40) { idx = 3; }
                        else { idx = 4; }

                        return { idx: idx + 1, col: this.colors[idx] };

                    }
                };

                scope.$watch(attrs.passwordStrength, function (password) {
                    if (!password) {
                        elem.css({ "display": "none"  });
                    } else {
                        var c = strength.getColor(strength.mesureStrength(password));
                        elem.css({ "display": "inline" });
                        elem.children('li')
                            .css({ "background": "#DDD" });
                        for(var i=0;i < c.idx; i++) {
                            angular.element(elem.children('li')[i])
                                .css({ "background": c.col });
                        }

                    }
                });

            },
            template: '<li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>'
        };
    }])
    .directive( 'ngRemoteValidate',
    [ '$http', '$timeout', '$q', '$log',
        function( $http, $timeout, $q, $log ) {

            var directiveId = 'ngRemoteValidate';

            return {
                restrict: 'A',
                require: 'ngModel',
                link: function( scope, el, attrs, ngModel ) {
                    var cache = {},
                        handleChange,
                        setValidation,
                        addToCache,
                        request,
                        shouldProcess,
                        options = {
                            ngRemoteThrottle: 400,
                            ngRemoteMethod: 'POST'
                        };

                    angular.extend( options, attrs );

                    if( options.ngRemoteValidate.charAt( 0 ) === '[' ) {
                        options.urls = eval( options.ngRemoteValidate );
                    } else {
                        options.urls = [ options.ngRemoteValidate ];
                    }

                    addToCache = function( response ) {
                        var value = response[ 0 ].data.value;
                        if ( cache[ value ] ) return cache[ value ];
                        cache[ value ] = response;
                    };

                    shouldProcess = function( value ) {
                        var otherRulesInValid = false;
                        for ( var p in ngModel.$error ) {
                            if ( ngModel.$error[ p ] && p != directiveId ) {
                                otherRulesInValid = true;
                                break;
                            }
                        }
                        return !( ngModel.$pristine || otherRulesInValid );
                    };

                    setValidation = function( response, skipCache ) {
                        var i = 0,
                            l = response.length,
                            isValid = true;
                        for( ; i < l; i++ ) {
                            if( !response[ i ].data.isValid ) {
                                isValid = false;
                                break;
                            }
                        }
                        if( !skipCache ) {
                            addToCache( response );
                        }
                        ngModel.$setValidity( directiveId, isValid );
                        el.removeClass( 'ng-processing' );
                        ngModel.$processing = false;
                    };

                    handleChange = function( value ) {
                        if( typeof value === 'undefined' ) return;

                        if ( !shouldProcess( value ) ) {
                            return setValidation( [ { data: { isValid: true, value: value } } ], true );
                        }

                        if ( cache[ value ] ) {
                            return setValidation( cache[ value ], true );
                        }

                        if ( request ) {
                            $timeout.cancel( request );
                        }

                        request = $timeout( function( ) {
                            el.addClass( 'ng-processing' );
                            ngModel.$processing = true;
                            var calls = [],
                                i = 0,
                                l = options.urls.length,
                                toValidate = { value: value },
                                httpOpts = { method: options.ngRemoteMethod };

                            if(options.ngRemoteMethod == 'POST'){
                                httpOpts.data = toValidate;
                            } else {
                                httpOpts.params = toValidate;
                            }

                            for( ; i < l; i++ ) {

                                httpOpts.url =  options.urls[ i ];
                                calls.push( $http( httpOpts ) );
                            }
                            $q.all( calls ).then(setValidation);

                        }, options.ngRemoteThrottle );
                        return true;
                    };

                    scope.$watch( function( ) {
                        return ngModel.$viewValue;
                    }, handleChange );
                }
            };

    }] )
    .directive('ngModelValidate', [function () {
        return {
            require: 'ngModel',
            scope: {
                ngModelValidate: "&"
            },
            link: function (scope, elem , attrs, ctrl) {
                var ngModelValidate = scope.ngModelValidate && scope.ngModelValidate();
                ctrl.$parsers.unshift(function(viewValue) {
                    if(!ngModelValidate) {
                        ngModelValidate = scope.ngModelValidate && scope.ngModelValidate();
                    }
                    if(ngModelValidate) {
                        var valid = ngModelValidate(viewValue);
                        ctrl.$setValidity(valid.errorKey, valid.isValid);
                        if(valid.isValid) {
                            return viewValue;
                        }else {
                            return undefined;
                        }
                    }
                    return viewValue;
                });
            }
        };
    }])
;