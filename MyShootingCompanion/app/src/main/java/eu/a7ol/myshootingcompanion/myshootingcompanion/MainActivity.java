package eu.a7ol.myshootingcompanion.myshootingcompanion;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    boolean startRunFive = false;
    Handler handler = new Handler();
    int delay = 1000; //milliseconds
    final int SAMPLE_RATE = 44100; // The sampling rate
    boolean mShouldContinue = true;
    boolean blocking = false;
    int iddd = 0;
    int reeksNr, shotInReeks;
    long frstTime, lstTime, intrTime, starttime;
    String timeDif;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        System.loadLibrary("opencv_java3");
        Log.println(Log.INFO, "tagg125", "testing");

        Log.println(Log.INFO, "tagg125", "testing0 " + pref.getInt("soundlv", 3856561));
        /*editor.putInt("soundlv", 3856561); // Storing integer
        editor.commit();*/
        Log.println(Log.INFO, "tagg125", "testing" + pref.getInt("soundlv", 3856561));

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestRecordAudioPermission();
        int bufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();


        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);


        // You don't have to do anything here if you just
        Context context = MainActivity.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
        builder2.setTitle("Information");
        builder2.setMessage("Please enjoy this app...\nPress start to start your run!");

        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder2.show();

    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_item:

                if (startRunFive) {
                    Toast.makeText(this, "Already Running!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "Starting up!...", Toast.LENGTH_SHORT).show();
                    SharedPreferences preft55 = getApplicationContext().getSharedPreferences("MyPref", 0);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "GO!", Toast.LENGTH_SHORT).show();
                            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 500);

                            mShouldContinue = true;
                            starttime = System.currentTimeMillis();
                            recordAudio();
                        }
                    }, preft55.getInt("timeforbeep", 5000));
            /*
            handler.postDelayed(new Runnable(){
                public void run(){
                   //
                    handler.postDelayed(this, delay);
                }
            }, delay);*/
                    startRunFive = true;
                }
                return true;
            case R.id.item2:
                Toast.makeText(this, "Coming soon! #2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);

                return true;
            case R.id.item3:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set sound level");
                builder.setMessage("Please set the appropriate sound level.\nThis number can be verry high 30 or even 40 million.");
                SharedPreferences preft3 = getApplicationContext().getSharedPreferences("MyPref", 0);

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setText("" + preft3.getInt("soundlv", 3800000));
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putInt("soundlv", Integer.parseInt(m_Text)); // Storing integer
                        editor.commit();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            case R.id.item4:
                SharedPreferences preft4 = getApplicationContext().getSharedPreferences("MyPref", 0);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Set shots per run");
                builder2.setMessage("Please set the appropriate shots per run.");

                final EditText input2 = new EditText(this);
                input2.setText("" + preft4.getInt("shotperrun", 5));
                input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                input2.setRawInputType(Configuration.KEYBOARD_12KEY);
                builder2.setView(input2);

                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text2 = input2.getText().toString();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putInt("shotperrun", Integer.parseInt(m_Text2)); // Storing integer
                        editor.commit();


                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder2.show();
                return true;
            case R.id.item5:
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

                if (iddd % pref.getInt("shotperrun", 5) == 0) {
                    data.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Please finish your current run!", Toast.LENGTH_SHORT).show();

                }

                return true;
            case R.id.item6:
                SharedPreferences preft5 = getApplicationContext().getSharedPreferences("MyPref", 0);

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("Set time before beep");
                builder3.setMessage("Please set the appropriate time before the beep sound in ms. 1000ms is 1 second.");

                final EditText input3 = new EditText(this);
                input3.setText("" + preft5.getInt("timeforbeep", 5000));
                input3.setInputType(InputType.TYPE_CLASS_NUMBER);
                input3.setRawInputType(Configuration.KEYBOARD_12KEY);
                builder3.setView(input3);

                builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text3 = input3.getText().toString();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putInt("timeforbeep", Integer.parseInt(m_Text3)); // Storing integer
                        editor.commit();


                    }
                });
                builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder3.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void recordAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

                // buffer size in bytes
                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                    bufferSize = SAMPLE_RATE * 2;
                }

                short[] audioBuffer = new short[bufferSize / 2];

                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

                if (record.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.e("", "Audio Record can't initialize!");
                    return;
                }
                record.startRecording();

                Log.v("", "Start recording");

                long shortsRead = 0;
                while (mShouldContinue) {
                    int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
                    shortsRead += numberOfShort;
                    int accu = 0;
                    for (int i = 0; i < audioBuffer.length - 1; i++) {
                        accu += Math.abs(audioBuffer[i]);
                    }

                    int amp = accu / (128 * audioBuffer.length);
                    Log.println(Log.INFO, "tagg125", "" + Short.toString((audioBuffer[12])) + " The length is: " + audioBuffer.length + " reads are: " + Long.toString(shortsRead) + " amplitude is: " + Integer.toString(amp) + " accu is: " + accu);


                    if (accu > pref.getInt("soundlv", 3800000) && !blocking) {
                        iddd += 1;
                        int colorBGC = Color.rgb(255, 255, 255);
                        int picture = R.drawable.target;

                        if (iddd % pref.getInt("shotperrun", 5) == 1) {
                            frstTime = System.currentTimeMillis();
                        }

                        if (iddd % pref.getInt("shotperrun", 5) != 1) {
                            shotInReeks += 1;
                            intrTime = System.currentTimeMillis() - intrTime;
                            timeDif = "" + Long.toString(intrTime) + " ms";
                            intrTime = System.currentTimeMillis();
                            picture = R.drawable.target;
                        } else {
                            reeksNr += 1;
                            shotInReeks = 1;
                            intrTime = System.currentTimeMillis();
                            timeDif = "First round (" + Long.toString(System.currentTimeMillis() - starttime) + " ms)";
                            colorBGC = Color.rgb(235, 235, 242);
                            picture = R.drawable.start;

                        }


                        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        System.out.println(timestamp);


                        if (iddd % pref.getInt("shotperrun", 5) == 0) {
                            mShouldContinue = false;
                            blocking = true;
                            lstTime = System.currentTimeMillis() - starttime;

                            data.add(new DataModel(
                                    "Shot #" + (iddd) + " (R:" + (reeksNr) + " S:" + (shotInReeks) + ")",
                                    "" + timestamp + "\n(Total time " + lstTime + " ms.)",
                                    iddd,
                                    R.drawable.finish,
                                    colorBGC,
                                    timeDif
                            ));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter.notifyDataSetChanged();
                                    // data.clear();
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Call smooth scroll
                                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                        }
                                    });
                                   /* DialogFragment dialog = new MyDialogFragment(lstTime, data);
                                    dialog.show(getFragmentManager(), "MyDialogFragmentTag");*/
//

                                    // Use the Builder class for convenient dialog construction
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Completed!");
                                    builder.setMessage("You took " + Long.toString((lstTime / 1000)) + " seconds. \nOr " + Long.toString((lstTime)) + " milliseconds.\n\nPress start again to log your next run!");
                                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            data.clear();


                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // You don't have to do anything here if you just
                                            // want it dismissed when clicked
                                        }
                                    }).setNeutralButton("Add Points", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        public void onClick(DialogInterface dialog, final int id) {
                                            // You don't have to do anything here if you just
                                            Context context = MainActivity.this;
                                            LinearLayout layout = new LinearLayout(context);
                                            layout.setOrientation(LinearLayout.HORIZONTAL);

                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                            builder2.setTitle("Enter your points");
                                            builder2.setMessage("Please enter your points\nYou should do this in one number\nDo not add spaces.");

                                            final EditText input2 = new EditText(MainActivity.this);

                                            input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            input2.setRawInputType(Configuration.KEYBOARD_12KEY);
                                            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pref.getInt("shotperrun", 5))});

                                            builder2.setView(input2);

                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String m_Text2 = input2.getText().toString();
                                                    int score = 0;//
                                                    for (int i = 0; i < m_Text2.length(); i++) {
                                                        score += Integer.parseInt(String.valueOf(m_Text2.charAt(i)));
                                                    }
                                                    int colorBGCC = Color.rgb(255, 255, 255);

                                                    data.set(iddd - 1, new DataModel(
                                                            "Shot #" + (iddd) + " (R:" + (reeksNr) + " S:" + (shotInReeks) + ")",
                                                            "" + timestamp + "\n(Total time " + lstTime + " ms)\n\n" + "Score: " + score + "\nGem. Score: " + score / m_Text2.length() + "",
                                                            iddd,
                                                            R.drawable.finish,
                                                            colorBGCC,
                                                            timeDif
                                                    ));
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                            builder2.show();
                                        }
                                    });

                                    // Create the AlertDialog object and return it
                                    builder.create();
                                    builder.show();
                                    //
                                    startRunFive = false;
                                    blocking = false;

                                }
                            });

                        } else {
                            lstTime = System.currentTimeMillis() - starttime;
                            data.add(new DataModel(
                                    "Shot #" + (iddd) + " (R:" + (reeksNr) + " S:" + (shotInReeks) + ")",
                                    "" + timestamp + "\n(Total time " + lstTime + " ms.)",
                                    iddd,
                                    picture,
                                    colorBGC,
                                    timeDif
                            ));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call smooth scroll
                                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                    }
                                });

                            }
                        });


                    }
                }

                record.stop();
                record.release();

                Log.v("", String.format("Recording stopped. Samples read: %d", shortsRead));
            }
        }).start();
    }

    private void requestRecordAudioPermission() {
        //check API version, do nothing if API version < 23!
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Activity", "Granted!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Activity", "Denied!");
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}