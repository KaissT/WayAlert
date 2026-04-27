package edu.polytech.filrouge_tp3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class Screen3Fragment extends Fragment {
    public final static int FRAGMENT_ID = 3;
    private final String TAG = "frallo " + getClass().getSimpleName();

    private ListView listView;

    public Screen3Fragment() {
        Log.d(TAG, "Screen3Fragment created");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.type, container, false);
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
    }

    public void refreshList() {
        if (getActivity() instanceof ControlActivity && listView != null) {
            List<Accident> accidents = ((ControlActivity) getActivity()).getListeAccidents();
            Log.d(TAG, "Rafraîchissement de la liste : " + accidents.size() + " accidents trouvés.");

            ArrayAdapter<Accident> adapter = new ArrayAdapter<Accident>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    accidents
            ) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    TextView tv = v.findViewById(android.R.id.text1);
                    Accident item = getItem(position);
                    if (item != null) {
                        tv.setText(item.getDescription() + "\nDate: " + item.getDate());
                    }
                    return v;
                }
            };
            listView.setAdapter(adapter);
        }
    }
}
