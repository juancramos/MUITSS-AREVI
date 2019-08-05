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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.Utils;

public class PagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
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

        mNextBtn = findViewById(R.id.intro_btn_next);
        mBackBtn = findViewById(R.id.intro_btn_back);
        mFinishBtn = findViewById(R.id.intro_btn_finish);
        mSkipBtn = findViewById(R.id.intro_btn_skip);

        indicators = new ImageView[]{zero, one, two};

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        Class<?> theme = getApplication().getTheme().getClass().getSuperclass();

        boolean isDark = Utils.isDarkTheme();

        final int color1 = isDark ? ContextCompat.getColor(this, R.color.colorPrimaryDark) : ContextCompat.getColor(this, R.color.colorPrimary);
        final int color2 = isDark ? ContextCompat.getColor(this, R.color.colorOnPrimaryDark) : ContextCompat.getColor(this, R.color.colorOnPrimary);
        final int color3 = isDark ? ContextCompat.getColor(this, R.color.colorOnSecondaryDark) : ContextCompat.getColor(this, R.color.colorOnSecondary);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                }
                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                mBackBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mSkipBtn.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onNextButtonClick(View view) {
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

    public void onSetAppFontSizeClick(View view) {
        switch (view.getId()) {
            case R.id.APP_THEME:
                Utils.saveTheme(this, Constants.APP_THEME_DEFAULT_FONT_SIZE);
                break;
            case R.id.APP_THEME_DARK:
                Utils.saveTheme(this, Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE);
                break;
            case R.id.APP_THEME_MEDIUM_FONT_SIZE:
                Utils.saveTheme(this, Constants.APP_DARK_THEME_MEDIUM_FONT_SIZE);
                break;
            case R.id.APP_THEME_LARGE_FONT_SIZE:
                Utils.saveTheme(this, Constants.APP_DARK_THEME_LARGE_FONT_SIZE);
                break;
            case R.id.APP_THEME_NORMAL_FONT_SIZE:
                Utils.saveTheme(this, Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE);
                break;
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ImageView img;

        int[] bgs = new int[]{R.drawable.ic_person_add_24dp, R.drawable.ic_content_paste_24dp, R.drawable.ic_accessibility_24dp};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            assert getArguments() != null;
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            img = rootView.findViewById(R.id.section_img);
            img.setImageResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }


}
