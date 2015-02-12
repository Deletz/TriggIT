package de.triggit.android.triggit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;
import de.triggit.android.triggit.service.TriggItemDAO;

/**
 * Created by DooM on 07.02.2015.
 */
public class TriggitListAdapter extends ArrayAdapter<TriggitItem> {

    public TriggitListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public TriggitListAdapter(Context context, int textViewResourceId, List<TriggitItem> items) {
        super(context, textViewResourceId, items);
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View v = contentView;

        if(v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.triggit_row_layout, null);
        }

        final TriggitItem item = getItem(position);

        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        TextView text = (TextView) v.findViewById(R.id.textView);
        final ToggleButton button = (ToggleButton) v.findViewById(R.id.toggleButton);
        setButtonState(item.getActive(), button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListAdapter", "Button click: Old (" + item.getActive()+") - new ("+!item.getActive()+")" );
                item.setActive(!item.getActive());
                setButtonState(item.getActive(), v);
                TriggItemDAO dao = new TriggItemDAO(getContext());
                dao.open();
                dao.updateItem(item);
                dao.close();
            }
        });

        if(item instanceof WLANTriggItem) {
            WLANTriggItem wlan = (WLANTriggItem) item;
            image.setImageResource(R.drawable.wlan_image);

        } else {
            image.setImageResource(R.drawable.gps_image);
        }
        text.setText(item.getName());

        return v;
    }

    private void setButtonState(boolean state, View v) {
        ToggleButton b = (ToggleButton) v;
        if(state == true) {
            b.setText(b.getTextOn());
        } else {
            b.setText(b.getTextOff());
        }
    }

}
