cordova.define("com-greatday-plugins.GreatDayPlugin", function (require, exports, module) {
  var exec = require('cordova/exec');

  // exports.coolMethod = function (arg0, success, error) {
  //     exec(success, error, 'GreatDayPlugin', 'coolMethod', [arg0]);
  // };

  module.exports = {
    /**
     * Just get camera
     * arg0 = String (name photo)
     */
    getCamera: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getCamera', [arg0]);
    },

    /**
     * Get camera with swap on or off
     * arg0 = String (name photo)
     */
    getCameraSwap: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getCameraSwap', [arg0]);
    },

    /**
     * Just Get Location
     */
    getLocation: function (onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocation', []);
    },

    /*
    * get location with radius
    * arg0 = String ("{data:[{work_lat: -6.202394, work_lon: 106.652710, work_radius: 1000 },{work_lat: -6.175110, work_lon: 106.865036, work_radius: 1000 }]}")
    */
    getLocationRadius: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationRadius', [arg0]);
    },

    /**
     * get location and camera
     * arg0 = String (name photo)
     */
    getLocationCamera: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationCamera', [arg0]);
    },

    /**
     * get Location and camera swap on/off
     * arg0 = String (name photo)
     */
    getLocationCameraSwap: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationCameraSwap', [arg0]);
    },

    /*
    * get Location with radius and camera
    * photo: String (name photo)
    * location: String "{data:[{work_lat: -6.202394, work_lon: 106.652710, work_radius: 1000 },{work_lat: -6.175110, work_lon: 106.865036, work_radius: 1000 }]}"
    */
    getLocationRadiusCamera: function (photo, location, onSuccess, onError) {
      let data = [];
      data.push({
        photo: photo,
        location: location
      })
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationRadiusCamera', data);
    },

    /*
    * get Location with radius and camera swap on/off
    * photo: String (name photo)
    * location: String "{data:[{work_lat: -6.202394, work_lon: 106.652710, work_radius: 1000 },{work_lat: -6.175110, work_lon: 106.865036, work_radius: 1000 }]}"
    */
    getLocationRadiusCameraSwap: function (photo, location, onSuccess, onError) {
      let data = [];
      data.push({
        photo: photo,
        location: location
      })
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationRadiusCameraSwap', data);
    }
  };

});
