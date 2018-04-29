package com.example.yangm89.grocerytracker;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;


public class ListItemFragment extends Fragment {
    private ListItemFragment.OnFragmentInteractionListener mListener;

    public ListItemFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_items_fragment, container, false);
    }

    public void onResume() {
        super.onResume();
       // String temp = mListener.updateItemList();
       // TextView list = ((TextView) getActivity().findViewById(R.id.textview_list_items));
        //list.setText(temp);

   /*     HashMap<String, ItemSpec> items = mListener.getItemMap() ;
        LinearLayout layout = ((LinearLayout) getActivity().findViewById(R.id.linear_layout_items_container)) ;
        for(String key : items.keySet())
        {
            ItemSpec i = items.get(key) ;
            TextView t = new TextView(getActivity()) ;

        }
        */

        mListener.printListItems();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemFragment.OnFragmentInteractionListener) {
            mListener = (ListItemFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        //3 - Ensure the activity implements this activity
        HashMap<String, ItemSpec> getItemMap() ;
        void printListItems() ;
    }
}
