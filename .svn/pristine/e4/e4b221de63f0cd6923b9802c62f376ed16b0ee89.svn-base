package com.varel.photo_editor.libs;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.varel.photo_editor.R;

public abstract class ActionBarLib implements View.OnClickListener {

   private View view;
   private LinearLayout LeftLayout;
   private LinearLayout RightLayout;
   private LinearLayout ButtonBack;
   private ImageButton ButtonLogo;
   private ImageButton ButtonSetting;
   private ImageButton ButtonEdit;
   private TextView TextTitle;


   public final static int ACTION_BAR_LAYOUT_LEFT = R.id.action_bar_layout_left;
   public final static int ACTION_BAR_LAYOUT_RIGHT = R.id.action_bar_layout_right;
   public final static int ACTION_BAR_ITEM_BACK = R.id.action_bar_layout_back;
   public final static int ACTION_BAR_IMAGE_BACK = R.id.action_bar_button_back;
   public final static int ACTION_BAR_ITEM_LOGO = R.id.action_bar_logo;
   public final static int ACTION_BAR_ITEM_TITLE = R.id.action_bar_title;
   public final static int ACTION_BAR_ITEM_SETTING = R.id.action_bar_setting;
   public final static int ACTION_BAR_ITEM_EDIT = R.id.action_bar_edit;

   private boolean hasParent = false;

   public abstract void onActionBarClick(View v);

   public ActionBarLib(View pView) {
      view = pView;
   }

   public ActionBarLib(ViewGroup parent) {
      view = parent.getRootView();
   }

   public void initActionBar() {
      LeftLayout = (LinearLayout) view.findViewById(ACTION_BAR_LAYOUT_LEFT);
      RightLayout = (LinearLayout) view.findViewById(ACTION_BAR_LAYOUT_RIGHT);
      ButtonBack = (LinearLayout) view.findViewById(ACTION_BAR_ITEM_BACK);

      ButtonLogo = (ImageButton) view.findViewById(ACTION_BAR_ITEM_LOGO);
      TextTitle = (TextView) view.findViewById(ACTION_BAR_ITEM_TITLE);
      ButtonSetting = (ImageButton) view.findViewById(ACTION_BAR_ITEM_SETTING);
      ButtonEdit = (ImageButton) view.findViewById(ACTION_BAR_ITEM_EDIT);

      int[] allItems = {
              ACTION_BAR_ITEM_SETTING,
              ACTION_BAR_ITEM_EDIT,
      };
      setInvisibleItem(allItems);


      LeftLayout.setOnClickListener(this);
      ButtonBack.setOnClickListener(this);
      view.findViewById(ACTION_BAR_IMAGE_BACK).setOnClickListener(this);
      ButtonLogo.setOnClickListener(this);
      TextTitle.setOnClickListener(this);
      ButtonSetting.setOnClickListener(this);
      ButtonEdit.setOnClickListener(this);

      setActionBack();
   }

   public void setVisibleItem(int idItem) {
      changeVisibleItem(idItem, true);
   }

   public void setVisibleItem(int[] idItems) {
      for(int idItem : idItems) {
         changeVisibleItem(idItem, true);
      }
   }

   public void setInvisibleItem(int idItem) {
      changeVisibleItem(idItem, false);
   }

   public void setInvisibleItem(int[] idItems) {
      for(int idItem : idItems) {
         changeVisibleItem(idItem, false);
      }
   }

   public void setTitle(String title) {
      TextTitle.setText(title);
   }

   private void changeVisibleItem(int idItem, boolean pVisible) {
      int visible = pVisible ? View.VISIBLE : View.GONE;
      switch (idItem) {
         case ACTION_BAR_LAYOUT_LEFT:
            LeftLayout.setVisibility(visible);
            break;

         case ACTION_BAR_LAYOUT_RIGHT:
            RightLayout.setVisibility(visible);
            break;

         case ACTION_BAR_ITEM_BACK:
            ButtonBack.setVisibility(visible);
            break;

         case ACTION_BAR_ITEM_LOGO:
            ButtonLogo.setVisibility(visible);
            break;

         case ACTION_BAR_ITEM_TITLE:
            TextTitle.setVisibility(visible);
            break;

         case ACTION_BAR_ITEM_SETTING:
            ButtonSetting.setVisibility(visible);
            break;

         case ACTION_BAR_ITEM_EDIT:
            ButtonEdit.setVisibility(visible);
            break;
      }
   }

   public void hasParentActivity(boolean has) {
      hasParent = has;
      setActionBack();
   }

   private void setActionBack() {
      view.findViewById(ACTION_BAR_IMAGE_BACK).setVisibility(hasParent ? View.VISIBLE : View.GONE);
   }

   public void onClick(View v) {
      Log.d("ACTION_BAR", "CLICK: " + v.getId() + ";");
      switch(v.getId()) {
         case ACTION_BAR_ITEM_BACK:
         case ACTION_BAR_ITEM_LOGO:
         case ACTION_BAR_IMAGE_BACK:
            Log.d("ACTION_BAR", "CLICK: ACTION_BAR_ITEM_BACK;");
            break;
      }
      onActionBarClick(v);
   }
}
