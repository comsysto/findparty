package com.comsysto.dalli.android.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.ToggleButton;

import com.comsysto.dalli.android.R;

/**
 * Acitivty for displaying all Tasks!
 * 
 * 
 * @author stefandjurasic
 *
 */
public class ReviewActivity extends AbstractTaskListActivity {

	private SlidingDrawer slidingDrawer;
	private ImageView slidingImage;

	private ToggleButton showOnlyActiveTasks;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.review);	
		super.onCreate(savedInstanceState);
		this.slidingDrawer = (SlidingDrawer)findViewById(R.id.drawer);
		this.slidingImage = (ImageView)findViewById(R.id.handle);
		this.showOnlyActiveTasks = (ToggleButton)findViewById(R.id.activeTasksFilter);
		
		this.showOnlyActiveTasks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				initArrayAdapter();
			}

			
			
		});
		
		this.slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				slidingImage.setImageResource(android.R.drawable.arrow_down_float);
			}
		});
		
		this.slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				slidingImage.setImageResource(android.R.drawable.arrow_up_float);
			}
		});

		
	}

	@Override
	protected void filter() {
		if (showOnlyActiveTasks.isChecked()) {
			filterActiveTasks();
		}
	}
}
