// Generated by CoffeeScript 1.8.0
(function() {
  var $window, MapService,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  $window = window;

  MapService = (function(_super) {
    __extends(MapService, _super);

    function MapService(map) {
      this.map = map;
    }

    MapService.prototype.init = function(map, callback) {
      this.geocoder = new $window.google.maps.Geocoder();
      if (callback) {
        return callback.apply(this.geocoder, [this.geocoder]);
      }
    };

    MapService.prototype.getAddress = function(lat, lng, callback) {
      if (!this.geocoder) {
        this.init();
      }
      return this.geocoder.geocode({
        location: new $window.google.maps.LatLng(lat, lng)
      }, function(result) {
        if (result) {
          return callback.apply(void 0, [result.address]);
        }
      });
    };

    MapService.prototype.getAddrPois = function(lat, lng, callback) {
      var deferred;
      deferred = jQuery.Deferred();
      this.geocoder.geocode({
        location: new $window.google.maps.LatLng(lat, lng)
      }, function(result) {
        var addresses;
        console.log(result);
        addresses = {};
        if (result) {
          $.each(result, function(index, poi) {
            var address;
            address = poi.formatted_address;
            return addresses[address] = {
              poiweight: 1,
              location: poi.geometry.location
            };
          });
        }
        if (result && result[0]) {
          return deferred.resolve(addresses, result[0].formatted_address);
        } else {
          return deferred.reject("no result");
        }
      });
      return deferred.promise();
    };

    MapService.prototype.getLocation = function(address, callback) {
      if (!this.geocoder) {
        this.init();
      }
      return this.geocoder.geocode({
        address: address
      }, function(point) {
        if (point) {
          return callback.apply(void 0, [point]);
        }
      });
    };

    MapService.prototype.getLocPois = function(address) {
      var deferred;
      deferred = jQuery.Deferred();
      if (!this.geocoder) {
        this.init();
      }
      this.geocoder.geocode({
        address: address
      }, function(ress) {
        var addresses, res, _i, _len;
        addresses = [];
        if (ress) {
          for (_i = 0, _len = ress.length; _i < _len; _i++) {
            res = ress[_i];
            address = {
              address: res.formatted_address,
              location: {
                lat: res.geometry.location.lat(),
                lng: res.geometry.location.lng()
              },
              similarity: 1
            };
            if (res.geometry.bounds) {
              address.bounds = {
                sw: {
                  lat: res.geometry.bounds.getSouthWest().lat(),
                  lng: res.geometry.bounds.getSouthWest().lng()
                },
                ne: {
                  lat: res.geometry.bounds.getNorthEast().lat(),
                  lng: res.geometry.bounds.getNorthEast().lng()
                }
              };
            }
            addresses.push(address);
          }
        }
        return deferred.resolve(addresses);
      });
      return deferred.promise();
    };

    MapService.prototype.translate = function(lat, lng, callback) {};

    return MapService;

  })(window.cnmap.IMapService);

  MapService.factory = function() {
    return new window.cnmap.MapService;
  };

  window.cnmap = window.cnmap || {};

  window.cnmap.MapService = MapService;

}).call(this);

//# sourceMappingURL=MapServiceImpl.js.map
