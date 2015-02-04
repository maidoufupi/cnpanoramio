/**!
 * The MIT License
 *
 * Copyright (c) 2013 the photoshows Team, http://www.photoshows.cn/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * photoshows
 * http://anypossiblew.github.io/
 *
 * @authors https://github.com/anypossiblew
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

    var infoWindow;
    var labels = {};
    var thumbPhotoIds = [];

    this.preZoom = 0;
    this.preBounds = null;

    this.opts = $jQuery.extend({clickable: true, auto: true, mapVendor: "gps"}, opts);

    if (this.opts.map) {
      this.setMap(this.opts.map);
    }

    this.getMap = function () { //    Map    Returns the map on which this layer is displayed.
      return this.opts.map;
    };

    /**
     * 初始化地图
     *
     * @param map {Map}
     */
    this.setMap = function (map/*:Map*/) { //	None	Renders the layer on the specified map. If map is set to null, the layer will be removed.

      var that = this;

      if (map) {
        this.opts.map = map;

        // 当map出现时说明地图加载过，才创建地图的各种对象
        infoWindow = L.popup();
        //infoWindow.setMap(map);

        map.on('load', function(e) {
          that.mapChanged(e) // e is an event object (MouseEvent in this case)
        });
        map.on('viewreset', function(e) {
          that.mapChanged(e) // e is an event object (MouseEvent in this case)
        });
        map.on('moveend', function(e) {
          that.mapChanged(e) // e is an event object (MouseEvent in this case)
        });
      } else {
        this.opts.map = undefined;
        // TODO
      }

      //function getBoundsThumbnails() {
      //
      //  $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);
      //
      //  if (!that.opts.auto) {
      //    return;
      //  }
      //
      //  var bounds = that.getBounds();
      //  // 地图为初始化完时 getBounds()为空
      //  if (!bounds.ne) {
      //    return;
      //  }
      //
      //  var thumbs = that.getBoundsThumbnails(that.getBounds(),
      //    that.getLevel(),
      //    that.getSize());
      //}

    };

    this.hideLabel = function (photoId) {
      var label = labels[photoId];
      if (label) {
        this.opts.map.removeLayer(label);
        //label.setOpacity(0);
      }
    };

    /**
     * 创建图片图标
     *
     * @param photo
     */
    this.createMarker = function (photo) {
      var map = this.opts.map;
      var that = this;
      var label = labels[photo.id];
      if (label) {
        this.opts.map.addLayer(label);
        //label.setOpacity(1);
      } else {

        var myIcon = L.icon({
          iconUrl: that.getIconUrl(photo.oss_key)
          //iconRetinaUrl: 'my-icon@2x.png',
          //iconSize: [38, 95],
          //iconAnchor: [22, 94],
          //popupAnchor: [-3, -76],
          //shadowUrl: 'my-icon-shadow.png',
          //shadowRetinaUrl: 'my-icon-shadow@2x.png',
          //shadowSize: [68, 95],
          //shadowAnchor: [22, 94]
        });

        label = L.marker([photo.point.lat, photo.point.lng],
          {icon: myIcon,
           riseOnHover: true}).addTo(map);

        label.photoId = photo.id;
        if (that.opts.clickable) {
          label.on('click',
            function (e) {
              if (that.opts.suppressInfoWindows) {
                infoWindow.setLatLng(this.getLatLng())
                  .setContent(that.getInfoWindowContent(photo))
                  .openOn(map);
              } else {
                $jQuery(that).trigger("data_clicked", [this.photoId]);
              }
            });
        }
        //map.entities.push(label);
        labels[photo.id] = label;
      }
    };

    this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
      this.opts = options;
    };

    this.trigger = function (event) {
      if (this.opts.map) {
        this.opts.map.fireEvent("load");
      }
    };

    this.getBounds = function () {
      var bounds = this.opts.map.getBounds();
      if (bounds) {
        return {
          ne: bounds.getNorthEast(),
          sw: bounds.getSouthWest()
        };
      } else {
        return {};
      }
    };

    this.getLevel = function () {
      return this.opts.map && parseInt(this.opts.map.getZoom());
    };

    this.getSize = function () {
      return {
        width: this.opts.map.getSize().x,
        height: this.opts.map.getSize().y
      };
    };

    this.clearMap = function () {
      var map = this.opts.map;
      if (map) {
        for(var i in labels) {
          map.removeLayer(labels[i]);
        }
        //map.eachLayer(function(layer) {
        //  console.log(layer);
        //  //map.removeLayer(layer);
        //});
      }
      labels = [];
      this.thumbPhotoIds = [];
    };

    this.setAuto = function (auto) {
      this.opts.auto = auto;
    };
  };

  $window.cnmap.PanoramioLayer.prototype = new $window.cnmap.Panoramio();

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

  return $window.cnmap.PanoramioLayer;
});
