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

    //adatok hozzaadasa a RecyclerViewhez
    private void addDataToRecyclerView() {

        // a jelenlegi bejelentkezett admin szobajit kerem le
        getAdminRooms();

        //az adatbazis inicializalom a GroupID-ra
        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID");

        //a recyclerViewhez hozzadok egy layoutMenagert
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //le kerem az adatbazisbol a beirt kerdeseket
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //egy kerdes osztalyt hozok letre es ahoz a hozok letre Stringeket
                Question questionEventListener;
                String one= null, two = null,
                        three= null, four= null,
                        five= null, iDont= null,
                        expire= null, permission = null;

                // vegig megyek az adatbazison
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
                                }else
                                    permission = String.valueOf(forth.getValue());
                            }
                            //meghivom az Question osztalyt es abban lesznek egy kerdes adatai
                            questionEventListener = new Question(
                                    String.valueOf(forDataSnapshot.getKey()),
                                    String.valueOf(secondFor.getKey()),
                                    String.valueOf(thirdFor.getKey()),
                                    one, two, three, four, five, iDont, expire, permission);
                            //hozzaadom egy ArrauListhez ha mar nincs benne
                            if (userRooms.contains(String.valueOf(forDataSnapshot.getKey()))){
                                    mArrayList.add(questionEventListener);
                            }

                        }
                    }
                }

                //egy uj Question Adaptert hozok letre aminek atadom az ArrayListet
                myQuestionAdapter = new MyQuestionAdapter(getContext(), mArrayList);
                //a RecyclerViewhez hozzaadom az adapert
                mRecyclerView.setAdapter(myQuestionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //ha valami gond van akkor Toast
                Toast.makeText(getActivity(),
                        "Error mRecyclerView"+ databaseError.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //lekerel az aktualis bejelentkezett Admin kerdes szobait
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

    //ijnicializalom a szukseges elemeket
    private void bindWidget(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mArrayList = new ArrayList<>();
        rootView = view.findViewById(R.id.rootView);
    }

    // a kerdest hosszan nyomva elojonnek a lehetosegek pl Torles vagy Aktivalas
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case 121:
                //torlest vegzi ami meghivja az adapter removeItem() fuggvenyet
                myQuestionAdapter.removeItem(item.getGroupId());
                displayMessage("Item deleted....");
                return true;

            case 122:
                //a kerdes aktivalasat vegzi, ami meghivja az adapter setPermissionTrue fuggvenyet
                myQuestionAdapter.setPermissionTrue(item.getGroupId());
                displayMessage("Permission selected true...");
                return true;

                default:
                    return super.onContextItemSelected(item);

        }
    }

    //ha sikerult valamelyik kerdes modositasa akkor a kijelzo aljan kiirja, hogy mit csinalt az admin a kerdessel
    public void displayMessage(String message){
        //Snackbar az az adott szoveg kijelzese a kepernyo aljan
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
