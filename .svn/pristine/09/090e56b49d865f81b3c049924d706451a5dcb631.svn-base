package com.varel.photo_editor.activities;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.varel.photo_editor.fragments.FiltersEditorFragment;
import com.varel.photo_editor.abstract_libs.FrameWorkActivity;

public class FiltersEditorActivity extends FrameWorkActivity {

   @Override
   protected Fragment createFragment(Bundle savedInstanceState) {
      Bundle bundle = getIntent().getExtras();
      byte[] value = null;
      if(bundle != null) {
         value = bundle.getByteArray(FiltersEditorFragment.ID_SAVED_INSTANCE_STATE_BITMAP);
      }
      if(value != null) {
        return FiltersEditorFragment.newInstance(value);
      } else {
         return new FiltersEditorFragment();
      }
   }
}
