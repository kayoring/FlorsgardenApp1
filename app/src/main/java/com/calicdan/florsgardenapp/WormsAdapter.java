package com.calicdan.florsgardenapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
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
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull HomeModel model) {

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

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("description", description.getText().toString());
                        map.put("Surl", Surl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Worms")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(name.getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(name.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nameText.getContext());
                builder.setTitle("Are you sure you want to permanently remove this item?");
                builder.setMessage("Deleted data can't be undone.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Worms")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.nameText.getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.nameText.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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