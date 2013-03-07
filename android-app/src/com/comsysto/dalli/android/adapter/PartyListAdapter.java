package com.comsysto.dalli.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.comsysto.dalli.android.R;
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

	public PartyListAdapter(Context context, List<Party> objects) {
		super(context, R.layout.list_item, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Party party = getItem(position);
		if (convertView == null) {
			convertView = (LinearLayout) View.inflate(context,
					R.layout.list_item, null);
		}
		final TextView textView = (TextView) convertView
				.findViewById(android.R.id.text1);
		textView.setText(party.getCategory() + ", " + new SimpleDateFormat("MM/dd/yyyy").format(party.getStartDate()));

		return convertView;
	}

}
