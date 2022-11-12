package com.calicdan.florsgardenapp;

import android.annotation.SuppressLint;
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

public class ImageRecogResultAdapter extends FirebaseRecyclerAdapter<HomeModel, ImageRecogResultAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageRecogResultAdapter(@NonNull FirebaseRecyclerOptions<HomeModel> options) {
        super(Objects.requireNonNull(options));
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageRecogResultAdapter.myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull HomeModel model) {

        holder.nameText.setText(model.getName());
        holder.descriptionText.setText(model.getDescription());
        holder.categoryText.setText(model.getCategory());

        Glide.with(holder.profileImage.getContext())
                .load(model.getSurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.profileImage);




    }

    @NonNull
    @Override
    public ImageRecogResultAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recog_result_item,parent,false);
        return new ImageRecogResultAdapter.myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nameText, descriptionText, categoryText;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = (CircleImageView)itemView.findViewById(R.id.profileImage);

            nameText = (TextView)itemView.findViewById(R.id.nameText);
            categoryText = (TextView)itemView.findViewById(R.id.categoryText);

            descriptionText = (TextView)itemView.findViewById(R.id.descriptionText);




        }

    }
}