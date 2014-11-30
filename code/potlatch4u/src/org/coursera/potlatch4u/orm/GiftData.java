package org.coursera.potlatch4u.orm;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom ORM container class, for Gift Data.
 * <p>
 * This class is meant as a helper class for those working with the
 * ContentProvider. The use of this class is completely optional.
 * <p>
 * ORM = Object Relational Mapping
 * http://en.wikipedia.org/wiki/Object-relational_mapping
 * <p>
 * This class is a simple one-off POJO class with some simple ORM additions that
 * allow for conversion between the incompatible types of the POJO java classes,
 * the 'ContentValues', and the 'Cursor' classes from the use with
 * ContentProviders.
 * 
 * @author Michael A. Walker
 * 
 */
public class GiftData implements Parcelable {

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public final long id;
    public long loginId;
    public long giftId;
    public String title;
    public String description;
    public String imageUri;
    public long touchCount;

    /**
     * Constructor WITHOUT _id, this creates a new object for insertion into the
     * ContentProvider
     * 
     * @param loginId
     * @param giftId
     * @param title
     * @param description
     * @param audioLink
     * @param videoLink
     * @param imageName
     * @param imageUri
     * @param tags
     * @param creationTime
     * @param touchCount
     * @param latitude
     * @param longitude
     */
    public GiftData(long loginId, long giftId, String title, String description, String imageUri,

    long touchCount) {
        this.id = -1;
        this.loginId = loginId;
        this.giftId = giftId;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.touchCount = touchCount;
    }

    /**
     * Constructor WITH _id, this creates a new object for use when pulling
     * already existing object's information from the ContentProvider
     * 
     * @param id
     * @param loginId
     * @param giftId
     * @param title
     * @param description
     * @param imageName
     * @param imageUri
     * @param creationTime
     * @param touchCount
     */
    public GiftData(long KEY_ID, long loginId, long giftId, String title, String description, String imageLink,
            long touchCount) {
        this.loginId = loginId;
        this.giftId = giftId;
        this.title = title;
        this.description = description;
        this.imageUri = imageLink;
        this.touchCount = touchCount;
        this.id = KEY_ID;
    }

    public GiftData(long loginId, long giftId, String title, String description, String imageLink) {
        this(loginId, giftId, title, description, imageLink, 0);
    }

    /**
     * Override of the toString() method, for testing/logging
     */
    @Override
    public String toString() {
        return " loginId: " + loginId + " giftId: " + giftId + " title: " + title + " description: " + description
                + " imageUri: " + imageUri + " touchCount: " + touchCount;
    }

    /**
     * Helper Method that allows easy conversion of object's data into an
     * appropriate ContentValues
     * 
     * @return contentValues A new ContentValues object
     */
    public ContentValues getCV() {
        return GiftCreator.getCVfromGift(this);
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftData clone() {
        return new GiftData(loginId, giftId, title, description, imageUri, touchCount);
    }

    // these are for parcelable interface
    @Override
    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public int describeContents() {
        return 0;
    }

    @Override
    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(loginId);
        dest.writeLong(giftId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUri);
        dest.writeLong(touchCount);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<GiftData> CREATOR = new Parcelable.Creator<GiftData>() {
        public GiftData createFromParcel(Parcel in) {
            return new GiftData(in);
        }

        public GiftData[] newArray(int size) {
            return new GiftData[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private GiftData(Parcel in) {
        id = in.readLong();
        loginId = in.readLong();
        giftId = in.readLong();
        title = in.readString();
        description = in.readString();
        imageUri = in.readString();
        touchCount = in.readLong();
    }

}
