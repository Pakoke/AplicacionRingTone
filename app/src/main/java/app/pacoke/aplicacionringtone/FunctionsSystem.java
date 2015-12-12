package app.pacoke.aplicacionringtone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import Binders.SampleData;
import UtilsApp.*;
import java.util.ArrayList;
import java.util.List;

public class FunctionsSystem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_example);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        SampleEnumMapAdapter adapter = new SampleEnumMapAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SampleData> sampleData = getSampleData();
        adapter.setSample2Data(sampleData);
    }

    private List<SampleData> getSampleData() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            dataSet.add(data);
        }

        return dataSet;
    }

}
