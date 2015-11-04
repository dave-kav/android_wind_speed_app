package com.example.dave.myassignment1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Dave Kavanagh.
 * Completed 22/10/2015
 * david.j.kavanagh@mycit.ie
 * r00013469
 *
 * Name:        Wind Speed Calculator
 *
 * Description: Convert between km/r, knots and beaufort
 *
 * This is the 2nd activity which provides the user with options
 * to choose from in terms of wind speeds. This activity returns
 * values to the main Activity and displays them.
 *
 */
public class PickUnits extends Activity {

    int from;
    int to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_pick);
        getWindow().setBackgroundDrawableResource(R.drawable.winter_wind);

        //create button that takes spinner values and returns to mainActivity
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
                from = fromSpinner.getSelectedItemPosition();

                Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
                to = toSpinner.getSelectedItemPosition();

                Intent intent = new Intent(PickUnits.this, MainActivity.class);
                intent.putExtra("from", from).putExtra("to", to);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        okButton.getBackground().setColorFilter(Color.parseColor("#6c8af4"), PorterDuff.Mode.ADD);
    }

    @Override
    //avoids app crash due to unexpected result i.e. no values from spinners
    public void onBackPressed() {
        Intent intent = new Intent(PickUnits.this, MainActivity.class);
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("from", from);
        outState.putInt("to", to);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        from = savedInstanceState.getInt("from");
        to = savedInstanceState.getInt("to");
    }
}
