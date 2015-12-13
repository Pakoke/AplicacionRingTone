package app.ringtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import app.ringtone.functions.infoApp;
import app.pacoke.aplicacionringtone.R;
import binders.SampleData;
import factories.IntentFactory;
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
        data.mTitle = getString(R.string.f_informacion_app);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_info), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_information);
        dataSet.add(data);
        data = new SampleData();
        data.mTitle = getString(R.string.f_informacion_app);
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_info), "drawable", getPackageName());
        data.mContent = getString(R.string.f_content_information);
        dataSet.add(data);


        return dataSet;
    }

    public void startActivityInfo(RecyclerView.ViewHolder view){
        Intent intent = IntentFactory.createIntent(this, infoApp.class);
        startActivity(intent);
    }

}
