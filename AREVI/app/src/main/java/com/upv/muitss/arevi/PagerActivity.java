package com.upv.muitss.arevi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.ArgbEvaluator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.views.ARConfigurationFragmentView;
import com.upv.muitss.arevi.views.AppConfigurationFragmentView;
import com.upv.muitss.arevi.views.CustomViewPager;
import com.upv.muitss.arevi.views.ProfileConfigurationFragmentView;


public class PagerActivity extends AppCompatActivity {

    private CustomViewPager mViewPager;
    private ImageView[] indicators;
    private ImageButton mNextBtn, mBackBtn;
    private Button mFinishBtn, mSkipBtn;
    private int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_pager);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ImageView zero = findViewById(R.id.intro_indicator_0);
        ImageView one = findViewById(R.id.intro_indicator_1);
        ImageView two = findViewById(R.id.intro_indicator_2);

        AppState.getInstance().setUser(new User());
        AppState.getInstance().setUserInfo(new UserInfo());

        mNextBtn = findViewById(R.id.intro_btn_next);
        mBackBtn = findViewById(R.id.intro_btn_back);
        mFinishBtn = findViewById(R.id.intro_btn_finish);
        mSkipBtn = findViewById(R.id.intro_btn_skip);

        indicators = new ImageView[]{zero, one, two};

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        boolean isDark = Utils.isDarkTheme();

        final int color1 = isDark ? ContextCompat.getColor(this, R.color.colorPrimaryDark) : ContextCompat.getColor(this, R.color.colorPrimary);
        final int color2 = isDark ? ContextCompat.getColor(this, R.color.colorOnPrimaryDark) : ContextCompat.getColor(this, R.color.colorOnPrimary);
        final int color3 = isDark ? ContextCompat.getColor(this, R.color.colorOnSecondaryDark) : ContextCompat.getColor(this, R.color.colorOnSecondary);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.setPagingEnabled(true);

                if (position == 2 && validateProfileFormError()) {
                    mViewPager.setPagingEnabled(false);
                    page = page == 2 ? page - 1 : page;
                    mViewPager.setCurrentItem(page, true);
                }
                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setPagingEnabled(true);
                Fragment fragment = mSectionsPagerAdapter.getFragment(position);
                if (fragment != null) {
                    fragment.onResume();
                }

                switch (position) {
                    case 0:
                        Utils.hideKeyboardFrom(getBaseContext(), mViewPager);
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        if (validateProfileFormError()){
                            position = position - 1;
                            mViewPager.setCurrentItem(page, true);
                            mViewPager.setPagingEnabled(false);
                            break;
                        }
                        Utils.hideKeyboardFrom(getBaseContext(), mViewPager);
                        mViewPager.setBackgroundColor(color3);
                        break;
                }
                page = position;
                updateIndicators(page);

                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                mBackBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mSkipBtn.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (page == 1) {
                    mViewPager.setFocusable(true);
                    mViewPager.setFocusableInTouchMode(true);
                    mViewPager.requestFocus();
                    mViewPager.setFocusable(false);
                    mViewPager.setFocusableInTouchMode(false);
                }
            }
        });
    }

    public void onNextButtonClick(View view) {
        if (page == 1){
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();

            boolean notComplete = validateProfileFormError();

            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
            if (notComplete) return;
        }
        page += 1;
        mViewPager.setCurrentItem(page, true);
    }
    public void onSkipButtonClick(View view) {
        finish();
    }
    public void onFinishButtonClick(View view) {
        Intent toMain = new Intent(PagerActivity.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }
    public void onBackButtonClick(View view) {
        page -= 1;
        mViewPager.setCurrentItem(page, true);
    }

    public boolean validateProfileFormError(){
        AppState appState = AppState.getInstance();

        Utils.validateForm(findViewById(R.id.fragment_pager_profile_config_form));

        return !appState.getUser().isValidState() || !appState.getUserInfo().isValidState();
    }

    public void onSetAppFontSizeClick(View view) {
        RadioGroup rgTheme = findViewById(R.id.APP_THEME_RADIO);
        int rbThemeId = rgTheme.getCheckedRadioButtonId();

        RadioGroup rgThemeSize = findViewById(R.id.APP_THEME_SIZE_RADIO);
        int rbThemeSizeId = rgThemeSize.getCheckedRadioButtonId();

        if (rbThemeId == R.id.APP_THEME) {
            switch (rbThemeSizeId) {
                case R.id.APP_THEME_MEDIUM_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_THEME_MEDIUM_FONT_SIZE);
                    break;
                case R.id.APP_THEME_LARGE_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_THEME_LARGE_FONT_SIZE);
                    break;
                case R.id.APP_THEME_DEFAULT_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_THEME_DEFAULT_FONT_SIZE);
                    break;
            }
        }
        else if (rbThemeId == R.id.APP_THEME_DARK) {
            switch (rbThemeSizeId) {
                case R.id.APP_THEME_MEDIUM_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_DARK_THEME_MEDIUM_FONT_SIZE);
                    break;
                case R.id.APP_THEME_LARGE_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_DARK_THEME_LARGE_FONT_SIZE);
                    break;
                case R.id.APP_THEME_DEFAULT_FONT_SIZE:
                    Utils.saveTheme(this, Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE);
                    break;
            }
        }
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;
        private SparseArray<String> mFragmentTags;
        private FragmentManager mFragmentManager;

        SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
            mFragmentTags = new SparseArray<>();
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AppConfigurationFragmentView.newInstance(position + 1);
                case 1:
                    return ProfileConfigurationFragmentView.newInstance(position + 1);
                case 2:
                    return ARConfigurationFragmentView.newInstance(position + 1);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }
    }


}
