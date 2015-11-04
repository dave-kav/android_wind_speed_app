package com.example.dave.myassignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
 * This is the 3rd activity and displays a result passed in from
 * the main activity. Also performs a check on the result and
 * displays an image according to how high or low the result is.
 * A dialog provides options for the user to see the image, to
 * go again or to create a notification,
 *
 */
public class Results extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        getWindow().setBackgroundDrawableResource(R.drawable.winter_wind);

        //method that displays result, units converted from
        setText();

        //create a dialog with 3 buttons
        Button options = (Button) findViewById(R.id.optionsButton);
        options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(Results.this, R.style.AlertDialog);
                dialog.setTitle(getString(R.string.optionsDialogTitle));
                dialog.setMessage(getString(R.string.optionsDialogMessage));

                //add 1st button - relaunches mainActivity
                dialog.setNegativeButton(getString(R.string.optionsDialogNegative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Results.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                //add 2nd button - create notification
                dialog.setNeutralButton(getString(R.string.optionsDialogNeutral),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //create pendingIntent for call
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(getString(R.string.notify_intent_content)));
                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                        Results.this, 0, callIntent, PendingIntent.FLAG_ONE_SHOT);

                                //Create the Builder
                                NotificationCompat.Builder myBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                                        getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(getString(R.string.notification_title))
                                        .setContentText(getString(R.string.notification_text))
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true);

                                //”Build” the Notification object
                                Notification myNotification = myBuilder.build();
                                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                nManager.notify(0, myNotification);
                            }
                        });

                //create 3rd button to display an image
                dialog.setPositiveButton(getString(R.string.optionsDialogPositive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showGraphic();
                            }
                        });

                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.show();
            }
        });
        options.getBackground().setColorFilter(Color.parseColor("#6c8af4"), PorterDuff.Mode.ADD);
    }

    @Override
    //create a pop up requesting user to confirm they wish to quit the app
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Results.this, R.style.AlertDialog);
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
                        Results.this.finish();
                    }
                });
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();
    }

    //display result information on textviews
    private void setText() {
        //get result and round off to 2 decimals
        double result = getIntent().getDoubleExtra(getString(R.string.results_result), -1);
        result = Math.round(result * 100.0) / 100.0;
        String from = ("\t" + getIntent().getStringExtra(getString(R.string.result_from)));
        String to = ("\t\t" + getIntent().getStringExtra(getString(R.string.result_to)));

        TextView fromResult = (TextView) findViewById(R.id.resultFrom);
        fromResult.setText(getString(R.string.fromResultText) + from);
        TextView toResult = (TextView) findViewById(R.id.resultTo);
        toResult.setText(getString(R.string.toResultText) + to);
        TextView finalResult = (TextView) findViewById(R.id.finalResult);
        finalResult.setText("\n\t\t" + result);
    }

    //check the windspeed and display a graphic accordingly
    private void showGraphic() {
        double result = getIntent().getDoubleExtra(getString(R.string.results_result), -1);
        int resultType = getIntent().getIntExtra("resultType", -1);
        ImageView mImageView = (ImageView) findViewById(R.id.imageView);
        TextView description = (TextView) findViewById(R.id.textView2);

        //speed converted to is kilometers
        if (resultType == 0) {
            if (result >= 0 && result < 12) {
                mImageView.setImageResource(R.mipmap.light_breeze);
                description.setText(R.string.lightBreeze);
            } else if (result >= 12 && result < 39) {
                mImageView.setImageResource(R.mipmap.strong_wind);
                description.setText(R.string.strong_wind);
            } else if (result >= 39 && result < 75) {
                mImageView.setImageResource(R.mipmap.gale);
                description.setText(R.string.gale);
            } else if (result >= 75 && result < 117) {
                mImageView.setImageResource(R.mipmap.violent_storm);
                description.setText(R.string.storm);
            } else {
                mImageView.setImageResource(R.mipmap.hurricane);
                description.setText(R.string.hurricane);
            }
        }

        //speed converted to is knots
        else if (resultType == 1) {
            if (result >= 0 && result < 7) {
                mImageView.setImageResource(R.mipmap.light_breeze);
                description.setText(R.string.lightBreeze);
            } else if (result >= 7 && result < 21) {
                mImageView.setImageResource(R.mipmap.strong_wind);
                description.setText(R.string.strong_wind);
            } else if (result >= 21 && result < 40) {
                mImageView.setImageResource(R.mipmap.gale);
                description.setText(R.string.gale);
            } else if (result >= 40 && result < 63) {
                mImageView.setImageResource(R.mipmap.violent_storm);
                description.setText(R.string.storm);
            } else {
                mImageView.setImageResource(R.mipmap.hurricane);
                description.setText(R.string.hurricane);
            }
        }

        //speed converted to is beaufort
        else {
            if (result >= 0 && result < 3) {
                mImageView.setImageResource(R.mipmap.light_breeze);
                description.setText(R.string.lightBreeze);
            } else if (result >= 3 && result < 6) {
                mImageView.setImageResource(R.mipmap.strong_wind);
                description.setText(R.string.strong_wind);
            } else if (result >= 6 && result < 9) {
                mImageView.setImageResource(R.mipmap.gale);
                description.setText(R.string.gale);
            } else if (result >= 9 && result < 12) {
                mImageView.setImageResource(R.mipmap.violent_storm);
                description.setText(R.string.storm);
            } else {
                mImageView.setImageResource(R.mipmap.hurricane);
                description.setText(R.string.hurricane);
            }
        }
    }
}