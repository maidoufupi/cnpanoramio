/**
 * Created by tiwen.wang on 11/28/2014.
 */

'use strict';

angular.module('ponmApp.services')
  .factory('PhotosManager', ['$q', 'UserPhoto', function ($q, UserPhoto) {

    function PhotosManager(userId, pageSize) {
      this.userId = userId;
      this.pageSize = pageSize;
      this.pageNo = 0;
      this.ended = false;
      this.reqStack = [];
    }

    PhotosManager.prototype.get = function(pageNo) {

    };

    PhotosManager.prototype.more = function() {
      var d = $q.defer();
      if(this.ended) {
        d.reject('ended');
        return d.promise;
      }
      if(this.reqStack.length) {
        d.reject('processing');
        return d.promise;
      }
      this.pageNo++;
      this.reqStack.push(this.pageNo);
      var that = this;
      UserPhoto.get({userId: this.userId, pageSize: this.pageSize, pageNo: this.pageNo},
        function (data) {
          if (data.status == "OK") {
            if(data.photos.length < that.pageSize) {
              that.ended = true;
            }
            var pageNo = that.reqStack.shift();
            d.resolve(data.photos, pageNo);
          }else {
            var pageNo = that.reqStack.shift();
            d.reject(data.status, pageNo);
          }
        }, function (error) {
          var pageNo = that.reqStack.shift();
          d.reject(error.data, pageNo);
        });
      return d.promise;
    };

    return PhotosManager;

  }])
;
