package com.varel.photo_editor.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import com.varel.photo_editor.R;
import com.varel.photo_editor.abstract_libs.ImageFragment;
import com.varel.photo_editor.activities.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class FiltersEditorFragment extends ImageFragment implements View.OnClickListener, View.OnLongClickListener{

   public static final String ID_SAVED_INSTANCE_STATE_BITMAP = "com.varel.photo_editor.fragments.binmap";
   public static final String ID_SAVED_INSTANCE_STATE_BORDER = "com.varel.photo_editor.fragments.isBorder";
   public static final String ID_SAVED_INSTANCE_STATE_GAUSSIAN = "com.varel.photo_editor.fragments.isGaussian";
   public static final String ID_SAVED_INSTANCE_STATE_LIGHT = "com.varel.photo_editor.fragments.isLight";
   public static final String ID_SAVED_INSTANCE_STATE_ROTATE = "com.varel.photo_editor.fragments.isRotate";
   public static final String ID_SAVED_INSTANCE_STATE_FILTER = "com.varel.photo_editor.fragments.activeFilter";

   public static final int ID_MENU_GET_IMAGE = 100;
   public static final int ID_MENU_SAVE_IMAGE = 200;
   public static final int ID_MENU_ROTATE = 300;
   public static final int ID_MENU_BUTTON_CAMERA = 101;
   public static final int ID_MENU_BUTTON_GALLERY = 102;
   public static final int ID_MENU_BUTTON_SAVE = 201;
   public static final int ID_MENU_BUTTON_ROTATE_NONE = 301;
   public static final int ID_MENU_BUTTON_ROTATE_90 = 302;
   public static final int ID_MENU_BUTTON_ROTATE_180 = 303;
   public static final int ID_MENU_BUTTON_ROTATE_270 = 304;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      actionBar = getActivity().getActionBar();
      actionBar.setHomeButtonEnabled(true);
      actionBar.setTitle(R.string.activity_title_editor);
      setHasOptionsMenu(true);

      File wallpaperDirectory = new File(folderApplication);
      if(!wallpaperDirectory.exists()) { wallpaperDirectory.mkdirs(); }

      imagesEffectSave = new ArrayList<Bitmap>();
      imagesEffectSaveIndex = new ArrayList<Integer>();

      Bundle bundle = getArguments();
      if(bundle != null) {
         byte [] img_tmp = bundle.getByteArray(ID_SAVED_INSTANCE_STATE_BITMAP);
         imageBitmap_original = (Bitmap) BitmapFactory.decodeByteArray(img_tmp , 0, img_tmp.length);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.filters_fragment, parent, false);
      setVariables(v);
      return v;
   }

   @Override
   public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      imageBitmap_original.compress(Bitmap.CompressFormat.PNG, 100, stream);
      outState.putByteArray(ID_SAVED_INSTANCE_STATE_BITMAP, stream.toByteArray());
      outState.putBoolean(ID_SAVED_INSTANCE_STATE_BORDER, isBorder);
      outState.putBoolean(ID_SAVED_INSTANCE_STATE_LIGHT, isLight);
      outState.putBoolean(ID_SAVED_INSTANCE_STATE_GAUSSIAN, isGaussian);
      outState.putFloat(ID_SAVED_INSTANCE_STATE_ROTATE, isRotate);
      outState.putInt(ID_SAVED_INSTANCE_STATE_FILTER, isActiveFilters);
   }

   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      if (savedInstanceState != null) {
         byte [] img_tmp = savedInstanceState.getByteArray(ID_SAVED_INSTANCE_STATE_BITMAP);
         boolean border = savedInstanceState.getBoolean(ID_SAVED_INSTANCE_STATE_BORDER);
         boolean light = savedInstanceState.getBoolean(ID_SAVED_INSTANCE_STATE_LIGHT);
         boolean gaussian = savedInstanceState.getBoolean(ID_SAVED_INSTANCE_STATE_GAUSSIAN);
         float rotate = savedInstanceState.getFloat(ID_SAVED_INSTANCE_STATE_ROTATE);
         int filters = savedInstanceState.getInt(ID_SAVED_INSTANCE_STATE_FILTER);

         try {
            isBorder = border ? border : isBorder;
            isLight = light ? light : isLight;
            isGaussian = gaussian ? gaussian : isGaussian;
            isRotate = rotate > 0 ? rotate : isRotate;
            isRotate = rotate > 0 ? rotate : isRotate;
            isActiveFilters = filters;
            if(isActiveFilters != R.id.button_effect_real) {
               onClick(getActivity().findViewById(isActiveFilters));
            } else {
               updateFiltersButtons(isActiveFilters, R.id.button_effect_real);
            }
            if(img_tmp != null) {
               imageBitmap_original = (Bitmap) BitmapFactory.decodeByteArray(img_tmp , 0, img_tmp.length);
               imageBitmap_resize_original = imageBitmap_resize_effect = reSizeImage(imageBitmap_original);
            }
         } catch (Error ignored){}
         updateActiveTopNav();
         updateImageToView(imageBitmap_resize_effect);
      }
      setImage();
   }

   public void onClick(View v) {
      final int ID = v.getId();
      switch (ID) {
         case R.id.image_view:
            getActivity().openContextMenu(imageView);
            break;

         case R.id.button_effect_real:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageBitmap_resize_original;
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_tint:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyTintEffect(imageBitmap_resize_original, 100);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();

            break;

         case R.id.button_effect_sheding_yellow:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyShadingFilter(imageBitmap_resize_original, Color.YELLOW);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_grayscale:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyGreyscaleEffect(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_sheding_cyan:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyShadingFilter(imageBitmap_resize_original, Color.CYAN);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_sepia_green:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applySepiaToningEffect(imageBitmap_resize_original, 10, 0.88, 2.45, 1.43);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_sepia_blue:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applySepiaToningEffect(imageBitmap_resize_original, 10, 1.2, 0.87, 2.1);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_sepia:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applySepiaToningEffect(imageBitmap_resize_original, 10, 1.5, 0.6, 0.12);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_round_corner:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyRoundCornerEffect(imageBitmap_resize_original, 20);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_invert:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyInvertEffect(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_flea:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyFleaEffect(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_engrave:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyEngraveEffect(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_emboss:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyEmbossEffect(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_contrast:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyContrastEffect(imageBitmap_resize_original, 70);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_color_green:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyColorFilterEffect(imageBitmap_resize_original, 0, 255, 0);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_brightness:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyBrightnessEffect(imageBitmap_resize_original, 80);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();
            break;

         case R.id.button_effect_black:
            updateFiltersButtons(isActiveFilters, ID);
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  try {
                     imageBitmap_resize_effect = imagesEffectSave.get(imagesEffectSaveIndex.indexOf(ID));
                  } catch ( IndexOutOfBoundsException e ) {
                     imageBitmap_resize_effect = imageFilters.applyBlackFilter(imageBitmap_resize_original);
                     imagesEffectSaveIndex.add(ID);
                     imagesEffectSave.add(imageBitmap_resize_effect);
                  } finally {
                     updateImageToView(imageBitmap_resize_effect);
                  }
               }
            }.execute();

            break;


         case R.id.button_top_effect_border:
            isBorder = !isBorder;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case R.id.button_top_effect_gaussian:
            isGaussian = !isGaussian;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case R.id.button_top_effect_light:
            isLight = !isLight;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case R.id.button_top_effect_rotate:
            getActivity().openContextMenu(getView().findViewById(R.id.layout_top_effect_rotate));
            break;

         case R.id.button_top_action_done:
            saveImage();
            break;

         case R.id.button_top_action_add_photo:
            getActivity().openContextMenu(imageView);
            break;
      }
   }

   public boolean onLongClick(View v) {
      switch (v.getId()) {

         case R.id.image_view:
            getActivity().openContextMenu(getView().findViewById(R.id.layout_buttons_effects));
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

         case R.id.layout_buttons_effects :
            menu.add(ID_MENU_SAVE_IMAGE, ID_MENU_BUTTON_SAVE, 1, "Сохранить");
            break;

         case R.id.layout_top_effect_rotate :
            menu.add(ID_MENU_ROTATE, ID_MENU_BUTTON_ROTATE_NONE, 1, "Не поворачивать").setIcon(R.drawable.ic_action_screen_rotation_white);
            menu.add(ID_MENU_ROTATE, ID_MENU_BUTTON_ROTATE_90, 2, "На право").setIcon(R.drawable.ic_action_rotate_right);
            menu.add(ID_MENU_ROTATE, ID_MENU_BUTTON_ROTATE_270, 3, "На лево").setIcon(R.drawable.ic_action_rotate_right);
            menu.add(ID_MENU_ROTATE, ID_MENU_BUTTON_ROTATE_180, 4, "Перевернуть").setIcon(R.drawable.ic_action_rotate_bottom);
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

         case ID_MENU_BUTTON_SAVE:
            saveImage();
            break;

         case ID_MENU_BUTTON_ROTATE_NONE:
            isRotate = 0;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case ID_MENU_BUTTON_ROTATE_90:
            isRotate = 90;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case ID_MENU_BUTTON_ROTATE_180:
            isRotate = 180;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
            break;

         case ID_MENU_BUTTON_ROTATE_270:
            isRotate = 270;
            updateActiveTopNav();
            new ApplyFiltersTasks() {
               protected void doingInBackground() {
                  updateImageToView(imageBitmap_resize_effect);
               }
            }.execute();
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

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
      super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
      if(requestCode == 0 || requestCode == 1) {
         updateFiltersButtons(0, R.id.button_effect_real);
      }
   }

   private void updateActiveTopNav() {
      Resources r = getResources();

      getView().findViewById(R.id.layout_top_effect_border).setBackgroundColor(
              isBorder ?
                      r.getColor(R.color.background_top_button_active) :
                      r.getColor(R.color.background_top_button)
      );
      ((ImageView) getView().findViewById(R.id.button_top_effect_border)).setImageBitmap(
              isBorder ?
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_border_active) :
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_border)
      );

      getView().findViewById(R.id.layout_top_effect_gaussian).setBackgroundColor(
              isGaussian ?
                      r.getColor(R.color.background_top_button_active) :
                      r.getColor(R.color.background_top_button)
      );
      ((ImageView) getView().findViewById(R.id.button_top_effect_gaussian)).setImageBitmap(
              isGaussian ?
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_gaussian_action) :
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_gaussian)
      );

      getView().findViewById(R.id.layout_top_effect_light).setBackgroundColor(
              isLight ?
                      r.getColor(R.color.background_top_button_active) :
                      r.getColor(R.color.background_top_button)
      );
      ((ImageView) getView().findViewById(R.id.button_top_effect_light)).setImageBitmap(
              isLight ?
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_brightness_high) :
                      BitmapFactory.decodeResource(r, R.drawable.ic_action_bightness_low)
      );

      getView().findViewById(R.id.layout_top_effect_rotate).setBackgroundColor(
              isRotate != 0 ?
                      r.getColor(R.color.background_top_button_active) :
                      r.getColor(R.color.background_top_button)
      );
   }

   private void updateFiltersButtons(int oldActive, int newActive) {
      Resources r = getResources();
      isActiveFilters = newActive;
      try {
         if(oldActive != 0) {
            getView().findViewById(oldActive).setBackgroundColor(
                    r.getColor(R.color.background_effects_buttons)
            );
         }

      } catch (Error e) {
         Log.e("ERROR:", e.toString());
      }

      try {
         if(newActive != 0) {
            getView().findViewById(newActive).setBackgroundColor(
                    r.getColor(R.color.background_effects_buttons_active)
            );
         }
      } catch (Error e) {
         Log.e("ERROR:", e.toString());
      }
   }

   public static FiltersEditorFragment newInstance(byte [] image) {
      Bundle args = new Bundle();
      if(image != null) {
         args.putByteArray(ID_SAVED_INSTANCE_STATE_BITMAP, image);
      }
      FiltersEditorFragment fragment = new FiltersEditorFragment();
      fragment.setArguments(args);
      return fragment;
   }

   private void setVariables(View view) {

      imageView = (ImageView) view.findViewById(R.id.image_view);
      imageView.setOnClickListener(this);
      imageView.setOnLongClickListener(this);

      view.findViewById(R.id.button_effect_real).setOnClickListener(this);
      view.findViewById(R.id.button_effect_tint).setOnClickListener(this);
      view.findViewById(R.id.button_effect_sheding_yellow).setOnClickListener(this);
      view.findViewById(R.id.button_effect_grayscale).setOnClickListener(this);
      view.findViewById(R.id.button_effect_sheding_cyan).setOnClickListener(this);
      view.findViewById(R.id.button_effect_sepia_green).setOnClickListener(this);
      view.findViewById(R.id.button_effect_sepia_blue).setOnClickListener(this);
      view.findViewById(R.id.button_effect_sepia).setOnClickListener(this);
      view.findViewById(R.id.button_effect_round_corner).setOnClickListener(this);
      view.findViewById(R.id.button_effect_invert).setOnClickListener(this);
      view.findViewById(R.id.button_effect_flea).setOnClickListener(this);
      view.findViewById(R.id.button_effect_engrave).setOnClickListener(this);
      view.findViewById(R.id.button_effect_emboss).setOnClickListener(this);
      view.findViewById(R.id.button_effect_contrast).setOnClickListener(this);
      view.findViewById(R.id.button_effect_color_green).setOnClickListener(this);
      view.findViewById(R.id.button_effect_brightness).setOnClickListener(this);
      view.findViewById(R.id.button_effect_black).setOnClickListener(this);

      view.findViewById(R.id.button_top_effect_border).setOnClickListener(this);
      view.findViewById(R.id.button_top_effect_gaussian).setOnClickListener(this);
      view.findViewById(R.id.button_top_effect_light).setOnClickListener(this);
      view.findViewById(R.id.button_top_effect_rotate).setOnClickListener(this);
      view.findViewById(R.id.button_top_action_done).setOnClickListener(this);
      view.findViewById(R.id.button_top_action_add_photo).setOnClickListener(this);

      view.findViewById(R.id.image_view).setOnCreateContextMenuListener(this);
      view.findViewById(R.id.layout_buttons_effects).setOnCreateContextMenuListener(this);
      view.findViewById(R.id.layout_top_effect_rotate).setOnCreateContextMenuListener(this);
   }

}
