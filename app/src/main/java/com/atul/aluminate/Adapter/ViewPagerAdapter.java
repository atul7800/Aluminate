package com.atul.aluminate.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.atul.aluminate.Fragment.ChatsFragment;
import com.atul.aluminate.Fragment.UsersFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 1: return new ChatsFragment();

            case 2: return new UsersFragment();

            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;

        if (position == 0)
        {
            title="Chats";
        } else if (position == 1){
            title = "Users";
        }

        return title;


    }
}
