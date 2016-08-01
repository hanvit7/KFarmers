package org.qblex.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * Created by VIT_HOME on 2016-05-02.
 */
public class FragButton8 extends Fragment implements Button.OnClickListener {
    private static final String TAG = FragButton8.class.getSimpleName();

    ButtonSelectedListener8 listener;

    Button button, button2, button3, button4, button5, button6, button7, button8;
    Animation buttonOn1, buttonOn2, buttonOn3, buttonOn4, buttonOn5, buttonOn6, buttonOn7, buttonOn8;
    Animation buttonOff1, buttonOff2, buttonOff3, buttonOff4, buttonOff5, buttonOff6, buttonOff7, buttonOff8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_button8, container, false);

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

        return root;
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            switch (v.getId()) {
                case R.id.button:
                    listener.onButtonSelected8(1);
                    break;
                case R.id.button2:
                    listener.onButtonSelected8(2);
                    break;
                case R.id.button3:
                    listener.onButtonSelected8(3);
                    break;
                case R.id.button4:
                    listener.onButtonSelected8(4);
                    break;
                case R.id.button5:
                    listener.onButtonSelected8(5);
                    break;
                case R.id.button6:
                    listener.onButtonSelected8(6);
                    break;
                case R.id.button7:
                    listener.onButtonSelected8(7);
                    break;
                case R.id.button8:
                    listener.onButtonSelected8(8);
                    break;
                default:
                    break;
            }
    }

    public void updataButton(int index, int status) {
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
            default:
                break;
        }
    }

    public interface ButtonSelectedListener8 {
        void onButtonSelected8(int index);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ButtonSelectedListener8) {
            listener = (ButtonSelectedListener8) activity;
        }
    }

    //장비의 설정 상태 변경, 화면 방향 변경시에 다음 메소드가 호출된다.
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        TextView textCounter = (TextView) getView().findViewById(R.id.txtcounter);
//        int a = Integer.parseInt(textCounter.getText().toString());
//        outState.putInt("counter", a);//번들에 저장 키와 값으로...
    }

}
