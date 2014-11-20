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

        var infoWindow ;
        var labels = {};
        var thumbPhotoIds = [];

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $jQuery.extend( {clickable: true, auto: true}, opts);

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
//                this.bindTo("zoom", map);
//                this.bindTo("center", map);
                // 当map出现时说明地图加载过，才创建地图的各种对象
                infoWindow = new qq.maps.InfoWindow();
                infoWindow.setMap(map);

                var listener = qq.maps.event.addListener(
                    map,
                    'idle',
                    getBoundsThumbnails
                );
                listener = qq.maps.event.addListener(
                    map,
                    'dragend',
                    getBoundsThumbnails
                );
            } else {
                this.opts.map = undefined;
                // TODO
//                this.clearVisible();
            }

            var that = this;
            function getBoundsThumbnails() {

                $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);

                if(!that.opts.auto) {
                    return;
                }

                var bounds = map.getBounds();
                // 地图为初始化完时 getBounds()为空
                if(!bounds) {
                    return;
                }

                var thumbs = that.getBoundsThumbnails(that.getBounds(),
                                                     that.getLevel(),
                                                     that.getSize());
            }

        };

        this.hideLabel = function(photoId) {
            if(labels[photoId]) {
                labels[photoId].setVisible(false);
                labels[photoId].setMap(null);
            }
        };

        /**
         * 创建图片图标
         *
         * @param photo
         */
        this.createMarker = function(photo) {
            var map = this.opts.map;
            var that = this;
            var label = labels[photo.id];
            if(label) {
                label.setVisible(true);
                label.setMap(map);
            }else {
                label = new qq.maps.Label({
                    style: {
                        padding: 0,
                        border: 0,
                        cursor: "pointer"
                    }
                });
                label.photoId = photo.id;
                label.setContent(that.getLabelContent(photo.oss_key));  //自定义点标记覆盖物内容);
                label.setMap(map);
                label.setPosition(new qq.maps.LatLng(photo.point.lat, photo.point.lng));
                if(that.opts.clickable) {
                    qq.maps.event.addListener(
                        label,
                        'click',
                        function () {
                            if (that.opts.suppressInfoWindows) {
                                infoWindow.setOptions({
                                    content: that.getInfoWindowContent(photo),
                                    position: this.getPosition()
                                });
                                infoWindow.open();
                            }else {
                                $jQuery(that).trigger("data_clicked", [this.photoId]);
                            }
                        });
                }
                labels[photo.id] = label;
            }
        };

//        this.getLabelContent = function(photoOssKey) {
//
//            if(this.opts.phone) {
//                return "<img src='" + this.staticCtx + "/"
//                    + photoOssKey + "@!panor-lg' style='width: 34px; height: 34px;'>";
//            }else {
//                return "<img src='" + this.staticCtx + "/" + photoOssKey
//                    + "@!panor-lg' style='width: 34px; height: 34px;'>";
//            }
//
//        };

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
                (photo.address || '') +
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
            if(this.opts.map) {
                qq.maps.event.trigger(this.opts.map, "dragend");
//                qq.maps.event.trigger(this.opts.map, "idle");
            }
        };

        this.center_changed = function () {
//            infoWindows = [];
        };

        this.getBounds = function() {

            var bounds = this.opts.map&&this.opts.map.getBounds();
            if(bounds) {
                return {
                    ne: {
                        lat: bounds.getNorthEast().lat,
                        lng: bounds.getNorthEast().lng
                    },
                    sw: {
                        lat: bounds.getSouthWest().lat,
                        lng: bounds.getSouthWest().lng
                    }
                };
            }else {
                return {

                };
            }

        };

        this.getLevel = function() {
            return this.opts.map.getZoom();
        };

        this.getSize = function() {
            var mapContainer = $jQuery(this.opts.map.getContainer());
            return {
                width: parseInt(mapContainer.width()),
                height: parseInt(mapContainer.height())
            };
        };

        this.clearMap = function() {
            $.each(labels, function( index, label ) {
                label && label.setMap(null);
            });
            labels = [];
            this.thumbPhotoIds = [];
        };

        this.setAuto = function(auto) {
            this.opts.auto = auto;
        };
    };

//    $window.cnmap.Panoramio.prototype = new qq.maps.MVCObject();
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
