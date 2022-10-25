package com.calicdan.florsgardenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class WormsAdapter extends FirebaseRecyclerAdapter<HomeModel,WormsAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public WormsAdapter(@NonNull FirebaseRecyclerOptions<HomeModel> options) {
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



        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.profileImage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1500)
                        .create();



                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.txtName);
                EditText description = view.findViewById(R.id.txtDesc);
                EditText Surl = view.findViewById(R.id.txtSurl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);


                name.setText(model.getName());
                description.setText(model.getDescription());
                Surl.setText(model.getSurl());

                dialogPlus.show();

            }
        });

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

        ImageView imgEdit;

        Button btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = (CircleImageView)itemView.findViewById(R.id.profileImage);

            nameText = (TextView)itemView.findViewById(R.id.nameText);

            descriptionText = (TextView)itemView.findViewById(R.id.descriptionText);

            imgEdit = (ImageView)itemView.findViewById(R.id.imgEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);


        }

    }
}