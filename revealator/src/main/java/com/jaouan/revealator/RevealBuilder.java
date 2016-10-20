package com.jaouan.revealator;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.jaouan.revealator.animations.AnimationListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Revealator "reveal" builder.
 */
public class RevealBuilder {

    private View mViewToReveal;

    private View mFromView;

    private int mTranslateDuration = 250;

    private int mRevealDuration = 250;

    private boolean mChildsAnimation = false;

    private long mChildAnimationDuration = 500;

    private int mDelayBetweenChildAnimation = 50;

    private boolean mCurvedTranslation = false;

    private PointF mCurveControlPoint;

    private Runnable mEndAction;

    private float mHideFromViewAtInterpolatedTime = .8f;

    /**
     * Reveal builder's contructor.
     *
     * @param viewToReveal View to reveal.
     */
    RevealBuilder(@NonNull final View viewToReveal) {
        this.mViewToReveal = viewToReveal;
    }

    /**
     * Defines reveal duration.
     *
     * @param revealDuration Reveal duration.
     * @return Builder.
     */
    public RevealBuilder withRevealDuration(final int revealDuration) {
        this.mRevealDuration = revealDuration;
        return this;
    }

    /**
     * Defines translate duration.
     *
     * @param translateDuration Translate duration.
     * @return Builder.
     */
    public RevealBuilder withTranslateDuration(final int translateDuration) {
        this.mTranslateDuration = translateDuration;
        return this;
    }

    /**
     * Defines when from view starts to hide.
     *
     * @param hideFromViewAtInterpolatedTime End from view at interpolated time of translation. Must be between 0 and 1. (default : 0.8f)
     * @return Builder.
     */
    public RevealBuilder withHideFromViewAtTranslateInterpolatedTime(final float hideFromViewAtInterpolatedTime) {
        this.mHideFromViewAtInterpolatedTime = hideFromViewAtInterpolatedTime;
        return this;
    }

    /**
     * Defines that childs should be animated after reveal.
     *
     * @return Builder.
     */
    public RevealBuilder withChildsAnimation() {
        this.mChildsAnimation = mViewToReveal instanceof ViewGroup;
        return this;
    }

    /**
     * Defines that translation must be curved.
     *
     * @return Builder.
     */
    public RevealBuilder withCurvedTranslation() {
        this.mCurvedTranslation = true;
        return this;
    }


    /**
     * Defines that translation must be curved.
     *
     * @param curveControlPoint Relative curved control point.
     * @return Builder.
     */
    public RevealBuilder withCurvedTranslation(final PointF curveControlPoint) {
        this.mCurveControlPoint = curveControlPoint;
        return this.withCurvedTranslation();
    }

    /**
     * Defines by child animation duration.
     *
     * @param childAnimationDuration Child animation duration.
     * @return Builder.
     */
    public RevealBuilder withChildAnimationDuration(final int childAnimationDuration) {
        this.mChildAnimationDuration = childAnimationDuration;
        return this;
    }

    /**
     * Defines delay between child animation.
     *
     * @param delayBetweenChildAnimation Delay between child animation.
     * @return Builder.
     */
    public RevealBuilder withDelayBetweenChildAnimation(final int delayBetweenChildAnimation) {
        this.mDelayBetweenChildAnimation = delayBetweenChildAnimation;
        return this;
    }

    /**
     * Defines the view to translate to the view to reveal.
     *
     * @param fromView View to translate to the view to reveal.
     * @return Builder.
     */
    public RevealBuilder from(@NonNull final View fromView) {
        this.mFromView = fromView;
        return this;
    }

    /**
     * Defines end action callback.
     *
     * @param endAction End action callback.
     * @return Builder.
     */
    public RevealBuilder withEndAction(@NonNull final Runnable endAction) {
        this.mEndAction = endAction;
        return this;
    }

    /**
     * Let's animate !
     */
    public void start() {
        // - Make view to reveal invisible.
        mViewToReveal.setVisibility(View.INVISIBLE);

        // - Find and hide all childs if necessary.
        final List<View> ordoredChildsViews = new ArrayList<>();
        if (this.mChildsAnimation) {
            RevealatorHelper.findAllVisibleChilds((ViewGroup) this.mViewToReveal, ordoredChildsViews);
            for (final View childView : ordoredChildsViews) {
                childView.setVisibility(View.INVISIBLE);
            }
        }

        // - If from view does not exist.
        if (this.mFromView == null) {
            // - Reveal view and childs.
            revealViewAndChilds(ordoredChildsViews);
        } else {
            // - Prepare to reveal view when translation end.
            final Animation.AnimationListener animationListener = new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    revealViewAndChilds(ordoredChildsViews);
                }
            };
            // - Translate and hide the "from view" and delay reveal animation.
            RevealatorHelper.translateAndHideView(this.mFromView, this.mViewToReveal, this.mTranslateDuration, this.mCurvedTranslation, this.mCurveControlPoint, this.mHideFromViewAtInterpolatedTime, animationListener);
        }
    }

    /**
     * Reveal view and childs.
     *
     * @param ordoredChildsViews Childs views.
     */
    private void revealViewAndChilds(final List<View> ordoredChildsViews) {
        RevealatorHelper.revealView(mViewToReveal, mRevealDuration, new Runnable() {
                    @Override
                    public void run() {
                        // - Show childs view if necessary.
                        RevealatorHelper.orderedShowViews(ordoredChildsViews, mChildAnimationDuration, mDelayBetweenChildAnimation);

                        // - Fire end action if necessary.
                        if (mEndAction != null) {
                            mEndAction.run();
                        }
                    }
                }
        );
    }

}