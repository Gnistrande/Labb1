package com.example.louises.labb1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.util.Log;

//import com.example.louises.labb1.dummy.DummyContent;
import com.example.louises.labb1.dummy.Datasource;
import com.example.louises.labb1.dummy.Item;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";
    public static final String ARG_ITEM_RATING = "item_rating";



    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    private String mItemID = new String();
    private String mItemTitle = new String();
    private String mItemDesc = new String();
    private int mItemRating;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            // Behöver vi göra något här när vi har Item?
            mItemID = getArguments().getString(ARG_ITEM_ID);
            mItemTitle = getArguments().getString(ARG_ITEM_TITLE);
            mItemDesc = getArguments().getString(ARG_ITEM_DESCRIPTION);
            mItemRating = getArguments().getInt(ARG_ITEM_RATING);

            //mItem = datasource.fetchAll(2, true).get(getArguments().getInt(ARG_ITEM_ID));
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);

        //String argumentsText = savedInstanceState.toString();
        //Log.d("*************************************", argumentsText);

        /*Item dumItem = new Item();
        dumItem.setTitle("Nalle Puh");
        dumItem.setRating(5);
        dumItem.setDescription("Han äter honung");*/

        Log.d("*************FRAGMENT************", "id = " + mItemID + "  title = " + mItemTitle + " description = " + mItemDesc + " rating = " + mItemRating);

        // Show the dummy content as text in a TextView.
        if (mItemID != null) {
            ((TextView) rootView.findViewById(R.id.textViewId)).setText(mItemTitle);
        }
        if (mItemTitle != null) {
            ((TextView) rootView.findViewById(R.id.title)).setText(mItemTitle);
        }
        if (mItemDesc != null) {
            ((TextView) rootView.findViewById(R.id.desc)).setText(mItemDesc);
        }
        if (mItemRating != 0) {
            ((RatingBar) rootView.findViewById(R.id.ratingBar)).setRating(mItemRating);
        }

        return rootView;
    }
}
