package com.comsysto.findbuddies.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findparty.Party;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * The PartyListAdapter holds the Parties to be displayed in a List and
 *
 * LongClick will be ignored and can be handled from the List itself.
 * 
 * @author stefandjurasic
 *
 */
public class PartyListAdapter extends ArrayAdapter<Party> {

	private Context context;

	public PartyListAdapter(Context context, List<Party> parties) {
		super(context, R.layout.list_item, parties);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Party party = getItem(position);
		if (convertView == null) {
			convertView = (LinearLayout) View.inflate(context,R.layout.list_item, null);
		}
		final TextView textView = (TextView) convertView.findViewById(android.R.id.text1);

        String displayName = CategoryType.valueOf(party.getCategory()).getDisplayName();
        String date = PartyManagerApplication.getInstance().getSimpleDateFormat().format(party.getStartDate());
        textView.setText(displayName + ", " + date);

		return convertView;
	}

}
