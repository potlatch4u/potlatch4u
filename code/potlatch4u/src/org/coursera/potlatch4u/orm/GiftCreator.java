

package org.coursera.potlatch4u.orm;

import java.util.ArrayList;


import android.content.ContentValues;
import android.database.Cursor;

/**
 * GiftCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects, ContentValues, and Cursors.
 * 
 * @author Michael A. Walker
 * 
 */
public class GiftCreator {

	/**
	 * Create a ContentValues from a provided GiftData.
	 * 
	 * @param data
	 *            GiftData to be converted.
	 * @return ContentValues that is created from the GiftData object
	 */
	public static ContentValues getCVfromGift(final GiftData data) {
		ContentValues rValue = new ContentValues();
		rValue.put(MoocSchema.Gift.Cols.LOGIN_ID, data.loginId);
		rValue.put(MoocSchema.Gift.Cols.GIFT_ID, data.giftId);
		rValue.put(MoocSchema.Gift.Cols.TITLE, data.title);
		rValue.put(MoocSchema.Gift.Cols.BODY, data.description);
		rValue.put(MoocSchema.Gift.Cols.IMAGE_LINK, data.imageUri);
		rValue.put(MoocSchema.Gift.Cols.GIFT_TIME, data.touchCount);
		return rValue;
	}

	/**
	 * Get all of the GiftData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor to get GiftData(s) of.
	 * @return ArrayList<GiftData\> The set of GiftData
	 */
	public static ArrayList<GiftData> getGiftDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<GiftData> rValue = new ArrayList<GiftData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getGiftDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}

	/**
	 * Get the first GiftData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return GiftData object
	 */
	public static GiftData getGiftDataFromCursor(Cursor cursor) {

		long rowID = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.ID));
		long loginId = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.LOGIN_ID));
		long giftId = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.GIFT_ID));
		String title = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.TITLE));
		String body = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.BODY));
		String imageMetaData = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.IMAGE_LINK));
		long giftTime = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Gift.Cols.GIFT_TIME));

		// construct the returned object
		GiftData rValue = new GiftData(rowID, loginId, giftId, title, body,
 imageMetaData, giftTime);

		return rValue;
	}
}
