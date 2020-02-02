package  com.kuruvatech.pipelinerecorder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import com.kuruvatech.pipelinerecorder.LoginActivity;


public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	public static final String PREF_NAME = "PipeLine";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	public static  boolean isKill = false;
	
	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// Email address (make variable public to access from outside)
	public static final String KEY_HOTEL_INFO = "hotelinfo";

	// Email address (make variable public to access from outside)
	public static final String KEY_ISOPEN = "isopen";

	//To store the firebase id in shared preferences
	public static final String UNIQUE_ID = "uniqueid";
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String email , String uniqueId){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_NAME, name);
		
		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		//Storing the unique id
		editor.putString(Constants.UNIQUE_ID, uniqueId);
		// commit changes
		editor.commit();

	}
	public String getEmail()
	{
		String id = pref.getString(KEY_EMAIL, null);
		return id;
	}
	public void setHotelInfo(String hotelInfo)
	{
		editor.putString(KEY_HOTEL_INFO,hotelInfo);
		editor.commit();
	}
	public void setHotelopen(String isopen)
	{
		editor.putString(KEY_ISOPEN,isopen);
		editor.commit();
	}
	public String getHotelInfo()
	{
		String id = pref.getString(KEY_HOTEL_INFO, null);
		return id;
	}
	public String getHotelopen()
	{
		String id = pref.getString(KEY_ISOPEN, null);
		return id;
	}
		/**
		 * Check login method wil check user login status
		 * If false it will redirect user to login page
		 * Else won't do anything
		 * */

		public boolean checkLogin(){
			// Check login status
			if(!this.isLoggedIn()){
				// user is not logged in redirect him to Login Activity
				Intent i = new Intent(_context, LoginActivity.class);

				// Closing all the Activities
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				// Add new Flag to start new Activity
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//	i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				// Staring Login Activity
				_context.startActivity(i);
			}
			return true;
		}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		//final boolean b = _context.stopService(new Intent(_context, NotificationListener.class));
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);

	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
