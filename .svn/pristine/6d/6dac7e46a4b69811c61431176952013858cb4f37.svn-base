package com.varel.photo_editor.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.Button;
import com.varel.photo_editor.R;
import com.varel.photo_editor.abstract_libs.ImageFragment;
import com.varel.photo_editor.libs.ActionBarLib;
import com.varel.photo_editor.viewpagerindicator.TitlePageIndicator;

import java.util.List;

public class HistoryFragment extends ImageFragment {

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
      View v = inflater.inflate(R.layout.history_fragment, parent, false);
      setVariables(v);
//      actionBar = new ActionBarLib(parent) {
//         @Override
//         public void onActionBarClick(View v) {}
//      };
//      actionBar.initActionBar();

      return v;
   }

   private void setVariables(View view) {
//            ButtonCreateSquare = (Button) v.findViewById(R.id.button_create_square);
//            ButtonCreateSquare.setOnClickListener(new View.OnClickListener() {
//               public void onClick(View v) {
//                  Intent i = new Intent(getActivity(), SquareActivity.class);
//                  startActivity(i);
//               }
//            });
//
//            ButtonCreateFilters = (Button) v.findViewById(R.id.button_create_filters);
//            ButtonCreateFilters.setOnClickListener(new View.OnClickListener() {
//               public void onClick(View v) {
//                  Intent i = new Intent(getActivity(), FiltersEditorActivity.class);
//                  startActivity(i);
//               }
//            });
//      imageView = (ImageView) view.findViewById(R.id.image_view);
//      imageView.setOnLongClickListener(this);
//      imageView.setOnClickListener(this);
//      imageView.setOnCreateContextMenuListener(this);
   }

   public static HistoryFragment newInstance() {
      Bundle args = new Bundle();
      //args.putSerializable(EXTRA_CRIME_ID, crimeId);
      HistoryFragment fragment = new HistoryFragment();
      fragment.setArguments(args);
      return fragment;
   }
}
