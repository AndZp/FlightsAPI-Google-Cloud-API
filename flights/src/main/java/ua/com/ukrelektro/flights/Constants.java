package ua.com.ukrelektro.flights;

import com.google.api.server.spi.Constant;

/**
 * Contains the client IDs and scopes for allowed clients consuming the
 * helloworld API.
 */
public class Constants {
	public static final String WEB_CLIENT_ID = "75420113411-p3p3rsj5c3ap33dl9qs7ob441ebn1r7v.apps.googleusercontent.com";
	public static final String ANDROID_CLIENT_ID = "75420113411-rab0rmve0pn0ti2k23ben9m9ks04h85s.apps.googleusercontent.com";
	public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
	public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
	public static final String EMAIL_SCOPE = Constant.API_EMAIL_SCOPE;
	public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;

	public static final String MEMCACHE_ANNOUNCEMENTS_KEY = "RECENT_ANNOUNCEMENTS";
}
