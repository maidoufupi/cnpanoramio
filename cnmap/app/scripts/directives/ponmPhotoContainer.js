/**
 * Created by any on 2014/5/5.
 */
'use strict';

angular.module('ponmApp.directives')
  .directive('ponmPhotoContainer', ['$window', '$parse', '$animate', '$log', '$q',
    function ($window, $parse, $animate, $log, $q) {

      function Photosphere(image, maxSize) {
        this.image = image;
        this.maxSize = maxSize;// || gl.getParameter(gl.MAX_TEXTURE_SIZE);
        //this.worker = new Worker("worker.js");
      }

      Photosphere.prototype.loadPhotosphere = function (holder) {
        holder.innerHTML = "wait...";

        this.defer = $q.defer();

        this.holder = holder;

        if (this.canUseCanvas()) {
          var self = this;
          this.canDoWebGL();
          this.loadEXIF(function () {
            self.cropImage();
          });
        } else {
          // this is the ugly scroll backup.
          // for silly people on a really old browser!
          holder.innerHTML = "<div style='width:100%;height:100%;overflow-x:scroll;overflow-y:hidden'><div style='margin: 10px; background: #ddd; opacity: 0.6; width: 300px; height: 20px; padding: 4px; position: relative'>If you upgrade to a better browser this is 3D!</div><img style='height:100%;margin-top: -48px' src='" + this.image + "' /></div>";
        }

        return this.defer.promise;
      };

      Photosphere.prototype.canUseCanvas = function () {
        // return false; // debugging! i don't have a non-supporting browser :$
        // https://github.com/Modernizr/Modernizr/blob/master/feature-detects/canvas.js

        var elem = document.createElement('canvas');
        var ctx = elem.getContext && elem.getContext('2d');
        return !!ctx;
      };

      Photosphere.prototype.cropImage = function () {
        var self = this;
        // if have crop or resize
        if (self.exif && ((self.exif['crop_width'] != self.exif['full_width'])
          || (self.maxSize &&
          (self.maxSize < self.exif['full_width'] || self.maxSize < self.exif['full_height'])))) {
          if (this.image instanceof Image) {
//                        this.image.crossOrigin = "anonymous"; //"Anonymous";
            self.start3D(resize(this.image));
          } else {
            var img = new Image();
            img.crossOrigin = "anonymous";
            img.onload = function () {
              self.start3D(resize(img));
            };
            img.src = this.image;
          }
        } else {
          if (this.image instanceof Image) {
//                        this.image.crossOrigin = "anonymous"; //"Anonymous";
            self.start3D(resize(this.image));
          } else {
            var img = new Image();
            img.crossOrigin = "anonymous";
            img.onload = function () {
              self.start3D(img);
            };
            img.src = this.image;
          }
        }

        function resize(img) {
          var canvas = document.createElement('canvas');
          canvas.width = self.exif['full_width'];
          canvas.height = self.exif['full_height'];

          if (self.maxSize != undefined) {
            // Now check the size (too big and it'll fail)
            // http://snipplr.com/view/753/create-a-thumbnail-maintaining-aspect-ratio-using-gd/
            if (self.maxSize < canvas.width || self.maxSize < canvas.height) {
              var wRatio = self.maxSize / canvas.width;
              var hRatio = self.maxSize / canvas.height;
              if ((wRatio * canvas.height) < self.maxSize) {
                // Horizontal
                canvas.height = Math.ceil(wRatio * canvas.height);
                canvas.width = self.maxSize;
              } else { // Vertical
                canvas.width = Math.ceil(hRatio * canvas.width);
                canvas.height = self.maxSize;
              }
            }
          }

          var context = canvas.getContext("2d");

          context.fillStyle = "#000";
          context.fillRect(0, 0, canvas.width, canvas.height);
          context.drawImage(img,
            (self.exif['x'] / self.exif['full_width']) * canvas.width,
            (self.exif['y'] / self.exif['full_height']) * canvas.height,
            (self.exif['crop_width'] / self.exif['full_width']) * canvas.width,
            (self.exif['crop_height'] / self.exif['full_height']) * canvas.height
          );
          return canvas.toDataURL("image/png");
        }
      };

      Photosphere.prototype.canDoWebGL = function () {
        // Modified mini-Modernizr
        // https://github.com/Modernizr/Modernizr/blob/master/feature-detects/webgl-extensions.js
        var canvas, ctx, exts;
        if (this.isCanDoWebGL === undefined) {
          try {
            canvas = document.createElement('canvas');
            ctx = canvas.getContext('webgl') || canvas.getContext('experimental-webgl');
            exts = ctx.getSupportedExtensions();

            this.maxSize = ctx.getParameter(ctx.MAX_TEXTURE_SIZE);
          } catch (e) {
            return false;
          }
          this.isCanDoWebGL = !!ctx;
        }
        return this.isCanDoWebGL;
      };

      Photosphere.prototype.start3D = function (image) {
        if (window['THREE'] == undefined) {
          alert("Please make sure three.js is loaded");
        }

        // Start Three.JS rendering
        this.target = new THREE.Vector3();
        this.lat = 0;
        this.lon = 90;
        this.onMouseDownMouseX = 0,
          this.onMouseDownMouseY = 0,
          this.isUserInteracting = false,
          this.onMouseDownLon = 0,
          this.onMouseDownLat = 0;

        this.camera = new THREE.PerspectiveCamera(75, parseInt(this.holder.innerWidth()) / parseInt(this.holder.innerHeight()), 1, 1100);
        this.scene = new THREE.Scene();
        var mesh = new THREE.Mesh(new THREE.SphereGeometry(200, 20, 40), this.loadTexture(image));
        mesh.scale.x = -1;
        this.scene.add(mesh);

        // Check for WebGL
//                console.log(this.canDoWebGL());
        if (this.canDoWebGL()) {
          // This is for nice browsers + computers
          try {
            this.renderer = new THREE.WebGLRenderer();
//                        this.maxSize = this.renderer.context.getParameter(this.renderer.context.MAX_TEXTURE_SIZE);
          } catch (e) {
            this.renderer = new THREE.CanvasRenderer();
          }
        } else {
          this.renderer = new THREE.CanvasRenderer();
        }

        this.renderer.setSize(parseInt(this.holder.innerWidth()), parseInt(this.holder.innerHeight()));
        this.holder.innerHTML = "";
        this.holder.append(this.renderer.domElement);

        this.defer.resolve();

        var self = this;
        this.renderer.domElement.addEventListener('touchstart', function (event) {
          self.onDocumentTouchStart(event, self);
        }, false);
        this.renderer.domElement.addEventListener('touchmove', function (event) {
          self.onDocumentTouchMove(event, self);
        }, false);
        this.renderer.domElement.addEventListener('mousedown', function (event) {
          self.onDocumentMouseDown(event, self);
        }, false);

        // 第三种做法：用单独的插件
        jQuery(this.renderer.domElement).mousewheel(function (event) {
          self.onMouseWheel(event, self);
        });
        // 第二种做法：event参数不同 不好取事件参数
//                var mousewheelevt=(/Firefox/i.test(navigator.userAgent))? "DOMMouseScroll" : "mousewheel"; //FF doesn't recognize mousewheel as of FF3.x
//                if (this.renderer.domElement.attachEvent) //if IE (and Opera depending on user setting)
//                    this.renderer.domElement.attachEvent("on"+mousewheelevt, function(event){self.onMouseWheel(event, self); });
//                else if (this.renderer.domElement.addEventListener) //WC3 browsers
//                    this.renderer.domElement.addEventListener(mousewheelevt, function(event){self.onMouseWheel(event, self); }, false);
        // 第一种做法：仅chrome支持
//                this.renderer.domElement.addEventListener( 'mousewheel', function(event){self.onMouseWheel(event, self); }, false );

        document.addEventListener('mousemove', function (event) {
          self.onDocumentMouseMove(event, self);
        }, false);
        document.addEventListener('mouseup', function (event) {
          self.onDocumentMouseUp(event, self);
        }, false);

        this.restart();
      };

      Photosphere.prototype.restart = function () {
        this.resetTimer(this);
        var self = this;
        this.stopTimer = setTimeout(function () {
          self.stop();
        }, 3000);
      };

      Photosphere.prototype.stop = function () {
        if (this.timer != undefined) {
          clearTimeout(this.timer);
        }
        if (this.interval != undefined) {
          clearInterval(this.interval);
        }
        if (this.stopTimer != undefined) {
          clearTimeout(this.stopTimer);
        }
      };

      Photosphere.prototype.startMoving = function () {
        var self = this;
        this.interval = setInterval(function () {
          self.lon = self.lon - 0.1;

          if (-3 < self.lat && self.lat < 3) {
          }
          else if (self.lat > 10) {
            self.lat -= 0.1
          }
          else if (self.lat > 0) {
            self.lat -= 0.04;
          }
          else if (self.lat < 0 && self.lat > 10) {
            self.lat += 0.1;
          }
          else if (self.lat < 0) {
            self.lat += 0.04;
          }

          self.render();
        }, 10);
      };

      Photosphere.prototype.resetTimer = function (self, t) {
        if (self.timer != undefined) {
          clearTimeout(self.timer);
        }
        if (self.interval != undefined) {
          clearInterval(self.interval);
        }

        self.startMoving();

//                self.timer = setTimeout(function(){
//                    self.startMoving();
//                }, t);
      };

      Photosphere.prototype.onWindowResize = function (self) {
        self = self || this;
        self.camera.aspect = parseInt(self.holder.innerWidth()) / parseInt(self.holder.innerHeight());
        self.camera.updateProjectionMatrix();

        self.renderer.setSize(parseInt(self.holder.innerWidth()), parseInt(self.holder.innerHeight()));

        self.render();

      };

      Photosphere.prototype.onMouseWheel = function (event, self) {

        var proposed = self.camera.fov - (event.wheelDeltaY || event.detail || event.deltaY) * 3;
        if (proposed > 10 && proposed < 100) {
          self.camera.fov = proposed;
          self.camera.updateProjectionMatrix();

          self.render();

          event.preventDefault();
        }

      };

      Photosphere.prototype.onDocumentMouseDown = function (event, self) {

        event.preventDefault();

        self.isUserInteracting = true;

        self.onPointerDownPointerX = event.clientX;
        self.onPointerDownPointerY = event.clientY;

        self.onPointerDownLon = self.lon;
        self.onPointerDownLat = self.lat;

        self.stop();
      };

      Photosphere.prototype.onDocumentMouseMove = function (event, self) {

        if (self.isUserInteracting) {

          self.lon = ( self.onPointerDownPointerX - event.clientX ) * 0.1 + self.onPointerDownLon;
          self.lat = ( event.clientY - self.onPointerDownPointerY ) * 0.1 + self.onPointerDownLat;
          self.render();

          self.stop();
//                    self.resetTimer(self, 9000);

        }

      };

      Photosphere.prototype.onDocumentTouchStart = function (event, self) {

        if (event.touches.length == 1) {

          event.preventDefault();

          self.onPointerDownPointerX = event.touches[0].pageX;
          self.onPointerDownPointerY = event.touches[0].pageY;

          self.onPointerDownLon = lon;
          self.onPointerDownLat = lat;

        }

      };

      Photosphere.prototype.onDocumentTouchMove = function (event, self) {

        if (event.touches.length == 1) {

          event.preventDefault();

          self.lon = ( self.onPointerDownPointerX - event.touches[0].pageX ) * 0.1 + self.onPointerDownLon;
          self.lat = ( event.touches[0].pageY - self.onPointerDownPointerY ) * 0.1 + self.onPointerDownLat;

          self.render();

          self.stop();
//                    self.resetTimer(self, 9000);

        }

      };

      Photosphere.prototype.onDocumentMouseUp = function (event, self) {

        self.isUserInteracting = false;
        self.render();

      };

      Photosphere.prototype.loadTexture = function (path) {
        var texture = new THREE.Texture();
        var material = new THREE.MeshBasicMaterial({map: texture, overdraw: true});
        var self = this;
        if (path instanceof Image) {
          texture.needsUpdate = true;
          material.map.image = path;

          setTimeout(function () {
            self.render();
          }, 100);
        } else {
          texture.needsUpdate = true;
          THREE.ImageUtils.crossOrigin = "anonymous";
          material.map = THREE.ImageUtils.loadTexture(path);
          setTimeout(function () {
            self.render();
          }, 100);
//                    var image = new Image();
//                    image.onload = function () {
//                        texture.needsUpdate = true;
//                        material.map.image = this;
//                        setTimeout(function(){ self.render(); }, 100);
//                    };
//                    image.src = path;
        }
        return material;
      };

      Photosphere.prototype.render = function () {
        this.lat = Math.max(-85, Math.min(85, this.lat));
        var phi = ( 90 - this.lat ) * Math.PI / 180,
          theta = this.lon * Math.PI / 180;

        this.target.x = 500 * Math.sin(phi) * Math.cos(theta);
        this.target.y = 500 * Math.cos(phi);
        this.target.z = 500 * Math.sin(phi) * Math.sin(theta);

        this.camera.lookAt(this.target);

        this.renderer.render(this.scene, this.camera);
      };

      Photosphere.prototype.setEXIF = function (data) {
        this.exif = data;
        return this;
      };

      Photosphere.prototype.loadEXIF = function (callback) {
        if (this.exif != undefined) {
          callback();
          return;
        }
        var self = this;
        this.loadBinary(function (data) {
          var xmpEnd = "</x:xmpmeta>";
          var xmpp = data.substring(data.indexOf("<x:xmpmeta"), data.indexOf(xmpEnd) + xmpEnd.length);

          var getAttr = function (attr) {
            var x = xmpp.indexOf(attr + '="') + attr.length + 2;
            return xmpp.substring(x, xmpp.indexOf('"', x));
          };

          self.exif = {
            "full_width": getAttr("GPano:FullPanoWidthPixels"),
            "full_height": getAttr("GPano:FullPanoHeightPixels"),
            "crop_width": getAttr("GPano:CroppedAreaImageWidthPixels"),
            "crop_height": getAttr("GPano:CroppedAreaImageHeightPixels"),
            "x": getAttr("GPano:CroppedAreaLeftPixels"),
            "y": getAttr("GPano:CroppedAreaTopPixels")
          };
          console.log(self.exif);
          callback();
        });
      };

      return {
        restrict: 'EA',
        templateUrl: "views/ponmPhotoContainer.html",
        link: function (scope, element, attrs, ngModel) {

          var image1 = null;

          var ponmPhotoContainer = element.find(".ponm-photo-container"),
            loading = element.find(".loading"),
            imgContainer = element.find(".flat-image"),
            canvas = element.find(".p360-image"),
            clickCreateP360 = element.find(".flat-image .image-button"),
            clickBackFlat = element.find(".p360-image .image-button");
          var photosphere = null;

          initImageContainer();

          function initImageContainer() {
            var flatImage = imgContainer.find(".flat-canvas");
            flatImage.on("dblclick", function (e) {
              panzoomReset();
            });
            var isClick;
            flatImage.on("mousedown", function (e) {
              isClick = true;
            });
            $(document).on('mousemove', function () {
              isClick = false;
            });
            flatImage.on("click", function (e) {
              if(isClick) {
                scope.$emit('image.click');
              }
            });
          }

          /**
           * 360度全景图标 点击创建全景
           */
          clickCreateP360.on("click", function (e) {
            e.preventDefault();
            createP360();
          });

          // 平面图标
          clickBackFlat.on("click", function (e) {
            e.preventDefault();
            hide(canvas);
            show(imgContainer);
            photosphere.stop();
          });

          element.css("width", "100%");
          element.css("height", "100%");
          element.css("background-color", attrs.ponmPhotoColor);

          var containerWidth = element.innerWidth(),
            containerHeight = element.innerHeight();

          attrs.$observe('ponmPhotoSrc', function (data) {
            if (angular.isDefined(data)) {
              drawImage();
            }
          });

          function changeP360(is360) {
            $log.debug("changeP360: " + is360);
            if (angular.isString(is360)) {
              is360 = angular.lowercase(is360);
              is360 = is360 == "true";
            }

            if (is360) {
              $animate.addClass(imgContainer, 'p360');
              show(clickCreateP360);
            } else {
              hide(clickCreateP360);
            }
          }

          /**
           * 图片缩放平移控制
           *
           * $panzoom
           */
          var $panzoom = null;
          function panzoom(panzoomDom) {
            $panzoom = angular.element(panzoomDom).panzoom({
              $zoomIn: element.parent().find(".zoom-in"),
              $zoomOut: element.parent().find(".zoom-out"),
              $zoomRange: element.parent().find(".zoom-range"),
              $reset: element.parent().find(".reset"),
              transition: true,
              disablePan: true
            });
            $panzoom.parent().on('mousewheel.focal', function (e) {
              e.preventDefault();
              var delta = e.delta || e.originalEvent.wheelDelta;
              var zoomOut = delta ? delta < 0 : e.originalEvent.deltaY > 0;
              $panzoom.panzoom('zoom', zoomOut, {
                increment: 0.1,
                animate: true,
                focal: e
              });
            });
            $panzoom.on('panzoomzoom', function (e, panzoom, scale, changed) {

              if (scale < 1) {
                $panzoom.panzoom("resetZoom");
                $panzoom.panzoom("resetPan");
                $panzoom.panzoom("option", "disablePan", true);
              } else if (Math.abs(scale - 1) < 0.05) {
                if (scale != 1) {
                  $panzoom.panzoom("resetZoom");
                  $panzoom.panzoom("option", "disablePan", true);
                }
                $panzoom.panzoom("resetPan");
              }else {
                $panzoom.panzoom("option", "disablePan", false);
              }

            });
          }

          function panzoomReset() {
            $panzoom.panzoom("reset");
            $panzoom.panzoom("option", "disablePan", true);
          }

          // 图片属性各变量
          var photoScale, // 图片高宽比例
            photoSrc, // 图片地址url
            photoLazy, // 加载清晰图片前的不清晰图片url
            photoIs360, // 是否为360度图片
            photo360Src; // 360度图片地址url croxy代理过的地址，解决跨域访问问题

          /**
           * 加载图片
           */
          function drawImage() {

            photoSrc = attrs.ponmPhotoSrc;
            // re-init variables
            photoScale = 1;
            photoLazy = '';
            photoIs360 = false;
            photo360Src = '',
            scope.imagePercentComplete = 0;

            if (scope.photo) {
              photoIs360 = scope.photo.is360;
              if (photoIs360) {
                photo360Src = scope.corsproxyCtx + '/' + scope.photo.oss_key + '@0e_2000w_1000h.jpg';
              }
              photoLazy = scope.staticCtx + '/' + scope.photo.oss_key + '@!photo-preview-sm';
              if (scope.photo.width) {
                photoScale = scope.photo.height / scope.photo.width;
              }
            }

            if (!photoSrc) {
              $animate.addClass(loading, "ponm-hide");
              return;
            }

            if (imgContainer) {
              imgContainer.find(".flat-canvas").empty();

              changeP360(photoIs360);

              hide(canvas);

              $animate.removeClass(loading, "ponm-hide");
            }
            if (photoSrc) {
              image1 = new Image();
              image1.onload = function () {
                setPhotoContainerHeight();
                show(imgContainer);
              };

              if (photoLazy) {
                image1.photoLazy = true;
                image1.src = photoLazy;
              }

              imageRequest(photo360Src || photoSrc, image1).then(function (src) {
                if(src == (photo360Src || photoSrc)) {
                  image1.photoLazy = false;
                  image1.src = src;
                  $animate.addClass(loading, "ponm-hide");
                }
              });

              var flatImage = imgContainer.find(".flat-canvas");
              flatImage.append(image1);
              flatImage.on("dblclick", function (e) {
                panzoomReset();
              });

              panzoom(imgContainer.find(".flat-canvas"));
              panzoomReset();
            }
          }

          /**
           * XMLHttpRequest方式预加载图片
           *
           * @param $src
           * @returns {*}
           */
          function imageRequest($src) {
            var deferred = jQuery.Deferred();

            var req = new XMLHttpRequest();
            req.onprogress = function (evt) {
              if (evt.lengthComputable) {
                var percentComplete = (evt.loaded / evt.total) * 100;
                scope.imagePercentComplete = percentComplete;
              }
            }

            req.onreadystatechange = function () {
              if (req.readyState == 4) {
                if (req.status == 200) {
                  setTimeout(function () {
                    deferred.resolve($src);
                  }, 500);
                }
              }
            };

            req.open("GET", $src, true);
            req.send();
            return deferred.promise();
          }

          /**
           * 设置图片拉伸比例
           */
          function setImageScale() {

            var $img = angular.element(image1);
            $img.css('height', 'initial');
            $img.css('width', 'initial');

            if(image1.photoLazy) {
              var imgWidth = $img.outerWidth(),
                imgHeight = $img.outerHeight();
              if (photoScale > (containerHeight / containerWidth)) {
                $img.css('height', '100%');
              } else {
                $img.css('width', '100%');
              }
            }
          }

          angular.element($window).resize(function (e) {
            setPhotoContainerHeight();
            photosphere && photosphere.onWindowResize();
          });

          /**
           * 设置图片容易的line-height，使图片垂直居中
           */
          function setPhotoContainerHeight() {

            containerWidth = element.innerWidth();
            containerHeight = element.innerHeight();

            ponmPhotoContainer.css("line-height", containerHeight + "px");

            setImageScale();
          }

          function show(element) {
            $animate.addClass(element, "ponm-show");
          }

          function hide(element) {
            $animate.removeClass(element, "ponm-show");
          }

          /**
           * 创建360度试图
           */
          function createP360() {
            hide(imgContainer);
            show(canvas);

            if (canvas.find("canvas").length && clickCreateP360.ponmPhotoSrcL == photo360Src) {
              show(canvas);
              photosphere.restart();
            } else {
              clickCreateP360.ponmPhotoSrcL = photo360Src;
              canvas.find(".p360-canvas").empty();
              $animate.removeClass(loading, "ponm-hide");

              photosphere = new Photosphere(photo360Src)
                .setEXIF({
                  "full_width": attrs.ponmPhotoWidth,
                  "full_height": attrs.ponmPhotoHeight,
                  "crop_width": attrs.ponmPhotoWidth,
                  "crop_height": attrs.ponmPhotoHeight,
                  "x": 0,
                  "y": 0
                });
              photosphere.loadPhotosphere(canvas.find(".p360-canvas"))
                .then(function () {
                  $animate.addClass(loading, "ponm-hide");
                });
            }
          }
        }
      };
    }])
;
