package com.github.vivchar.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaly Vivchar on 13/01/17.
 */
public class ViewPagerIndicator extends LinearLayoutCompat {

        protected static final String STATE_SUPER = "STATE_SUPER";
        protected static final String STATE_INDEX = "STATE_INDEX";
        protected static final float NO_SCALE = 1f;
        protected static final int DEF_VALUE = 10;
        protected static final int DEF_ICON = R.drawable.white_circle;

        protected int mItemColor = Color.WHITE;
        protected int mItemSelectedColor = Color.WHITE;
        protected int mPageCount;
        protected int mSelectedIndex;
        protected float mItemScale = NO_SCALE;
        protected int mItemSize = DEF_VALUE;
        protected int mDelimiterSize = DEF_VALUE;
        protected int mItemIcon = DEF_VALUE;

        @NonNull
        protected final List<ImageView> mIndexImages = new ArrayList<>();
        @Nullable
        public ViewPager.OnPageChangeListener mListener;

        public ViewPagerIndicator(@NonNull final Context context) {
                this(context, null);
        }

        public ViewPagerIndicator(@NonNull final Context context, @Nullable final AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public ViewPagerIndicator(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                setOrientation(HORIZONTAL);
                final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);
                try {
                        mItemSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_itemSize, DEF_VALUE);
                        mItemScale = attributes.getFloat(R.styleable.ViewPagerIndicator_itemScale, NO_SCALE);
                        mItemSelectedColor = attributes.getColor(R.styleable.ViewPagerIndicator_itemSelectedTint, Color.WHITE);
                        mItemColor = attributes.getColor(R.styleable.ViewPagerIndicator_itemTint, Color.WHITE);
                        mDelimiterSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_delimiterSize, DEF_VALUE);
                        mItemIcon = attributes.getResourceId(R.styleable.ViewPagerIndicator_itemIcon, DEF_ICON);
                } finally {
                        attributes.recycle();
                }
                if (isInEditMode()) {
                        createEditModeLayout();
                }
        }

        protected void createEditModeLayout() {
                for (int i = 0; i < 5; ++i) {
                        final FrameLayout boxedItem = createBoxedItem(i);
                        addView(boxedItem);
                        if (i == 1) {
                                final View item = boxedItem.getChildAt(0);
                                final ViewGroup.LayoutParams layoutParams = item.getLayoutParams();
                                layoutParams.height *= mItemScale;
                                layoutParams.width *= mItemScale;
                                item.setLayoutParams(layoutParams);
                        }
                }
        }

        public void setupWithViewPager(@NonNull final ViewPager viewPager) {
                setPageCount(viewPager.getAdapter().getCount());
                viewPager.addOnPageChangeListener(new OnPageChangeListener());
        }

        public void addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
                mListener = listener;
        }

        public void setSelectedIndex(final int selectedIndex) {
                if (selectedIndex < 0 || selectedIndex > mPageCount - 1) {
                        return;
                }

                final ImageView unselectedView = mIndexImages.get(mSelectedIndex);
                unselectedView.animate().scaleX(NO_SCALE).scaleY(NO_SCALE).setDuration(300).start();
                unselectedView.setColorFilter(mItemColor, android.graphics.PorterDuff.Mode.SRC_IN);

                final ImageView selectedView = mIndexImages.get(selectedIndex);
                selectedView.animate().scaleX(mItemScale).scaleY(mItemScale).setDuration(300).start();
                selectedView.setColorFilter(mItemSelectedColor, android.graphics.PorterDuff.Mode.SRC_IN);

                mSelectedIndex = selectedIndex;
        }

        public void setPageCount(final int pageCount) {
                mPageCount = pageCount;
                mSelectedIndex = 0;
                removeAllViews();
                mIndexImages.clear();

                for (int i = 0; i < pageCount; ++i) {
                        addView(createBoxedItem(i));
                }

                setSelectedIndex(mSelectedIndex);
        }

        @NonNull
        protected FrameLayout createBoxedItem(final int position) {
                final FrameLayout box = new FrameLayout(getContext());
                final ImageView item = createItem();
                box.addView(item);
                mIndexImages.add(item);

                final LinearLayoutCompat.LayoutParams boxParams = new LinearLayoutCompat.LayoutParams(
                        (int) (mItemSize * mItemScale),
                        (int) (mItemSize * mItemScale)
                );
                if (position > 0) {
                        if (isRtl()) {
                                boxParams.setMargins(0, 0, mDelimiterSize, 0);
                        } else {
                                boxParams.setMargins(mDelimiterSize, 0, 0, 0);
                        }
                }
                box.setLayoutParams(boxParams);
                return box;
        }

        @NonNull
        protected ImageView createItem() {
                final ImageView index = new ImageView(getContext());
                final FrameLayout.LayoutParams indexParams = new FrameLayout.LayoutParams(mItemSize, mItemSize);
                indexParams.gravity = Gravity.CENTER;
                index.setLayoutParams(indexParams);
                index.setImageResource(mItemIcon);
                index.setScaleType(ImageView.ScaleType.FIT_CENTER);
                index.setColorFilter(mItemColor, android.graphics.PorterDuff.Mode.SRC_IN);
                return index;
        }

        private boolean isRtl() {
                final boolean result;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        result = getContext().getResources().getConfiguration().getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
                } else {
                        result = false;
                }
                return result;
        }

        protected class OnPageChangeListener implements ViewPager.OnPageChangeListener {

                @Override
                public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                        if (mListener != null) {
                                mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        }
                }

                @Override
                public void onPageSelected(final int position) {
                        setSelectedIndex(position);
                        if (mListener != null) {
                                mListener.onPageSelected(position);
                        }
                }

                @Override
                public void onPageScrollStateChanged(final int state) {
                        if (mListener != null) {
                                mListener.onPageScrollStateChanged(state);
                        }
                }
        }

        @Override
        protected Parcelable onSaveInstanceState() {
                final Bundle bundle = new Bundle();
                bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
                bundle.putInt(STATE_INDEX, mSelectedIndex);
                return bundle;
        }

        @Override
        protected void onRestoreInstanceState(final Parcelable parcel) {
                final Bundle bundle = (Bundle) parcel;
                super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
                setSelectedIndex(bundle.getInt(STATE_INDEX));
        }
}