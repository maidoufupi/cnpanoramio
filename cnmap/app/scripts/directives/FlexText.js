/**
 * Created by any on 2014/5/19.
 */
'use strict';
angular.module('ponmApp.directives')
    .directive('flexText', ['$compile', '$rootScope', '$log',
        function ($compile, $rootScope, $log) {

            // Constructor
            function FT(elem, opts) {
                this.$textarea = elem;
                this.options = opts;
                this._init();
            }

            FT.prototype = {
                _init: function () {
                    var _this = this;

                    // Insert wrapper elem & pre/span for textarea mirroring
                    this.$textarea.wrap('<div class="flex-text-wrap" />').before('<pre><span /><br /></pre>');
                    if(_this.options.hasUnderline) {
                        this.$textarea.after('<div class="under-line"/>');
                    }

                    this.$span = this.$textarea.parent().find('span');
                    this.$underLine = this.$textarea.parent().find('.under-line');

                    // Add input event listeners
                    // * input for modern browsers
                    // * propertychange for IE 7 & 8
                    // * keyup for IE >= 9: catches keyboard-triggered undos/cuts/deletes
                    // * change for IE >= 9: catches mouse-triggered undos/cuts/deletions (when textarea loses focus)
                    this.$textarea.on('input propertychange keyup change', function () {
                        _this._mirror();
                    });

                    this.$textarea.on('focus', function() {
                        if(_this.$underLine) {
                            _this.$underLine.addClass("active");
                        }
                    });

                    this.$textarea.on('blur', function() {
                        if(_this.$underLine) {
                            _this.$underLine.removeClass("active");
                        }
                    });

                    // jQuery val() strips carriage return chars by default (see http://api.jquery.com/val/)
                    // This causes issues in IE7, but a valHook can be used to preserve these chars
//                    $.valHooks.textarea = {
//                        get: function (elem) {
//                            return elem.value.replace(/\r?\n/g, "\r\n");
//                        }
//                    };

                    // Mirror contents once on init
                    this._mirror();
                }

                // Mirror pre/span & textarea contents
                ,_mirror: function () {
                    this.$span.text(this.$textarea.val());
                }
            };

            return {
                restrict: 'A',
//            require: '',
                link: function (scope, elem, attrs) {

                    var ft = new FT(elem, {hasUnderline: !!attrs.hasUnderline});

                    scope.$watch(function() {
                        return elem.val();
                    }, function(val) {
                        ft._mirror();
                    });

                }
            };
        }]);