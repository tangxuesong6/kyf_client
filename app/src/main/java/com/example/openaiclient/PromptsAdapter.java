package com.example.openaiclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openaiclient.bean.PromptsBean;
import com.example.openaiclient.util.LogUtil;

import java.util.List;

public class PromptsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PromptsBean> list;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PromptsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.prompts_item, parent, false));
    }

    public PromptsAdapter(List<PromptsBean> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PromptsBean promptsBean = list.get(position);
        PromptsViewHolder promptsViewHolder = (PromptsViewHolder) holder;
        promptsViewHolder.title.setText(promptsBean.act);
        promptsViewHolder.content.setText(promptsBean.prompt);
        promptsViewHolder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("PromptsAdapter", "onClick: "+ promptsBean.prompt);
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(promptsBean.prompt);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PromptsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llLayout;
        TextView title;
        TextView content;

        public PromptsViewHolder(View view) {
            super(view);
            llLayout = view.findViewById(R.id.ll_prompts_back);
            title = view.findViewById(R.id.tv_prompts_title);
            content = view.findViewById(R.id.tv_prompts_content);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String content);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
