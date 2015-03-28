package com.varel.photo_editor.abstract_libs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import com.varel.photo_editor.R;

public abstract class FrameWorkActivity extends Activity {
   protected abstract Fragment createFragment(Bundle savedInstanceState);

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.main);
      FragmentManager fm = getFragmentManager();
      Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
      if (fragment == null) {
         fragment = createFragment(savedInstanceState);
         fm.beginTransaction()
                 .add(R.id.fragmentContainer, fragment)
                 .commit();
      }
   }

   @Override
   public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
      try {
         super.onSaveInstanceState(outState);
      } catch (NullPointerException e) {}
   }
}