package com.jaouan.revealator;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Revealator "unreveal" builder.
 */
public class UnrevealBuilder {

    private View mViewToUnreveal;

    private int mUnrevealDuration = 250;

    private int mTranslateDuration = 250;

    private float mShowFromViewInterpolatedDuration = 0.2f;

    private boolean mCurvedTranslation = false;

    private PointF mCurveControlPoint;

    private Runnable mEndAction;

    private View mToView;

    /**
     * Unreveal builder's contructor.
     *
     * @param viewToUnreveal View to unreveal.
     */
    UnrevealBuilder(@NonNull final View viewToUnreveal) {
        this.mViewToUnreveal = viewToUnreveal;
    }

    /**
     * Defines the view toView translate after the "unrevealation".
     *
     * @param toView View toView translate.
     * @return Builder.
     */
    public UnrevealBuilder to(final View toView) {
        this.mToView = toView;
        return this;
    }

    /**
     * Defines the unreveal duration.
     *
     * @param unrevealDuration Unreveal duration.
     * @return Builder.
     */
    public UnrevealBuilder withUnrevealDuration(final int unrevealDuration) {
        this.mUnrevealDuration = unrevealDuration;
        return this;
    }

    /**
     * Defines the translate duration.
     *
     * @param translateDuration translate duration.
     * @return Builder.
     */
    public UnrevealBuilder withTranslateDuration(final int translateDuration) {
        this.mTranslateDuration = translateDuration;
        return this;
    }

    /**
     * Defines from view's showing animation interpolated duration.
     *
     * @param showFromViewInterpolatedDuration Ends showing from view interpolated duration. Must be between 0 and 1. (default : 0.2f)
     * @return Builder.
     */
    public UnrevealBuilder withShowFromViewInterpolatedDuration(final float showFromViewInterpolatedDuration) {
        this.mShowFromViewInterpolatedDuration = showFromViewInterpolatedDuration;
        return this;
    }


    /**
     * Defines that translation must be curved.
     *
     * @return Builder.
     */
    public UnrevealBuilder withCurvedTranslation() {
        this.mCurvedTranslation = true;
        return this;
    }

    /**
     * Defines that translation must be curved.
     *
     * @param curveControlPoint Relative curved control point.
     * @return Builder.
     */
    public UnrevealBuilder withCurvedTranslation(final PointF curveControlPoint) {
        this.mCurveControlPoint = curveControlPoint;
        return this.withCurvedTranslation();
    }

    /**
     * Defines end action callback.
     *
     * @param endAction End action callback.
     * @return Builder.
     */
    public UnrevealBuilder withEndAction(@NonNull final Runnable endAction) {
        this.mEndAction = endAction;
        return this;
    }

    /**
     * Let's animate !
     */
    public void start() {
        // - Reveal the view !
        RevealatorHelper.unrevealView(this.mViewToUnreveal, this.mUnrevealDuration, new Runnable() {
                    @Override
                    public void run() {
                        // - If no to view, fire end action if necessary.
                        if (mToView == null && mEndAction != null) {
                            mEndAction.run();
                        }
                    }
                }
        );

        // - If to view exists, show and translate the "to view".
        if (this.mToView != null) {
            RevealatorHelper.showAndTranslateView(this.mToView, this.mViewToUnreveal, (int) (this.mUnrevealDuration * 0.9f), this.mTranslateDuration, this.mCurvedTranslation, this.mCurveControlPoint, this.mShowFromViewInterpolatedDuration, new Runnable() {
                        @Override
                        public void run() {
                            // - Fire end action if necessary.
                            if (mEndAction != null) {
                                mEndAction.run();
                            }
                        }
                    }
            );
        }
    }

}