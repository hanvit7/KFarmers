package org.qblex.QbleRemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * Created by VIT_HOME on 2016-04-27.
 */
public class FragButton9 extends Fragment implements Button.OnClickListener {
    private static final String TAG = FragButton9.class.getSimpleName();

    ButtonSelectedListener9 listener;

    Button button, button2, button3, button4, button5, button6, button7, button8, button9;
    Animation buttonOn1, buttonOn2, buttonOn3, buttonOn4, buttonOn5, buttonOn6, buttonOn7, buttonOn8, buttonOn9;
    Animation buttonOff1, buttonOff2, buttonOff3, buttonOff4, buttonOff5, buttonOff6, buttonOff7, buttonOff8, buttonOff9;


    //    public void onButtonClicked1() {
//        button1 = (Button) findViewById(R.id.button);
//        buttonOn1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttonon1);
//        buttonOff1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttonoff1);
//        button1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//
//                if (action == MotionEvent.ACTION_DOWN) {
//                    return true;
//                } else if (action == MotionEvent.ACTION_UP) {
//                    //button1.playSoundEffect(SoundEffectConstants.CLICK);
//                    try {
//                        if (pinFlag[1] == 1) socket.sendString("#pin=1,0");
//                        else socket.sendString("#pin=1,1");
//                        Log.d("MB", "button[1]" + pinFlag[1]);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//    }
//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_button9, container, false);

        button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = (Button) root.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) root.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = (Button) root.findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = (Button) root.findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = (Button) root.findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = (Button) root.findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8 = (Button) root.findViewById(R.id.button8);
        button8.setOnClickListener(this);
        button9 = (Button) root.findViewById(R.id.button9);
        button9.setOnClickListener(this);

        buttonOn1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon1);
        buttonOff1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff1);
        buttonOn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon2);
        buttonOff2 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff2);
        buttonOn3 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon3);
        buttonOff3 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff3);
        buttonOn4 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon4);
        buttonOff4 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff4);
        buttonOn5 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon5);
        buttonOff5 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff5);
        buttonOn6 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon6);
        buttonOff6 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff6);
        buttonOn7 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon7);
        buttonOff7 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff7);
        buttonOn8 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon8);
        buttonOff8 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff8);
        buttonOn9 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon9);
        buttonOff9 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff9);

        return root;
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            switch (v.getId()) {
                case R.id.button:
                    Log.d(TAG, "onClick1");
                    listener.onButtonSelected9(1);
                    break;
                case R.id.button2:
                    listener.onButtonSelected9(2);
                    break;
                case R.id.button3:
                    listener.onButtonSelected9(3);
                    break;
                case R.id.button4:
                    listener.onButtonSelected9(4);
                    break;
                case R.id.button5:
                    listener.onButtonSelected9(5);
                    break;
                case R.id.button6:
                    listener.onButtonSelected9(6);
                    break;
                case R.id.button7:
                    listener.onButtonSelected9(7);
                    break;
                case R.id.button8:
                    listener.onButtonSelected9(8);
                    break;
                case R.id.button9:
                    listener.onButtonSelected9(9);
                    break;

            }
    }

    public void updataButton(int index, int status) {
        Log.d(TAG, "onButtonRecv2");
        switch (index) {
            case 1:
                if (status == 1) button.startAnimation(buttonOff1);
                else button.startAnimation(buttonOn1);
                break;
            case 2:
                if (status == 1) button2.startAnimation(buttonOff2);
                else button2.startAnimation(buttonOn2);
                break;
            case 3:
                if (status == 1) button3.startAnimation(buttonOff3);
                else button3.startAnimation(buttonOn3);
                break;
            case 4:
                if (status == 1) button4.startAnimation(buttonOff4);
                else button4.startAnimation(buttonOn4);
                break;
            case 5:
                if (status == 1) button5.startAnimation(buttonOff5);
                else button5.startAnimation(buttonOn5);
                break;
            case 6:
                if (status == 1) button6.startAnimation(buttonOff6);
                else button6.startAnimation(buttonOn6);
                break;
            case 7:
                if (status == 1) button7.startAnimation(buttonOff7);
                else button7.startAnimation(buttonOn7);
                break;
            case 8:
                if (status == 1) button8.startAnimation(buttonOff8);
                else button8.startAnimation(buttonOn8);
                break;
            case 9:
                if (status == 1) button9.startAnimation(buttonOff9);
                else button9.startAnimation(buttonOn9);
                break;
            default:
                break;
        }
    }

//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else if (action == MotionEvent.ACTION_UP) {
//            //button1.playSoundEffect(SoundEffectConstants.CLICK);
//            try {
//                if (pinFlag[1] == 1) socket.sendString("#pin=1,0");
//                else socket.sendString("#pin=1,1");
//                Log.d("MB", "button[1]" + pinFlag[1]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }
//

    public interface ButtonSelectedListener9 {
        void onButtonSelected9(int index);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ButtonSelectedListener9) {
            listener = (ButtonSelectedListener9) activity;
        }
    }



//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (activity instanceof ButtonSelectedListener){
//        listener = (ButtonSelectedListener) getActivity();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof Activity){
//            Activity  activity = (Activity) context;
//            listener = (ButtonSelectedListener) activity;
//        }

//        try {
//            Activity activity = getActivity();
//            listener = (ButtonSelectedListener) activity;
//            Toast.makeText(getActivity(), "listener", Toast.LENGTH_SHORT).show();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement HomeAddressDetailFragmentInteractionListener");
//        }
//    }


    //장비의 설정 상태 변경, 화면 방향 변경시에 다음 메소드가 호출된다.
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        TextView textCounter = (TextView) getView().findViewById(R.id.txtcounter);
//        int a = Integer.parseInt(textCounter.getText().toString());
//        outState.putInt("counter", a);//번들에 저장 키와 값으로...
    }

}
