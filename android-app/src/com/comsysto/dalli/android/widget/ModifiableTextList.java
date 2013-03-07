package com.comsysto.dalli.android.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.comsysto.dalli.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom widget combining an EditText, a button and a list.
 * Through this widget a user can add new Strings to a list. The holding
 * Activity can then extract via {@link #getEntries()} the values or
 * also {@link #setEntries(List)} initially.
 * 
 * To add this widget to another view, just put this lines into xml:
 * {@code
 *	    <com.comsysto.dalli.android.widget.ModifiableTextList 
 *	    	android:id="@+id/tagList"
 *			android:layout_width="fill_parent" 
 *			android:layout_height="wrap_content" 
 *			android:orientation="vertical" 
 *			widget:name="Tag"   	
 *	    />
 *	     }
 * 
 * @author stefandjurasic
 *
 */
public class ModifiableTextList extends LinearLayout {
	
	private EditText newEntryEditText;
	private Button addEntryButton;
	private ListView listView;
	private List<String> entries = new ArrayList<String>();
	private ArrayAdapter<String> arrayAdapter;
	private Context context;

	public ModifiableTextList(Context context) {
		super(context);
		init(context);
	}

	public ModifiableTextList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		String name = attrs.getAttributeValue("http://www.comsysto.com/dalli/android/widget", "name");
		this.newEntryEditText.setHint(name);
	}

	private void init(Context context) {
		this.context = context;
		inflate(context, R.layout.modifiable_text_list, this);
		newEntryEditText = (EditText)findViewById(R.id.newEntryEditText);
		assert(newEntryEditText != null);
		
		addEntryButton = (Button)findViewById(R.id.addEntryButton);
		assert(addEntryButton != null);
		addEntryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				entries.add(newEntryEditText.getText().toString());
				newEntryEditText.setText("");
				arrayAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(listView);
			}
		});
		
		listView = (ListView)findViewById(R.id.listView);
		arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, entries);
		listView.setAdapter(arrayAdapter);
		assert(listView != null);
	}

	public List<String> getEntries() {
		return entries;
	}

	public void setEntries(List<String> entries) {
		this.arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, entries);
		this.listView.setAdapter(this.arrayAdapter);
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {
        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            View listItem = arrayAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (arrayAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }	
	
	
}
