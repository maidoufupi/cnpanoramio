
BMap = window.BMap
class MapEvent extends window.ponm.IMapEvent

  trigger: (instance, eventName) ->
    listeners = instance._e_ || {};
    for listener in listeners when listener._eventName is eventName
      args = Array.prototype.slice.call(arguments, 2);
      listener._handler.apply(instance, args);

window.ponm.MapEvent = new MapEvent