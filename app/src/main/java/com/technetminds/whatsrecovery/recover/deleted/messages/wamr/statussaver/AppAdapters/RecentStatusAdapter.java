package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.DownloadStatusActivity;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Layout_Manager;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.StatusModel;

import java.net.URLConnection;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StatusModel> statusModels;
    private Context context;

    public RecentStatusAdapter(Context context, ArrayList<StatusModel> statusModels) {
        this.context = context;
        this.statusModels = statusModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pictures_row_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            Glide.with(context).load(statusModels.get(position).FilePath).into(((ViewHolder) holder).imageView);
            ((ViewHolder) holder).textView.setTypeface(Layout_Manager.setFonts(context));
            if (!statusModels.get(position).isVideo) {
                ((ViewHolder) holder).textView.setText("Photo");
            } else if (statusModels.get(position).isVideo) {
                ((ViewHolder) holder).textView.setText("Video");
            }

            ((ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DownloadStatusActivity.class);
                    intent.putExtra("statusPath", statusModels.get(position).FilePath);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return statusModels.size();
    }

    private boolean isImageFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("image");
    }

    private boolean isVideoFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dpIV);
            textView = itemView.findViewById(R.id.infoTV);
        }
    }
}
