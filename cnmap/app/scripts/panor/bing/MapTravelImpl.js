// Generated by CoffeeScript 1.8.0
(function() {
  var TravelLayer,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  TravelLayer = (function(_super) {
    __extends(TravelLayer, _super);

    function TravelLayer() {
      return TravelLayer.__super__.constructor.apply(this, arguments);
    }

    TravelLayer.prototype.createPoint = function(photo) {
      return new Microsoft.Maps.Location(photo.point.lat, photo.point.lng);
    };

    TravelLayer.prototype.createMarker = function(photo) {
      var marker, that;
      that = this;
      marker = new Microsoft.Maps.Pushpin(this.createPoint(photo), {
        icon: this.getMarkerImage(photo),
        width: 34,
        height: 34
      });
      marker.photo = photo;
      this.map.entities.push(marker);
      if (this.opts.clickable) {
        Microsoft.Maps.Events.addHandler(marker, 'click', function(e) {
          if (!e.target.getDraggable()) {
            return jQuery(that).trigger("data_clicked", [e.target.photo.id]);
          }
        });
      }
      photo.marker = marker;
      return marker;
    };

    TravelLayer.prototype.createPolyline = function(points) {
      var polyline;
      polyline = new Microsoft.Maps.Polyline(points, {
        strokeThickness: 2
      });
      this.map.entities.push(polyline);
      return polyline;
    };

    TravelLayer.prototype.setPolylinePath = function(polyline, points) {
      return polyline.setLocations(points);
    };

    TravelLayer.prototype.removeMarker = function(photo) {
      if (photo.marker) {
        this.map.entities.remove(photo.marker);
        return delete photo.marker;
      }
    };

    TravelLayer.prototype.toggleSpotLine = function(spot, visible) {
      if (spot.polyline) {
        if (visible) {
          return spot.polyline.setOptions({
            visible: true
          });
        } else {
          return spot.polyline.setOptions({
            visible: false
          });
        }
      }
    };

    TravelLayer.prototype.spotEditable = function(spot, editable) {
      var editMarker, photo, that, _i, _len, _ref, _results;
      editable = !!editable;
      that = this;
      editMarker = function(marker) {
        if (marker) {
          if (that.opts.editable && editable) {
            marker.setOptions({
              draggable: true
            });
          } else {
            marker.setOptions({
              draggable: false
            });
          }
          if (that.opts.editable && editable) {
            return marker.dragListener = Microsoft.Maps.Events.addHandler(marker, 'dragend', function(e) {
              var loc, photo;
              photo = e.entity.photo;
              if (!photo.oPoint) {
                photo.oPoint = $.extend({}, photo.point);
              }
              loc = e.entity.getLocation();
              photo.point.lat = loc.latitude;
              photo.point.lng = loc.longitude;
              that.updateSpotLine(spot);
              return $(that).trigger("spot.edited", [spot.id]);
            });
          } else {
            Microsoft.Maps.Events.removeHandler(marker.dragListener);
            return delete marker.dragListener;
          }
        }
      };
      _ref = spot.photos;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        photo = _ref[_i];
        _results.push(editMarker(photo.marker));
      }
      return _results;
    };

    TravelLayer.prototype.cancelSpotEdit = function(spot) {
      var cancelMarker, photo, that, _i, _len, _ref;
      that = this;
      cancelMarker = function(photo) {
        if (photo.oPoint) {
          photo.point = photo.oPoint;
          delete photo.oPoint;
        }
        if (photo.marker) {
          return photo.marker.setLocation(that.createPoint(photo));
        }
      };
      _ref = spot.photos;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        photo = _ref[_i];
        cancelMarker(photo);
      }
      return this.updateSpotLine(spot);
    };

    return TravelLayer;

  })(window.cnmap.ITravelLayer);

  window.cnmap.TravelLayer = TravelLayer;

}).call(this);

//# sourceMappingURL=MapTravelImpl.js.map
