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
public class FragButton1 extends Fragment implements Button.OnClickListener {
    private static final String TAG = FragButton1.class.getSimpleName();

    ButtonSelectedListener1 listener;

    Button button;
    Animation buttonOn1;
    Animation buttonOff1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_button1, container, false);

        button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(this);

        buttonOn1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonon1);
        buttonOff1 = AnimationUtils.loadAnimation(getActivity(), R.anim.buttonoff1);

        return root;
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            switch (v.getId()) {
                case R.id.button:
                    listener.onButtonSelected1(1);
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
            default:
                break;
        }
    }

    public interface ButtonSelectedListener1 {
        void onButtonSelected1(int index);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ButtonSelectedListener1) {
            listener = (ButtonSelectedListener1) activity;
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
