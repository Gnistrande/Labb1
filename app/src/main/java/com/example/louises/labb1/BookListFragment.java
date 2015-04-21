package com.example.louises.labb1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.view.ActionMode.Callback;

import com.example.louises.labb1.dummy.Datasource;
import com.example.louises.labb1.dummy.DummyContent;
import com.example.louises.labb1.dummy.Item;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Books. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link BookDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class BookListFragment extends ListFragment {

    private Datasource datasource;
    private ArrayAdapter<Item> adapter;
    private ActionMode mActionMode;

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
        public void onItemSelected(String id, Item mItem);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id, Item mItem) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //För att kunna skapa en optionsmenu och kunna lyssna på våra items
        setHasOptionsMenu(true);

        //hämtar ifrån databasen
        datasource = new Datasource(getActivity());

        datasource.open();

        // TODO: replace with a real list adapter.
        adapter = new ArrayAdapter<Item>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                datasource.fetchAll(2, true)
        );

        setListAdapter(adapter);

        ActionMode randomActionMode = getActivity().startActionMode(mActionModeCallback);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
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
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.item_detail_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.delete:

                    long temp_id = adapter.getItem(mActivatedPosition).getId();
                    getActivity().getSupportFragmentManager().popBackStack();
                    //delete from databasen
                    datasource.deleteItem(String.valueOf(temp_id));
                    //Remove from arrayadapter
                    adapter.remove(adapter.getItem(mActivatedPosition));
                    setActivatedPosition(-1);

                    //Stack and Pop with FragmentManager
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;

            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {

            mActionMode = null;


        }
    };

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        if(mActionMode == null) {
            mActionMode = getActivity().startActionMode(mActionModeCallback);
        }

        setActivatedPosition(position);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

        mCallbacks.onItemSelected(adapter.getItem(position).getId() + "", adapter.getItem(position));      //

        view.setSelected(true);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                //Om vi vill göra detta i en Activity, behöver vi nedanstående rad + kalla på denna som en funktion
                //BookListFragment bf = (BookListFragment) getSupportFragmentManager().findFragmentById(R.id.book_detail);

                datasource.insertItem("New Item", 5,  "Description");
                adapter.clear();
                adapter.addAll(datasource.fetchAll(2, true));

                adapter.notifyDataSetChanged();

                return true;
            case R.id.sorting:
                Dialog d = onCreateDialog();
                d.show();
                return true;
            case R.id.ascending:
                boolean isC = true;

                //CheckBox checkBox = (CheckBox) getView().findViewById(R.id.ascending);
                if (item.isChecked()) {
                    item.setChecked(false);
                    isC = false;
                } else {
                    item.setChecked(true);
                    isC = true;
                }

                getActivity().getPreferences(0).edit().putBoolean("ascending", isC).commit(); //MODE_PRIVATE

                Log.d("******* ", "isAscending: " + isC);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Dialog onCreateDialog() {
        int which = getActivity().getPreferences(0).getInt("choice", 0);
        Log.d("******* ", "Which: " + which);

        CharSequence[] arrayOfOptions = {"id", "title", "rating"};
        ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Sorting")
                .setSingleChoiceItems(arrayOfOptions, which, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //chosenOption = which;
                        //Only using chosenOption because setSingle... wanted another argument... ^^ TODO
                        //Log.d("******* ", "int which = " + which);
                        getActivity().getPreferences(0).edit().putInt("choice", which).commit(); //MODE_PRIVATE

                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });

        return builder.create();
    }
}



