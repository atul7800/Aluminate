package com.atul.aluminate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.atul.aluminate.Fragment.ChatsFragment;
import com.atul.aluminate.Fragment.ProfileFragment;
import com.atul.aluminate.Fragment.UsersFragment;
import com.atul.aluminate.Model.Users;
import com.bumptech.glide.Glide;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends Fragment {

    FirebaseAuth mAuth;
    Toolbar toolbar;

    CircleImageView imageView;
    TextView username;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_chat, container, false);



        mAuth = FirebaseAuth.getInstance();

        //casting of the views
        imageView = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.usernameonmainactivity);



        toolbar = view.findViewById(R.id.toolbarmain);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                username.setText(users.getUsername()); // set the text of the user on textivew in toolbar

                if (users.getImageURL() != null)
                {
                    if (users.getImageURL().equals("default")) {

                        imageView.setImageResource(R.drawable.user);
                    } else {

                        Glide.with(getActivity()).load(users.getImageURL()).into(imageView);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return null;
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;
        ArrayList<String> titles;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

            this.fragments = new ArrayList<>();
            this. titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment (Fragment fragment, String title) {

            fragments.add(fragment);
            titles.add(title);



        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }


    //@Override
//    public void onDetach() {
//        super.onDetach();
//
//        getActivity().getFragmentManager().popBackStack();
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.logout) {
//
//            mAuth.signOut();
//            finish();
//            return  true;
//
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void Status (final String status) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);



    }

    @Override
    public void onResume() {
        super.onResume();
        Status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        Status("offline");
    }


}