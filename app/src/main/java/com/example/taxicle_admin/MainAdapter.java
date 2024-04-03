package com.example.taxicle_admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_admin.Model.Main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;


public class MainAdapter extends FirebaseRecyclerAdapter<Main, MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Main> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Main model) {
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());

        holder.btnEdit.setOnClickListener(v -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup))
                    .setExpanded(true, 1500)
                    .create();


            View view = dialogPlus.getHolderView();
            EditText name = view.findViewById(R.id.txtName);
            EditText phone = view.findViewById(R.id.txtPhone);
            EditText address = view.findViewById(R.id.txtAddress);

            Button btnUpdate = view.findViewById(R.id.btnUpdate);
            Button btnChangePass = view.findViewById(R.id.btnChangePass);

            name.setText(model.getName());
            phone.setText(model.getPhone());
            address.setText(model.getAddress());

            dialogPlus.show();

            btnUpdate.setOnClickListener(v1 -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("address", address.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("Driver")
                        .child(getRef(position).getKey()).updateChildren(map)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(holder.name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(holder.name.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        });
            });

            btnChangePass.setOnClickListener(v1 -> {
                Intent intent = new Intent(holder.name.getContext(), ChangePasswordActivity.class);
                intent.putExtra("email", holder.email.getText().toString());
                holder.name.getContext().startActivity(intent);
            });
        });

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
            builder.setTitle("Are you Sure?");
            builder.setMessage("Deleted data can't be Undo.");

            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                FirebaseDatabase.getInstance().getReference().child("Driver")
                        .child(getRef(position).getKey()).removeValue();

                FirebaseDatabase.getInstance().getReference().child("AvailableDriver")
                        .child(getRef(position).getKey()).removeValue();
            });
            builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                Toast.makeText(holder.name.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            }));
            builder.show();
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, email, address;

        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.rulestext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            phone = (TextView) itemView.findViewById(R.id.phonetext);
            address = (TextView) itemView.findViewById(R.id.addresstext);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
