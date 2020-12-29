package com.greatday.plugins;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.greatdayhr.videorecruitment.VideoRecruitmentPlugin;
import com.greatdayhr.videorecruitment.VideoRecruitmentPluginListener;
import com.senjuid.camera.CameraPlugin;
import com.senjuid.camera.CameraPluginListener;
import com.senjuid.camera.CameraPluginOptions;
import com.senjuid.location.LocationPlugin;
import com.senjuid.location.LocationPluginOptions;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.pm.ActivityInfo;

/**
 * This class echoes a string called from JavaScript.
 */
public class GreatDayPlugin extends CordovaPlugin {

  private CallbackContext context;
  private CameraPlugin cameraPlugin;
  private LocationPlugin locationPlugin;
  private VideoRecruitmentPlugin videoRecruitmentPlugin;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.context = callbackContext;

    switch (action) {
      case "getCamera": {
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));

        CameraPluginOptions options = new CameraPluginOptions.Builder()
          .setName(photoName)
          .setDisableFacingBack(true)
          .setMaxSize(maxSize)
          .setQuality(quality)
          .build();
        this.takePhoto(options);
        return true;
      }
      case "getCameraSwap": { // ini dipanggil, di panggil di semua fitur
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));

        CameraPluginOptions options = new CameraPluginOptions.Builder()
          .setName(photoName)
          .setDisableFacingBack(false)
          .setMaxSize(maxSize)
          .setQuality(quality)
          .build();
        this.takePhoto(options);
        return true;
      }
      case "getLocation": {
        this.getLocation(null);
        return true;
      }
      case "getLocationRadius": {
        String workLocationData = args.getString(0);
        this.getLocation(workLocationData);
        return true;
      }
      case "getLocationCamera": { // ini dipanggil, attendance
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));

        CameraPluginOptions options = new CameraPluginOptions.Builder()
          .setName(photoName)
          .setDisableFacingBack(true)
          .setMaxSize(maxSize)
          .setQuality(quality)
          .build();
        this.getLocationAndTakePhoto(options, null, null, null, false);
        return true;
      }
      case "getLocationCameraSwap": { // ini dipanggil, act recording
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));

        CameraPluginOptions options = new CameraPluginOptions.Builder()
          .setName(photoName)
          .setDisableFacingBack(false)
          .setMaxSize(maxSize)
          .setQuality(quality)
          .build();
        this.getLocationAndTakePhoto(options, null, null, null, false);
        return true;
      }
      case "getLocationRadiusCamera": {
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));
        String location = data.getString("location");
        String label1 = data.getString("label1");
        String label2 = data.getString("label2");
        boolean showAddress = Boolean.parseBoolean(data.getString("show_address"));

        CameraPluginOptions options = new CameraPluginOptions.Builder()
                .setName(photoName)
                .setDisableFacingBack(true)
                .setMaxSize(maxSize)
                .setQuality(quality)
                .build();
        this.getLocationAndTakePhoto(options, location, label1, label2, showAddress);
        return true;
      }
      case "getLocationRadiusCameraSwap": {
        JSONObject data = args.getJSONObject(0);
        String photoName = data.getString("photoName");
        int quality = parseQuality(data.getString("quality"));
        int maxSize = parseMaxSize(data.getString("max_size"));
        String location = data.getString("location");
        String label1 = data.getString("label1");
        String label2 = data.getString("label2");

        CameraPluginOptions options = new CameraPluginOptions.Builder()
          .setName(photoName)
          .setDisableFacingBack(false)
          .setMaxSize(maxSize)
          .setQuality(quality)
          .build();
        this.getLocationAndTakePhoto(options, location, label1, label2, false);
        return true;
      }
      case "getLocationLabelLanguage": {
        JSONObject data = args.getJSONObject(0);
        String label1 = data.getString("label1");
        String label2 = data.getString("label2");
        String language = data.getString("language");
        this.getLocationWithLanguage(null, label1, label2, language);
        return true;
      }
      case "getLocationLabelLanguageRadius": {
        this.context = callbackContext;
        JSONObject data = args.getJSONObject(0);
        String label1 = data.getString("label1");
        String label2 = data.getString("label2");
        String language = data.getString("language");
        String location = data.getString("location");
        this.getLocationWithLanguage(location, label1, label2, language);
        return true;
      }
      case "getRecruitmentData": {
        videoRecruitmentPlugin = new VideoRecruitmentPlugin(new VideoRecruitmentPluginListener() {
          @Override
          public void onComplete(@NotNull JSONArray jsonArray) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, jsonArray.toString());
            GreatDayPlugin.this.context.sendPluginResult(result);
          }
        });

        JSONObject data = args.getJSONObject(0);
        String questions = data.getString("questions");
        Intent i = videoRecruitmentPlugin.getIntent(this.cordova.getActivity(), questions);
        this.cordova.startActivityForResult(this, i, VideoRecruitmentPlugin.REQUEST);
        return true;
      }
      case "setWhiteLabel": {
        JSONObject data = args.getJSONObject(0);
        String name = data.getString("name");
        String packageId = data.getString("packageId");
        setWhiteLabel(name, packageId);
        return true;
      }
    }
    return false;
  }

  private void takePhoto(CameraPluginOptions options) {
    cameraPlugin = new CameraPlugin(GreatDayPlugin.this.cordova.getActivity());
    cameraPlugin.setCameraPluginListener(new CameraPluginListener() {
      @Override
      public void onSuccess(@NotNull String s, boolean b) {
        JSONObject jsonLocation = new JSONObject();
        try {
          jsonLocation.put("path", s);
          jsonLocation.put("native", b);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonLocation);
        GreatDayPlugin.this.context.sendPluginResult(result);
      }

      @Override
      public void onCancel() {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "cancelled");
        GreatDayPlugin.this.context.sendPluginResult(result);
      }
    });

    this.cordova.startActivityForResult(this, cameraPlugin.getIntent(options), CameraPlugin.REQUEST);
  }

  private void getLocation(String location) throws JSONException {
    LocationPlugin.LocationPluginListener locationPluginListener = new LocationPlugin.LocationPluginListener() {
      @Override
      public void onLocationRetrieved(Double lon, Double lat, Boolean isMock) {
        JSONObject jsonLocation = new JSONObject();
        try {
          String address = locationPlugin.getCompleteAddress(lon, lat);
          jsonLocation.put("latitude", String.valueOf(lat));
          jsonLocation.put("longitude", String.valueOf(lon));
          jsonLocation.put("address", address);
          jsonLocation.put("mock", isMock);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonLocation);
        GreatDayPlugin.this.context.sendPluginResult(result);
      }

      @Override
      public void onCanceled() {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "error location");
        GreatDayPlugin.this.context.sendPluginResult(result);
      }
    };

    locationPlugin = new LocationPlugin(this.cordova.getActivity());
    locationPlugin.setLocationPluginListener(locationPluginListener);
    LocationPluginOptions options = new LocationPluginOptions.Builder()
      .setData(location)
      .build();
    Intent intent = locationPlugin.getIntent(options);
    this.cordova.startActivityForResult(this, intent, LocationPlugin.REQUEST);
  }

  private void getLocationAndTakePhoto(CameraPluginOptions cameraOption, String location, String message1, String message2, boolean showAddress) throws JSONException {
    JSONObject jsonLocation = new JSONObject();
    CameraPluginListener cameraPluginListener = new CameraPluginListener() {
      @Override
      public void onSuccess(@NotNull String s, boolean b) {
        try {
          jsonLocation.put("path", s);
          jsonLocation.put("native", b);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonLocation);
        GreatDayPlugin.this.context.sendPluginResult(result);
      }

      @Override
      public void onCancel() {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "cancelled");
        GreatDayPlugin.this.context.sendPluginResult(result);
      }
    };
    LocationPlugin.LocationPluginListener locationPluginListener = new LocationPlugin.LocationPluginListener() {
      @Override
      public void onLocationRetrieved(Double lon, Double lat, Boolean isMock) {
        try {
          String address = "";
          if(showAddress == true) {
            address = locationPlugin.getCompleteAddress(lat, lon);
          }

          jsonLocation.put("latitude", String.valueOf(lat));
          jsonLocation.put("longitude", String.valueOf(lon));
          jsonLocation.put("address", address);
          jsonLocation.put("mock", isMock);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        cameraPlugin = new CameraPlugin(GreatDayPlugin.this.cordova.getActivity());
        cameraPlugin.setCameraPluginListener(cameraPluginListener);
        GreatDayPlugin.this.cordova.startActivityForResult(GreatDayPlugin.this,
          cameraPlugin.getIntent(cameraOption), CameraPlugin.REQUEST);
      }

      @Override
      public void onCanceled() {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "error location");
        GreatDayPlugin.this.context.sendPluginResult(result);
      }
    };

    locationPlugin = new LocationPlugin(this.cordova.getActivity());
    locationPlugin.setLocationPluginListener(locationPluginListener);
    LocationPluginOptions options = new LocationPluginOptions.Builder()
      .setData(location)
      .setMessage(message1, message2)
      .build();
    Intent intent = locationPlugin.getIntent(options);
    this.cordova.startActivityForResult(this, intent, LocationPlugin.REQUEST);
  }

  private void getLocationWithLanguage(String location, String message1, String message2, String language) throws JSONException {
    if (language != null) {
      com.senjuid.location.util.LocaleHelper.setLocale(this.cordova.getContext(), language);
    }

    LocationPlugin.LocationPluginListener locationPluginListener = new LocationPlugin.LocationPluginListener() {
      @Override
      public void onLocationRetrieved(Double lon, Double lat, Boolean isMock) {
        JSONObject jsonLocation = new JSONObject();
        try {
          String address = locationPlugin.getCompleteAddress(lon, lat);
          jsonLocation.put("latitude", String.valueOf(lat));
          jsonLocation.put("longitude", String.valueOf(lon));
          jsonLocation.put("address", address);
          jsonLocation.put("mock", isMock);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonLocation);
        GreatDayPlugin.this.context.sendPluginResult(result);
      }

      @Override
      public void onCanceled() {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "error location");
        GreatDayPlugin.this.context.sendPluginResult(result);
      }
    };

    locationPlugin = new LocationPlugin(this.cordova.getActivity());
    locationPlugin.setLocationPluginListener(locationPluginListener);
    LocationPluginOptions options = new LocationPluginOptions.Builder()
      .setData(location)
      .setMessage(message1, message2)
      .build();
    Intent intent = locationPlugin.getIntent(options);
    this.cordova.startActivityForResult(this, intent, LocationPlugin.REQUEST);
  }

  private void setWhiteLabel(String name, String packageId) {
    try {
      PackageManager pm = this.cordova.getContext().getPackageManager();
      ActivityInfo ai = pm.getActivityInfo(this.cordova.getActivity().getIntent().getComponent(), PackageManager.GET_META_DATA);
      if(ai.name.contains("default")){
        pm.setComponentEnabledSetting(
          new ComponentName(packageId, packageId + "." + name),
          PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
          PackageManager.DONT_KILL_APP);
      }else{
        pm.setComponentEnabledSetting(
          new ComponentName(packageId, packageId + ".default"),
          PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
          PackageManager.DONT_KILL_APP);
      }
    }catch (Exception e){}
  }

  @Override
  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    if (cameraPlugin != null) {
      cameraPlugin.onActivityResult(requestCode, resultCode, data);
    }
    if (locationPlugin != null) {
      locationPlugin.onActivityResult(requestCode, resultCode, data);
    }
    if (videoRecruitmentPlugin != null) {
      videoRecruitmentPlugin.onActivityResult(requestCode, resultCode, data);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private int parseQuality(String qualityStr) {
    if (qualityStr != null && !qualityStr.isEmpty()) {
      return Integer.parseInt(qualityStr);
    }
    return 100;
  }

  private int parseMaxSize(String maxSizeStr) {
    if (maxSizeStr != null && !maxSizeStr.isEmpty()) {
      return Integer.parseInt(maxSizeStr);
    }
    return 1024;
  }
}