package com.varel.photo_editor.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import com.varel.photo_editor.R;
import com.varel.photo_editor.abstract_libs.ImageFragment;
import com.varel.photo_editor.libs.ActionBarLib;
import com.varel.photo_editor.viewpagerindicator.TitlePageIndicator;

import java.util.List;

public class SignFragment extends ImageFragment {

   private Button ButtonCreateSquare;
   private Button ButtonCreateFilters;

   private List<View> mPages;
   private View mJokesRuPage;
   private View mJokesEnPage;

   private ViewPager mPager;
   private TitlePageIndicator mTitleIndicator;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      actionBar = getActivity().getActionBar();
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.sign_fragment, parent, false);
      setVariables(v);
      return v;
   }

   private void setVariables(View view) {

   }

   public static SignFragment newInstance() {
      Bundle args = new Bundle();
      //args.putSerializable(EXTRA_CRIME_ID, crimeId);
      SignFragment fragment = new SignFragment();
      fragment.setArguments(args);
      return fragment;
   }
}
