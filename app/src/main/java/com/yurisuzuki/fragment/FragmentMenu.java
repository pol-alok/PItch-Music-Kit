package com.yurisuzuki.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yurisuzuki.CustomTypefaceSpan;
import com.yurisuzuki.MainActivity;
import com.yurisuzuki.playsound.R;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public final class FragmentMenu extends Fragment {
    ArrayList<TextView> textViews;

	public int containerId;
	public int containerWidth;

	public static FragmentMenu newInstance() {
		return new FragmentMenu();
	}

	public FragmentMenu() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_menu, container, false);

		containerId = container.getId();
		containerWidth = container.getWidth();

		textViews = new ArrayList<>();
		textViews.add((TextView) layout.findViewById(R.id.menu_title_make_markers));
		textViews.add((TextView) layout.findViewById(R.id.menu_title_play_guitar));
		textViews.add((TextView) layout.findViewById(R.id.menu_title_play_piano));
		textViews.add((TextView) layout.findViewById(R.id.menu_title_play_music_box));
		textViews.add((TextView) layout.findViewById(R.id.menu_title_about));

		configureMenus();

		return layout;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void configureMenus(){
		Typeface tfLight = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/HelveticaNeueLTStd-Lt.otf");
		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/HelveticaNeueLTStd-Bd.otf");

		for(TextView textView : textViews){
			final String text = (String)textView.getText();
			String[] list = text.split(" ");
			String firstWord = list[0];
			String secondWord;

			if(list.length > 1){
				firstWord += " ";
				secondWord = list[1];

				if(list.length == 3){
					secondWord = secondWord + " " + list[2];
				}
			}else{
				secondWord = "";
			}

			// Create a new spannable with the two strings
			Spannable spannable = new SpannableString(firstWord+secondWord);

			// Set the custom typeface to span over a section of the spannable object
			spannable.setSpan( new CustomTypefaceSpan("sans-serif",tfLight), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan( new CustomTypefaceSpan("sans-serif",tfBold), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			textView.setText(spannable);

			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String tag = (String)v.getTag();
					switch (tag) {
						case "marker":
							((MainActivity) getActivity()).showMakeMarkerTop();
							break;
						case "about":
							((MainActivity) getActivity()).showAbout();
							break;
						default:
							((MainActivity) getActivity()).showInstruction(tag);
							break;
					}
				}
			});
		}
	}
}
