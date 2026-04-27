package edu.polytech.filrouge_tp3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class InformationsFragment extends Fragment {

    private static final String TAG = "frallo InformationsFragment";
    public final static int FRAGMENT_ID = 2;
    private Notifiable notifiable;

    private Spinner spinnerTypeAccident;
    private RadioGroup radioGroupGravite;
    private Button buttonValider;
    private String selectedType = "";

    public InformationsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Notifiable) {
            notifiable = (Notifiable) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notifiable != null) notifiable.onFragmentDisplayed(FRAGMENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informations, container, false);
        spinnerTypeAccident = view.findViewById(R.id.spinnerTypeAccident);
        radioGroupGravite  = view.findViewById(R.id.radioGroupGravite);
        buttonValider      = view.findViewById(R.id.buttonValider);

        setupSpinnerDynamic();
        setupSpinnerListener();

        buttonValider.setOnClickListener(clic -> {
            creerAccidentRapide();
        });

        return view;
    }

    private void setupSpinnerDynamic() {
        List<String> types = new ArrayList<>();
        types.add("Collision voiture - moto");
        types.add("Collision voiture - voiture");
        types.add("Renversement véhicule lourd");
        types.add("Collision avec obstacle");
        types.add("Sortie de route");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeAccident.setAdapter(adapter);
    }

    private void setupSpinnerListener() {
        spinnerTypeAccident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void creerAccidentRapide() {
        try {
            int typeFactory = AccidentFactoryDetailMode.COLLISION_VOITURE;
            if (selectedType.contains("moto")) typeFactory = AccidentFactoryDetailMode.COLLISION_MOTO;
            else if (selectedType.contains("lourd")) typeFactory = AccidentFactoryDetailMode.VEHICULE_LOURD;
            else if (selectedType.contains("obstacle")) typeFactory = AccidentFactoryDetailMode.OBSTACLE;

            Accident accident = AccidentFactoryDetailMode.build(typeFactory);
            accident.setDescription(selectedType);
            accident.setDate("Maintenant (Rapide)");
            
            int checkedId = radioGroupGravite.getCheckedRadioButtonId();
            if (checkedId == R.id.radioMoyenne) accident.setGravite(Gravite.MOYEN);
            else if (checkedId == R.id.radioGrave) accident.setGravite(Gravite.GRAVE);
            else accident.setGravite(Gravite.FAIBLE);

            if (notifiable != null) {
                notifiable.onAccidentCreated(accident);
                notifiable.onClick(3);
            }
            Toast.makeText(requireContext(), "Accident enregistré !", Toast.LENGTH_SHORT).show();

        } catch (Throwable t) {
            Log.e(TAG, "Erreur Factory", t);
        }
    }
}
