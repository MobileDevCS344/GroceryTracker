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
        //do something here
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_category) ;
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String item = ((EditText) getActivity().findViewById(R.id.editText_item)).getText().toString() ;
        double price = Double.parseDouble(((EditText) getActivity().findViewById(R.id.number_price)).getText().toString()) ;
        double quantity = Double.parseDouble(((EditText) getActivity().findViewById(R.id.editText_quantity)).getText().toString()) ;
        String category = ((Spinner) getActivity().findViewById(R.id.spinner_category)).getSelectedItem().toString() ;

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
        //void addItemToList(String s);
    }
}
