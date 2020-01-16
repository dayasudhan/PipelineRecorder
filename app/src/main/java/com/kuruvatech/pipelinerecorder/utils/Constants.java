package com.kuruvatech.pipelinerecorder.utils;


public class Constants {

    public static final String LOCALHOST = "http://192.168.1.104:3000";

    public static final String RELEASE_URL = "http://pipelinemap.herokuapp.com";
    public static final String DEBUG_URL = "http://pipelinemap.herokuapp.com";

    public static final String MAIN_URL = DEBUG_URL;
    public static final String USERNAME = "PipelineMap";
    public static final String PARTY = "PipelineMap";
    public static final String GET_PIPELINE_URL = MAIN_URL + "/v1/plinemap/all";
    public static final String GET_PIPELINE_URL_FINAL = MAIN_URL + "/v1/plinemap/phonelive/yes/live";
    public static final String POST_PIPELINE_URL = MAIN_URL + "/v1/pline/b";
    public static final String GET_PIPELINE_NEARBY_URL = MAIN_URL + "/v1/plinemap/nearby";
    public static final String GET_PIPELINE_WITHIN_URL = MAIN_URL + "/v1/plinemap/geowithin";
    public static final String GET_PIPELINE_VENDOR = MAIN_URL + "/v1/plinemap/";
    public static final String POST_LETTER_URL = MAIN_URL + "/v1/candidate/suggestion/" +USERNAME;
    public static final String LOGIN_URL = MAIN_URL + "/v1/m/login/";

    public static final String FIREBASE_APP = "https://project-8598805513533999178.firebaseio.com";
    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
    public static final String INVITE_TEXT = "Invite Your Friends to this app" +

            "Download this android App: https://play.google.com/store/apps/details?id=com.kuruvatech.PipelineMap";
    public static final String INVITE_SUBJECT = "Your App";


    public static final String SECUREKEY_KEY = "securekey";
    public static final String VERSION_KEY = "version";
    public static final String CLIENT_KEY = "client";

    public static final String SECUREKEY_VALUE = "EjR7tUPWx7WhsVs9FuVO6veFxFISIgIxhFZh6dM66rs";
    public static final String VERSION_VALUE = "1";
    public static final String CLIENT_VALUE = "bhoomika";
    public static final String ADMOBAPPID   =  "ca-app-pub-6150934781455122~3158839988";
    public static final String ADUNITID   =  "ca-app-pub-6150934781455122/2281377166";;
    public static final int TITLE_TEXT_COLOR_RED = 00;
    public static final int TITLE_TEXT_COLOR_GREEN = 177;
    public static final int TITLE_TEXT_COLOR_BLUE = 106;


    // Activity request codes 
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "PipelineMap";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";


}
