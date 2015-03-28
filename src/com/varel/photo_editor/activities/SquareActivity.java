package com.varel.photo_editor.activities;

import android.app.Fragment;
import android.os.Bundle;
import com.varel.photo_editor.fragments.SquareFragment;
import com.varel.photo_editor.abstract_libs.FrameWorkActivity;

public class SquareActivity extends FrameWorkActivity {
   @Override
   protected Fragment createFragment(Bundle savedInstanceState) {
      return new SquareFragment();
   }
}
