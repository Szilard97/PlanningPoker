package com.example.myplanningpokeruser.ViewQuestion;


import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private Button voteButton, viewQuestionButton;
    private EditText roomNameEditText, questionIdEditText;
    private DatabaseReference mRef;
    private Date expireDate, currentDate;
    private String expireDateString, permissionString, questionString, expectedVotes,
            enteredRoomName, enteredQuestionId, voted;
    private Boolean alreadyVoted;
    private Integer votedNumber, expctedVote;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        bindWidget(view);

        vote();

        viewQuestion();

        return view;
    }

    //view question (call checkEnteredData)
    private void viewQuestion() {

        viewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnteredData();
            }
        });

    }

    //sent data from the MainFragment to the Votefragment and call expiredOrAllVoted
    private void checkEnteredData() {

        if(roomNameEditText.getText().toString().equals("") &&
            questionIdEditText.getText().toString().equals("")){
            Toast.makeText(getContext(),
                    "Please enter Room name and Question ID", Toast.LENGTH_SHORT).show();
        }else{
            ViewResultFragment viewResultFragment = new ViewResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString("QuestionId", questionIdEditText.getText().toString());
            bundle.putString("RoomName", roomNameEditText.getText().toString());
            viewResultFragment.setArguments(bundle);

            expiredOrAllVoted();

        }

    }

    //voting button Listen and check the submission
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

    //I'll check the expiration date and see if it expires
    private void expirationDateCheck() {

        makeSureYouHaveAlreadyVoted();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(expireDate.after(currentDate) && permissionString.equals("True") && !alreadyVoted){
                    VoteFragment voteFragment = new VoteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("QuestionId", questionIdEditText.getText().toString());
                    bundle.putString("RoomName", roomNameEditText.getText().toString());
                    voteFragment.setArguments(bundle);

                    addVotingNumber();

                    MainActivity.fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, voteFragment)
                            .addToBackStack("Main")
                            .commit();

                }
                else{
                    Toast.makeText(getActivity(), "Sorry but the vote is over, Permission is False or has already voted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    // user has already voted
    private void makeSureYouHaveAlreadyVoted() {

        alreadyVoted = false;

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        final String [] user = email.split("@");

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupID");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        for (DataSnapshot thirdFor : secondFor.getChildren()) {
                            for(DataSnapshot forth: thirdFor.getChildren()){
                                if(String.valueOf(forth.getValue()).contains(user[0])
                                        && String.valueOf(forDataSnapshot.getKey())
                                        .equals(roomNameEditText.getText().toString())){
                                    alreadyVoted = true;
                                }


                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get actual votes and ++
    private void addVotingNumber() {

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupID");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        for (DataSnapshot thirdFor : secondFor.getChildren()) {
                            Log.d("fanta", thirdFor.getKey()+" dfsdf");
                            for(DataSnapshot forth: thirdFor.getChildren()){

                                if (String.valueOf(forDataSnapshot.getKey())
                                        .equals(roomNameEditText.getText().toString())
                                        && String.valueOf(forth.getKey()).equals("Expected voted")){
                                    expctedVote = Integer.parseInt(String.valueOf(forth.getValue()));

                                }else if(String.valueOf(forDataSnapshot.getKey())
                                        .equals(roomNameEditText.getText().toString())
                                        && String.valueOf(forth.getKey()).equals("Voted")){
                                    votedNumber = Integer.parseInt(String.valueOf(forth.getValue()));
                                    questionString = thirdFor.getKey();
                                    votedNumber++;
                                    addVoteToDatabase(votedNumber);
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //add actual votes to the database
    private void addVoteToDatabase(Integer votedNumber) {

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupID");

        mRef.child(roomNameEditText.getText().toString()).child(questionIdEditText.getText().toString())
                .child(questionString).child("Voted").setValue(votedNumber);

    }

    //initialization
    private void bindWidget(View view) {
        voteButton = view.findViewById(R.id.mVoteButton);
        roomNameEditText = view.findViewById(R.id.mRoomNameEditText);
        questionIdEditText = view.findViewById(R.id.mQuestionId);
        viewQuestionButton = view.findViewById(R.id.mViewResultButton);
        mRef = FirebaseDatabase.getInstance().getReference().child("GroupID");
    }

    //expire date is expired and all user  voted
    private void expiredOrAllVoted() {

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Log.d("fanta", String.valueOf(dataSnapshot1.getKey()));
                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                        Log.d("fanta", String.valueOf(dataSnapshot2.getKey()));
                        for (DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()){
                            Log.d("fanta", String.valueOf(dataSnapshot3.getKey()));
                            for (DataSnapshot dataSnapshot4: dataSnapshot3.getChildren()){
                                Log.d("fanta", String.valueOf(dataSnapshot4.getKey()));

                                if(String.valueOf(dataSnapshot1.getKey()).equals(enteredRoomName)&&
                                        String.valueOf(dataSnapshot2.getKey()).equals(enteredQuestionId)){
                                    if(String.valueOf(dataSnapshot4.getKey()).equals("ExpirationDate")){
                                        expireDateString = dataSnapshot4.getValue().toString();
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("Expected votes")){
                                        expectedVotes = dataSnapshot4.getValue().toString();
                                        expctedVote = Integer.parseInt(expectedVotes);
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("Voted")){
                                        voted = dataSnapshot4.getValue().toString();
                                        votedNumber = Integer.parseInt(voted);
                                    }
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(votedNumber >= expctedVote){
            MainActivity.fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, new ViewResultFragment())
                    .addToBackStack("Main")
                    .commit();
        }
        else{
            Toast.makeText(getActivity(), "Sorry but the vote is over, Permission is False or has already voted", Toast.LENGTH_SHORT).show();
        }

    }
}
