package com.example.taxicle_admin;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_admin.Model.Rule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;


public class RuleAdapter extends FirebaseRecyclerAdapter<Rule, RuleAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RuleAdapter(@NonNull FirebaseRecyclerOptions<Rule> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Rule model) {
        holder.txtRule.setText(model.getContent());

        holder.btnEdit.setOnClickListener(v -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.txtRule.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_rule_popup))
                    .setExpanded(true, 700)
                    .create();



            View view = dialogPlus.getHolderView();
            EditText txtRule = view.findViewById(R.id.txtRule);

            Button btnUpdate = view.findViewById(R.id.btnUpdate);

            txtRule.setText(model.getContent());

            dialogPlus.show();

            btnUpdate.setOnClickListener(v1 -> {
                Map<String, Object> map = new HashMap<>();
                map.put("content", txtRule.getText().toString());

                FirebaseDatabase.getInstance().getReference(Rule.class.getSimpleName())
                        .child(getRef(position).getKey()).updateChildren(map)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(holder.txtRule.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(holder.txtRule.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        });
            });
        });

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtRule.getContext());
            builder.setTitle("Are you Sure?");
            builder.setMessage("Deleted data can't be Undo.");

            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                FirebaseDatabase.getInstance().getReference(Rule.class.getSimpleName())
                        .child(getRef(position).getKey()).removeValue();
            });
            builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                Toast.makeText(holder.txtRule.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            }));
            builder.show();
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtRule;

        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRule = (TextView) itemView.findViewById(R.id.rulestext);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
