package com.example.planningpoker.AddAndViewRoom;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.planningpoker.Question.MyQuestionAdapter;
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

public class MyRoomFragment extends Fragment {


    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private ArrayList<Question> mArrayList;
    private MyQuestionAdapter myQuestionAdapter;
    private ArrayList<String> userRooms;

    public MyRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_my_room, container, false);

        bindWidget(view);

        addDataToRecyclerView();

       return view;
    }

    private void addDataToRecyclerView() {



        getUserRooms();

        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Question questionEventListener;
                String one= null, two = null, three= null, four= null, five= null, iDont= null, expire= null;

                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    Log.d("korte", String.valueOf(forDataSnapshot.getKey()));

                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        //Log.d("korte", String.valueOf(secondFor.getKey()));

                        for (DataSnapshot thirdFor: secondFor.getChildren()){
                            //Log.d("korte", String.valueOf(thirdFor.getKey()));
                            for(DataSnapshot forth: thirdFor.getChildren()){

                                if(String.valueOf(forth.getKey()).equals("1")){
                                    one = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getKey()).equals("2")){
                                    two = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getKey()).equals("3")){
                                    three = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getKey()).equals("4")){
                                    four = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getKey()).equals("5")){
                                    five = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getKey()).equals("ExpirationDate")){
                                    expire = "Expire date: " + String.valueOf(forth.getValue());
                                }else{
                                    iDont = String.valueOf(forth.getValue());
                                }
                            }
                            questionEventListener = new Question(
                                    String.valueOf(forDataSnapshot.getKey()),
                                    String.valueOf(secondFor.getKey()),
                                    String.valueOf(thirdFor.getKey()),
                                    one, two, three, four, five, iDont, expire);

                            if (userRooms.contains(String.valueOf(forDataSnapshot.getKey()))){
                                    mArrayList.add(questionEventListener);
                                    Log.d("barack", String.valueOf(forDataSnapshot.getKey()) + " benne  van");
                            }

                        }
                    }
                }

                myQuestionAdapter = new MyQuestionAdapter(getContext(), mArrayList);
                mRecyclerView.setAdapter(myQuestionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),
                        "Error mRecyclerView"+ databaseError.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserRooms() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        String [] user = email.split("@");

        Log.d("szilva", user[0]);

       userRooms = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference().child("Admins").child(user[0]);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    userRooms.add(dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bindWidget(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);

        mArrayList = new ArrayList<>();
    }

}
