package com.example.myplanningpokeruser.ViewQuestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private Button voteButton;
    private EditText roomNameEditText, questionIdEditText;
    private DatabaseReference mRef;
    private Date expireDate, currentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindWidget();

        vote();
    }

    private void vote() {
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomNameEditText.getText().toString().isEmpty() &&
                        questionIdEditText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter Room name and Question ID",
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

            String data = dataSnapshot.child("Group ID's")
                    .child(roomNameEditText.getText().toString())
                    .child(questionIdEditText.getText().toString())
                    .child("ExpirationDate")
                    .child("Date")
                    .getValue().toString();

                String time = dataSnapshot.child("Group ID's")
                        .child(roomNameEditText.getText().toString())
                        .child(questionIdEditText.getText().toString())
                        .child("ExpirationDate")
                        .child("Time")
                        .getValue().toString();

                String expireDateString = data  + " " + time;

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String currentDateString = formatter.format(new Date());

                try {
                    expireDate = formatter.parse(expireDateString);
                    currentDate = formatter.parse(currentDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(expireDate.after(currentDate)){
                    startActivity(new Intent(MainActivity.this, VoteActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry but the vote is over", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Users", databaseError.toString());

            }
        });
    }

    private void bindWidget() {
        voteButton = findViewById(R.id.mVoteButton);
        roomNameEditText = findViewById(R.id.mRoomNameEditText);
        questionIdEditText = findViewById(R.id.mQuestionId);
        mRef = FirebaseDatabase.getInstance().getReference();
    }
}
