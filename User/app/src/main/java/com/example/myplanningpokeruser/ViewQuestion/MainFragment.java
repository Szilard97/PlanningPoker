package com.example.myplanningpokeruser.ViewQuestion;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplanningpokeruser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private Button voteButton;
    private EditText roomNameEditText, questionIdEditText;
    private DatabaseReference mRef;
    private Date expireDate, currentDate;
    private String expireDateString, permissionString;


    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        bindWidget(view);

        vote();


        return view;
    }

    //szavazas gomb Listener es ellenorzes hogy be irtae az adatokat
    private void vote() {
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomNameEditText.getText().toString().isEmpty() &&
                        questionIdEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Room name and Question ID",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    expirationDateCheck();
            }
        });
    }

    //lekerem a lejarati datumot es ellenorzom hogy lejart-e
    private void expirationDateCheck() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        for (DataSnapshot thirdFor : secondFor.getChildren()) {
                            for(DataSnapshot forth: thirdFor.getChildren()){
                                if(roomNameEditText.getText().toString().equals(String.valueOf(forDataSnapshot.getKey()))){
                                    if(String.valueOf(forth.getKey()).equals("ExpirationDate")){
                                        expireDateString = String.valueOf(forth.getValue());
                                    }else if(String.valueOf(forth.getKey()).equals("Permission")){
                                        permissionString = String.valueOf(forth.getValue());
                                    }
                                }
                            }
                        }
                    }
                }

                //a jelenlegi datum es idot keri le
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String currentDateString = formatter.format(new Date());

                //datumok atalakitasa
                try {
                    expireDate = formatter.parse(expireDateString);
                    currentDate = formatter.parse(currentDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("korte", e.toString());
                }

                //a lejarati datum es a jelenlegi datum ellenorzese
                if(expireDate.after(currentDate) && permissionString.equals("True")){
                    VoteFragment voteFragment = new VoteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("QuestionId", questionIdEditText.getText().toString());
                    bundle.putString("RoomName", roomNameEditText.getText().toString());
                    voteFragment.setArguments(bundle);

                    MainActivity.fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, voteFragment)
                            .addToBackStack(null)
                            .commit();

                }
                else{
                    Toast.makeText(getActivity(), "Sorry but the vote is over or Permission is False", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    // a szukseges adatok inicializalasa
    private void bindWidget(View view) {
        voteButton = view.findViewById(R.id.mVoteButton);
        roomNameEditText = view.findViewById(R.id.mRoomNameEditText);
        questionIdEditText = view.findViewById(R.id.mQuestionId);
        mRef = FirebaseDatabase.getInstance().getReference().child("GroupID");
    }

}
