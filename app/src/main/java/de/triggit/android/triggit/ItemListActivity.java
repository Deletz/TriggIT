package de.triggit.android.triggit;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;

import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * <p/>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }



        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(TriggitItem item) {


            // In single-pane mode, simply start the detail activity
            // for the selected item ID.

            Intent detailIntent = new Intent(this, SettingsActivity.class);
            long id = 0;
            if(item instanceof WLANTriggItem) {
                detailIntent.putExtra("item_type", "wlan");
                id = ((WLANTriggItem) item).getPrimaryKey();
            } else {
                detailIntent.putExtra("item_type", "gps");
                id = ((GPSTriggItem) item).getPrimarykey();
            }

            detailIntent.putExtra("item_id", id);
            startActivity(detailIntent);
    }

}
