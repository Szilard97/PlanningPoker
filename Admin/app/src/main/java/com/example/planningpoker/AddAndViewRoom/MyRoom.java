package com.example.planningpoker.AddAndViewRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.planningpoker.Question.MyQuestionAdapter;
import com.example.planningpoker.Question.Question;
import com.example.planningpoker.R;

import java.util.ArrayList;

public class MyRoom extends AppCompatActivity {

    RecyclerView recyclerView1;
    MyQuestionAdapter adapter;

    ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        bindWidget();

        Question q1 = new Question("nem");

        questionList.add(q1);

        adapter = new MyQuestionAdapter(this, questionList);
    }

    private void bindWidget() {
        questionList = new ArrayList<>();
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
    }
}
