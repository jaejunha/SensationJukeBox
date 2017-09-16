package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Taek on 2017. 9. 16..
 */

public class MusicListAdapter extends ArrayAdapter<MusicItem> {
    private View v; //view를 선언합니다.
    private ViewHolder holder; //holder를 선언합니다.

    // ListViewAdapter의 생성자
    public MusicListAdapter(Context context, int resource, ArrayList<MusicItem> objects){
        super(context,resource,objects);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.music_item, null); //리스트 뷰의 아이템 리스트를 불러옵니다.
            holder = new ViewHolder(); //Holder를 선언합니다.
            //각 textView와 ImageView를 연결합니다.
            holder.Title = (TextView) v.findViewById(R.id.title);
            holder.Artist = (TextView) v.findViewById(R.id.artist);
            v.setTag(holder); //setTag를 사용하여 임시 저장합니다.
        }else { //convertView가 null이 아니기 때문에 다음을 실행 합니다.
            v = convertView;
            holder = (ViewHolder) v.getTag();
        }

        MusicItem musicItem = (MusicItem) getItem(position); //포지션 별로 값을 채워 줍니다.
        holder.Title.setText(musicItem.getTitle());
        holder.Artist.setText(musicItem.getArtist());
        return v;
    }
}

//holder하기 위한 뷰를 미리 생성해둡니다.
class ViewHolder {
    TextView Title;
    TextView Artist;
}
