package com.yurisuzuki.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.yurisuzuki.ActivityIntro;
import com.yurisuzuki.playsound.R;

/**
  * A simple {@link Fragment} subclass.
  * Use the {@link FragmentIntroBase#newInstance} factory method to
  * create an instance of this fragment.
  */
 public class FragmentIntroBase extends Fragment {


    IntroFragmentAdapter introFragmentAdapter;
    ViewPager viewPager;
    PageIndicator pageIndicator;
    public String type;
     /**
      * Use this factory method to create a new instance of
      * this fragment using the provided parameters.
      *
      * @param type Parameter 1.
      * @return A new instance of fragment FragmentIntroBase.
      */
     // TODO: Rename and change types and number of parameters
     public static FragmentIntroBase newInstance(String type) {
         FragmentIntroBase fragment = new FragmentIntroBase();
         Bundle args = new Bundle();
         args.putString("type", type);
         fragment.setArguments(args);
         return fragment;
     }

     public FragmentIntroBase() {
         // Required empty public constructor
     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         if (getArguments() != null) {
             type = getArguments().getString("type");
         }
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_intro_base, container, false);

         TextView titleView = view.findViewById(R.id.inst_title);
         if(type != null){
             int title;
             switch (type) {
                 case "guitar":
                     title = R.string.inst_title_guitar;
                     break;
                 case "piano":
                     title = R.string.inst_title_piano;
                     break;
                 case "musicbox":
                     title = R.string.inst_title_mb;
                     break;
                 case "trace":
                     title = R.string.inst_title_trace;
                     break;
                 default:
                     title = R.string.inst_title_trace;
                     break;
             }
             titleView.setText(title);
         }

         Button startButton = view.findViewById(R.id.get_started_button);

         startButton.setOnClickListener(new View.OnClickListener() {
             @TargetApi(Build.VERSION_CODES.M)
             @Override
             public void onClick(View v) {
                 viewPager.setCurrentItem(0);
                 ((ActivityIntro)FragmentIntroBase.this.getActivity()).jumpToCamera();
                 //Toast.makeText(getContext(), "skip button is working",
                         //Toast.LENGTH_LONG).show();
             }
         });
         return view;
     }

    @Override
         public void onActivityCreated(Bundle savedInstanceState) {
             super.onActivityCreated(savedInstanceState);

             if (savedInstanceState == null) {
                 viewPager = getActivity().findViewById(R.id.pager);
                 pageIndicator = (CirclePageIndicator) getActivity().findViewById(
                         R.id.indicator);
                 ((CirclePageIndicator)pageIndicator).setFillColor(0xFF939597);
                 introFragmentAdapter = new IntroFragmentAdapter(this.getActivity()
                         .getFragmentManager());
                 introFragmentAdapter.type = this.type;
                 viewPager.setAdapter(introFragmentAdapter);

                 pageIndicator.setViewPager(viewPager);
                 introFragmentAdapter.notifyDataSetChanged();
             }
         }

     @Override
     public void onAttach(Activity activity) {
         super.onAttach(activity);
         try {
             //mListener = (OnFragmentInteractionListener) activity;
         } catch (ClassCastException e) {
             e.printStackTrace();
         }
     }

     @Override
     public void onDetach() {
         super.onDetach();
         //mListener = null;
     }
 }
