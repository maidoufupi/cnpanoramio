/**
 * Created by any on 2014/7/4.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmRectMultiSelect',
    [        '$log', '$parse', 'safeApply',
    function ($log,   $parse,   safeApply) {

        function checkMaxMinPos(a, b, aW, aH, bW, bH, maxX, minX, maxY, minY) {
            'use strict';

            if (a.left < b.left) {
                if (a.left < minX) {
                    minX = a.left;
                }
            } else {
                if (b.left < minX) {
                    minX = b.left;
                }
            }

            if (a.left + aW > b.left + bW) {
                if (a.left > maxX) {
                    maxX = a.left + aW;
                }
            } else {
                if (b.left + bW > maxX) {
                    maxX = b.left + bW;
                }
            }
            ////////////////////////////////
            if (a.top < b.top) {
                if (a.top < minY) {
                    minY = a.top;
                }
            } else {
                if (b.top < minY) {
                    minY = b.top;
                }
            }

            if (a.top + aH > b.top + bH) {
                if (a.top > maxY) {
                    maxY = a.top + aH;
                }
            } else {
                if (b.top + bH > maxY) {
                    maxY = b.top + bH;
                }
            }

            return {
                'maxX': maxX,
                'minX': minX,
                'maxY': maxY,
                'minY': minY
            };
        }

        var defaults = {
            selector: ".photo",
            moveselect: true
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                var options;
                var opt = scope.$eval(attrs.ponmRectMultiSelect || "{}");
                options = angular.extend({}, defaults, opt);

                var initialW = 0,
                    initialH = 0,
                    mousedown = false;

                var ghostSelect = angular.element('<div class="ghost-select"><span></span></div>');
                var ghostSelectGrid = angular.element('<div id="grid"></div>').append(ghostSelect);
                element.prepend(ghostSelectGrid);

                var bigGhost = angular.element("<div id='big-ghost' class='big-ghost'></div>");

                element.append(bigGhost);

                var offsetX = 0,
                  offsetY = 0;

                element.bind('mousedown', function (e) {

                  offsetX = element.offset().left;
                  offsetY = element.offset().top;

                    var mouseX = e.pageX - offsetX,
                        mouseY = e.pageY - offsetY;

                    bigGhost.removeClass("ponm-show");
                    ghostSelect.addClass("ponm-show");
                    ghostSelect.css({
                        'left': mouseX,
                        'top': mouseY
                    });

                    initialW = mouseX;
                    initialH = mouseY;

                    mousedown = true;

                });
//                element.bind("mouseup", selectElements);
                $(document).bind("mousemove", openSelector);
//                $(document).bind("scroll", openSelector);
                $(document).bind("mouseup", function(e) {
                    if(!mousedown) {
                        return;
                    }
                    mousedown = false;

                    var w = Math.abs(initialW - e.pageX + offsetX);
                    var h = Math.abs(initialH - e.pageY + offsetY);

                    if(w < 10 && h < 10) {
                        return;
                    }
                    selectElements(e);

                    ghostSelect.removeClass("ponm-show");
                    ghostSelect.width(0).height(0);
                });
//                $(document).bind("mousemove", openSelector);

                function openSelector(e) {
                    if(!mousedown) {
                        return;
                    }

                    e.preventDefault();

                    var mouseX = e.pageX - offsetX,
                        mouseY = e.pageY - offsetY;

//                    $log.debug("mouseX: " +  mouseX + " mouseY: " +  mouseY);

                    var w = Math.abs(initialW - mouseX);
                    var h = Math.abs(initialH - mouseY);

                    if(w < 10 && h < 10) {
                        return;
                    }

                    ghostSelect.css({
                        'width': w,
                        'height': h
                    });
                    if (mouseX <= initialW && mouseY >= initialH) {
                        ghostSelect.css({
                            'left': mouseX
                        });
                    } else if (mouseY <= initialH && mouseX >= initialW) {
                        ghostSelect.css({
                            'top': mouseY
                        });
                    } else if (mouseY < initialH && mouseX < initialW) {
                        ghostSelect.css({
                            'left': mouseX,
                            "top": mouseY
                        });
                    }

                    if(options.moveselect) {
                        selectElements(e);
                    }
                }

                function selectElements(e) {

                    var maxX = 0;
                    var minX = 5000;
                    var maxY = 0;
                    var minY = 5000;
                    var totalElements = 0;
                    var elementArr = new Array();
                    var setter  = $parse(attrs.ponmRectSelector || 'selected').assign;
                    angular.forEach(element.find(options.selector),
                        function(bElem, key) {

                        bElem = angular.element(bElem);
                        var scope = bElem.scope();
                        var result = doObjectsCollide(ghostSelect, bElem);

                        if (result == true) {
                            safeApply(scope, function() {
                                setter(scope, true);
                            });
                            return;

                            var aElemPos = bElem.offset();
                            var bElemPos = bElem.offset();
                            var aW = bElem.width();
                            var aH = bElem.height();
                            var bW = bElem.width();
                            var bH = bElem.height();

                            var coords = checkMaxMinPos(aElemPos, bElemPos, aW, aH, bW, bH, maxX, minX, maxY, minY);
                            maxX = coords.maxX;
                            minX = coords.minX;
                            maxY = coords.maxY;
                            minY = coords.minY;
                            var parent = bElem.parent();

                            //console.log(aElem, bElem,maxX, minX, maxY,minY);
                            if (bElem.css("left") === "auto" && bElem.css("top") === "auto") {
                                bElem.css({
                                    'left': parent.css('left'),
                                    'top': parent.css('top')
                                });
                            }
                            bigGhost.addClass("ponm-show");
                            bigGhost.attr("x", Number(minX - 20));
                            bigGhost.attr("y", Number(minY - 10));

                            bigGhost.css({
                                'width': maxX + 40 - minX,
                                'height': maxY + 20 - minY,
                                'top': minY - 10,
                                'left': minX - 20
                            });


                        }else if(!e.shiftKey) {
                            safeApply(scope, function() {
                                setter(scope, false);
                            });
                        }
                    });

                }

                function doObjectsCollide(a, b) { // a and b are your objects
                    var aTop = a.offset().top;
                    var aLeft = a.offset().left;
                    var bTop = b.offset().top;
                    var bLeft = b.offset().left;

                    return !(
                        ((aTop + a.outerHeight()) < (bTop)) ||
                        (aTop > (bTop + b.outerHeight())) ||
                        ((aLeft + a.outerWidth()) < bLeft) ||
                        (aLeft > (bLeft + b.outerWidth()))
                        );
                }
            }
        }
    }]);
