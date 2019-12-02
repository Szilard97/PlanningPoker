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
import com.example.planningpoker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyQuestionAdapter  extends RecyclerView.Adapter<MyQuestionAdapter.QuestionViewHolder> {

    private Context mContext;
private static final String TAG = "MyQuestionAdapter";
    private ArrayList<Question> questionList;
    private DatabaseReference myRef;

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

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView textViewRoom, textViewQuestionId,
                    textViewExpireDate,
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
            cardView = itemView.findViewById(R.id.eCardView);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an Option");
            menu.add(this.getAdapterPosition(), 121, 0, "Delete this item");
            menu.add(this.getAdapterPosition(), 122, 1, "Set Permission to True");
            menu.add(this.getAdapterPosition(), 123, 0, "Set Permission to False");


        }
    }

    public void removeItem(int position){
        removeQuestion(questionList.get(position).getRoomName(),
                questionList.get(position).getId());
        questionList.remove(position);
        notifyDataSetChanged();
    }

    private void removeQuestion( String roomName,  String id) {
        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID").child(roomName).child(id);
        myRef.setValue(null);
    }

    public  void setPermissionTrue(int possition){

        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID")
                .child(questionList.get(possition)
                        .getRoomName()).child(questionList
                        .get(possition).getId());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("komlo", dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public  void setPermissionFalse(int possition){

        myRef = FirebaseDatabase.getInstance().getReference().child("GroupID")
                .child(questionList.get(possition)
                        .getRoomName()).child(questionList
                        .get(possition).getId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("komlo", dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
