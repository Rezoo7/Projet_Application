package fr.mguigourez.projet_application.Listeners;

import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BarNombrePagesListener implements SeekBar.OnSeekBarChangeListener {

    private TextView affichage;


    public BarNombrePagesListener(TextView t){
        this.affichage = t;
    }

    @Override
    public void onProgressChanged(SeekBar b, int progress, boolean fromUser) {

        this.affichage.setText(""+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
