package com.varel.photo_editor.abstract_libs;

import android.os.AsyncTask;

public abstract class AsyncTasks extends AsyncTask<Void, Void, String> {
   protected abstract void doingInBackground();
   protected abstract void doingAfter();
   protected abstract void doingBefore();

   public AsyncTasks() {}

   @Override
   protected void onPreExecute() {
      doingBefore();
   }

   @Override
   protected String doInBackground(Void... params) {
      doingInBackground();
      return "";
   }

   @Override
   protected void onPostExecute(String result) {
      doingAfter();
   }
}