package za.co.neilson.alarm.alert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import za.co.neilson.alarm.Alarm;
import za.co.neilson.alarm.R;

/**
 * Created by swucomputer on 2017. 5. 30..
 */



public class AlarmAlertActivity2 extends Activity implements View.OnClickListener {

    //hj
    //Alarm에서 AlarmAlertActivity를 부르는건데, AlarmAlertActivity2는 ImageProblem에서 넘어오는거니까
    //Alarm 객체 다 삭제


    //private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private MathProblem mathProblem;
    private ImageProblem imageProblem;

    Vibrator vibrator;

    private boolean alarmActive;

    private TextView problemView;
    private TextView answerView;
    private String answerString;


    String check_module = "default";

    String authenticated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        IntentFilter filter_on = new IntentFilter(Intent.ACTION_SCREEN_ON);

        registerReceiver(vibrateReceiver, filter);
        registerReceiver(vibrateReceiver_on, filter_on);



        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.alarm_alert2);   //alarm_alert


        mathProblem = new MathProblem(3);

        authenticated = "no";






        try {
            answerString = String.valueOf(mathProblem.getAnswer());
            if (answerString.endsWith(".0")) {
                answerString = answerString.substring(0, answerString.length() - 2);
            }

            problemView = (TextView) findViewById(R.id.textView1_2);
            problemView.setText(mathProblem.toString());

            answerView = (TextView) findViewById(R.id.textView2_2);
            answerView.setText("?");

            ((Button) findViewById(R.id.Button0_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button1_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button2_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button3_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button4_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button5_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button6_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button7_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button8_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button9_2)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button_clear_2)).setOnClickListener(this);
            //((Button) findViewById(R.id.Button_decimal)).setOnClickListener(this);
            ((Button) findViewById(R.id.Button_minus_2)).setOnClickListener(this);

        } catch(Exception e){

        }


        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: "
                                + incomingNumber);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {

                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {

                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        // Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();

        startAlarm();

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true; //알람이 울리고 있는 상태
    }

    private void startAlarm() {

        //if (alarm.getAlarmTonePath() != "") {
            mediaPlayer = new MediaPlayer();
            //if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = { 1000, 200, 200, 200 };
                vibrator.vibrate(pattern, 0);
            //}
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                //mediaPlayer.setDataSource(this,Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        //}

    }

    public BroadcastReceiver vibrateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long[] pattern = {100,300,100,700,300,2000};

            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){ //스크린 off 이면

                if(authenticated=="no") //문제 맞추지 못한 상태에서
                    vibrator.vibrate(pattern, 0); //0:무한반복
                else {
                    vibrator.cancel();
                    finish();
                    finishAffinity();
                }


            }


        }
    };



    public BroadcastReceiver vibrateReceiver_on = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long[] pattern_origin = { 1000, 200, 200, 200 };

            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                //vibrator.cancel();
                //Toast.makeText(AlarmAlertActivity2.this, "screen on", Toast.LENGTH_SHORT).show();

                vibrator.vibrate(pattern_origin, 0);

                try {


                if(authenticated=="yes") {

    //                     Toast.makeText(AlarmAlertActivity2.this,"authen",Toast.LENGTH_SHORT).show();
                        //vibrator.vibrate(new long[]{0, 2 * DateUtils.SECOND_IN_MILLIS}, -1);


                        vibrator.cancel();


                        finish();

                        finishAffinity();
                    }

                } catch(Exception e){

                }
            }
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }

        //hj 누수 막기
        try {
            unregisterReceiver(vibrateReceiver);
            unregisterReceiver(vibrateReceiver_on);
        } catch (Exception e) {

        }

        super.onDestroy();
    }






    //키보드에서 눌리는 동작들 같음

    @Override
    public void onClick(View v) {


        try {
            if (!alarmActive)
                return;
            String button = (String) v.getTag();
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (button.equalsIgnoreCase("clear")) {
                if (answerBuilder.length() > 0) {
                    answerBuilder.setLength(answerBuilder.length() - 1);
                    answerView.setText(answerBuilder.toString());
                }
            } else if (button.equalsIgnoreCase(".")) {
                if (!answerBuilder.toString().contains(button)) {
                    if (answerBuilder.length() == 0)
                        answerBuilder.append(0);
                    answerBuilder.append(button);
                    answerView.setText(answerBuilder.toString());
                }
            } else if (button.equalsIgnoreCase("-")) {
                if (answerBuilder.length() == 0) {
                    answerBuilder.append(button);
                    answerView.setText(answerBuilder.toString());
                }
            } else {
                answerBuilder.append(button);
                answerView.setText(answerBuilder.toString());

                // 사칙연산 정답으로 알람이 꺼지는 동작
                if (isAnswerCorrect()) { //메소드 추가
                    authenticated="yes"; //연산 정답임을 표시 //이게 안먹히는 듯



                    alarmActive = false;

                    if (vibrator != null) {

                        vibrator.cancel();
                        finishAndRemoveTask();
                    }
                    try {
                        mediaPlayer.stop();
                    } catch (IllegalStateException ise) {

                    }
                    try {
                        mediaPlayer.release();
                    } catch (Exception e) {

                    }




                    this.finish();
                    finishAffinity();
                }
            }
            if (answerView.getText().length() >= answerString.length()
                    && !isAnswerCorrect()) {
                answerView.setTextColor(Color.RED);
            } else {
                answerView.setTextColor(Color.BLACK);
            }
        } catch(Exception e){

        }
    }



    //사칙연산 정답일 경우
    public boolean isAnswerCorrect() {
        boolean correct = false;
        try {
            correct = mathProblem.getAnswer() == Float.parseFloat(answerBuilder
                    .toString());
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //if(correct==true)
            //authenticated="yes";

        return correct;



    }







}
