package com.example.blueprinthelper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    int[] elementsToBlock = {R.id.chooseSizeTypeButton,R.id.mainTextBlock};
    private int brightness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        EditText editText = (EditText) findViewById(elementsToBlock[1]);
        editText.setTypeface(Typeface.createFromAsset(getAssets(), "gost_type_B.ttf"));
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.System.canWrite(MainActivity.this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:com.example.blueprinthelper"));
                startActivity(intent);
            }
        }


        try{
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            brightness = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
            showToast(String.valueOf(brightness));
        }
        catch(Settings.SettingNotFoundException e){
            showToast("хуйня");
        }

        CheckBox repeatChkBx = (CheckBox) findViewById( R.id.blockCheckBox);
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Button button = (Button) findViewById(elementsToBlock[0]);
                EditText editText = (EditText) findViewById(elementsToBlock[1]);
                if ( isChecked )
                {
                    button.setEnabled(false);
                    editText.setEnabled(false);
                }else{
                    button.setEnabled(true);
                    editText.setEnabled(true);
                }

            }
        });
    }

    public void sizeButtonListener(View v){
        Button button = (Button) findViewById(elementsToBlock[0]);

        PopupMenu popup = new PopupMenu(MainActivity.this, button);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            EditText editText = (EditText) findViewById(elementsToBlock[1]);
            public boolean onMenuItemClick(MenuItem item) {
                editText.setTextSize(TypedValue.COMPLEX_UNIT_MM,Float.parseFloat(item.getTitle().toString()));
                return true;
            }
        });

        popup.show();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
