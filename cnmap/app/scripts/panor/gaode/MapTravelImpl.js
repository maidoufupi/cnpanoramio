// Generated by CoffeeScript 1.7.1
(function() {
  var TravelLayer,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  TravelLayer = (function(_super) {
    __extends(TravelLayer, _super);

    function TravelLayer() {
      return TravelLayer.__super__.constructor.apply(this, arguments);
    }

    TravelLayer.prototype.initMap = function(map) {
      var photo, point, spot, spotMinDate, _i, _j, _k, _l, _len, _len1, _len2, _len3, _ref, _ref1, _ref2, _ref3;
      if (map) {
        this.map = map;
      }
      this.mapEventListener.setMap(this.marker, this.map);
      point = [];
      if (this.travel) {
        _ref = this.travel.spots;
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          spot = _ref[_i];
          spot.spotDate = new Date(spot.time_start);
          if (!spotMinDate || spotMinDate > spot.spotDate) {
            spotMinDate = spot.spotDate;
          }
        }
        this.travel.time_start = spotMinDate;
        _ref1 = this.travel.spots;
        for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
          spot = _ref1[_j];
          spot.day = Math.round((spot.spotDate - spotMinDate) / (1000 * 60 * 60 * 24)) + 1;
          _ref2 = spot.photos;
          for (_k = 0, _len2 = _ref2.length; _k < _len2; _k++) {
            photo = _ref2[_k];
            this.createLabel(photo);
          }
          point = [];
          _ref3 = spot.photos;
          for (_l = 0, _len3 = _ref3.length; _l < _len3; _l++) {
            photo = _ref3[_l];
            point.push(this.createPoint(photo));
          }
          new AMap.Polyline({
            map: this.map,
            path: point,
            strokeStyle: 'dashed'
          });
        }
        this.travel.spots.sort(function(a, b) {
          return a.day - b.day;
        });
        if (this.travel.spots.length) {
          return this.travel.time_end = this.travel.spots[this.travel.spots.length - 1].time_start;
        }
      }
    };

    TravelLayer.prototype.createPoint = function(photo) {
      return new AMap.LngLat(photo.point.lng, photo.point.lat);
    };

    TravelLayer.prototype.createLabel = function(photo) {
      var label, that;
      that = this;
      label = new AMap.Marker({
        map: this.map,
        position: new AMap.LngLat(photo.point.lng, photo.point.lat),
        offset: new AMap.Pixel(-15, -15),
        content: this.getLabelContent(photo.oss_key)
      });
      label.photoId = photo.id;
      if (this.opts.clickable) {
        AMap.event.addListener(label, 'click', function() {
          return jQuery(that).trigger("data_clicked", [this.photoId]);
        });
      }
      label.photoId = photo.id;
      return label.setMap(this.map);
    };

    TravelLayer.prototype.createSpot = function(spot) {
      var cdistance, distance, photo, _i, _len, _ref;
      distance = 0;
      _ref = spot.photos;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        photo = _ref[_i];
        cdistance = window.cnmap.GPS.distanceInMeter(spot.center_lat, spot.center_lng, photo.point.lat, photo.point.lng);
        if (cdistance > distance) {
          distance = cdistance;
        }
      }
      return new AMap.Circle({
        map: this.map,
        center: new AMap.LngLat(spot.center_lng, spot.center_lat),
        radius: distance,
        strokeStyle: 'dashed',
        strokeColor: '#0066FF',
        strokeOpacity: 0.2,
        fillColor: '#0066FF',
        fillOpacity: 0.2
      });
    };

    TravelLayer.prototype.createLine = function(spots) {
      var i, path, spot;
      path = (function() {
        var _i, _len, _results;
        _results = [];
        for (i = _i = 0, _len = spots.length; _i < _len; i = ++_i) {
          spot = spots[i];
          _results.push((function(spot, i) {
            return new AMap.LngLat(spot.center_lng, spot.center_lat);
          })(spot, i));
        }
        return _results;
      })();
      return new AMap.Polyline({
        map: this.map,
        path: path,
        strokeStyle: 'dashed'
      });
    };

    TravelLayer.prototype.createMarker = function() {
      return new AMap.Marker({
        map: this.map,
        icon: "images/marker.png",
        animation: "AMAP_ANIMATION_BOUNCE"
      });
    };

    return TravelLayer;

  })(window.cnmap.ITravelLayer);

  window.cnmap.TravelLayer = TravelLayer;

}).call(this);

//# sourceMappingURL=MapTravelImpl.map
