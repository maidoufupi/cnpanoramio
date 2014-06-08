/*!
 * jQuery Cookie Plugin v1.4.0
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define([window, 'jquery', 'Panoramio'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function ($window, $jQuery) {

// 全局命名空间
    $window.cnmap = $window.cnmap || {};

    $window.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var infoWindow = new qq.maps.InfoWindow();;
        var labels = {};
        var thumbPhotoIds = [];

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $jQuery.extend( {clickable: true}, opts);

        if (this.opts.map) {
            this.setMap(this.opts.map);
        }

        this.getMap = function () { //    Map    Returns the map on which this layer is displayed.
            return this.opts.map;
        };

        /**
         *
         * @param map {Map}
         */
        this.setMap = function (map/*:Map*/) { //	None	Renders the layer on the specified map. If map is set to null, the layer will be removed.
            if (map) {
                this.opts.map = map;
                this.bindTo("zoom", map);
                this.bindTo("center", map);

                infoWindow.setMap(map);

                var listener = qq.maps.event.addListener(
                    map,
                    'idle',
                    getBoundsThumbnails
                );
            } else {
                this.opts.map = undefined;
                // TODO
                this.clearVisible();
            }

            var that = this;
            function getBoundsThumbnails() {
                var bounds = map.getBounds();
                // 地图为初始化完时 getBounds()为空
                if(!bounds) {
                    return;
                }
                var mapContainer = $jQuery(map.getContainer());
                var size = {
                    width: parseInt(mapContainer.width()),
                    height: parseInt(mapContainer.height())
                };
                var thumbs = that.getBoundsThumbnails({
                        ne: {
                            lat: bounds.getNorthEast().lat,
                            lng: bounds.getNorthEast().lng
                        },
                        sw: {
                            lat: bounds.getSouthWest().lat,
                            lng: bounds.getSouthWest().lng
                        }
                    },
                    map.getZoom(),
                    size,
                    processRes
                )
            }

            function processRes(thumbs) {
                var photoIds = [];
                var photos = {};
                for (var i in thumbs) {
                    photoIds.push(thumbs[i].photo_id);
                    photos[thumbs[i].photo_id] = thumbs[i];
                }
                cnmap.utils.compareArray(
                    thumbPhotoIds,
                    photoIds,
                    function(a, c, b) {
                        if(a) {
                            if(labels[a]) {
                                labels[a].setVisible(false);
                            }
                            var index = thumbPhotoIds.indexOf(a);
                            if (index > -1) {
                                thumbPhotoIds.splice(index, 1);
                            }
                        }

                        if(c) {
                            // do nothing
                        }

                        if(b) {
                            thumbPhotoIds.push(b);
                            if(labels[b]) {
                                labels[b].setVisible(true);
                                labels[b].setMap(map);
                            }else{
                                var label = new qq.maps.Label();
                                label.photoId = b;
                                label.setContent(that.getLabelContent(b));  //自定义点标记覆盖物内容);
                                label.setMap(map);
                                label.setPosition(new qq.maps.LatLng(photos[b].lat, photos[b].lng));
                                if(that.opts.clickable) {
                                    qq.maps.event.addListener(
                                        label,
                                        'click',
                                        function () {
                                            if (that.opts.suppressInfoWindows) {
                                                infoWindow.setOptions({
                                                    content: that.getInfoWindowContent(photos[this.photoId]),
                                                    position: this.getPosition()
                                                });
                                                infoWindow.open();
                                            }else {
                                                $jQuery(that).trigger("data_clicked", [this.photoId]);
                                            }
                                        });
                                }
                                labels[b] = label;
                            }
                        }
                    })

                // trigger data_changed event
                $jQuery(that).trigger("data_changed", [thumbs]);
            }
        };

        this.getLabelContent = function(photoId) {
            if(this.opts.suppressInfoWindows) {
                return "<img src='" + this.ctx + "/api/rest/photo/" + photoId
                    + "/3' style='width: 34px; height: 34px;'>";
            }else {
                if(this.opts.phone) {
                    return "<img src='" + this.ctx + "/api/rest/photo/"
                        + photoId + "/3' style='width: 34px; height: 34px;'>";
                }else {
                    return "<a href='" + this.ctx + "/photo/" + photoId +"'><img src='" + this.ctx + "/api/rest/photo/"
                        + photoId + "/3' style='width: 34px; height: 34px;'></a>";
                }
            }
        };

//        this.getInfoWindowContent = function(photoId) {
//            var infoWindow = document.createElement("div");
//            $jQuery(infoWindow).css({
//                'width': '200px',
//                'height': '200px',
//                'display': 'inline-block'
//            }).append(
//                "<a href='" + this.ctx + "/photo/" + photoId+ "'><img src='" + this.ctx + "/api/rest/photo/" + photoId + "/2' ></a>"
//            )
//            return infoWindow;
//        };

        this.getInfoWindowContent = function(photo) {

            var date = new Date(photo.create_date),
                dates;
            dates = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
            return '<div class="panoramio-info-window" style="\'width\': \'200px\',\'height\': \'200px\',\'display\': \'inline-block\'">' +
                '<div class="header">' +
                photo.address +
                '</div>' +
                '<div class="body">' +
                '<a href="'+ this.ctx + '/photo/' + photo.photo_id +'">' +
                '<img src="'+ this.ctx + '/api/rest/photo/' + photo.photo_id +'/2">' +
                '</a>' +
                '</div>' +
                '<div class="media">' +
                '<a class="pull-left" href="'+ this.ctx + '/user/' + photo.user_id +'">' +
                '<img class="media-object" src="'+ this.ctx + '/api/rest/user/' + photo.user_id +'/avatar" alt="'
                +photo.username+'">' +
                '</a>' +
                '<div class="media-body">' +
                '<h4 class="media-heading">作者：<a href="'+ this.ctx + '/user/' + photo.user_id +'">'
                +photo.username+'</a></h4>' +
                dates +
                '</div>' +
                '</div>' +
                '</div>';

//            return "<a href='" + this.ctx + "/photo/" + photoId +"'><img src='" + this.ctx + "/api/rest/photo/"
//                + photoId + "/2' style='max-height: 200px; max-width: 200px;'></a>";
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            this.opts = options;
        };

        this.trigger = function(event) {
            qq.maps.event.trigger(this.opts.map, "idle");
        };

        this.center_changed = function () {
//            infoWindows = [];
        };
    };

    $window.cnmap.Panoramio.prototype = new qq.maps.MVCObject();
    $window.cnmap.PanoramioLayer.prototype = new $window.cnmap.Panoramio();

//    $.cnmap.qq.PanoramioLayer.prototype.zoom_changed = function () {
//        infoWindows = [];
//    };

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

    return $window.cnmap.PanoramioLayer;
});