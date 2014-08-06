/**
 * Created by any on 2014/8/5.
 */
'use strict';

angular.module('ponmApp.directives')
.directive('itemPicker', ['$parse', '$log',
    function($parse, $log) {

        return {
            restrict: 'EA',
            require: 'ngModel',
            scope: {
                loadData: "&",
                newData: "&",
                itemValueName: "@"
            },
            templateUrl: 'views/tagPickerView.html',
            link: function(scope, element, attrs, ngModel) {

                var dataType = "String";

                scope.itemValueName = scope.itemValueName || "value";
                scope.placeHolder = attrs.placeHolder || "添加";

                var items = null;
                scope.selectedItems = [];

                var inputE = element.find('div.dropdown-toggle');
                inputE.on('focus click', function() {
                    if(!items) {
                        loadDataF();
                        scope.$apply(function() {
                            return scope.items;
                        })
                    }
                });

                function loadDataF() {
                    if(scope.loadData) {
                        var loadDataRef;
                        if(loadDataRef = scope.loadData()) {
                            scope.loadingItems = true;
                            items = loadDataRef(function(res) {
                                items = res;
                                convertItems();
                                scope.loadingItems = false;
                            });
                            convertItems();
                        }else {
                            $log.error("load-data function " + attrs.loadData + " is not defined!")
                        }
                    }else {
                        $log.error("load-data attribute is not defined!");
                    }
                }

                function convertItems() {
                    scope.items = [];
                    if(angular.isArray(items) && items.length) {
                        scope.loadingItems = false;
                    }
                    angular.forEach(items, function(item, key) {
                        if(angular.isObject(item)) {
                            dataType = "Object";
                            scope.items.push({
                                value: item[scope.itemValueName],
                                id: item.id,
                                $key: key
                            })
                        }else if(angular.isString(item)) {
                            dataType = "String";
                            scope.items.push({
                                value: item,
                                $key: key
                            })
                        }

                    })
                }

                angular.element('.dropdown-menu').on('click', function(event) {
                    event.preventDefault();
                    event.stopPropagation();
                });

                scope.setItem = function(item) {
                    var viewValue = [];
                    // 多选
                    if(scope.multipleSelect) {
                        scope.selectedItems = [];
//                            var itemValues = [];
                        item.$active = !item.$active;

                        angular.forEach(scope.items, function(item, key) {
                            if(item.$active) {
                                scope.selectedItems.push(item);
                                viewValue.push(items[item.$key]);
//                                    itemValues.push(item.value);
                            }
                        });
//                            scope.item = itemValues.join(attrs.multipleSelect || ",");

                    }else {
                        // 单选
                        if(item.$active) {
                            return;
                        }
//                            scope.item = item.value;
                        angular.forEach(scope.items, function(item, key) {
                            item.$active = false;
                        });
                        item.$active = true;
                        scope.selectedItems = [item];
                        viewValue = items[item.$key];
                    }
                    scope.originalItemValue = scope.item;
                    ngModel.$setViewValue(viewValue);
                };

                scope.clearItems = function() {
                    scope.originalItemValue = "";
                    scope.item = "";
                    scope.items = null;
                };

//                    scope.$watch('item', function(newItem) {
//                        if(newItem != scope.originalItemValue) {
//                            angular.forEach(scope.items, function(item, key) {
//                                item.$active = false;
//                            })
//                        }
//                    });

                /**
                 * 创建新值
                 *
                 * @param value
                 * @returns {boolean}
                 */
                scope.createData = function(value) {
                    if(value) {
                        if(scope.newData) {
                            if(items = scope.newData()(value, function(res) {
                                items = res;
                                scope.newItem = '';
                                convertItems();
                            })) {
                                scope.newItem = '';
                                scope.clearItems();
                                convertItems();
                            }
                        }
                    }

                };

                //是否允许多选
                if(attrs.multipleSelect === undefined || attrs.multipleSelect === "false") {
                    scope.multipleSelect = false;
                }else {
                    scope.multipleSelect = true;
                }
            }
        }
    }])

    .directive('contenteditable', ['$parse', function ($parse) {
        return {
            require: 'ngModel',
            scope: {
                ngModel: "@"
            },
            link: function (scope, elm, attrs, ctrl) {

                elm.placeholder = attrs.placeHolder;

                elm.css("cursor", "pointer");

                // view -> model
                elm.on('blur', function () {

                    if(attrs.multipleLine) {
//                        elm.css("height", '1.5em');
                        elm.css("overflow", 'hidden');
                        scope.$apply(function () {
                            if (elm.placeholder != elm.html()) {
                                var htmlValue = "";
                                var contents = elm.contents();
                                angular.forEach(contents, function(content, key) {
                                    if(content instanceof String) {
                                        if(htmlValue) {
                                            htmlValue += "\n";
                                        }
                                        htmlValue = htmlValue + content;
                                    }else {
                                        if(content.textContent != "") {
                                            if(htmlValue) {
                                                htmlValue += "\n";
                                            }
                                            htmlValue = htmlValue + content.textContent;
                                        }
//                                        htmlValue = htmlValue + angular.element(content).text();
                                    }
                                });
                                ctrl.$setViewValue(htmlValue);
                            }
                            ctrl.$render();
                        });
                    }else {
                        scope.$apply(function () {
                            if (elm.placeholder != elm.html()) {
                                ctrl.$setViewValue(elm.html());
                            }
                            ctrl.$render();
                        });
                    }
                });

                elm.on('focus', function () {
                    scope.$apply(function () {
                        if (elm.placeholder == elm.html()) {
                            removePlaceHolder();
                        }
                    });
                    if(attrs.multipleLine) {
//                        elm.css("height", (attrs.multipleLine || 4) + 'em');
                    }
                });

                // model -> view
                ctrl.$render = function () {
                    if (ctrl.$viewValue) {
                        elm.html(ctrl.$viewValue);
                    } else {
                        addPlaceHolder();
                    }
                };

                // render init value
                ctrl.$render();

                function addPlaceHolder() {
                    elm.css('color', 'grey');
                    elm.html(elm.placeholder);
                }

                function removePlaceHolder() {
                    elm.css('color', '');
                    // 针对firefox如果div无内容则height为0的bug
//                    elm.css('height', elm.height());
                    elm.html("")
                }

                if (attrs.editable && !scope.$eval(attrs.editable)) {
                    elm.on('input', function (event) {
                        elm.html(ctrl.$viewValue);
                        event.preventDefault();
                        event.stopPropagation();
                    });
                    elm.on('keypress keydown keyup onchange', function (event) {
                        event.preventDefault();
                        event.stopPropagation();
                    });
                }

                if(attrs.multipleLine) {

                }else {
                    elm.on('keypress', function (event) {
                        if (event.keyCode == 13) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                    })
                }

                elm.on("mouseenter", function(e) {
                    ctrl.$render();
                    scope.$apply(function() {
                        scope.mouseEnter = true;
                    });
                });
                elm.on("mouseleave", function(e) {
                    scope.$apply(function() {
                        scope.mouseEnter = false;
                    });
                });

            }
        };
    }])
;