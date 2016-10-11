# NumberPicker
![demo](http://i.imgur.com/6uPlzny.gif)

**Installation**

Add the following dependency to your module's build.gradle:

    compile 'biz.borealis.numberpicker:NumberPicker:1.0.1'

**How to use it**

    <biz.borealis.numberpicker.NumberPicker
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
        
**XML Attributes**

    <biz.borealis.numberpicker.NumberPicker
        ...
        xmlns:app="http://schemas.android.com/apk/res-auto"
        app:np_min_number="1"
        .../>

| Name   |      Type      |  Default |
|----------|:-------------:|------:|
|np_min_number|integer|0|
|np_max_number|integer|100|
|np_text_size_selected|dimension|48sp|
|np_text_size|dimension|36sp|
|np_text_color_selected|color|#d6000000|
|np_text_color|color|#8a000000|
|np_fade_text_color|boolean|true|
|np_animate_text_size|boolean|true|

**Interface**

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.np);
        numberPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChanged(int newValue) {
            }
        });
