package edu.polytech.filrouge_tp3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Screen1Fragment extends Fragment {
    private final String TAG = "frallo "+getClass().getSimpleName();

    private Notifiable notifiable;
    public final static int FRAGMENT_ID = 0;

    public Screen1Fragment() {
        Log.d(TAG,"screenFragment type 1 created");
    }


    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
            //Log.d(TAG, "Class " + requireActivity().getClass().getSimpleName() + " implements Notifiable.");
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen1, container, false);
        TextView text = view.findViewById(R.id.labelScreen1Fragment);
        text.setText(getActivity().getResources().getString(R.string.Screen1Fragment_label)+"");
        view.findViewById(R.id.button).setOnClickListener(clic -> {notifiable.onClick(3);
        });
        return view;
    }



}