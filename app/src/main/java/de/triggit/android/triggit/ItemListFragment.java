package de.triggit.android.triggit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;
import de.triggit.android.triggit.service.GPSDAO;
import de.triggit.android.triggit.service.WLANDAO;
import de.triggit.android.triggit.ui.AddDialog;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link }.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment implements Button.OnClickListener  {

    private List<TriggitItem> items;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(TriggitItem item);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(TriggitItem item) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addButton = new Button(getActivity());
        addButton.setText("Hinzufügen");
        addButton.setOnClickListener(this);
        getListView().addFooterView(addButton);
    }

    private WLANDAO wlanDAO;
    private GPSDAO gpsDAO;
    private Button addButton;
    private ListAdapter listAdapter;



    @Override
    public void onClick(View v) {
        Log.d("ItemListFragment", "Add New Item");
        AddDialog dialog = new AddDialog();
        dialog.show(getFragmentManager(), "ADDDIALOG");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadALlItems();
        listAdapter = new TriggitListAdapter(getView().getContext(), android.R.layout.simple_list_item_activated_1, items);
        setListAdapter(listAdapter);

        registerForContextMenu(getListView());
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    private void loadALlItems() {
        items = new ArrayList<>();
        wlanDAO = new WLANDAO(getView().getContext());
        wlanDAO.open();
        for(WLANTriggItem item : wlanDAO.getAllItems()) {
            Log.d("ItemListFragment", "Add to List (WLAN): " + item.getName());
            items.add(item);
        }
        wlanDAO.close();

        gpsDAO = new GPSDAO(getView().getContext());
        gpsDAO.open();
        for(GPSTriggItem item : gpsDAO.getAllItems()) {
            Log.d("ItemListFragment", "Add to List (GPS): " + item.getName());
            items.add(item);
        }
        gpsDAO.close();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, view.getId(), 0, "Ändern");
        menu.add(0, view.getId(), 0, "Löschen");
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getTitle()=="Ändern"){
            editItem((int) info.id);

        }
        else if(item.getTitle()=="Löschen"){
            removeItem((int) info.id);
        } else {
            return false;
        }
        return true;
    }

    private void editItem(int id) {
        Log.d("itemListFragment - Context Menu:", "Edit Item: " + id);
        TriggitItem item = items.get(id);
        mCallbacks.onItemSelected(item);
    }

    public void removeItem(int id) {
        Log.d("itemListFragment - Context Menu:", "Remove Item: " + id);
        TriggitItem item = items.get(id);

        if(item instanceof WLANTriggItem) {
            WLANTriggItem wlan = (WLANTriggItem) item;
            wlanDAO.open();
            wlanDAO.deleteWLANItem(wlan);
            wlanDAO.close();
        } else {
            GPSTriggItem gps = (GPSTriggItem) item;
            gpsDAO.open();
            gpsDAO.deleteGPSItem(gps);
            gpsDAO.close();
        }
        refresh();
    }

    private void refresh() {
        loadALlItems();
        listAdapter = new TriggitListAdapter(getView().getContext(), android.R.layout.simple_list_item_activated_1, items);
        setListAdapter(listAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(items.get(position));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
