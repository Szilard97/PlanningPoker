package com.example.myplanningpokeruser.ViewQuestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myplanningpokeruser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoteActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private Button buttonVote1, buttonVote2,
            buttonVote3, buttonVote4, buttonVote5, buttonIDont;
    private String enteredQuestionId, enteredRoomName;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        bindWidget();

        questionVisualization();

        vote();
    }

    private void questionVisualization() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String data = dataSnapshot.child("Group ID's")
                        .child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .getValue().toString();

                String [] question = data.split("=");

                textViewQuestion.setText(question[0].substring(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    private void vote() {
        mRef = FirebaseDatabase.getInstance().getReference();

        buttonVote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("1").setValue(user[0]);
                finish();
            }
        });

        buttonVote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("2").setValue(user[0]);
                finish();
            }

        });

        buttonVote3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("3").setValue(user[0]);
                finish();
            }
        });

        buttonVote4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("4").setValue(user[0]);
                finish();
            }
        });

        buttonVote5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("5").setValue(user[0]);
                finish();
            }
        });

        buttonIDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String email = currentFirebaseUser.getEmail();

                String [] user = email.split("@");

                mRef.child("Group ID's").child(enteredRoomName)
                        .child(enteredQuestionId)
                        .child("Question")
                        .child(textViewQuestion.getText().toString())
                        .child("I don't want to answer").setValue(user[0]);
                finish();
            }
        });
    }

    private void bindWidget() {

        textViewQuestion = findViewById(R.id.vTextViewQuestion);
        buttonIDont = findViewById(R.id.buttonIDont);
        buttonVote1 = findViewById(R.id.buttonVote1);
        buttonVote2 = findViewById(R.id.buttonVote2);
        buttonVote3 = findViewById(R.id.buttonVote3);
        buttonVote4 = findViewById(R.id.buttonVote4);
        buttonVote5 = findViewById(R.id.buttonVote5);

        Intent intent = getIntent();
        enteredQuestionId = intent.getExtras().getString("QuestionId");
        enteredRoomName = intent.getExtras().getString("RoomName");

        mRef = FirebaseDatabase.getInstance().getReference();
    }


}
