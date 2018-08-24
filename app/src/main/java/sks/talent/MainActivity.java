package sks.talent;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "sks.talenp.MESSAGE";
    private String filename = "AAfile";
    private EditText editText = null;
    private TextView showView = null;
    private Button recordButton = null;
    private File recordFile = null;
    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_msg);
        showView = (TextView) findViewById(R.id.show_msg);
        recordButton = (Button) findViewById(R.id.bt_record);
        ButtonListener b = new ButtonListener();
        recordButton.setOnClickListener(b);
        recordButton.setOnTouchListener(b);
        recordFile = new File("/mnt/sdcard", "record.amr");
    }

    public void startRecording(View view) {
        PermissionUtils.checkPermissions(this, new Runnable() {
            @Override
            public void run() {
                //权限申请成功，开始录音
                realStartRecording();
            }
        });
    }

    private void realStartRecording() {
        mediaRecorder = new MediaRecorder();
        if (recordFile.exists()) {
            recordFile.delete();
        }
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频来源为麦克风
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //默认的输出格式和编码方式
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());//指定音频输出路径

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();//开始录音
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stop_play(View view) {
        String str = null;

        if (recordFile != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
        str = showView.getText().toString();
        showView.setText(str + "\n" + "录音结束！");

        playRecordImmediately(recordFile.getAbsolutePath());
    }

    /**
     * 播放录音
     *
     * @param path 录音位置
     */
    private void playRecordImmediately(String path) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(path));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();

        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    class ButtonListener implements View.OnClickListener, View.OnTouchListener {
        private String str = null;

        public void onClick(View v) {
        }

        public boolean onTouch(View v, MotionEvent event) {

            if (v.getId() == R.id.bt_record) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stop_play(v);
                    recordButton.setBackgroundColor(Color.GREEN);
//                    recordButton.setBackgroundResource(R.drawable.green);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    str = showView.getText().toString();
                    showView.setText(str + "\n" + "开始录音……");
                    startRecording(v);
                    recordButton.setBackgroundColor(Color.YELLOW);
//                    recordButton.setBackgroundResource(R.drawable.yellow);
                }
            }
            return false;
        }
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_msg);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        String string = "My write file test!";
        FileOutputStream outputStream;
        BufferedWriter bufferdWriter = null;
        BufferedReader bufferedReader = null;
        EditText editText = (EditText) findViewById(R.id.edit_message);
        editText.setText(this.getFilesDir().toString());

        try {
            outputStream = this.openFileOutput(filename, Activity.MODE_PRIVATE);
            bufferdWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferdWriter.write(string);
            bufferdWriter.close();
            //outputStream.write(string.getBytes());
            //outputStream.close();
            //Toast.makeText(getApplicationContext(), getFileDir().toString(), Toast.LENGTH_SHORT).show();

            FileInputStream inputStream = this.openFileInput(filename);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String msg = editText.getText().toString();
        String str = showView.getText().toString();
        showView.setText(str + "\n" + msg);
        editText.setText("");
    }
}
