package project.senior.komsan.pointme;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Komsa'n on 3/15/2015.
 */
public class Favorite extends ActionBarActivity {
    private final String[] mStrings = SearchData.sDataStrings;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.favorite_layout);

        mListView = (ListView) findViewById(R.id.favorite_list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mStrings));
        mListView.setTextFilterEnabled(true);

    }


}
