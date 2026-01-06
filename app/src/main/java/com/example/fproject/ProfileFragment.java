package com.example.fproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView profileImage, backIcon;
    private TextView userNameText, userEmailText;
    private Button logoutButton, editProfileButton, changePasswordButton;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Views
        profileImage = view.findViewById(R.id.profileImage);
        backIcon = view.findViewById(R.id.backIcon);
        userNameText = view.findViewById(R.id.userName);
        userEmailText = view.findViewById(R.id.userEmail);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        // Fade-in animation
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(700);
        view.startAnimation(animation);

        // Load user data
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmailText.setText(user.getEmail());
            userNameText.setText("User"); // Replace with real username if available
            profileImage.setImageResource(R.drawable.profile_placeholder);
        }

        // Back icon click
        backIcon.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());

        // Change password
        changePasswordButton.setOnClickListener(v -> showPasswordResetDialog());

        // Edit profile (placeholder)
        editProfileButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Edit Profile Coming Soon!", Toast.LENGTH_SHORT).show());

        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            requireActivity().finish(); // Return to login activity
        });
    }

    // Change password dialog
    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");

        final EditText input = new EditText(getActivity());
        input.setHint("Enter new password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String newPass = input.getText().toString().trim();

            if (newPass.length() < 6) {
                Toast.makeText(getActivity(),
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.updatePassword(newPass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                        "Password updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),
                                        "Failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
