package fr.mguigourez.projet_application;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.SeekBar;
        import android.widget.Spinner;
        import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    LinearLayout layout=( LinearLayout ) findViewById(R.id.linear_view_button);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        for( int i = 0; i < 20; i++ ){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,5,0,0);
            TextView text = new TextView (this);
            text.setText(i);
            text.setLayoutParams(params);
            layout.addView(text);

        }


    }
}
