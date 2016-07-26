package com.tistory.massivcode.realmmemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by massivcode on 2016. 7. 25..
 */
public class MemoAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Memo> mData;

    public MemoAdapter(Context mContext, ArrayList<Memo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.memo_layout, parent, false);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.memo_title_tv);
            holder.contentsTextView = (TextView) convertView.findViewById(R.id.memo_contents_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Memo each = mData.get(position);
        holder.titleTextView.setText(each.getTitle());
        holder.contentsTextView.setText(each.getContents());

        return convertView;
    }

    public void deleteMemo(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void swapMemo(Memo memo) {
        int id = memo.getId();
        int index = -1;

        for (int i = 0; i < mData.size(); i++) {
            Memo each = mData.get(i);
            if(each.getId() == id) {
                index = i;
            }
        }

        if(index != -1) {
            System.out.println(mData);
            mData.remove(index);
            System.out.println(mData);
            mData.add(index, memo);
            System.out.println(mData);
        }

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView titleTextView, contentsTextView;
    }
}
