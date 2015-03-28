package com.varel.photo_editor.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import com.varel.photo_editor.R;
import com.varel.photo_editor.abstract_libs.ImageFragment;
import com.varel.photo_editor.activities.MainActivity;
import com.varel.photo_editor.libs.ActionBarLib;
import com.varel.photo_editor.libs.ImageFilters;

import java.util.ArrayList;
import java.util.List;

public class SquareFragment extends ImageFragment implements View.OnClickListener, View.OnLongClickListener {

   public static final int ID_MENU_GET_IMAGE = 100;
   public static final int ID_MENU_BUTTON_CAMERA = 101;
   public static final int ID_MENU_BUTTON_GALLERY = 102;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      TYPE_RESIZE_IMAGE = RESIZE_IMAGE_SQUARE;
      actionBar = getActivity().getActionBar();
      actionBar.setHomeButtonEnabled(true);
      actionBar.setTitle(R.string.activity_title_instagram);
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.square_fragment, parent, false);
      setVariables(v);
//      actionBar = new ActionBarLib(v) {
//         @Override
//         public void onActionBarClick(View v) {
//            switch (v.getId()) {
//               case ACTION_BAR_ITEM_BACK:
//                  Log.d("ACTION_BAR", "ACTION_BAR_ITEM_BACK");
//                  break;
//            }
//         }
//      };
//      actionBar.hasParentActivity(true);
//      actionBar.initActionBar();
//      int[] viewsForVisible = {
//              ActionBarLib.ACTION_BAR_ITEM_EDIT
//      };
//      actionBar.setVisibleItem(viewsForVisible);
//      actionBar.setTitle(getString(R.string.activity_title_instagram));
      return v;
   }

   public void onClick(View v) {
      final int ID = v.getId();
      switch (ID) {
         case R.id.image_view:
            getActivity().openContextMenu(imageView);
            break;
      }
   }

   public boolean onLongClick(View v) {
      switch (v.getId()) {

         case R.id.image_view:

            break;

      }
      return true;
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
      switch (v.getId()) {
         case R.id.image_view :
            // .setIcon(R.drawable.menu_quit_icon)
            menu.add(ID_MENU_GET_IMAGE, ID_MENU_BUTTON_CAMERA, 1, "Сделать фото");
            menu.add(ID_MENU_GET_IMAGE, ID_MENU_BUTTON_GALLERY, 2, "Открыть с галерея");
            break;

      }
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case ID_MENU_BUTTON_CAMERA:
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
            break;

         case ID_MENU_BUTTON_GALLERY:
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);//one can be replced with any action code
            break;

         default:
            return super.onContextItemSelected(item);
      }

      return true;
   }

   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      getActivity().getMenuInflater().inflate(R.menu.action_bar, menu);
      super.onCreateOptionsMenu(menu, inflater);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            Intent upIntent = new Intent(getActivity(), MainActivity.class);
            if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
               TaskStackBuilder.from(getActivity())
                       .addNextIntent(upIntent)
                       .startActivities();
            } else {
               NavUtils.navigateUpTo(getActivity(), upIntent);
            }
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }


   public static SquareFragment newInstance() {
      Bundle args = new Bundle();
      //args.putSerializable(EXTRA_CRIME_ID, crimeId);
      SquareFragment fragment = new SquareFragment();
      fragment.setArguments(args);
      return fragment;
   }

   private void setVariables(View view) {
      imageView = (ImageView) view.findViewById(R.id.image_view);
      imageView.setOnLongClickListener(this);
      imageView.setOnClickListener(this);
      imageView.setOnCreateContextMenuListener(this);
   }

}
