package com.example.dave.myassignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * This is the main activity
 *
 */
public class MainActivity extends Activity {

    private String[] speeds;
    private int a = -1, b = -1;
    private double speed = -1;
    private TextView fromView;
    private TextView toView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.winter_wind);

        Resources res = getResources();
        speeds = res.getStringArray(R.array.unitArray);

        //This chunk creates a button that launches the 2nd activity
        Button unitButton = (Button) findViewById(R.id.button1);
        unitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent pickUnits = new Intent(MainActivity.this, PickUnits.class);
                startActivityForResult(pickUnits, 1);   //activity launched with a result expected.
            }
        });
        unitButton.getBackground().setColorFilter(Color.parseColor("#6c8af4"), PorterDuff.Mode.ADD);

        //This section creates the button which perform the covnversion and launches the 3rd activity
        Button convertButton = (Button) findViewById(R.id.button2);
        convertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText speedField = (EditText) findViewById(R.id.editTextSpeed);
                String speedString = speedField.getText().toString();
                //if user doesn't enter speed, a warning is displayed
                if (speedString.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog).create();
                    alertDialog.setTitle(getString(R.string.speedDialogTitle));
                    alertDialog.setMessage(getString(R.string.speedDialogMessage));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.speedDialogButton),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.show();
                } else {
                    speed = Double.parseDouble(speedString);
                    //if user has not picked any speed units a warning is displayed
                    if (a < 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog).create();
                        alertDialog.setTitle(getString(R.string.noUnitDialogTitle));
                        alertDialog.setMessage(getString(R.string.noUnitDialogMsg));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.unitDialogButton),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setIcon(R.mipmap.ic_launcher);
                        alertDialog.show();
                    }
                    else {
                        double result = convert();
                        //if user has chosen same units for from and too, warning is displayed
                        if (result < 0) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog).create();
                            alertDialog.setTitle(getString(R.string.unitDialogTitle));
                            alertDialog.setMessage(getString(R.string.unitDialogMessage));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.unitDialogButton),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            alertDialog.show();
                            //if all is in order, launch results activity, passing extras as required via the intent
                        } else {
                            Intent resultScreen = new Intent(MainActivity.this, Results.class);
                            resultScreen.putExtra("result", result);
                            resultScreen.putExtra("from", speeds[a]);
                            resultScreen.putExtra("to", speeds[b]);
                            resultScreen.putExtra("resultType", b);
                            startActivity(resultScreen);
                            finish();
                        }
                    }
                }
            }
        });
        convertButton.getBackground().setColorFilter(Color.parseColor("#6c8af4"), PorterDuff.Mode.ADD);
    }

    @Override
    //create a pop up requesting user to confirm they wish to quit the app
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        dialog.setTitle(R.string.QuitDialogTitle);
        dialog.setMessage(R.string.QuitDialogMessage);

        dialog.setNegativeButton((getString(R.string.QuitDialogNegative)),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.setPositiveButton((getString(R.string.QuitDialogPositive)),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });

        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();
    }

    @Override
    //receives result from pickUnits (2nd) activity, updates textViews accordingly
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode == RESULT_OK) {
           fromView = (TextView) findViewById(R.id.fromPanel);
           toView = (TextView) findViewById(R.id.toPanel);

           a = data.getIntExtra("from", -1);
           fromView.setText(speeds[a]);

           b = data.getIntExtra("to", -1);
           toView.setText(speeds[b]);
       }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (a>-1)
            outState.putInt("a", a);
        if (b>-1)
            outState.putInt("b", b);
        outState.putDouble("speed", speed);
        outState.putStringArray("speeds", speeds);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getInt("a") > -1) {
            a = savedInstanceState.getInt("a");
            fromView = (TextView) findViewById(R.id.fromPanel);
            fromView.setText(speeds[a]);
        }
        if (savedInstanceState.getInt("b") > -1) {
            b = savedInstanceState.getInt("b");
            toView = (TextView) findViewById(R.id.toPanel);
            toView.setText(speeds[b]);
        }

        speed = savedInstanceState.getDouble("speed");
        speeds = savedInstanceState.getStringArray("speeds");
    }

    /**
     * Coverts between various speed units
     * 0 = Km/hr
     * 1 = Knots
     * 2 = Beaufort
     */
    protected double convert() {
        //if units are the same, if -1 is returned, dialog will display warning user of
        //same speeds picked for from and too
        if (a == b) {
            return -1;
        }

        //km/hr -> knots
        else if (a == 0 && b == 1) {
            return (speed * 0.539957);
        }

        //knots -> km/hr
        else if (a == 1 && b == 0) {
            return (speed * 1.852);
        }

        //km/hr -> beaufort
        else if (a == 0 && b == 2) {
            convertToBeaufort();
            return speed;
        }

        //beaufort -> km/hr
        else if (a == 2 && b == 0) {
            convertFromBeaufort();
            return  speed;
        }

        //knots -> beaufort
        else if (a == 1 && b == 2) {
            //convert to km/hr first
            speed = speed * 1.852;
            convertToBeaufort();
            return speed;
        }

        //beaufort -> knots
        else if (a == 2 && b == 1) {
            //convert to km/hr first
            convertFromBeaufort();
            //convert to knots
            speed = speed * 0.539957;
        }
        return speed;
    }

    //takes a speed in km/hr and assigns it it's appropriate beaufort value
    private void convertToBeaufort() {
        if (speed < 1 && speed <5)
            speed = 0;

        else if (speed < 5)
            speed = 1;

        else if (speed > 5 && speed <= 11)
            speed = 2;

        else if (speed > 11 && speed <= 19)
            speed = 3;

        else if (speed > 19 && speed <= 29)
            speed = 4;

        else if (speed > 29 && speed <= 39)
            speed = 5;

        else if (speed > 39 && speed <= 50)
            speed = 6;

        else if (speed > 50 && speed <= 61)
            speed = 7;

        else if (speed > 61 && speed <= 74)
            speed = 8;

        else if (speed > 74 && speed <= 87)
            speed = 9;

        else if (speed > 87 && speed <= 102)
            speed = 10;

        else if (speed > 102 && speed <= 118)
            speed = 11;

        else
            speed = 12;
    }

    //converts from beaufort to km/hr - midpoint of upper and lower bounds in scale
    private void convertFromBeaufort() {
        if (speed == 0)
            speed = 1;

        else if (speed == 1)
            speed = 3;

        else if (speed == 2)
            speed = 9;

        else if (speed == 3)
            speed = 15;

        else if (speed == 4)
            speed = 45;

        else if (speed == 5)
            speed = 35;

        else if (speed == 6)
            speed = 45;

        else if (speed == 7)
            speed = 56;

        else if (speed == 8)
            speed = 68;

        else if (speed == 9)
            speed = 81;

        else if (speed == 10)
            speed = 95;

        else if (speed == 11)
            speed = 110;

        else
            speed = 119;
    }
}
