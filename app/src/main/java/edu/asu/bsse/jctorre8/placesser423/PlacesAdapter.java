package edu.asu.bsse.jctorre8.placesser423;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.asu.bsse.jctorre8.placesser423.data.PlacesContract;

/**
 * Copyright 2018 Jean Torres,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Class to perform various earth surface calculations. Given lat/lon points
 * this class can perform distance and bearing calculations - Great Circle,
 * and Rhumb-line.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Jean Torres jctorre8@asu.edu
 * @version April, 2018
 **/
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>{

   private Cursor mCursor;
   private Context mContext;
   final private ListItemClickListener mOnClickListener;

   /**
    * The interface that receives onClick messages.
    */
   public interface ListItemClickListener {
      void onListItemClick(String clickedItemName);
   }

   public PlacesAdapter(Context context, Cursor cursor) {
      this.mContext = context;
      this.mOnClickListener = (ListItemClickListener) context;
      this.mCursor = cursor;
   }

   @Override
   public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
       LayoutInflater inflater = LayoutInflater.from(mContext);
       View view = inflater.inflate(R.layout.place_list_item, viewGroup, false);
       return new PlaceViewHolder(view);
   }

   @Override
   public void onBindViewHolder(PlaceViewHolder holder, int position) {
      // Move the mCursor to the position of the item to be displayed
      if (!mCursor.moveToPosition(position))
         return; // bail if returned null

      // Update the view holder with the information needed to display
      String name = mCursor.getString(mCursor.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME));
      long id = mCursor.getLong(mCursor.getColumnIndex(PlacesContract.PlacesEntry._ID));

       // Display the place name
       holder.listItemPlaceView.setText(name);
       holder.viewHolderIndex.setText(String.valueOf(position));

      holder.itemView.setTag(id);
   }

   @Override
   public int getItemCount() {

      return this.mCursor.getCount();
   }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

   class PlaceViewHolder extends RecyclerView.ViewHolder
           implements View.OnClickListener {

      TextView listItemPlaceView;
      TextView viewHolderIndex;

      public PlaceViewHolder(View itemView) {
         super(itemView);
          viewHolderIndex= (TextView) itemView.findViewById(R.id.place_item_number);
          listItemPlaceView = (TextView) itemView.findViewById(R.id.place_view_holder_instance);
          itemView.setOnClickListener(this);
      }

      @Override
      public void onClick(View v) {
          String clickedIndex = listItemPlaceView.getText().toString();
         mOnClickListener.onListItemClick(clickedIndex);
      }
   }

}
