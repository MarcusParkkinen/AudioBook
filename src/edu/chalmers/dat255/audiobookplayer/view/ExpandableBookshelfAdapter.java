package edu.chalmers.dat255.audiobookplayer.view;

import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;

/**
 * TBA
 * 
 * @author Fredrik Åhs
 * @version 0.1
 */

public class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Entry<String, List<String>>> listData;
	private BookshelfFragment bsf;

	public ExpandableBookshelfAdapter(Context context, List<Entry<String, List<String>>> listData, BookshelfFragment bsf) { //IndexedMap<String, List<String>> listData) {
		this.context = context;
		this.listData = listData;
		this.bsf = bsf;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return listData.get(groupPosition).getValue().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return listData.get(groupPosition).getValue().size();
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//temporary
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.bookshelf_child_row, parent, false);
		}
		TextView textViewTitle = (TextView) view.findViewById(R.id.bookshelfTrackTitle);
		textViewTitle.setText(getChild(groupPosition, childPosition).toString());
		//temporary value
		int duration = 6367;
		//set the duration of the track
		TextView textViewTime = (TextView) view.findViewById(R.id.bookshelfTrackTime);				
		textViewTime.setText(DateUtils.formatElapsedTime(duration));

		return view;
	}

	public Object getGroup(int groupPosition) {
		return listData.get(groupPosition).getKey();
	}

	public int getGroupCount() {
		return listData.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.bookshelf_group_row, parent, false);
		}		

		//set name of group
		TextView textView = (TextView) convertView.findViewById(R.id.bookshelfBookTitle);
		textView.setText(getGroup(groupPosition).toString());

		//set the progress of the progressbar 
		//need to get actual progress, or current and total time of book 
		int progress = 50;
		ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.bookshelfProgressBar);
		if(progress >= 0 && progress <= 100) {
			progressBar.setProgress(progress);
		}

		//set the coverart
		final ExpandableListView expandedListView = (ExpandableListView) parent;
		ImageView imageView = (ImageView)convertView.findViewById(R.id.bookshelfBookCover);
		imageView.setImageResource(R.drawable.ic_launcher);
		//click the coverart to 'open the book' and show tracks (expands/collapses book)
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isExpanded){
					expandedListView.collapseGroup(groupPosition);		
				}
				else{
					expandedListView.expandGroup(groupPosition);
				}
			}
		});
		
		//ongroupclick in bookshelffragment no longer functions correctly, this is a workaround.
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				bsf.groupClicked(groupPosition);
			}
		});
		
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}	
}