package com.example.quizapp_elfadil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private RadioGroup optionsRadioGroup;
    private Button submitButton, clearButton;
    private ImageView questionImageView;

    private List<Quiz> quizList;
    private int currentQuestionIndex = 0;
    private int userScore = 0;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        clearButton = findViewById(R.id.clearButton);
        questionImageView = findViewById(R.id.questionImageView);

        quizList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        fetchQuizQuestions();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsRadioGroup.clearCheck();
            }
        });
    }

    private void fetchQuizQuestions() {
        db.collection("Quiz(MULTIPLE)")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Quiz quiz = document.toObject(Quiz.class);
                                String imageUrl = document.getString("imageUrl");
                                quiz.setImageUrl(imageUrl);
                                quizList.add(quiz);
                            }
                            Collections.shuffle(quizList);
                            displayQuestion();
                        } else {
                            Toast.makeText(QuizActivity.this, "Error fetching quiz questions", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void displayQuestion() {
        if (currentQuestionIndex < quizList.size()) {
            Quiz currentQuiz = quizList.get(currentQuestionIndex);
            questionTextView.setText(currentQuiz.getQuestion());
            option1RadioButton.setText(currentQuiz.getOption1());
            option2RadioButton.setText(currentQuiz.getOption2());
            option3RadioButton.setText(currentQuiz.getOption3());
            option4RadioButton.setText(currentQuiz.getOption4());


            Glide.with(this /* context */)
                    .load(currentQuiz.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(questionImageView);
        }
    }

    private void submitAnswer() {
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedOptionId == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            String selectedOption = selectedRadioButton.getText().toString();
            String correctAnswer = quizList.get(currentQuestionIndex).getCorrectAnswer();
            if (selectedOption.equals(correctAnswer)) {
                userScore++;
                Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect answer!", Toast.LENGTH_SHORT).show();
            }
            currentQuestionIndex++;
            optionsRadioGroup.clearCheck();
            if (currentQuestionIndex < quizList.size()) {
                displayQuestion();
            } else {
                Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                intent.putExtra("score", userScore);
                startActivity(intent);
                finish();
            }
        }
    }
}
