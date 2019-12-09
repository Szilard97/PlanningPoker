package com.example.myplanningpokeruser.ViewQuestion;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
public class ViewResultFragment extends Fragment {

    private DatabaseReference mRef;
    private TextView textViewRoomName, textViewQuestionID, textViewQuestion,
            textView1, textView2, textView3, textView4, textView5, textViewIdont;
    private String enteredRoomName, enteredQuestionId, expireDateString, expectedVotes, voted;
    private Date expireDate, currentDate;



    public ViewResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_result, container, false);

        bindWidget(view);

        validationAndShowResult();

        return view;
    }

    private void bindWidget(View view) {
        mRef = FirebaseDatabase.getInstance().getReference()
                .child("GroupID");
        textViewRoomName = view.findViewById(R.id.textViewRoomName);
        textViewQuestionID = view.findViewById(R.id.cTextViewQuestionId);
        textViewQuestion = view.findViewById(R.id.textViewQuestion);
        textView1 = view.findViewById(R.id.textViewReplyOne);
        textView2 = view.findViewById(R.id.textViewReplTwo);
        textView3 = view.findViewById(R.id.textViewReplyThree);
        textView4 = view.findViewById(R.id.textViewReplyFor);
        textView5 = view.findViewById(R.id.textViewReplyFive);
        textViewIdont = view.findViewById(R.id.IDont);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            Log.d("alma", bundle.toString());

            enteredQuestionId = bundle.getString("QuestionId");
            enteredRoomName = bundle.getString("RoomName");

            textViewQuestionID.setText(enteredQuestionId);
            textViewRoomName.setText(enteredRoomName);
        }else
            Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();
    }

    private void validationAndShowResult() {

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
                                    textViewQuestion.setText(String.valueOf(dataSnapshot3.getKey()));
                                    Log.d("fanta", "itt");
                                    if(String.valueOf(dataSnapshot4.getKey()).equals("1")){
                                        textView1.setText( "1: " + dataSnapshot4.getValue().toString());
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("2")){
                                        textView2.setText("2: " + dataSnapshot4.getValue().toString());
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("3")){
                                        textView3.setText("3: " + dataSnapshot4.getValue().toString());
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("4")){
                                        textView4.setText("4: " + dataSnapshot4.getValue().toString());
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("5")){
                                        textView5.setText("5" + dataSnapshot4.getValue().toString());
                                    }else if(String.valueOf(dataSnapshot4.getKey()).equals("I don't want to answer")) {
                                        textViewIdont.setText("I don't want to answer: " + dataSnapshot4.getValue().toString());
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

        //expiredOrAllVoted();
    }

}