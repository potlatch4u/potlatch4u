

package org.coursera.potlatch4u.orm;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 
 * <p>
 * based on the work by Vladimir Vivien (http://vladimirvivien.com/), which
 * provides a very logical organization of the meta-data of the Database and
 * Content Provider
 * <p>
 * This note might be moved to a 'Special Thanks' section once one is created
 * and moved out of future test code.
 * 
 * @author Michael A. Walker
 */
public class MoocSchema {

	/**
	 * Project Related Constants
	 */

	public static final String ORGANIZATIONAL_NAME = "org.coursera";
	public static final String PROJECT_NAME = "potlatch";

	/**
	 * ConentProvider Related Constants
	 */
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "."
			+ PROJECT_NAME + ".cloudprovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	// Define a static class that represents description of stored content
	// entity.
	public static class Gift {

        public static final String TABLE_NAME = "gift_table";
		// define a URI paths to access entity
		// BASE_URI/gift - for list of gift(s)
		// BASE_URI/gift/* - retrieve specific gift by id
		public static final String PATH = "gift";
		public static final int PATH_TOKEN = 110;

		public static final String PATH_FOR_ID = "gift/*";
		public static final int PATH_FOR_ID_TOKEN = 120;

		// URI for all content stored as gift entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		private final static String MIME_TYPE_END = "gift";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
				Cols.LOGIN_ID, Cols.GIFT_ID, Cols.TITLE, Cols.BODY,
				Cols.IMAGE_NAME, Cols.IMAGE_LINK, Cols.CREATION_TIME,
				Cols.GIFT_TIME };

		public static ContentValues initializeWithDefault(
				final ContentValues assignedValues) {
			// final Long now = Long.valueOf(System.currentTimeMillis());
			final ContentValues setValues = (assignedValues == null) ? new ContentValues()
					: assignedValues;
			if (!setValues.containsKey(Cols.LOGIN_ID)) {
				setValues.put(Cols.LOGIN_ID, 0);
			}
			if (!setValues.containsKey(Cols.GIFT_ID)) {
				setValues.put(Cols.GIFT_ID, 0);
			}
			if (!setValues.containsKey(Cols.TITLE)) {
				setValues.put(Cols.TITLE, "");
			}
			if (!setValues.containsKey(Cols.BODY)) {
				setValues.put(Cols.BODY, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_NAME)) {
				setValues.put(Cols.IMAGE_NAME, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_LINK)) {
				setValues.put(Cols.IMAGE_LINK, "");
			}
			if (!setValues.containsKey(Cols.CREATION_TIME)) {
				setValues.put(Cols.CREATION_TIME, 0);
			}
			if (!setValues.containsKey(Cols.GIFT_TIME)) {
				setValues.put(Cols.GIFT_TIME, 0);
			}
			return setValues;
		}

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database
			public static final String LOGIN_ID = "LOGIN_ID";
			public static final String GIFT_ID = "GIFT_ID";
			public static final String TITLE = "TITLE";
			public static final String BODY = "BODY";
			public static final String IMAGE_NAME = "IMAGE_NAME";
			public static final String IMAGE_LINK = "IMAGE_LINK";
			public static final String CREATION_TIME = "CREATION_TIME";
			public static final String GIFT_TIME = "GIFT_TIME";
		}
	}

	// Define a static class that represents description of stored content
	// entity.
	public static class Tags {
		public static final String TABLE_NAME = "tags_table";

		// define a URI paths to access entity
		// BASE_URI/tag - for list of tag(s)
		// BASE_URI/tag/* - retrieve specific tag by id
		public static final String PATH = "tag";
		public static final int PATH_TOKEN = 210;

		public static final String PATH_FOR_ID = "tag/*";
		public static final int PATH_FOR_ID_TOKEN = 220;

		// URI for all content stored as Restaurant entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		private final static String MIME_TYPE_END = "tags";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
				Cols.LOGIN_ID, Cols.GIFT_ID, Cols.TAG };

		public static ContentValues initializeWithDefault(
				final ContentValues assignedValues) {
			// final Long now = Long.valueOf(System.currentTimeMillis());
			final ContentValues setValues = (assignedValues == null) ? new ContentValues()
					: assignedValues;
			if (!setValues.containsKey(Cols.LOGIN_ID)) {
				setValues.put(Cols.LOGIN_ID, 0);
			}
			if (!setValues.containsKey(Cols.GIFT_ID)) {
				setValues.put(Cols.GIFT_ID, 0);
			}
			if (!setValues.containsKey(Cols.TAG)) {
				setValues.put(Cols.TAG, "");
			}
			return setValues;
		}

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database
			public static final String LOGIN_ID = "LOGIN_ID";
			public static final String GIFT_ID = "GIFT_ID";
			public static final String TAG = "TAG";
		}
	}

}
