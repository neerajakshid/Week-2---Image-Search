package com.neeraja.imagesearch.adapters;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.neeraja.imagesearch.R;
import com.neeraja.imagesearch.helpers.DimmensionsHelper;
import com.neeraja.imagesearch.models.ImageResultsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResultsAdapter extends ArrayAdapter<ImageResultsModel> {
    public ImageResultsAdapter(Context context, List<ImageResultsModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    private static class ViewHolder {
        DynamicHeightImageView ivImageItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResultsModel imageInfo = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_results, parent, false);;
                viewHolder.ivImageItem = (DynamicHeightImageView) convertView.findViewById(R.id.ivImageItem);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            DimmensionsHelper dimHelper = new DimmensionsHelper();
            int tbHeight = dimHelper.convertPixelsToDp(getContext(), imageInfo.pxTbHeight);
            int tbWidth = dimHelper.convertPixelsToDp(getContext(), imageInfo.pxTbWidth);
            viewHolder.ivImageItem.setImageResource(0);
            Picasso.with(getContext()).load(imageInfo.thumbUrl).resize(imageInfo.pxTbWidth,imageInfo.pxTbHeight).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivImageItem);
        return convertView;
        }
}
