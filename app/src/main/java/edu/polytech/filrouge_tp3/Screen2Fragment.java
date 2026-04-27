package edu.polytech.filrouge_tp3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Screen2Fragment extends Fragment {
    public final static int FRAGMENT_ID = 1;
    private final String TAG = "frallo " + getClass().getSimpleName();
    private Notifiable notifiable;

    private EditText editDescription, editDate;
    private RadioGroup radioGravite;
    private RadioGroup radioBlessure;
    private LinearLayout layoutBlessure;
    private CheckBox checkHumain;

    public Screen2Fragment() {
        Log.d(TAG, "screenFragment type 2 created");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen2, container, false);

        editDescription = view.findViewById(R.id.editDescription);
        radioGravite    = view.findViewById(R.id.radioGravite);
        checkHumain     = view.findViewById(R.id.checkHumain);
        layoutBlessure  = view.findViewById(R.id.layoutBlessure);
        radioBlessure   = view.findViewById(R.id.radioBlessure);

        checkHumain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutBlessure.setVisibility(View.VISIBLE);
            } else {
                layoutBlessure.setVisibility(View.GONE);
                radioBlessure.clearCheck();
            }
        });

        view.findViewById(R.id.btnValider).setOnClickListener(v -> {
            creerAccidentViaFactory();
        });

        return view;
    }

    private void creerAccidentViaFactory() {
        try {
            Accident accident = AccidentFactoryDetailMode.build(AccidentFactoryDetailMode.VOITURE);

            accident.setDescription(editDescription.getText().toString());
            accident.setHumain(checkHumain.isChecked());

            if (checkHumain.isChecked()) {
                int blessureId = radioBlessure.getCheckedRadioButtonId();
                if (blessureId == R.id.radioModeree) {
                    accident.setBlessure(Blessure.MODEREE);
                } else if (blessureId == R.id.radioGraveBlessure) {
                    accident.setBlessure(Blessure.GRAVE);
                }
            }

            int selectedId = radioGravite.getCheckedRadioButtonId();
            if (selectedId == R.id.radioMoyen) {
                accident.setGravite(Gravite.MOYEN);
            } else if (selectedId == R.id.radioGrave) {
                accident.setGravite(Gravite.GRAVE);
            } else {
                accident.setGravite(Gravite.FAIBLE);
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("accident", accident);

            Log.d(TAG, "Accident créé : " + accident.getDescription()
                    + " (" + accident.getGravite() + ")");
            Toast.makeText(getContext(), "Accident enregistré !",
                    Toast.LENGTH_SHORT).show();

            if (notifiable != null) {
                notifiable.onClick(FRAGMENT_ID);
            }

        } catch (Throwable throwable) {
            Log.e(TAG, "Erreur Factory : " + throwable.getMessage());
            Toast.makeText(getContext(), "Erreur : " + throwable.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
}