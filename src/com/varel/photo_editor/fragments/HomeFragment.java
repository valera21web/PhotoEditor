package com.varel.photo_editor.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.ImageButton;
import com.varel.photo_editor.R;
import com.varel.photo_editor.abstract_libs.ImageFragment;
import com.varel.photo_editor.activities.FiltersEditorActivity;
import com.varel.photo_editor.activities.SquareActivity;
import com.varel.photo_editor.libs.ActionBarLib;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeFragment extends ImageFragment {

   private ImageButton ButtonCreateInstagram;
   private ImageButton ButtonCreateFilters;
   private ImageButton ButtonOpenCamera;
   private ImageButton ButtonOpenGallery;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      actionBar = getActivity().getActionBar();
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.home_fragment, parent, false);
      setVariables(v);
//      actionBar = new ActionBarLib(parent) {
//         @Override
//         public void onActionBarClick(View v) {}
//      };
//      actionBar.initActionBar();
      return v;
   }

   private void setVariables(View view) {
      ButtonCreateFilters = (ImageButton) view.findViewById(R.id.home_button_filters);
      ButtonCreateFilters.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Intent i = new Intent(getActivity(), SquareActivity.class);
            startActivity(i);
         }
      });

      ButtonCreateInstagram = (ImageButton) view.findViewById(R.id.home_button_instagram);
      ButtonCreateInstagram.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Intent i = new Intent(getActivity(), FiltersEditorActivity.class);
            startActivity(i);
         }
      });
      ButtonOpenCamera = (ImageButton) view.findViewById(R.id.home_button_camera);
      ButtonOpenCamera.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
         }
      });

      ButtonOpenGallery = (ImageButton) view.findViewById(R.id.home_button_gallery);
      ButtonOpenGallery.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
         }
      });
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
      imagesEffectSave = new ArrayList<Bitmap>();
      imagesEffectSaveIndex = new ArrayList<Integer>();
      switch(requestCode) {
         case 0:
            if (resultCode == Activity.RESULT_OK) {
               Bundle extras = imageReturnedIntent.getExtras();
               imageBitmap_original = (Bitmap) extras.get("data");
            }

            break;

         case 1:
            if(resultCode == Activity.RESULT_OK){
               Uri selectedImage = imageReturnedIntent.getData();
               String[] filePathColumn = {MediaStore.Images.Media.DATA};
               Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
               cursor.moveToFirst();
               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
               String filePath = cursor.getString(columnIndex);
               cursor.close();

               imageBitmap_resize_effect = BitmapFactory.decodeFile(filePath);
            }
            break;
      }
      if(resultCode == Activity.RESULT_OK && (requestCode == 0 || requestCode == 1)) {
         Intent intent = new Intent(getActivity(), FiltersEditorActivity.class);

         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         imageBitmap_resize_effect.compress(Bitmap.CompressFormat.PNG, 100, stream);

         intent.putExtra(FiltersEditorFragment.ID_SAVED_INSTANCE_STATE_BITMAP, stream.toByteArray());  // set byte[]
         startActivity(intent);
      }
   }

   public static HomeFragment newInstance() {
      Bundle args = new Bundle();
      //args.putSerializable(EXTRA_CRIME_ID, crimeId);
      HomeFragment fragment = new HomeFragment();
      fragment.setArguments(args);
      return fragment;
   }
}
