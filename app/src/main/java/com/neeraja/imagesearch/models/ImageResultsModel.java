package com.neeraja.imagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageResultsModel implements Parcelable {
    public String fullUrl;
    public String thumbUrl;
    public String title;
    public Integer pxTbHeight;
    public Integer pxTbWidth;
    public Integer imageWidth;
    public Integer imageHeight;
    public ImageResultsModel(JSONObject jsonObject){
        try {
            this.fullUrl = jsonObject.getString("url");
            this.thumbUrl = jsonObject.getString("tbUrl");
            this.title = jsonObject.getString("title");
            this.pxTbHeight= jsonObject.getInt("tbHeight");
            this.pxTbWidth= jsonObject.getInt("tbWidth");
            this.imageHeight= jsonObject.getInt("height");
            this.imageWidth= jsonObject.getInt("width");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ImageResultsModel>  parseJSON (JSONArray jsonArray){
        ArrayList<ImageResultsModel> results = new ArrayList<ImageResultsModel>();
        for (int i=0 ; i<jsonArray.length();i++){
            try{
                results.add(new ImageResultsModel(jsonArray.getJSONObject(i)));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullUrl);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.title);
        dest.writeValue(this.pxTbHeight);
        dest.writeValue(this.pxTbWidth);
        dest.writeValue(this.imageWidth);
        dest.writeValue(this.imageHeight);
    }

    private ImageResultsModel(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbUrl = in.readString();
        this.title = in.readString();
        this.pxTbHeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pxTbWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imageWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imageHeight = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<ImageResultsModel> CREATOR = new Creator<ImageResultsModel>() {
        public ImageResultsModel createFromParcel(Parcel source) {
            return new ImageResultsModel(source);
        }

        public ImageResultsModel[] newArray(int size) {
            return new ImageResultsModel[size];
        }
    };
}
