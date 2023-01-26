package be.ehb.wifree_2.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import be.ehb.wifree_2.LoginActivity;
import be.ehb.wifree_2.R;
import be.ehb.wifree_2.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    Button btn_languages, btn_terms, btn_privacy, btn_logout;

    String languageCode;
    String[] languageCodes = {"en", "nl", "fr"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // binding with Setting layout
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // search for button and sets a onclicklistenerer
        btn_languages = root.findViewById(R.id.btn_languages);
        btn_languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the names of the supported languages
                String[] languageNames = getResources().getStringArray(R.array.language_names);

                // Create the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.select_language)
                        .setItems(languageNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                languageCode = languageCodes[which]; // position of array and clicked
                                setLocale(languageCode); // method that will set the language
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show(); // show the new layouts
            }
        });

        // search for button and sets a onclicklistenerer
        btn_terms = root.findViewById(R.id.btn_terms);
        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.app-privacy-policy.com/live.php?token=V8VTMPVm6f8sb2VXjc3Xlear9v5y1ZZq";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Start the activity
                startActivity(intent);
            }
        });

        // search for button and sets a onclicklistenerer
        btn_privacy = root.findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.app-privacy-policy.com/live.php?token=4qvqlMoGzYnBmxu4tt22s0AQMtHWCjiz";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Start the activity
                startActivity(intent);
            }
        });

        // search for button and sets a onclicklistenerer
        btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser(); // method that log's user out
            }
        });
        

        return root;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // this logs user out of account
        Intent intent = new Intent(getActivity(), LoginActivity.class); // redirects to the loginactivity
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // method that changes the languages
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode); // new locale with code given above
        Locale.setDefault(locale); // sets default
        Configuration config = new Configuration();
        config.locale = locale; // changes the configuration to the new locale
        // ensures that the app's will be displayed using the new locale
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());

        // Very short way to change the fragment
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }
}
