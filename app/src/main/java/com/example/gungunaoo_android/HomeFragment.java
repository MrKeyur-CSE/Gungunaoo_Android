package com.example.gungunaoo_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment implements RecyclerViewInterface{

    View inflate;
    RecyclerView music_list;
    adapter myadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_home, container, false);
        music_list = inflate.findViewById(R.id.music_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(music_list.getContext());
        music_list.setLayoutManager(layoutManager);
//        Log.d(String.valueOf(music_list), "onCreateView: .............");

        FirestoreRecyclerOptions<model> options = new FirestoreRecyclerOptions.Builder<model>()
                .setQuery(FirebaseFirestore.getInstance().collection("music"), model.class)
                .build();

        myadapter = new adapter(options, this);
        music_list.setAdapter(myadapter);

        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter.stopListening();
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(getActivity(), play_pause_screen.class);

        intent.putExtra("song_Name", myadapter.getItemId(position));
        intent.putExtra("song_Desc", myadapter.getItemId(position));

        startActivity(intent);
    }
}