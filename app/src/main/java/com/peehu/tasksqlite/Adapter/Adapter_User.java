package com.peehu.tasksqlite.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.peehu.tasksqlite.DBHelper;
import com.peehu.tasksqlite.Model.Task_Model;
import com.peehu.tasksqlite.Model.User_Model;
import com.peehu.tasksqlite.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_User extends RecyclerView.Adapter<Adapter_User.ProductViewHolder> {
    private Context mCtx;
    private List<User_Model> user_modelList;
    ViewGroup viewGroup;
    DBHelper dbHelper;
    int currid;


    public Adapter_User(Context mCtx, List<User_Model> user_modelList) {
        this.mCtx = mCtx;
        this.user_modelList = user_modelList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_task, parent, false);
        dbHelper = new DBHelper(mCtx);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {

        final User_Model userModel = user_modelList.get(position);
        holder.txt_title.setText(userModel.getName());
        holder.txt_des.setText(userModel.getEmail());
        final String id = userModel.getId();
      holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = LayoutInflater.from(mCtx).inflate(R.layout.add_task, viewGroup, false);
                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                ImageView cancel_button = dialogView.findViewById(R.id.cancel_button);
                final EditText ed_taks = dialogView.findViewById(R.id.ed_taks);
                final EditText ed_des = dialogView.findViewById(R.id.ed_description);
                Button sub = dialogView.findViewById(R.id.create_task);
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                ed_taks.setText(userModel.getName());
                ed_des.setText(userModel.getEmail());
                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String task = ed_taks.getText().toString().trim();
                        String des = ed_des.getText().toString().trim();
                        if (task.equalsIgnoreCase("") || des.equalsIgnoreCase("")) {
                            Toast.makeText(mCtx, "Please enter the values", Toast.LENGTH_LONG).show();
                        } else {
                            Boolean insert = dbHelper.updateUser(Integer.parseInt(id), task, des);
                            if (insert == true) {
                                Toast.makeText(mCtx, "User Updated Successfully", Toast.LENGTH_LONG).show();
                                user_modelList.get(position).setName(task);
                                user_modelList.get(position).setEmail(des);
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(mCtx, "Update  Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteUser(userModel);
                user_modelList.get(position);
                user_modelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, user_modelList.size());
            }
        });


    }


    @Override
    public int getItemCount() {
        return user_modelList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_des;
        CircleImageView edit, delete;


        public ProductViewHolder(final View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.title_task);
            txt_des = itemView.findViewById(R.id.des_task);
            edit = itemView.findViewById(R.id.img_edit);
            delete = itemView.findViewById(R.id.img_delete);


        }

    }
}
