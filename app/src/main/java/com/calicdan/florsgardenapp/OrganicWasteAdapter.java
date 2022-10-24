package com.calicdan.florsgardenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrganicWasteAdapter extends FirebaseRecyclerAdapter<HomeModel,OrganicWasteAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrganicWasteAdapter(@NonNull FirebaseRecyclerOptions<HomeModel> options) {
        super(Objects.requireNonNull(options));
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull HomeModel model) {

        holder.nameText.setText(model.getName());
        holder.descriptionText.setText(model.getDescription());

        Glide.with(holder.profileImage.getContext())
                .load(model.getSurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.profileImage);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,parent,false);
        return new myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nameText;
        TextView descriptionText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = (CircleImageView)itemView.findViewById(R.id.profileImage);

            nameText = (TextView)itemView.findViewById(R.id.nameText);

            descriptionText = (TextView)itemView.findViewById(R.id.descriptionText);


        }

    }
}