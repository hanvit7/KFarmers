package org.qblex.QbleRemo;

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
public class FragButton3 extends Fragment implements Button.OnClickListener {
    private static final String TAG = FragButton3.class.getSimpleName();

    ButtonSelectedListener3 listener;

    Button button, button2, button3;
    Animation buttonOn1, buttonOn2, buttonOn3;
    Animation buttonOff1, buttonOff2, buttonOff3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_button3, container, false);

        button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = (Button) root.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) root.findViewById(R.id.button3);
        button3.setOnClickListener(this);

        buttonOn1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon1);
        buttonOff1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff1);
        buttonOn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon2);
        buttonOff2 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff2);
        buttonOn3 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon3);
        buttonOff3 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff3);

        return root;
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            switch (v.getId()) {
                case R.id.button:
                    listener.onButtonSelected3(1);
                    break;
                case R.id.button2:
                    listener.onButtonSelected3(2);
                    break;
                case R.id.button3:
                    listener.onButtonSelected3(3);
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
            default:
                break;
        }
    }

    public interface ButtonSelectedListener3 {
        void onButtonSelected3(int index);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ButtonSelectedListener3) {
            listener = (ButtonSelectedListener3) activity;
        }
    }

    //장비의 설정 상태 변경, 화면 방향 변경시에 다음 메소드가 호출된다.
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
