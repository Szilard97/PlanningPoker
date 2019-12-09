package com.example.planningpoker.AddAndViewRoom;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.planningpoker.Question.MyQuestionAdapter;
import com.example.planningpoker.Question.Question;
import com.example.planningpoker.R;
import com.google.android.material.snackbar.Snackbar;
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
    private View rootView;

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

    //add data to the  RecyclerView
    private void addDataToRecyclerView() {

        //get current login admin
        getAdminRooms();

        //initialization database to the GroubId
        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID");

        //add the recyclerView a layoutMenager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get questions from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //create a Question class
                Question questionEventListener;
                String one= null, two = null,
                        three= null, four= null,
                        five= null, iDont= null,
                        expire= null, permission = null, voted = null;

                // checking the database
                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot secondFor : forDataSnapshot.getChildren()) {
                        for (DataSnapshot thirdFor: secondFor.getChildren()){
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
                                }else if(String.valueOf(forth.getKey()).equals("I don't want to answer")){
                                    iDont = String.valueOf(forth.getValue());
                                }else if (String.valueOf(forth.getKey()).equals("Permission")) {
                                    permission = String.valueOf(forth.getValue());
                                }else if(String.valueOf(forth.getValue()).equals("Voted"));
                                    voted = String.valueOf(forth.getValue());

                            }
                            //calling the Question class and there will be the questions data
                            questionEventListener = new Question(
                                    String.valueOf(forDataSnapshot.getKey()),
                                    String.valueOf(secondFor.getKey()),
                                    String.valueOf(thirdFor.getKey()),
                                    one, two, three, four, five, iDont, expire, permission, voted);

                            // adding that to an ArrauList if its not already there
                            if (userRooms.contains(String.valueOf(forDataSnapshot.getKey()))){
                                    mArrayList.add(questionEventListener);
                            }

                        }
                    }
                }

                //create a new Question adapter and adding the ArrayList to that
                myQuestionAdapter = new MyQuestionAdapter(getContext(), mArrayList);
                // adding the adapter to the RecyclerView
                mRecyclerView.setAdapter(myQuestionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if there an error then Toast
                Toast.makeText(getActivity(),
                        "Error mRecyclerView"+ databaseError.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //call the actual Admins question rooms
    private void getAdminRooms() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentFirebaseUser.getEmail();

        String [] user = email.split("@");

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

    //initializing required items
    private void bindWidget(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mArrayList = new ArrayList<>();
        rootView = view.findViewById(R.id.rootView);
    }

    // pressing the question the options will pop up
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case 121:
                //making the delete, calling the adapters removeItem() function
                myQuestionAdapter.removeItem(item.getGroupId());
                displayMessage("Item deleted....");
                return true;

            case 122:
                // activate the question which calls the adapters setPermissionTrue function
                myQuestionAdapter.setPermissionTrue(item.getGroupId());
                displayMessage("Permission selected true...");
                return true;

            /*case 123:
                myQuestionAdapter.setPermissionFalse(item.getGroupId());
                displayMessage("Permission selected true...");
                return true;*/

                default:
                    return super.onContextItemSelected(item);

        }
    }

    //if the question modification succeed the display writes what the admin done with the question
    public void displayMessage(String message){
        //Snackbar, show the text in the buttom of the display
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
