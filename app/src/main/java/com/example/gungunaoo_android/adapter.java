package com.example.gungunaoo_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class adapter extends FirestoreRecyclerAdapter<model, adapter.viewholder> {

    private final RecyclerViewInterface recyclerViewInterface;

    public adapter(@NonNull FirestoreRecyclerOptions<model> options, RecyclerViewInterface recyclerViewInterface) {
        super(options);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull model model) {
        holder.song_Name.setText(model.getSong_Name());
        holder.song_Desc.setText(model.getSong_Desc());
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song,parent,false);
        return new viewholder(view, recyclerViewInterface);
    }

    public static class viewholder extends RecyclerView.ViewHolder{

        TextView song_Name, song_Desc;
        public viewholder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            song_Name = (TextView) itemView.findViewById(R.id.songName);
            song_Desc = (TextView) itemView.findViewById(R.id.songDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos =getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onClickItem(pos);
                        }
                    }
                }
            });
        }
    }
}
