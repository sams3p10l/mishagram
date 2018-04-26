package com.example.specter.mishagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Message> messageList;
	private LinearLayout msgLayout;

	MessageAdapter(Context context)
	{
		this.context = context;
		this.messageList = new ArrayList<>();
	}

	public void addMessage(Message message)
	{
		messageList.add(message);
		notifyDataSetChanged();
	}

	public void removeMessage(int position)
	{
		messageList.remove(position);
	}

	@Override
	public int getCount()
	{
		return messageList.size();
	}

	@Override
	public Object getItem(int i)
	{
		Object rv = null;
		try {
			rv = messageList.get(i);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return rv;
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		ViewHolder holder = new ViewHolder();

		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (inflater != null)
			{
				view = inflater.inflate(R.layout.message_layout, viewGroup, false);
			}

			holder.message_holder = view.findViewById(R.id.message_id);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}

		final Message message = messageList.get(i);
		final SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		msgLayout = view.findViewById(R.id.messLayout);

		holder.message_holder.setText(message.getMsg());

		if(pref.getInt("contact_id", 0) != Integer.parseInt(message.getSender()))
		{
			holder.message_holder.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryTransparent));
			msgLayout.setGravity(Gravity.START);
		}

		return view;
	}

	public class ViewHolder
	{
		TextView message_holder;
	}
}
