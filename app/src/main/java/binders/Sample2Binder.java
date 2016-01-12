package binders;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;
import android.location.GpsStatus;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import app.pacoke.aplicacionringtone.R;

/**
 * Created by yqritc on 2015/03/20.
 */
public class Sample2Binder extends DataBinder<Sample2Binder.ViewHolder> {

    private List<SampleData> mDataSet = new ArrayList<>();

    public Sample2Binder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_sample2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        SampleData data = mDataSet.get(position);
        holder.mTitleText.setText(data.mTitle);
        holder.mContent.setText(data.mContent);
        holder.mListener.setOnClickListener(data.mListener);
        Picasso.with(holder.mImageView.getContext())
                .load(data.mDrawableResId)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addAll(List<SampleData> dataSet) {
        mDataSet.addAll(dataSet);
        notifyBinderDataSetChanged();
    }

    public void clear() {
        mDataSet.clear();
        notifyBinderDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        ImageView mImageView;
        TextView mContent;
        RelativeLayout mListener;

        public ViewHolder(View view) {
            super(view);
            mTitleText = (TextView) view.findViewById(R.id.title_type2);
            mImageView = (ImageView) view.findViewById(R.id.image_type2);
            mContent = (TextView) view.findViewById(R.id.content_type2);
            mListener = (RelativeLayout) view.findViewById(R.id.grid_layout_type2);
            //mListener = (View.OnClickListener) view.findViewById(R.id.grid_layout_type2).;
        }
    }
}

