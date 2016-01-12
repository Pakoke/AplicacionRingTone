package utilsApp;


import com.yqritc.recyclerviewmultipleviewtypesadapter.EnumListBindAdapter;

import java.util.List;

import binders.Sample2Binder;
import binders.SampleData;

/**
 * Created by yqritc on 2015/04/20.
 */
public class SampleEnumListAdapter extends EnumListBindAdapter<SampleEnumListAdapter.SampleViewType> {

    enum SampleViewType {
        //SAMPLE1,
        SAMPLE2
        //,SAMPLE3
    }

    public SampleEnumListAdapter() {
        addAllBinder(//new Sample1Binder(this),
                new Sample2Binder(this)
                //,new Sample3Binder(this)
        );
    }

    public void setSample2Data(List<SampleData> dataSet) {
        ((Sample2Binder) getDataBinder(SampleViewType.SAMPLE2)).addAll(dataSet);
    }
}
