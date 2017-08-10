package co.sanfen.itemtouchhelper.demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ListActivity {

    private ArrayList<HashMap<String, Object>>   listItems;    //存放文字、图片信息
    private SimpleAdapter listItemAdapter;           //适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




}
