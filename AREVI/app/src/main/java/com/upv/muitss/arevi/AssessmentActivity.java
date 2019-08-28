package com.upv.muitss.arevi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.views.AssessmentSUSFragmentView;
import com.upv.muitss.arevi.views.CustomViewPager;

public class AssessmentActivity extends AppCompatActivity {
    private CustomViewPager mViewPager;
    private ImageView[] indicators;
    private ImageButton mNextBtn, mBackBtn;
    private Button mFinishBtn, mSkipBtn;
    private int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_assessment);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ImageView zero = findViewById(R.id.intro_indicator_0_assessment);
        ImageView one = findViewById(R.id.intro_indicator_1_assessment);

        mNextBtn = findViewById(R.id.intro_btn_next_assessment);
        mBackBtn = findViewById(R.id.intro_btn_back_assessment);
        mFinishBtn = findViewById(R.id.intro_btn_finish_assessment);
        mSkipBtn = findViewById(R.id.intro_btn_skip_assessment);

        indicators = new ImageView[]{zero, one};

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager_container_assessment);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        boolean isDark = Utils.isDarkTheme();

        final int color1 = isDark ? ContextCompat.getColor(this, R.color.colorPrimaryDark) : ContextCompat.getColor(this, R.color.colorPrimary);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.setPagingEnabled(true);

                if (position == 1 && validateProfileFormError()) {
                    mViewPager.setPagingEnabled(false);
                    page = page == 1 ? page - 1 : page;
                    mViewPager.setCurrentItem(page, true);
                }
                /*
                color update
                 */
                mViewPager.setBackgroundColor(color1);

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
                        if (validateProfileFormError()){
                            position = position - 1;
                            mViewPager.setCurrentItem(page, true);
                            mViewPager.setPagingEnabled(false);
                            break;
                        }
                        mViewPager.setBackgroundColor(color1);
                        Utils.hideKeyboardFrom(getBaseContext(), mViewPager);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color1);
                        Utils.hideKeyboardFrom(getBaseContext(), mViewPager);
                        break;
                }
                page = position;
                updateIndicators(page);

                mNextBtn.setVisibility(position == 1 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                mBackBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mSkipBtn.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mViewPager.setFocusable(true);
                mViewPager.setFocusableInTouchMode(true);
                mViewPager.requestFocus();
                mViewPager.setFocusable(false);
                mViewPager.setFocusableInTouchMode(false);
            }
        });
    }

    public boolean validateProfileFormError(){
        boolean hasErrors = false;

        if (page > 1)
            hasErrors = Utils.validateForm(findViewById(R.id.fragment_pager_assessment_sus_1));
        else if (page < 1)
            hasErrors = Utils.validateForm(findViewById(R.id.fragment_pager_assessment_sus_2));

        return hasErrors;
    }

    private void startMainActivity(){
        Intent toMain = new Intent(AssessmentActivity.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;
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
                    return AssessmentSUSFragmentView.newInstance(position);
                case 1:
                    return AssessmentSUSFragmentView.newInstance(position);
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

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    public void onSkipButtonClick(View view) {
        startMainActivity();
    }

    public void onBackButtonClick(View view) {
        page -= 1;
        mViewPager.setCurrentItem(page, true);
    }

    public void onFinishButtonClick(View view) {
        startMainActivity();
    }

    public void onNextButtonClick(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        boolean notComplete = validateProfileFormError();

        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        if (notComplete) return;
        page += 1;
        mViewPager.setCurrentItem(page, true);
    }
}
