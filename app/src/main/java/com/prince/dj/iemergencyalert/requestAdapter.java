/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prince.dj.iemergencyalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/***
 * The adapter class for the RecyclerView, contains the sports data
 */
class requestAdapter extends RecyclerView.Adapter<requestAdapter.ChildViewHolder>  {

    //Member variables
    private GradientDrawable mGradientDrawable;
    private ArrayList<Database> mSportsData;
    private Context mContext;

    /**
     * Constructor that passes in the sports data and the context
     * @param sportsData ArrayList containing the sports data
     * @param context Context of the application
     */
    requestAdapter(Context context, ArrayList<Database> sportsData) {
        this.mSportsData = sportsData;
        this.mContext = context;

        //Prepare gray placeholder
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(Color.GRAY);

        //Make the placeholder same size as the images
        Drawable drawable = ContextCompat.getDrawable
                (mContext,R.drawable.user);
        if(drawable != null) {
            mGradientDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }


    /**
     * Required method for creating the viewholder objects.
     * @param parent The ViewGroup into which the new View is added after it is
     *               bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create ChildViewHolder.
     */
    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(mContext, LayoutInflater.from(mContext).
                inflate(R.layout.reports_template, parent, false), mGradientDrawable);
    }

    /**
     * Required method that binds the data to the viewholder.
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {

        //Get the current sport
        Database currentChild = mSportsData.get(position);

        //Bind the data to the views
        holder.bindTo(currentChild);

    }


    /**
     * Required method for determining the size of the data set.
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mSportsData.size();
    }


    /**
     * ChildViewHolder class that represents each row of data in the RecyclerView
     */
    static class ChildViewHolder extends RecyclerView.ViewHolder {

        private final SharedPreferences sharedpreferences;
        private DateFormat df;
        private Date date;        //Member Variables for the holder data
        private TextView incident,dateOfIncident,location,sender,department;
        private ImageView mSportsImage;
        private Context mContext;
        private Database mCurrentChild;
        private GradientDrawable mGradientDrawable;
        private DatabaseReference mref;
        private FirebaseDatabase mfirebaseDatabase;
        private FirebaseUser fireuser;
        private FirebaseAuth auth;
        private String userid;
        private ImageView img;
        private LinearLayout button4;


        /**
         * Constructor for the ChildViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        ChildViewHolder(final Context context, View itemView, GradientDrawable gradientDrawable) {
            super(itemView);

            //Initialize the views

            incident = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.price);
            dateOfIncident = itemView.findViewById(R.id.location);
            sender = itemView.findViewById(R.id.desc);


            mContext = context;
            mGradientDrawable = gradientDrawable;


            sharedpreferences = mContext.getSharedPreferences("userid", Context.MODE_PRIVATE);
            userid=sharedpreferences.getString("id", "");

            mfirebaseDatabase = FirebaseDatabase.getInstance();
            mref = mfirebaseDatabase.getReference();



            df = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            date = new Date();

        }

        void bindTo(Database currentChild){
            //Populate the textviews with data




            img = itemView.findViewById(R.id.img);
            incident = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.price);
            dateOfIncident = itemView.findViewById(R.id.location);
            sender = itemView.findViewById(R.id.desc);


//Glide.with(mContext).load(Uri.fromFile(new File(currentChild.get()))).into(img);


            incident.setText(currentChild.getIncident());
            location.setText((currentChild.getLocation()));
            dateOfIncident.setText((currentChild.getDate()));
           sender.setText((currentChild.getReportedBy()));


            //Get the current sport
            mCurrentChild = currentChild;



            //Load the images into the ImageView using the Glide library
//            Glide.with(mContext).load(currentChild.
//                    getImageResource()).placeholder(mGradientDrawable).into(mSportsImage);
        }
        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
            private String TAG = "DownloadImage";
            private Bitmap downloadImageBitmap(String sUrl) {
                Bitmap bitmap = null;
                try {
                    InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                    bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                    inputStream.close();
                } catch (Exception e) {
                    Log.d(TAG, "Exception 1, Something went wrong!");
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                return downloadImageBitmap(params[0]);
            }

            protected void onPostExecute(Bitmap result) {
                saveImage(mContext, result, userid + ".jpg");
            }
        }
        public void saveImage(Context context, Bitmap b, String imageName) {
            FileOutputStream foStream;
            try {
                foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
                b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
                foStream.close();
            } catch (Exception e) {
                Log.d("saveImage", "Exception 2, Something went wrong!");
                e.printStackTrace();
            }
        }

    }
}
