package com.example.specter.mishagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Message> messageList;

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
		ViewHolder holder;

		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (inflater != null)
			{
				if (messageList.size() % 2 == 0)
					view = inflater.inflate(R.layout.message_layout, viewGroup, false);
				else
					view = inflater.inflate(R.layout.message_layout2, viewGroup, false);
			}
			holder = new ViewHolder();

			holder.message_holder = view.findViewById(R.id.message_id);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();

		final Message message = messageList.get(i);

		holder.message_holder.setText(message.getMsg());

		return view;
	}

	public class ViewHolder
	{
		TextView message_holder;
	}
}
