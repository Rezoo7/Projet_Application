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

        Button bouton_tmp = new Button(this);
        bouton_tmp.setText("Troisième => ");

        bouton_tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent details = new Intent(getApplicationContext(), ThirdActivity.class);
                    startActivity(details);
                }
            });

        this.listeWithScroll.addView(bouton_tmp);






    }
}
