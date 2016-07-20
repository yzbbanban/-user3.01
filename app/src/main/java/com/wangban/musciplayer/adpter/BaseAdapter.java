package com.wangban.musciplayer.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
	private Context context;
	private List<T> data;
	private LayoutInflater inflater;

	public BaseAdapter(Context context, List<T> data) {

		super();
		setContext(context);
		setData(data);
		setInflater();
	}

	public Context getContext() {
		return context;
	}

	private final void setContext(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("contextnull");
		}
		this.context = context;
	}

	public List<T> getData() {
		return data;
	}

	private final void setData(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		this.data = data;
	}

	public final LayoutInflater getInflater() {
		return inflater;
	}

	private final void setInflater() {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
