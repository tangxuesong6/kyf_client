package com.example.openaiclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openaiclient.bean.MsgBean;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MsgBean> list;

    public MsgAdapter(List<MsgBean> list) {
        this.list = list;
    }

    public class RecieveViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        TextView left_msg;

        public RecieveViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);
        }
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rightLayout;
        TextView right_msg;

        public SendViewHolder(View view) {
            super(view);


            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MsgBean.TYPE_RECEIVED)
            return new RecieveViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_receive_item, parent, false));
        else
            return new SendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_send_item, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgBean msgBean = list.get(position);
        if (holder instanceof RecieveViewHolder) {
            ((RecieveViewHolder) holder).left_msg.setText(msgBean.getContent());
        } else {
            ((SendViewHolder) holder).right_msg.setText(msgBean.getContent());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position)
                .getType();
    }
}
