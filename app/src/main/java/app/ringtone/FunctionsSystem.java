package app.ringtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import app.ringtone.functions.About.infoApp;
import app.pacoke.aplicacionringtone.R;
import app.ringtone.functions.gallery.GridViewGalleryActivity;
import app.ringtone.functions.makephoto.RetrievePhotoManually;
import binders.SampleData;
import cz.msebera.android.httpclient.Header;
import dtos.RingToneRestClient;
import factories.IntentFactory;
import settingsApp.SharedSettings;
import utilsApp.*;
import java.util.ArrayList;
import java.util.List;

public class FunctionsSystem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_example);
        setContentView(R.layout.activity_functions_system);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        SampleEnumListAdapter adapter = new SampleEnumListAdapter();
        //SampleEnumMapAdapter adapter = new SampleEnumMapAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SampleData> sampleData = getSampleData();
        adapter.setSample2Data(sampleData);
    }

    private List<SampleData> createListFunctions(){
        List<SampleData> dataSet = new ArrayList<>();
        return dataSet;
    }

    private List<SampleData> getSampleData() {
        List<SampleData> dataSet = new ArrayList<>();

        SampleData data = new SampleData();
        data.mTitle = getString(R.string.f_title_informacion);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_info), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_information);
        data.mListener = new listenerActivityInfo(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_settings);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_settings), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_settings);
        data.mListener = new listenerActivitySettings(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_gallery);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_gallery), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_gallery);
        data.mListener = new listenerActivityGallery(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_audio);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_audio), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_audio);
        data.mListener = new listenerActivityInfo(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_video);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_video), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_video);
        data.mListener = new listenerActivityInfo(this.findViewById(R.id.grid_layout_type2));

        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_photo);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_photo), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_photo);
        data.mListener = new listenerActivityMakePhoto(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);

        data = new SampleData();
        data.mTitle = getString(R.string.f_title_streaming);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_streaming), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_streaming);
        data.mListener = new listenerActivityInfo(this.findViewById(R.id.grid_layout_type2));
        dataSet.add(data);



        return dataSet;
    }

    private class listenerActivityInfo implements View.OnClickListener{
        View view;
        public listenerActivityInfo(View view){this.view=view;}
        @Override
        public void onClick(View v) {
            Intent intent = IntentFactory.createIntent(v.getContext(), infoApp.class);
            startActivity(intent);
        }
    }

    private class listenerActivityMakePhoto implements View.OnClickListener{
        View view;
        public listenerActivityMakePhoto(View view){this.view=view;}
        @Override
        public void onClick(View v) {
            final Context currentContext = v.getContext();
            RetrievePhotoManually makephoto= new RetrievePhotoManually(v.getContext());
            RingToneRestClient.get("cameramethods/UserImage",makephoto);
        }
    }

    private class listenerActivitySettings implements View.OnClickListener{
        View view;
        public listenerActivitySettings(View view){this.view=view;}
        @Override
        public void onClick(View v) {
            Intent intent = IntentFactory.createIntent(v.getContext(), setting_activity.class);
            intent.putExtra("comeFrom",FunctionsSystem.class.getName());
            startActivity(intent);
        }
    }

    private class listenerActivityGallery implements View.OnClickListener{
        View view;
        public listenerActivityGallery(View view){this.view=view;}
        @Override
        public void onClick(View v) {
            Intent intent = IntentFactory.createIntent(v.getContext(), GridViewGalleryActivity.class);
            intent.putExtra("comeFrom",FunctionsSystem.class.getName());
            startActivity(intent);
        }
    }

}
