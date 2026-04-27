package edu.polytech.filrouge_tp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


/**
 * ControlActivity acts as the main orchestrator (Controller) of the application.
 * It manages the interaction between two distinct areas:
 * <ul>
 * <li><b>MenuFragment:</b> A static-like fragment (14% surface) providing navigation controls.</li>
 * <li><b>Main Content Area:</b> A dynamic zone (86% surface) displaying various ScreenFragments.</li>
 * </ul>
 *
 * <p>The activity is responsible for:
 * <ul>
 * <li>Handling fragment transactions and maintaining a clean BackStack.</li>
 * <li>Managing state persistence (e.g., current menu index and navigation flags) during configuration changes.</li>
 * <li>Providing a communication bridge between fragments via {@link Menuable} and {@link Notifiable} interfaces.</li>
 * </ul>
 * * <p>Layout distribution:
 * Screen1Fragment (dynamic) - 6/7 of the surface.
 * MenuFragment (static) - 1/7 of the surface.</p>
 *
 * @author F. Rallo
 * @version 1.1 - March 2026
 * @see Menuable
 * @see Notifiable
 */
public class ControlActivity extends AppCompatActivity implements Menuable, Notifiable {
    private static final String DATA_IS_STARTING = "sauvegarde";
    private static final String DATA_MENU_NUMBER = "num";
    private final String TAG = "frallo "+getClass().getSimpleName();
    private Fragment mainFragment;
    private MenuFragment menu;
    private boolean isStarting = true;
    private Fragment[] tabFragments = { new Screen1Fragment(), new SignalerDetail3Fragment(),
                                        new SignalerRapide2Fragment(), new Screen4Fragment(),
                                        new Screen5Fragment(), new Screen6Fragment(),
                                        new Screen7Fragment() };
    private int menuNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Start with "<#step> -->"  when not savedInstanceState
        if(savedInstanceState == null) {
             menuNumber = 0;
        }
        Log.d(TAG,"menuNumber "+menuNumber);

        //get index from MainActivity
        Intent intent = getIntent();
        if(intent!=null){
            menuNumber = intent.getIntExtra(getString(R.string.index),0);
            Log.d(TAG,"received menu#"+menuNumber);
        }

        //index to start menu  --> becomes dynamic
        Bundle args = new Bundle();
        args.putInt(getString(R.string.index), menuNumber);

        if (savedInstanceState == null) { // Très important pour éviter les superpositions au pivotement
            // 1. Charger le Menu
            menu = new MenuFragment();
            menu.setArguments(args);

            // 2. Charger le fragment de contenu initial (Screen1 par exemple)
            mainFragment = tabFragments[menuNumber];

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu, menu);
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.commit();
        }
    }




    @Override
    public void onMenuChange(int index) {
        menuNumber = index;
        //Log.d(TAG, "Menu change ==>" + menuNumber);
        mainFragment = tabFragments[index];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, mainFragment);

        if (!isStarting) {
            // Si ce n'est plus le démarrage, on veut pouvoir revenir en arrière
            transaction.addToBackStack(null);
        } else {
            // C'est le premier appel (auto), on ne l'ajoute pas à la pile car on ne veut pas pouvoir revenir en arrière
            isStarting = false;
        }

        transaction.commit();
    }

    @Override
    public void onFragmentDisplayed(int fragmentId) {
        Log.d(TAG, "onFragmentDisplayed ==>" + fragmentId);
        if(menuNumber != fragmentId){
            menuNumber = fragmentId;
            menu.setCurrentActivatedIndex(menuNumber);
        }
    }

    @Override
    public void onClick(int numFragment) {
        Log.d(TAG, "Menu " + numFragment +" has clicked!");
    }

    @Override
    public void onDataChange(int numFragment, Object data) {
        Log.d(TAG,"received "+ data + " data from fragment#"+numFragment);

    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"restaure menuNumber "+menuNumber);
        outState.putBoolean(DATA_IS_STARTING, isStarting);
        outState.putInt(DATA_MENU_NUMBER, menuNumber);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStarting = savedInstanceState.getBoolean(DATA_IS_STARTING);
        menuNumber = savedInstanceState.getInt(DATA_MENU_NUMBER);
    }


}
