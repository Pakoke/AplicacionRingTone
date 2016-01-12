package UtilsApp;

import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter;
import Binders.*;
import java.util.List;

/**
 * Created by yqritc on 2015/03/20.
 */
public class SampleListAdapter extends ListBindAdapter {

    public SampleListAdapter() {
        addAllBinder(new Sample1Binder(this),
                new Sample2Binder(this),
                new Sample3Binder(this));
    }

    public void setSample2Data(List<SampleData> dataSet) {
        ((Sample2Binder) getDataBinder(1)).addAll(dataSet);
    }
}
