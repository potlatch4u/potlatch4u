

package org.coursera.potlatch4u.orm;

import java.util.ArrayList;

import org.coursera.potlatch4u.orm.MoocSchema.Gift.Cols;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.RemoteException;

public class FormerResolver {

    private LocalCacheDBAdapter mDB;

	/**
	 * Constructor
	 * 
	 * @param activity
	 *            The Activity to get the ContentResolver from.
	 */
	public FormerResolver(Activity activity) {
        mDB = new LocalCacheDBAdapter(activity);
        mDB.open();
    }

    /*
     * Delete for each ORM Data Type
     */
    /**
     * Delete all GiftData(s) from the ContentProvider, that match the
     * selectionArgs
     * 
     * @param selection
     * @param selectionArgs
     * @return number of GiftData rows deleted
     * @throws RemoteException
     */
    private int deleteGiftData(final String selection, final String[] selectionArgs) throws RemoteException {
        return delete(selection, selectionArgs);
    }

    

	/*
	 * Insert for each ORM Data Type
	 */

	/**
	 * Insert a new GiftData object into the ContentProvider
	 * 
	 * @param giftObject
	 *            object to be inserted
	 * @return URI of inserted GiftData in the ContentProvider
	 * @throws RemoteException
	 */
    public void addGift(final GiftData giftObject) throws RemoteException {
		ContentValues tempCV = giftObject.getCV();
		tempCV.remove(MoocSchema.Gift.Cols.ID);
        insert(tempCV);
	}

	/**
	 * Query for each GiftData, Similar to standard Content Provider query,
	 * just different return type
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return an ArrayList of GiftData objects
	 * @throws RemoteException
	 */
    private ArrayList<GiftData> queryGiftData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the C.P.
        Cursor result = query(projection, selection,
				selectionArgs, sortOrder);
		// make return object
		ArrayList<GiftData> rValue = new ArrayList<GiftData>();
        // convert cursor to return object
		rValue.addAll(GiftCreator.getGiftDataArrayListFromCursor(result));
		result.close();
		// return 'return object'
		return rValue;
	}

    /**
     * Get a GiftData from the daga stored at the given rowID
     * 
     * @param id
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    public GiftData getGift(final long id) throws RemoteException {
        String[] selectionArgs = { String.valueOf(id) };
        ArrayList<GiftData> results = queryGiftData(null, MoocSchema.Gift.Cols.ID + "= ?", selectionArgs, null);
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    /**
     * Delete All rows, from AllGift table, that have the given rowID. (Should
     * only be 1 row, but Content Providers/SQLite3 deletes all rows with
     * provided rowID)
     * 
     * @param id
     * @return number of rows deleted
     * @throws RemoteException
     */
    public int deleteGift(long id) throws RemoteException {
        String[] args = { String.valueOf(id) };
        return deleteGiftData(MoocSchema.Gift.Cols.ID + " = ? ", args);
    }

    public ArrayList<GiftData> findByTitlePart(String filterWord) throws RemoteException {
        // create String that will match with 'like' in query
        filterWord = "%" + filterWord + "%";
        ArrayList<GiftData> currentList2 = queryGiftData(null,
        		Cols.TITLE + " LIKE ? ",
        		new String[] { filterWord }, null);
        return currentList2;
    }

    public static final int GIFT_ALL_ROWS = MoocSchema.Gift.PATH_TOKEN;

    private Cursor query(final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) {
        String modifiedSelection = selection;

        return query(MoocSchema.Gift.TABLE_NAME, projection, modifiedSelection, selectionArgs, sortOrder);

    }

    private Cursor query(final String tableName, final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) {
        return mDB.query(tableName, projection, selection, selectionArgs, sortOrder);
    }

    private int delete(String whereClause, String[] whereArgs) {
        int count = mDB.delete(MoocSchema.Gift.TABLE_NAME, whereClause, whereArgs);
        return count;

    }

    private long insert(ContentValues assignedValues) {

        final ContentValues values = MoocSchema.Gift.initializeWithDefault(assignedValues);
        values.remove(MoocSchema.Gift.Cols.ID);

        return mDB.insert(MoocSchema.Gift.TABLE_NAME, values);
    }

}
