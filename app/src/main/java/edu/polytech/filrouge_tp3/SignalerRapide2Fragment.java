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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
/**
 * Fragment "Informations" adapté de la maquette Figma WayAlert.
 *
 * Démonstration de l'utilisation d'un Spinner peuplé DYNAMIQUEMENT
 * via un ArrayAdapter<String>, conformément au cours "Adapters"
 * (slides 22-23 : ArrayAdapter + collection remplie par programmation,
 * plutôt que via android:entries="@array/...").
 *
 * L'écouteur {@link AdapterView.OnItemSelectedListener} (slides 11, 27)
 * est également mis en place pour réagir au choix de l'utilisateur.
 */
public class SignalerRapide2Fragment extends Fragment {

    private static final String TAG = "frallo InformationsFragment";

    /** Index occupé par ce fragment dans le tableau tabFragments[] de ControlActivity. */
    public final static int FRAGMENT_ID = 2;

    /** Pont de communication Fragment -> Activity (cf. cours Fragments). */
    private Notifiable notifiable;

    private Spinner spinnerTypeAccident;
    private RadioGroup radioGroupGravite;
    private Button buttonValider;

    /** Valeur courante sélectionnée dans le Spinner. */
    private String selectedType = "";

    public SignalerRapide2Fragment() {
        Log.d(TAG, "InformationsFragment created");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName()
                    + " ne met pas en oeuvre Notifiable.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signaler_rapide_2, container, false);

        spinnerTypeAccident = view.findViewById(R.id.spinnerTypeAccident);
        radioGroupGravite  = view.findViewById(R.id.radioGroupGravite);
        buttonValider      = view.findViewById(R.id.buttonValider);

        setupSpinnerDynamic();
        setupSpinnerListener();
        setupValidationButton();
        return view;
    }

    /**
     * Peuple le Spinner DYNAMIQUEMENT via un ArrayAdapter.
     *
     * C'est la différence fondamentale avec l'approche "sans adapter"
     * (slides 7-10) où l'on déclare les items dans un string-array XML :
     * ici la collection est construite en Java et pourrait tout aussi bien
     * venir d'une base, d'un fichier JSON ou d'une API.
     */
    private void setupSpinnerDynamic() {
        // 1. Construction de la collection (pourrait venir d'ailleurs).
        List<String> typesAccident = new ArrayList<>();
        typesAccident.add("Collision voiture - moto");
        typesAccident.add("Collision voiture - voiture");
        typesAccident.add("Renversement véhicule lourd");
        typesAccident.add("Collision avec obstacle");
        typesAccident.add("Sortie de route");

        // 2. Création de l'ArrayAdapter (cf. slide 23).
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                typesAccident);

        // 3. Layout utilisé pour la liste déroulante affichée au clic.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 4. Branchement sur le Spinner.
        spinnerTypeAccident.setAdapter(adapter);
    }

    /**
     * Écouteur de sélection d'un item du Spinner (slides 11, 27).
     * onItemSelected(AdapterView<?>, View, int position, long id)
     * – position : rang de l'élément sélectionné.
     */
    private void setupSpinnerListener() {
        spinnerTypeAccident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Type sélectionné : " + selectedType + " (position=" + position + ")");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "";
                Log.d(TAG, "Aucun type sélectionné");
            }
        });
    }

    /**
     * Bouton VALIDER : affiche un Toast récapitulatif avec le type d'accident
     * (valeur fournie par le Spinner dynamique) et la gravité choisie.
     */
    private void setupValidationButton() {
        buttonValider.setOnClickListener(clic -> {
            String gravite = getSelectedGravite();
            String recap   = "Type : " + selectedType + "\nGravité : " + gravite;
            Toast.makeText(requireContext(), recap, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Validation -> " + recap);
        });
    }

    /** Récupère le libellé du RadioButton coché dans le RadioGroup. */
    private String getSelectedGravite() {
        int checkedId = radioGroupGravite.getCheckedRadioButtonId();
        if (checkedId == -1) {
            return "non renseignée";
        }
        RadioButton rb = radioGroupGravite.findViewById(checkedId);
        return rb != null ? rb.getText().toString() : "non renseignée";
    }
}
