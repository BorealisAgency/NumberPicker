package biz.borealis.numberpickerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import biz.borealis.numberpicker.ValuePicker;
import biz.borealis.numberpicker.OnValueChangeListener;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ValuePicker picker = (ValuePicker) findViewById(R.id.np);
          picker.setOnValueChangeListener(new OnValueChangeListener() {
              @Override
              public void onValueChanged(String newValue) {
                  Log.e("Value picker example", newValue);
              }
          });

        List<String> values = new LinkedList<>();
        values.add("Item1");
        values.add("Item2");
        picker.updateValues(values);
    }
}
