

package org.coursera.potlatch4u.ui.gift;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Fragments require a Container Activity, this is the one for the Edit
 * GiftData, also handles launching intents for audio/video capture.
 */
public class CreateGiftActivity extends GiftActivityBase {

	private final static String LOG_TAG = CreateGiftActivity.class
			.getCanonicalName();

	public static final int MEDIA_TYPE_IMAGE = 1;

	static final int CAMERA_PIC_REQUEST = 1;

	private CreateGiftFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			fragment = CreateGiftFragment.newInstance();

			fragment.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fragment).commit();
		}
	}

	/**
	 * Method to be called when Photo Clicked button is pressed.
	 */
	public void addPhotoClicked(View aView) {
		launchCameraIntent();
	}

	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {
		Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Log.e("MyCameraApp", "SD not mounted!");
			Toast.makeText(null, "SD not mounted!", Toast.LENGTH_LONG).show(); // replyMessenger???
			return null;
		}

		// For future implementation: store videos in a separate directory
		File mediaStorageDir = new File(
				Environment
						.getExternalStorageDirectory(),
				"potlatch4u");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			Log.e(LOG_TAG, "typ of media file not supported: type was:" + type);
			return null;
		}

		return mediaFile;
	}
	
	// This function creates a new Intent to launch the built-in Camera activity

	private void launchCameraIntent() {
		Intent imageCaptureIntent = new Intent(ACTION_IMAGE_CAPTURE);
		fragment.imagePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		imageCaptureIntent
				.putExtra(MediaStore.EXTRA_OUTPUT, fragment.imagePath);
		Log.d(LOG_TAG, "Sending camera pic request for: " + fragment.imagePath);
		startActivityForResult(imageCaptureIntent, CAMERA_PIC_REQUEST);
	}

}
