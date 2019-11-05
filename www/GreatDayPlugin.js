cordova.define("com-greatday-plugins.GreatDayPlugin", function(require, exports, module) {
  var exec = require('cordova/exec');

// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'GreatDayPlugin', 'coolMethod', [arg0]);
// };

  module.exports = {
    //just Get Location
    getLocation: function (onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocation', []);
    },
    //get location with radius
    getLocationRadius: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationRadius', [arg0]);
    },
    //just get camera
    getCamera: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getCamera', [arg0]);
    },
    //get camera with swap on or off
    getCameraSwap: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getCameraSwap', [arg0]);
    },
    //get location and camera
    getLocationCamera: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationCamera', [arg0]);
    },
    //get Location and camera swap on/off
    getLocationCameraSwap: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationCameraSwap', [arg0]);
    },
    //get Location with radius and camera swap on/off
    getLocationRadiusCameraSwap: function (arg0, onSuccess, onError) {
      exec(onSuccess, onError, 'GreatDayPlugin', 'getLocationRadiusCameraSwap', [arg0]);
    }
  };

});
