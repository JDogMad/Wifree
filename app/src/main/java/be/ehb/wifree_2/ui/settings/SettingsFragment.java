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
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                                // Get the selected language code
                                languageCode = languageCodes[which];
                                System.out.println(languageCode);

                                // Set the app language to the selected language
                                setLocale(languageCode);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btn_terms = root.findViewById(R.id.btn_terms);
        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: MAKE FRAGMENT
                // Create a new instance of the terms and conditions fragment
                TermsFragment termsFragment = new TermsFragment();

                // Begin the fragment transaction
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace the current fragment with the new fragment
                transaction.replace(R.id.container, termsFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        btn_privacy = root.findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: CHANGE URL
                String url = "https://www.freeprivacypolicy.com/live/6b33855f-f75f-46ba-b440-c6920464db61";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Start the activity
                startActivity(intent);
            }
        });

        btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
        

        return root;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());

        // Refresh the fragment to apply the language change
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .commit();
        }
    }
}
