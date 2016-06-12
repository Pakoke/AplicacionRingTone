package binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.util.ArrayList;
import java.util.List;

import app.pacoke.aplicacionringtone.R;
import dtos.SampleFileData;

/**
 * Created by Javi on 14/06/2016.
 */
public class SampleFileBinder extends DataBinder<SampleFileBinder.ViewHolder> {

    private List<SampleFileData> mDataSet = new ArrayList<>();

    public SampleFileBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_sample_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        SampleFileData data = mDataSet.get(position);
        holder.mTitleText.setText(data.mTitle);
        holder.mListener.setOnClickListener(data.mListener);
        holder.mListener.setOnLongClickListener(data.mLongListener);
        Picasso.with(holder.mImageView.getContext())
                .load(data.mDrawableResId)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addAll(List<SampleFileData> dataSet) {
        mDataSet.addAll(dataSet);
        notifyBinderDataSetChanged();
    }

    public void clear() {
        mDataSet.clear();
        notifyBinderDataSetChanged();
    }

    public void add(SampleFileData data) {
        mDataSet.add(0,data);
        notifyBinderItemInserted(0);
        notifyBinderItemRangeChanged(0,mDataSet.size());
        //notifyBinderItemRangeInserted(0,mDataSet.size());
        notifyBinderDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        ImageView mImageView;
        GridLayout mListener;

        public ViewHolder(View view) {
            super(view);
            mTitleText = (TextView) view.findViewById(R.id.text_view_filename);
            mImageView = (ImageView) view.findViewById(R.id.image_view_music);
            mListener = (GridLayout) view.findViewById(R.id.grid_layout_music);
        }
    }

}
