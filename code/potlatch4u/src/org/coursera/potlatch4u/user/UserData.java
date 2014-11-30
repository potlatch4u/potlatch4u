package org.coursera.potlatch4u.user;

/**
 * UserData
 * 
 * @author nbischof
 * @since Nov 27, 2014
 * 
 */
public class UserData {
	public static String getUserName() {
		return System.getProperty("user.name", "unknown");
	}
}
