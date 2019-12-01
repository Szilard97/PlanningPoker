package com.example.planningpoker.AddAndViewRoom;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.planningpoker.Question.MyQuestionAdapter;
import com.example.planningpoker.Question.Question;
import com.example.planningpoker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRoomFragment extends Fragment {


    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private ArrayList<Question> mArrayList;
    private MyQuestionAdapter myQuestionAdapter;

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

        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Question questionEventListener;
                String one= null, two = null, three= null, four= null, five= null, iDont= null, expire= null;


                for(DataSnapshot forDataSnapshot: dataSnapshot.getChildren()) {
                    //Log.d("korte", String.valueOf(forDataSnapshot.getKey()));

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
                            mArrayList.add(questionEventListener);

                        }
                    }
                }

                myQuestionAdapter = new MyQuestionAdapter(getContext(), mArrayList);
                mRecyclerView.setAdapter(myQuestionAdapter);

                /*Question questionEventListener = forDataSnapshot.getValue(Question.class);
                mArrayList.add(questionEventListener);
                myQuestionAdapter = new MyQuestionAdapter(MyRoom.this, mArrayList);
                mRecyclerView.setAdapter(myQuestionAdapter);*/

                    /*try {

                    }catch (Exception e){
                     //Log.d("korte", e.toString());
                    }*/

                    /*Question questionEventListener =
                            new Question(String.valueOf(forDataSnapshot.getKey()),
                                    String.valueOf(forDataSnapshot.getKey()));*/

                //Log.d("korte", String.valueOf(forDataSnapshot.getKey()));
                //Log.d("korte", );
                //Question questionEventListener  = new Question(forDataSnapshot.getKey(), forDataSnapshot.getValue().toString(), "sdada");

                    /*try {
                        Question questionEventListener  = forDataSnapshot.getValue(Question.class);
                        Log.d("korte", String.valueOf(forDataSnapshot.getValue(Question.class)));
                        mArrayList.add(questionEventListener);
                        myQuestionAdapter = new MyQuestionAdapter(MyRoom.this, mArrayList);
                        mRecyclerView.setAdapter(myQuestionAdapter);
                    }catch (Exception e){
                        Log.d("korte", e.toString());
                    }*/
                //Log.d("korte", String.valueOf(mArrayList.size()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),
                        "Error mRecyclerView"+ databaseError.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindWidget(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);

        mArrayList = new ArrayList<>();
    }

}
