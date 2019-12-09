package com.example.planningpoker.Question;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planningpoker.AddAndViewRoom.MainFragment;
import com.example.planningpoker.LoginAndRegister.LoginActivity;
import com.example.planningpoker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MyQuestionAdapter  extends RecyclerView.Adapter<MyQuestionAdapter.QuestionViewHolder> {

    private Context mContext;
    private ArrayList<Question> questionList;
    private DatabaseReference myRef;
    private boolean isTrue = false;

    public MyQuestionAdapter(Context mContext, ArrayList<Question> questionList) {
        this.mContext = mContext;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new QuestionViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.example_layout, parent, false));

    }

    //adds the data to the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.textViewRoom.setText(question.getRoomName());
        holder.textViewQuestionId.setText(question.getId());
        holder.textViewQuestion.setText(question.getQuestion());
        holder.textViewExpireDate.setText(question.getExpireDate());
        holder.textViewAnswerOne.setText(question.getAnswer1());
        holder.textViewAnswerTwo.setText(question.getAnswer2());
        holder.textViewAnswerThree.setText(question.getAnswer3());
        holder.textViewAnswerFour.setText(question.getAnswer4());
        holder.textViewAnswerFive.setText(question.getAnswer5());
        holder.textViewAnswerIDont.setText(question.getIdontAnswer());
        holder.textViewPermission.setText(question.getPermission());
        holder.voted.setText(question.getVoted());

    }

    //how many questions are in the RecyclerView
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    //initialization
    class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView textViewRoom, textViewQuestionId,
                    textViewExpireDate, voted,
                    textViewQuestion, textViewAnswerOne,
                    textViewAnswerTwo, textViewAnswerThree,
                    textViewAnswerFour, textViewAnswerFive,
                    textViewAnswerIDont, textViewPermission;
        private CardView cardView;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRoom = itemView.findViewById(R.id.textViewRoomName);
            textViewQuestionId = itemView.findViewById(R.id.cTextViewQuestionId);
            textViewQuestion =  itemView.findViewById(R.id.textViewQuestion);
            textViewAnswerOne = itemView.findViewById(R.id.textViewReplyOne);
            textViewAnswerTwo = itemView.findViewById(R.id.textViewReplTwo);
            textViewAnswerThree = itemView.findViewById(R.id.textViewReplyThree);
            textViewAnswerFour = itemView.findViewById(R.id.textViewReplyFor);
            textViewAnswerFive = itemView.findViewById(R.id.textViewReplyFive);
            textViewAnswerIDont = itemView.findViewById(R.id.IDont);
            textViewExpireDate = itemView.findViewById(R.id.textViewEndDataEndTime);
            textViewPermission = itemView.findViewById(R.id.textViewPermission);
            voted = itemView.findViewById(R.id.textViewVoted);
            cardView = itemView.findViewById(R.id.eCardView);
            cardView.setOnCreateContextMenuListener(this);
        }

        //displays the setting options you want to change
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an Option");
            menu.add(this.getAdapterPosition(), 121, 0, "Delete this item");
            menu.add(this.getAdapterPosition(), 122, 1, "Set Permission to True");
            /*menu.add(this.getAdapterPosition(), 123, 2 ,"Set Permission to False");*/


        }
    }

    //question delete from the ArrayList
    public void removeItem(int position){
        removeQuestion(questionList.get(position).getRoomName(),
                questionList.get(position).getId());
        questionList.remove(position);
        notifyDataSetChanged();
    }

    //question delete from the Database
    private void removeQuestion( String roomName,  String id) {
        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID").child(roomName).child(id);
        myRef.setValue(null);
    }

    // permission true for question
    public  void setPermissionTrue(int position){

                //adatbazis inicializalasa
                myRef = FirebaseDatabase.getInstance().getReference().child("GroupID")
                        .child(questionList.get(position)
                                .getRoomName()).child(questionList
                                .get(position).getId());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Permission Text megvaltoztatas az adatbazisban
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                if( dataSnapshot2.getKey().equals("Permission")){

                                    myRef.child(dataSnapshot1.getKey())
                                            .child(dataSnapshot2.getKey())
                                            .setValue("True");

                                    //visszalep a MainFragmentre
                                    LoginActivity.fragmentManager.beginTransaction()
                                            .replace(R.id.frameLayout, new MainFragment(),
                                                    null).commit();

                                    break;

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //Permission false for question
    /*public  void setPermissionFalse(int position){

        //adatbazis inicializalasa
        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID")
                .child(questionList.get(position)
                        .getRoomName()).child(questionList
                        .get(position).getId());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Permission Text megvaltoztatas az adatbazisban
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                        if( dataSnapshot2.getKey().equals("Permission")){

                            myRef.child(dataSnapshot1.getKey())
                                    .child(dataSnapshot2.getKey())
                                    .setValue("False");

                            //visszalep a MainFragmentre
                            LoginActivity.fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, new MainFragment(),
                                            null).commit();

                            break;

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
}
