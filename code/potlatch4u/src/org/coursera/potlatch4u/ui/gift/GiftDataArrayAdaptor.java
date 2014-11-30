


package org.coursera.potlatch4u.ui.gift;

import static org.coursera.potlatch4u.ui.gift.GiftListActivity.LOG_TAG;

import java.util.List;

import org.coursera.potlatch4u.R;
import org.coursera.potlatch4u.orm.GiftData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GiftDataArrayAdaptor extends ArrayAdapter<GiftData> {

    int resource;

    public GiftDataArrayAdaptor(Context _context, int _resource,
            List<GiftData> _items) {
        super(_context, _resource, _items);
        Log.d(LOG_TAG, "constructor()");
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView = null;
        try {
            GiftData item = getItem(position);

            long KEY_ID = item.id;
            String title = item.title;
            long touchedCount = item.touchCount;
            
            if (convertView == null) {
                todoView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext()
                        .getSystemService(inflater);
                vi.inflate(resource, todoView, true);
            } else {
                todoView = (LinearLayout) convertView;
            }

            TextView KEY_IDTV = (TextView) todoView
            		.findViewById(R.id.gift_listview_custom_row_KEY_ID_textView);
            
            TextView titleTV = (TextView) todoView
                    .findViewById(R.id.gift_listview_custom_row_title_textView);
            TextView touchedCountTV = (TextView) todoView
                    .findViewById(R.id.gift_listview_custom_row_touched_count_textView);
            
            KEY_IDTV.setText("" + KEY_ID);
            titleTV.setText("" + title);
            touchedCountTV.setText(getTouchedCountDisplayValue(touchedCount));
            
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "exception in ArrayAdpter: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return todoView;
    }

    private String getTouchedCountDisplayValue(long touchedCount) {
        String text;
        if (touchedCount == 0) {
            text = "Not touched anybody yet.";
        } else if (touchedCount == 1) {
            text = "Touched one user.";
        } else {
            text = "Touched " + touchedCount + " users.";
        }
        return text;
    }

}
