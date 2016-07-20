package com.wangban.musciplayer.adpter;

import java.util.List;

import com.wangban.musciplayer.R;
import com.wangban.musciplayer.R.id;
import com.wangban.musciplayer.R.layout;
import com.wangban.musciplayer.util.CommandUtil;
import com.wangban.musciplayer.util.Consts;
import com.wangban.musciplayer.util.Music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicPlayerAdapter extends BaseAdapter<Music> implements Consts {

    private long selectIndex = -1;
    private int currentPosition;
    private boolean isPlay;
    private ViewHolder holder;

    public long getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int currentPosition) {
        this.selectIndex = currentPosition;
    }

    public MusicPlayerAdapter(Context context, List<Music> data) {
        super(context, data);

        // TODO Auto-generated constructor stub
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music music = getData().get(position);

        currentPosition = position;
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.music_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(id.tv_music_title);
            holder.tvDuration = (TextView) convertView.findViewById(id.tv_music_duration_list);
            holder.tvArtist = (TextView) convertView.findViewById(id.tv_music_artist);
            holder.tvAlbum = (TextView) convertView.findViewById(id.tv_music_Album);
            holder.ivIsPlay = (ImageView) convertView.findViewById(id.iv_is_lay);
            convertView.setTag(holder);
            //convertView.setBackgroundResource(R.drawable.selctor);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(music.getTitle());
        holder.tvAlbum.setText("--" + music.getAlbum());
        holder.tvArtist.setText(music.getArtist());
        holder.tvDuration.setText(CommandUtil.getTime(music.getDuration()));

        isPlay = getData().get(position).isplay();
        // Log.i(TAG, "11111111");

        //ImageView isplay show
       // Log.i(TAG, "getView: isplay" + isPlay);
       // Log.i(TAG, "getView" + position);

        if (selectIndex == currentPosition) {
            holder.ivIsPlay.setBackgroundResource(R.drawable.ic_laba02);

        } else {
            holder.ivIsPlay.setBackgroundResource(R.drawable.point);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvDuration;
        TextView tvAlbum;
        TextView tvArtist;
        ImageView ivIsPlay;
    }


}
