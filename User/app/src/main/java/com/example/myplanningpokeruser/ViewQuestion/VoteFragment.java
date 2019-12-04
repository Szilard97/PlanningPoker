package com.example.myplanningpokeruser.ViewQuestion;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplanningpokeruser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteFragment extends Fragment {

    private TextView textViewQuestion;
    private Button buttonVote1, buttonVote2,
            buttonVote3, buttonVote4, buttonVote5, buttonIDont;
    private String enteredQuestionId, enteredRoomName, enteredQuestion;
    private DatabaseReference mRef;
    private String voteData;


    public VoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_vote, container, false);

        bindWidget(view);

        questionVisualization();

        vote();

        return view;
    }

    //kiiratom az adatokat az adatbazisbol a VoteFragmentbe
    private void questionVisualization() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                        for (DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()){

                            if(String.valueOf(dataSnapshot1.getKey()).equals(enteredRoomName)&&
                                    String.valueOf(dataSnapshot2.getKey()).equals(enteredQuestionId)){
                                textViewQuestion.setText(String.valueOf(dataSnapshot3.getKey()));
                                enteredQuestion = String.valueOf(dataSnapshot3.getKey());
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    //a szavazo gombok Listener hogy mikor melyikre szavazok es lepik at a MainFragmentbe
    private void vote() {
        mRef = FirebaseDatabase.getInstance().getReference();

        buttonVote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("1", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        buttonVote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("2", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        buttonVote3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("3", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        buttonVote4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("4", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        buttonVote5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("5", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        buttonIDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voteButton("I don't want to answer", enteredRoomName, enteredQuestionId, enteredQuestion);
                Toast.makeText(getActivity(), "Thank you your vote!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    //szavazo gombok mentese
    private void voteButton(final String data, String roomName, String questionId, String question) {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        final String [] user = email.split("@");

        mRef = FirebaseDatabase.getInstance().getReference().child("GroupID").child(roomName)
                .child(questionId).child(question).child(data);

        //hozzaadom a szavazatot az adabazisba
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mRef.setValue( dataSnapshot.getValue().toString() + " " + user[0]);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("kiwi", databaseError.toString());
            }
        });
    }

    // a szukseges adatok inicializalasa
    private void bindWidget(View view) {

        textViewQuestion = view.findViewById(R.id.vTextViewQuestion);
        buttonIDont = view.findViewById(R.id.buttonIDont);
        buttonVote1 = view.findViewById(R.id.buttonVote1);
        buttonVote2 = view.findViewById(R.id.buttonVote2);
        buttonVote3 = view.findViewById(R.id.buttonVote3);
        buttonVote4 = view.findViewById(R.id.buttonVote4);
        buttonVote5 = view.findViewById(R.id.buttonVote5);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            Log.d("alma", bundle.toString());
            enteredQuestionId = bundle.getString("QuestionId");
            enteredRoomName = bundle.getString("RoomName");
        }else
            Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();

        Log.d("alma", enteredQuestionId + " " + enteredRoomName);

        mRef = FirebaseDatabase.getInstance().getReference().child("GroupID");
    }

}
