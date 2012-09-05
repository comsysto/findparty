package com.comsysto.dalli.android.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.comsysto.dalli.android.R;
import com.comsysto.findparty.Party;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 05.09.12
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public class SelectCategoryActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_category);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.category_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final String category = getItem(position);
                if (convertView == null) {
                    convertView = (LinearLayout) View.inflate(SelectCategoryActivity.this,
                            R.layout.category_item, null);
                }
                final TextView textView = (TextView) convertView
                        .findViewById(android.R.id.text1);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectCategoryActivity.this, CreatePartyActivity.class);
                        intent.putExtra("category", category);
                        intent.putExtra("new", true);
                        startActivity(intent);
                    }
                });

                textView.setText(category);


                return convertView;
            }
        };
        stringArrayAdapter.add("Running");
        stringArrayAdapter.add("Cycling");
        stringArrayAdapter.add("Clubbing");
        stringArrayAdapter.add("Play Dodge Ball");
        stringArrayAdapter.add("Snuggling");
        stringArrayAdapter.add("Hiking");
        stringArrayAdapter.add("Singing");

        setListAdapter(stringArrayAdapter);
        super.onCreate(savedInstanceState);
    }
}
