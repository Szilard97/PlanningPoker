package com.example.planningpoker.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planningpoker.R;

import java.util.List;

public class MyQuestionAdapter  extends RecyclerView.Adapter<MyQuestionAdapter.QuestionViewHolder> {

    private Context mContext;
    private List<Question> questionList;


    public MyQuestionAdapter(Context mContext, List<Question> questionList) {
        this.mContext = mContext;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.example_layout, null);
        return new QuestionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {

        Question question = questionList.get(position);

        holder.textViewRoom.setText(question.getRoomName());

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRoom;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoom = itemView.findViewById(R.id.textViewRoomName);
        }
    }
}
