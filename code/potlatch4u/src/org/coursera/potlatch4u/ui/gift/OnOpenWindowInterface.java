

package org.coursera.potlatch4u.ui.gift;

/**
 * An Interface for each that allows each C.R.U.D. (Create, Read, Update,
 * Delete, + List) Activity that allows its Fragment to open up the appropriate
 * other Fragment, when needed. (This is implemented to allow single APK to have
 * different behavior on tablets and phones.
 * 
 * @author Michael A. Walker
 * 
 */
public interface OnOpenWindowInterface {

    public void openViewGiftFragment(long index);

	public void openCreateGiftFragment();

	public void openListGiftFragment();
}
