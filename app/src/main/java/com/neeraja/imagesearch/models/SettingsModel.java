package com.neeraja.imagesearch.models;

import android.media.audiofx.BassBoost;
import android.util.Log;

/**
 * Created by Neeraja on 5/14/2015.
 */
public class SettingsModel {
    public String imageSize ;
    public String imageColor ;
    public String imageType;
    public String siteFilter;
    private String queryParams;


    private static SettingsModel settingsInstance = new SettingsModel();

    public String settingsQueryParam(){
        queryParams="";
        if(!(imageSize.equalsIgnoreCase("All")) && imageSize!=null){
            queryParams += "&imgsz="+imageSize;
        }
        if(!(imageColor.equalsIgnoreCase("All")) && imageColor!=null){
            queryParams+="&imgcolor="+imageColor;
        }
        if(!(imageType.equalsIgnoreCase("All")) && imageType!=null){
            queryParams+="&imgtype="+imageType;
        }
        if(siteFilter != null && !(siteFilter.equals(""))){
            queryParams+="&as_sitesearch="+siteFilter;
        }

       return queryParams;
    }

    public static SettingsModel sharedInstance() {
        return settingsInstance;
    }


 }
