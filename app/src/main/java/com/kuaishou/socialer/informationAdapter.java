package com.kuaishou.socialer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
//Adapter继承RecyclerView.Adapter<中间放的是自定义的Adapter的ViewHolder>
public class informationAdapter extends RecyclerView.Adapter<informationAdapter.ViewHolder> {
    List<collectedUser> collectedUsers;

    //自定义ViewHolder继承 RecyclerView.ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView phoneNumber;
        TextView Weibo;
        TextView QQ;
        TextView weChat;
        //绑定传来的itemView中的各个组件
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recordName);
            phoneNumber = itemView.findViewById(R.id.recordPhoneNumber);
            QQ = itemView.findViewById(R.id.recordQQ);
            weChat = itemView.findViewById(R.id.recordWeChat);
            Weibo = itemView.findViewById(R.id.recordWeiBo);
        }
    }
    //构造器存放list
    informationAdapter(List<collectedUser> collectedUsers)
    {
        this.collectedUsers = collectedUsers;
    }
    //加载子布局
    @NonNull
    @Override
    public informationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(collectedUsers.get(position).getName());
        holder.phoneNumber.setText(collectedUsers.get(position).getPhoneNumber());
        holder.Weibo.setText(collectedUsers.get(position).getWeiBo());
        holder.QQ.setText(collectedUsers.get(position).getQQ());
        holder.weChat.setText(collectedUsers.get(position).getWeChat());
    }

    @Override
    public int getItemCount() {
        return collectedUsers.size()-1;
    }
}
