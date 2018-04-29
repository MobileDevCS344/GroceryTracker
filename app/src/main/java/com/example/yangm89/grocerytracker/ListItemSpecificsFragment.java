package com.example.yangm89.grocerytracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.LauncherActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class ListItemSpecificsFragment extends Fragment {
    private ListItemSpecificsFragment.OnFragmentInteractionListener mListener ;
    private Spinner spinner ;

    public ListItemSpecificsFragment() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_new_list_fragment, container, false);
    }

    public void onResume() {
        super.onResume();

        // do something here
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_category) ;
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mListener.setSpinner(spinner);

        if(mListener.getSelectedSpinner() != 0)
        {
            int selection = mListener.getSelectedSpinner() ;
            spinner.setSelection(selection);
        }

        String itemName = mListener.getItemName();
        String itemPrice = mListener.getItemPrice();
        String itemQuantity = mListener.getItemQuantity();
        String itemProtein = mListener.getItemProtein();
        String itemCarbs = mListener.getItemCarbs();
        String itemFat = mListener.getItemFat();
        String itemOther = mListener.getItemOther();

        ((EditText) getActivity().findViewById(R.id.editText_item)).setText(itemName) ;
        ((EditText) getActivity().findViewById(R.id.number_price)).setText(itemPrice); ;
        ((EditText) getActivity().findViewById(R.id.editText_quantity)).setText(itemQuantity) ;
        ((EditText) getActivity().findViewById(R.id.editText_protein)).setText(itemProtein) ;
        ((EditText) getActivity().findViewById(R.id.editText_fat)).setText(itemFat); ;
        ((EditText) getActivity().findViewById(R.id.editText_carbs)).setText(itemCarbs); ;
        ((EditText) getActivity().findViewById(R.id.editText_other)).setText(itemOther); ;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemSpecificsFragment.OnFragmentInteractionListener) {
            mListener = (ListItemSpecificsFragment.OnFragmentInteractionListener) context;
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
        void addItemToList(String s, ItemSpec i);
        void setSpinner(Spinner s) ;
        int getSelectedSpinner() ;
        String getItemName() ;
        String getItemPrice() ;
        String getItemQuantity() ;
        String getItemProtein() ;
        String getItemCarbs() ;
        String getItemFat() ;
        String getItemOther() ;
    }
}
