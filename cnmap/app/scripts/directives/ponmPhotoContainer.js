/**
 * Created by any on 2014/5/5.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('ponmPhotoContainer', ['$window', '$parse', '$animate', '$log',
        function ($window, $parse, $animate, $log) {

            var draw360 = function(container, imageSrc, width, height) {
                var camera, scene, renderer;

                var onPointerDownPointerX,
                    onPointerDownPointerY,
                    onPointerDownLon,
                    onPointerDownLat;

                var texture_placeholder,
                    isUserInteracting = false,
                    isAutoWheel = true,
                    onMouseDownMouseX = 0, onMouseDownMouseY = 0,
                    lon = 0, onMouseDownLon = 0,
                    lat = 0, onMouseDownLat = 0,
                    phi = 0, theta = 0;

                init();
                animate();

                function init() {

                    var mesh;

                    camera = new THREE.PerspectiveCamera( 75, 2, 1, 1100 );
                    camera.target = new THREE.Vector3( 0, 0, 0 );

                    scene = new THREE.Scene();

                    var geometry = new THREE.SphereGeometry( 500, 60, 40 );
                    geometry.applyMatrix( new THREE.Matrix4().makeScale( -1, 1, 1 ) );

                    var material = new THREE.MeshBasicMaterial( {
                        map: THREE.ImageUtils.loadTexture( imageSrc )
                    } );

                    mesh = new THREE.Mesh( geometry, material );

                    scene.add( mesh );

                    renderer = new THREE.WebGLRenderer();
                    renderer.setSize( container.innerWidth(), container.innerHeight());

//                    $animate.removeClass(container.find(".flat-image"), 'show');
                    container.append( renderer.domElement );
                    $animate.addClass(angular.element(renderer.domElement), 'show');
//                    $animate.addClass(container.find(".flat-image-button"), 'show');

                    renderer.domElement.addEventListener( 'mousedown', onDocumentMouseDown, false );
                    renderer.domElement.addEventListener( 'mousemove', onDocumentMouseMove, false );
                    renderer.domElement.addEventListener( 'mouseup', onDocumentMouseUp, false );
                    renderer.domElement.addEventListener( 'mouseleave', onDocumentMouseUp, false );
                    renderer.domElement.addEventListener( 'mousewheel', onDocumentMouseWheel, false );
                    renderer.domElement.addEventListener( 'DOMMouseScroll', onDocumentMouseWheel, false);

                    //
                    renderer.domElement.addEventListener( 'click', function ( event ) {

                        event.preventDefault();
                        isAutoWheel = !isAutoWheel;

                    }, false );

                    renderer.domElement.addEventListener( 'dragover', function ( event ) {

                        event.preventDefault();
                        event.dataTransfer.dropEffect = 'copy';

                    }, false );

                    renderer.domElement.addEventListener( 'dragenter', function ( event ) {

                        document.body.style.opacity = 0.5;

                    }, false );

                    renderer.domElement.addEventListener( 'dragleave', function ( event ) {

                        document.body.style.opacity = 1;

                    }, false );

                    renderer.domElement.addEventListener( 'drop', function ( event ) {

                        event.preventDefault();

                        var reader = new FileReader();
                        reader.addEventListener( 'load', function ( event ) {

                            material.map.image.src = event.target.result;
                            material.map.needsUpdate = true;

                        }, false );
                        reader.readAsDataURL( event.dataTransfer.files[ 0 ] );

                        document.body.style.opacity = 1;

                    }, false );

                    //
                    window.addEventListener( 'resize', onWindowResize, false );

                }

                function onWindowResize() {

//                    camera.aspect = width / height;
                    camera.updateProjectionMatrix();

                    renderer.setSize( container.innerWidth(), container.innerHeight());

                }

                function onDocumentMouseDown( event ) {

                    event.preventDefault();

                    isUserInteracting = true;

                    onPointerDownPointerX = event.clientX;
                    onPointerDownPointerY = event.clientY;

                    onPointerDownLon = lon;
                    onPointerDownLat = lat;

                }

                function onDocumentMouseMove( event ) {

                    if ( isUserInteracting === true ) {

                        lon = ( onPointerDownPointerX - event.clientX ) * 0.1 + onPointerDownLon;
                        lat = ( event.clientY - onPointerDownPointerY ) * 0.1 + onPointerDownLat;

                    }
                }

                function onDocumentMouseUp( event ) {
                    isUserInteracting = false;
                }

                function onDocumentMouseWheel( event ) {

                    event.preventDefault();

                    var fov = camera.fov;
                    // WebKit
                    if ( event.wheelDeltaY ) {

                        fov -= event.wheelDeltaY * 0.05;

                        // Opera / Explorer 9
                    } else if ( event.wheelDelta ) {

                        fov -= event.wheelDelta * 0.05;

                        // Firefox
                    } else if ( event.detail ) {

                        fov += event.detail * 1.0;

                    }

                    if(fov > 10 && fov < 150) {
                        camera.fov = fov;
                    }
                    $log.debug("camera.fov " + camera.fov);
                    camera.updateProjectionMatrix();

                }

                function animate() {

                    requestAnimationFrame( animate );
                    update();

                }

                function update() {

                    if ( isUserInteracting === false && isAutoWheel ) {

                        lon += 0.1;

                    }

                    lat = Math.max( - 85, Math.min( 85, lat ) );
                    phi = THREE.Math.degToRad( 90 - lat );
                    theta = THREE.Math.degToRad( lon );

                    camera.target.x = 500 * Math.sin( phi ) * Math.cos( theta );
                    camera.target.y = 500 * Math.cos( phi );
                    camera.target.z = 500 * Math.sin( phi ) * Math.sin( theta );

                    camera.lookAt( camera.target );

                    /*
                     // distortion
                     camera.position.copy( camera.target ).negate();
                     */

                    renderer.render( scene, camera );

                }

                return renderer.domElement;
            };

            return {
                restrict: 'EA',
                templateUrl: "views/ponmPhotoContainer.html",
                link: function (scope, element, attrs, ngModel) {

                    var image1 = null,
                        image2 = null;
                    var imgWidth = 1,
                        imgHeight = 1;
                    var ponmPhotoContainer = element.find(".ponm-photo-container"),
                        imgContainer = element.find(".flat-image"),
                        canvas = element.find(".p360-image"),
                        clickCreateP360 = element.find(".flat-image .image-button"),
                        clickBackFlat   = element.find(".p360-image .image-button");

                    // 360度全景图标
                    clickCreateP360.on("click", function(e) {
                        e.preventDefault();
                        $animate.removeClass(imgContainer, 'show');
                        $animate.addClass(canvas, "show");

                        if(canvas.find("canvas").length && clickCreateP360.ponmPhotoSrcL == attrs.ponmPhotoSrcL1) {
                            $animate.addClass(canvas, "show");
                        }else {
                            clickCreateP360.ponmPhotoSrcL = attrs.ponmPhotoSrcL1;
                            canvas.find(".p360-canvas").empty();
                            draw360(canvas.find(".p360-canvas"), attrs.ponmPhotoSrcL1, attrs.ponmPhotoWidth, attrs.ponmPhotoHeight);
                        }
                    });
                    clickCreateP360.mouseenter(function(e) {
                        clickCreateP360.find(".icon-p360").css("opacity", 1);
                    });
                    clickCreateP360.mouseout(function(e) {
                        clickCreateP360.find(".icon-p360").css("opacity", 0.5);
                    });

                    // 平面图标
                    clickBackFlat.on("click", function(e) {
                        e.preventDefault();
                        $log.debug("click on image360");
                        $animate.removeClass(canvas, "show");
                        $animate.addClass(imgContainer, 'show');
                    });
                    clickBackFlat.mouseenter(function(e) {
                        clickBackFlat.find(".icon-pflat").css("opacity", 1);
                    });
                    clickBackFlat.mouseout(function(e) {
                        clickBackFlat.find(".icon-pflat").css("opacity", 0.5);
                    });

                    element.css("width", "100%");
                    element.css("height", "100%");
                    element.css("background-color", attrs.ponmPhotoColor);

                    var containerWidth = element.innerWidth(),
                        containerHeight = element.innerHeight();

                    scope.$watch(function() {
                        return attrs.ponmPhotoIs360;
                    }, function(is360) {
//                       drawImage();
//                        changeP360(is360);
                    });

                    scope.$watch(function() {
                        return attrs.ponmPhotoSrcL1;
                    }, function() {
                        drawImage();
                    });

//                    scope.$watch(function() {
//                        return attrs.ponmPhotoWidth;
//                    }, function() {
//                        getImgWidthHeight();
//                        setImageWidthHeight();
//                    });

                    function getImgWidthHeight() {
                        imgWidth = attrs.ponmPhotoWidth;
                        imgHeight = attrs.ponmPhotoHeight;
                        imgScale = imgHeight / imgWidth;
                    }

                    function changeP360(is360) {
                        $log.debug("changeP360: " + is360);
                        if(is360) {
                            $animate.addClass(clickCreateP360, 'show');
                            $animate.addClass(imgContainer, 'p360');
                        }else {
                            $animate.removeClass(clickCreateP360, 'show');
                        }
                    }
                    var $panzoom = null;
                    function panzoom(panzoomDom) {
                        $panzoom = angular.element(panzoomDom).panzoom({
                            $zoomIn: element.parent().find(".zoom-in"),
                            $zoomOut: element.parent().find(".zoom-out"),
                            $zoomRange: element.parent().find(".zoom-range"),
                            $reset: element.parent().find(".reset"),
                            transition: true
                        });
                        $panzoom.parent().on('mousewheel.focal', function( e ) {
                            e.preventDefault();
                            var delta = e.delta || e.originalEvent.wheelDelta;
                            var zoomOut = delta ? delta < 0 : e.originalEvent.deltaY > 0;
                            $panzoom.panzoom('zoom', zoomOut, {
                                increment: 0.1,
                                animate: true,
                                focal: e
                            });
                        });
                        $panzoom.on('panzoomzoom', function(e, panzoom, scale, changed) {
                            if(scale < 1) {
                                $panzoom.panzoom("resetZoom");
                                $panzoom.panzoom("resetPan");
                            }else if(Math.abs(scale - 1) < 0.05) {
                                if(scale != 1) {
                                    $panzoom.panzoom("resetZoom");
                                }
                                $panzoom.panzoom("resetPan");
                            }
                        });
                    }

                    function panzoomReset() {
                        $panzoom.panzoom("reset");
                    }

                    function drawImage() {
                        if (!attrs.ponmPhotoSrcL1) {
                            return;
                        }

                        var is360 = attrs.ponmPhotoIs360 && attrs.ponmPhotoIs360 != "false";

                        if(imgContainer) {
                            imgContainer.find(".flat-canvas").empty();

                            changeP360(is360);

                            $animate.removeClass(canvas, "show");
                            $animate.addClass(imgContainer, 'show');
                        }
//                        if (attrs.ponmPhotoSrcL2) {
//                            var image2 = new Image();
//                            image2.onload = function () {
//                                $animate.addClass(angular.element(this), 'show');
//                            };
//                            image2.src = attrs.ponmPhotoSrcL2;
//                            element.append(image2);
//                        }
                        if (attrs.ponmPhotoSrcL1) {
                            image1 = new Image();
                            image1.onload = function () {
                                var img = angular.element(image1);
                                imgWidth = img.outerWidth();
                                imgHeight = img.outerHeight();
                                imgScale = imgHeight / imgWidth;
                                $log.debug("image: "+ imgWidth + "x" + imgHeight);

                                setImageWidthHeight();
                                $animate.addClass(angular.element(this), 'show');
                                if (image2) {
                                    $animate.removeClass(angular.element(image2), 'show');
                                }

                            };
                            if (image2) {
                                angular.element(image1).css("position", "absolute");
                                angular.element(image1).css("top", "0");
                            }
                            image1.src = attrs.ponmPhotoSrcL1;

//                            var panzoomDiv = angular.element("<div/>").append(image1);
                            imgContainer.find(".flat-canvas").append(image1);
//                            element.append(imgContainer);

                            panzoom(image1);
                            panzoomReset();

                        }
                    }
                    angular.element($window).resize(function (e) {
                        setImageWidthHeight();
                    });
                    // 图片高宽比例
                    var imgScale = null;
                    function setImageWidthHeight() {

                        containerWidth = element.innerWidth();
                        containerHeight = element.innerHeight();

                        ponmPhotoContainer.css("line-height", containerHeight+"px");

//                        if(imgScale < containerHeight / containerWidth) {
//                            var width = containerWidth < imgWidth ? containerWidth : imgWidth;
//                            imgContainer.css("width", width);
//                            imgContainer.css("height", width * imgScale);
//                        }else {
//                            var height = containerHeight < imgHeight ? containerHeight : imgHeight;
//                            imgContainer.css("height", height);
//                            imgContainer.css("width", height / imgScale);
//                        }
                    }
                }
            };
    }])
;