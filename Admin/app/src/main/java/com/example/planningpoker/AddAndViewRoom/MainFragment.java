package com.example.planningpoker.AddAndViewRoom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.planningpoker.LoginAndRegister.LoginActivity;
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
public class MainFragment extends Fragment {

    private EditText mRoomName, mQuestion, mQuestionID, mVoteNumber;
    private Button mCreateRoomButton, mViewMyRoom, dateButton, timeButton;
    private DatabaseReference myRef;
    private TextView dateTextView, timeTextView;
    private Boolean license = true;
    private static final String TAG = "MainActivity";


    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        bindWidget(view);

        createRoom();

        viewMyRoom();

        setDate();

        setTime();

        return view;
    }


    //az elemek inicializalasa
    private void bindWidget(View view) {
        mRoomName = view.findViewById(R.id.RoomNameEditText);
        mQuestion = view.findViewById(R.id.mEditTextQuestion);
        mQuestionID = view.findViewById(R.id.mQuestionEditText);
        mCreateRoomButton = view.findViewById(R.id.mCreateRoomButton);
        mViewMyRoom = view.findViewById(R.id.mViewRoom);
        dateButton = view.findViewById(R.id.mButtonData);
        timeButton = view.findViewById(R.id.mButtonTime);
        dateTextView = view.findViewById(R.id.mTextViewData);
        timeTextView = view.findViewById(R.id.mTextViewTime);
        mVoteNumber = view.findViewById(R.id.mVoteNumberPlaneTaxt);
    }

    //datum gomb listener, hogy a datumot megadjam gomb nyomasara es meg hiv egy fugvenyt ami majd vegzi tovabb
    private void setDate() {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewDate();
            }
        });
    }

    //a datumot adom hozza a textViewhez, ezt hivja meg datum gomb nyomasara
    private void mTextViewDate() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

    //ido gomb listener, hogy az idot megadjam gomb nyomasara es meghiv egy fugvenyt ami majd vegzi tovabb
    private void setTime() {

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

    }

    //az idot adom hozza a textViewhez, ezt hivja meg az ido gomb nyomasra
    private void handleTimeButton() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        //24 oras formatum-e
        boolean is24HourFormat = DateFormat.is24HourFormat(getActivity());
        //time pikker ami megjelenik
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

    //a sajat szobam amiben latom a kerdeseimet ami gomb nyomasra meghiv egy fragmentet
    private void viewMyRoom() {

        mViewMyRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragment meghivas, addToBackStack az az hogy ha megnyomom a vissza gombot akkor lepjen vissza mert ha nem akkor automatikusan kilep
                LoginActivity.fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new MyRoomFragment(),
                                null).addToBackStack(null).commit();
            }
        });
    }

    //gomb nyomasra letre hoz egy szobat, de eloszor meghiv egy fugvenyt ami le ellenorzi, hogy a kert adatok ki vannak-e toltve
    private void createRoom() {

        mCreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mellenorzes hogy ki vannak-e toltve, ha nem akkor Toast
                if(controlInputFieldsForCreateRoomButton()){

                    if(controlInputFieldsForCreateRoomButton()) {
                        addToDatabase();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enter your data", Toast.LENGTH_SHORT).show();
                    }

                    createQuestion();

                    }else {
                    Toast.makeText(getActivity(), "Please enter your data", Toast.LENGTH_SHORT).show();
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

    //hozzaadja az adatbazishoz a kert adatokat
    private void createQuestion() {
        //ellenorzom, hogy a letrehozando kerdes mar letezik-e az adatbazisban
        dataControl();

        //le kerem az aktualis bejelenkezett email cimet
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();
        //le vagom az email cim @ elotti reszet
        String [] user = email.split("@");
        //inicializalom az adatbazist
        myRef = FirebaseDatabase.getInstance().getReference();

        //ha nem letezik akkor hozzaadom a kerdest az adabazishoz
        if( license ){
            //hozzaadja a Adminshoz az aminokat amikor letrehozank egy admint ha meg nincs benne
            myRef.child("Admins").child(user[0]).child(mRoomName.getText().toString()).setValue(" ");

            //letrhozza a valasz lehetosegeket 1 2 3 4 5 es nem akar szavazni resz
            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("1").setValue(" ");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("2").setValue(" ");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("3").setValue(" ");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("4").setValue(" ");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("5").setValue(" ");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("I don't want to answer").setValue(" ");

            //hozzaadja a datumot es idot amit elozoleg be allitottunk
            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("ExpirationDate")
                    .setValue(dateTextView.getText().toString() + " " + timeTextView.getText().toString());

            // kerdes lathotosag ami alapertelmezetten False ezt az admin a kerdeseit hosszan nyomva lesz lehetosege aktivalni
            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("Permission").setValue("False");

            myRef.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())
                    .child(mQuestion.getText().toString()).child("Expected votes").setValue(mVoteNumber.getText().toString());
        }else
            Toast.makeText(getActivity(), "Please enter required fields", Toast.LENGTH_SHORT).show();

        //ki torli az input fieldeket amiket beirt az admin
        createEmptyFields();

        Toast.makeText( getActivity(),"Your room is ready", Toast.LENGTH_SHORT).show();

    }

    //ellenorzi, hogy letezik-e a hozzaadando kerdes az adatbazisban
    private void dataControl() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("GroupID").child(mRoomName.getText().toString()).child(mQuestionID.getText().toString())).exists()){
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

    // az adatok az input filedekbol
    private void createEmptyFields() {
        mRoomName.setText("");
        mQuestion.setText("");
        mQuestionID.setText("");
        dateTextView.setText("");
        timeTextView.setText("");
        mVoteNumber.setText("");
    }

    //beirtae az osszes input adatot a kerdes hozzaadashoz
    private Boolean controlInputFieldsForCreateRoomButton() {

        if(!mRoomName.getText().toString().isEmpty() && !mQuestionID.getText().toString().isEmpty()
                && !mQuestion.getText().toString().isEmpty() && !mVoteNumber.getText().toString().isEmpty()){
            return true;
        }
        //ha nem akkor ki ir egy Toast, hogy nem irta be az osszes adatot
        Toast.makeText(getActivity(), "Items marked with * are required", Toast.LENGTH_SHORT).show();
        return false;
    }

}
