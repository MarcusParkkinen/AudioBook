package edu.chalmers.dat255.audiobookplayer.view;

import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * TBA
 * 
 * @author Fredrik Åhs
 * @version 0.1
 */

public class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Entry<String, List<String>>> listData;

	public ExpandableBookshelfAdapter(Context context, List<Entry<String, List<String>>> listData) { //IndexedMap<String, List<String>> listData) {
		this.context = context;
		this.listData = listData;
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
		TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
	}

	private TextView getGenericView() {
		//temporary
		// TODO: fix 'deprecated'
		@SuppressWarnings("deprecation")
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(36, 0, 0, 0);
        return textView;
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

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		//temporary
		TextView textView = getGenericView();
		textView.setText(getGroup(groupPosition).toString());
		return textView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}	
}