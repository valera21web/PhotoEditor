package com.varel.photo_editor.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import com.varel.photo_editor.R;
import com.varel.photo_editor.fragments.HistoryFragment;
import com.varel.photo_editor.fragments.HomeFragment;
import com.varel.photo_editor.fragments.SignFragment;
import com.varel.photo_editor.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends Activity {

   private String[] CONTENT;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main_activity);
      CONTENT = new String[] {
              getString(R.string.activity_sub_title_sign),
              getString(R.string.activity_sub_title_main),
              getString(R.string.activity_sub_title_history)
      };

      FragmentPagerAdapter adapter = new MainAdapter(getFragmentManager());

      ViewPager pager = (ViewPager)findViewById(R.id.pager);
      pager.setAdapter(adapter);

      TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
      indicator.setViewPager(pager);
      indicator.setCurrentItem(1);
   }


   @Override
   public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
      try {
         super.onSaveInstanceState(outState);
      } catch (NullPointerException e) {}
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.action_bar, menu);
      return super.onCreateOptionsMenu(menu);
   }

   class MainAdapter extends FragmentPagerAdapter {
      public MainAdapter(FragmentManager fm) {
         super(fm);
      }

      public Fragment getItem(int position) {
         switch(position) {
            case 0:
               return new SignFragment();

            case 1:
               return new HomeFragment();

            case 2:
               return new HistoryFragment();

            default:
               return new HomeFragment();
         }
      }

      @Override
      public CharSequence getPageTitle(int position) {
         return CONTENT[position % CONTENT.length].toUpperCase();
      }

      @Override
      public int getCount() {
         return CONTENT.length;
      }
   }

}
