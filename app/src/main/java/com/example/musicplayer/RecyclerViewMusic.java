package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.databinding.ItemMusicBinding;

import java.util.ArrayList;

public class RecyclerViewMusic extends  RecyclerView.Adapter<RecyclerViewMusic.Holder>{
    ArrayList<MusicModel>list=new ArrayList<>();

    OnClick OnClick;

    public void setOnClick(OnClick onClick) {
        this.OnClick = onClick;
    }

    public void setList(ArrayList<MusicModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMusicBinding binding =ItemMusicBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(list.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ItemMusicBinding binding;
        public Holder(ItemMusicBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.constraint.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MediaPlayeInstant.getInstance().reset();
                            OnClick.onClick(list.get(getAdapterPosition()).getTitle(),list.get(getAdapterPosition()).getDuration(),list.get(getAdapterPosition()).path);
                        }
                    }
            );
        }
        void bind(String name){
            binding.name.setText(name);

        }
    }

    interface OnClick{
        void onClick(String name,String duration,String path);
    }
}
