package com.example.comp211.quiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class Questions_List_ActivityFragment extends Fragment {

    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;


    public Questions_List_ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_questions__list_, container, false);

        button1 = (Button) view.findViewById(R.id.q1);
        button2 = (Button) view.findViewById(R.id.q2);
        button3 = (Button) view.findViewById(R.id.q3);
        button4 = (Button) view.findViewById(R.id.q4);
        button5 = (Button) view.findViewById(R.id.q4);
        button6 = (Button) view.findViewById(R.id.q4);
        button7 = (Button) view.findViewById(R.id.q4);
        button8 = (Button) view.findViewById(R.id.q4);
        button9 = (Button) view.findViewById(R.id.q9);
        button10 = (Button) view.findViewById(R.id.q10);




        return view;
    }

    public View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button questionButton = ((Button) v);
            String guess = questionButton.getText().toString();
            int question_no = Integer.parseInt(guess);

            Intent k = new Intent(getActivity(), SingleQuiz.class);
            k.putExtra("jumpTo", question_no);
            startActivity(k);
        }
    };
}
