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
    String expireDateString;


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

    private void expirationDateCheck() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    //Log.d("korte", String.valueOf(forDataSnapshot.getKey()));

                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        //Log.d("korte", String.valueOf(secondFor.getKey()));

                        for (DataSnapshot thirdFor : secondFor.getChildren()) {
                            //Log.d("korte", String.valueOf(thirdFor.getKey()));

                            for(DataSnapshot forth: thirdFor.getChildren()){
                                if(roomNameEditText.getText().toString().equals(String.valueOf(forDataSnapshot.getKey()))){
                                    if(String.valueOf(forth.getKey()).equals("ExpirationDate")){
                                        expireDateString = String.valueOf(forth.getValue());
                                        //Log.d("korte", expireDateString);
                                    }
                                }else{
                                    //Toast.makeText(getApplicationContext(), "I'm sorry, but no such room", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String currentDateString = formatter.format(new Date());

                try {
                    expireDate = formatter.parse(expireDateString);
                    currentDate = formatter.parse(currentDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("korte", e.toString());
                }


                if(expireDate.after(currentDate)){
                    VoteFragment voteFragment = new VoteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("QuestionId", questionIdEditText.getText().toString());
                    bundle.putString("RoomName", roomNameEditText.getText().toString());
                    voteFragment.setArguments(bundle);

                    MainActivity.fragmentManager.beginTransaction().replace(R.id.frameLayout, voteFragment).commit();

                }
                else{
                    Toast.makeText(getActivity(), "Sorry but the vote is over", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    private void bindWidget(View view) {
        voteButton = view.findViewById(R.id.mVoteButton);
        roomNameEditText = view.findViewById(R.id.mRoomNameEditText);
        questionIdEditText = view.findViewById(R.id.mQuestionId);
        mRef = FirebaseDatabase.getInstance().getReference().child("GroupID");
    }

}
