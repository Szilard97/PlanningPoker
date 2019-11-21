package com.example.planningpoker.AddAndViewRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.planningpoker.Question.Question;
import com.example.planningpoker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText mRoomName, mQuestion, mQuestionID;
    private Button mCreateRoomButton, mViewMyRoom, dateButton, timeButton;
    private DatabaseReference myRef;
    private TextView dateTextView, timeTextView;
    private ArrayList<Question> mQuestions = new ArrayList<Question>();
    private Boolean license = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindWidget();

        createRoom();

        viewMyRoom();

        setData();

        setTime();
    }

    private void setData() {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewTime();
            }
        });
    }

    private void mTextViewTime() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("dd/MM/yyyy", calendar1).toString();

                dateTextView.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void setTime() {

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

    }

    private void handleTimeButton() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("HH:mm", calendar1).toString();
                timeTextView.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    private void viewMyRoom() {

        mViewMyRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, MyRoom.class));
            }
        });
    }

    private void createRoom() {

        mCreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlInputFieldsForCreateRoomButton()){

                    if(controlInputFieldsForCreateRoomButton()) {
                        addToDatabase();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter your data", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void addToDatabase() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        String [] user = email.split("@");

        myRef = FirebaseDatabase.getInstance().getReference();

        dataControl();
    }

    private void createQuestion() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        String [] user = email.split("@");

        myRef = FirebaseDatabase.getInstance().getReference();

        if( license ){
            myRef.child("Admins").child(user[0]).child(mRoomName.getText().toString()).setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("ExpirationDate").child("Time").setValue(timeTextView.getText().toString());

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("ExpirationDate").child("Date").setValue(dateTextView.getText().toString());

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("1").setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("2").setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("3").setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("4").setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("5").setValue("");

            myRef.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child("Question").child(mQuestion.getText().toString()).child("I don't want to answer").setValue("");
        }

        createEmptyFields();

        Toast.makeText( getApplicationContext(),"Your room is ready", Toast.LENGTH_SHORT).show();

    }

    private void dataControl() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("Group ID's").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())).exists()){
                    license = false;
                    Log.d("alma", mRoomName.getText().toString());
                }
                else{
                    license = true;
                }
                createQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                license = true;
            }
        });
    }

    private void createEmptyFields() {
        mRoomName.setText("");
        mQuestion.setText("");
        mQuestionID.setText("");
        dateTextView.setText("");
        timeTextView.setText("");
    }

    private Boolean controlInputFieldsForCreateRoomButton() {

        if(!mRoomName.getText().toString().isEmpty() && !mQuestionID.getText().toString().isEmpty()
                && !mQuestion.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void bindWidget() {
        mRoomName = findViewById(R.id.RoomNameEditText);
        mQuestion = findViewById(R.id.mEditTextQuestion);
        mQuestionID = findViewById(R.id.mQuestionEditText);
        mCreateRoomButton = findViewById(R.id.mCreateRoomButton);
        mViewMyRoom = findViewById(R.id.mViewRoom);
        dateButton = findViewById(R.id.mButtonData);
        timeButton = findViewById(R.id.mButtonTime);
        dateTextView = findViewById(R.id.mTextViewData);
        timeTextView = findViewById(R.id.mTextViewTime);
    }
}
