package com.example.specter.mishagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ContactAdapter extends BaseAdapter
{
	private ArrayList<Contact> arrayList;
	private Context context;

	ContactAdapter(Context context)
	{
		this.context = context;
		this.arrayList = new ArrayList<>();
	}

	public void addContact(Contact contact)
	{
		arrayList.add(contact);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return arrayList.size();
	}

	@Override
	public Object getItem(int i)
	{
		Object rv = null;
        try {
            rv = arrayList.get(i);
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

		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (inflater != null)
			{
				view = inflater.inflate(R.layout.list_layout, null);
			}

			Log.d("tag", "inflated");
			holder = new ViewHolder();

			holder.thumbnail = view.findViewById(R.id.thumb);
			holder.text = view.findViewById(R.id.contact);
			holder.arrow = view.findViewById(R.id.arrow);

			view.setTag(holder);

		}
		else
			holder = (ViewHolder) view.getTag();
		
		final Contact contact = arrayList.get(i);
		Random random = new Random();
		final String fullName = contact.getFirstName() + " " + contact.getLastName();

		holder.thumbnail.setText(contact.getFirstName().substring(0, 1));
		holder.thumbnail.setBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		holder.text.setText(fullName);

		holder.arrow.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context, MessageActivity.class);
				Bundle bundle = new Bundle();
				SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);

				bundle.putString("name", fullName);
				bundle.putInt("senderID", pref.getInt("contact_id", 0));
				bundle.putInt("receiverID", contact.getId());
				intent.putExtras(bundle);

				context.startActivity(intent);
			}
		});

		return view;
	}

	public class ViewHolder
	{
		TextView thumbnail;
		TextView text;
		ImageView arrow;
	}
}
