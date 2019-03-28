package fr.mguigourez.projet_application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private LinearLayout listeWithScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.listeWithScroll = findViewById(R.id.liste);
        Button Test_Third = new Button(this);
        Test_Third.setText("Troisième => ");
        this.listeWithScroll.addView(Test_Third);

        Test_Third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(getApplicationContext(),ThirdActivity.class);
                startActivity(details);
            }
        });



        for(int i = 0; i<50;i++){

            TextView ProgrammaticallyTextView = new TextView(this);
            ProgrammaticallyTextView.setText(" Liste "+i);
            ProgrammaticallyTextView.setTextSize(25);

            this.listeWithScroll.addView(ProgrammaticallyTextView);
        }






    }
}
