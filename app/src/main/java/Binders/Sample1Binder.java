package Binders;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.pacoke.aplicacionringtone.R;

/**
 * Created by yqritc on 2015/03/20.
 */
public class Sample1Binder extends DataBinder<Sample1Binder.ViewHolder> {

    public Sample1Binder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_sample1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.mImageView.setImageResource(R.drawable.bird);
        Picasso.with(holder.mImageView.getContext())
                .load(R.drawable.bird)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        ImageView mImageView;
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
            mTitleText = (TextView) view.findViewById(R.id.title_type1);
            mImageView = (ImageView) view.findViewById(R.id.image_type1);
            mContent = (TextView) view.findViewById(R.id.content_type1);
        }
    }
}
