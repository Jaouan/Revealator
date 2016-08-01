package com.jaouan.revealator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Helper for the revealator.
 */
public final class RevealatorHelper {

    /**
     * Disallow instantiation.
     */
    private RevealatorHelper() {
    }

    /**
     * Get center locations delta between two views.
     *
     * @param viewA View A.
     * @param viewB View B.
     * @return Locations delta [X,Y].
     */
    private static int[] getCenterLocationsDelta(final View viewA, final View viewB) {
        int[] fromLocation = new int[2];
        viewA.getLocationOnScreen(fromLocation);
        int[] toLocation = new int[2];
        viewB.getLocationOnScreen(toLocation);
        int deltaX = toLocation[0] - fromLocation[0] + viewB.getMeasuredWidth() / 2 - viewA.getMeasuredWidth() / 2;
        int deltaY = toLocation[1] - fromLocation[1] + viewB.getMeasuredHeight() / 2 - viewA.getMeasuredHeight() / 2;
        return new int[]{deltaX, deltaY};
    }

    /**
     * Helps to hide then translate a view to another view.
     *
     * @param fromView From view.
     * @param toView   Target view.
     * @param duration Duration.
     */
    static void translateAndHideView(final View fromView, final View toView, final long duration) {
        // - Determine translate delta.
        final int[] delta = getCenterLocationsDelta(fromView, toView);

        // - Prepare translate animation.
        final TranslateAnimation translateAnimation = new TranslateAnimation(0, delta[0], 0, delta[1]);
        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new AccelerateInterpolator());

        // - Prepare hide animation.
        final ScaleAnimation hideAnimation = new ScaleAnimation(fromView.getScaleX(), 0, fromView.getScaleY(), 0, Animation.ABSOLUTE, delta[0] + fromView.getMeasuredWidth() / 2, Animation.ABSOLUTE, delta[1] + fromView.getMeasuredHeight() / 2);
        hideAnimation.setDuration((long) (duration *0.2f));
        hideAnimation.setStartOffset((long) (duration * 0.9f));
        hideAnimation.setInterpolator(new BounceInterpolator());

        // - Prepare animations set.
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(hideAnimation);
        animationSet.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                fromView.setVisibility(View.INVISIBLE);
            }
        });

        // - Let's move !
        fromView.startAnimation(animationSet);
    }

    /**
     * Helps to translate then show a view to another view.
     *
     * @param viewToTranslate      View to translate..
     * @param fromView             From view.
     * @param startDelay           Start delay.
     * @param duration             Translate duration.
     * @param animationEndCallBack Callback fired on animation end.
     */
    public static void showAndTranslateView(final View viewToTranslate, final View fromView, final int startDelay, final int duration, final Runnable animationEndCallBack) {
        // - Determine translate delta.
        final int[] delta = getCenterLocationsDelta(viewToTranslate, fromView);

        // - Prepare show animation.
        final ScaleAnimation showAnimation = new ScaleAnimation(0, viewToTranslate.getScaleX(), 0, viewToTranslate.getScaleY(), Animation.ABSOLUTE, viewToTranslate.getMeasuredWidth() / 2, Animation.ABSOLUTE,  viewToTranslate.getMeasuredHeight() / 2);
        showAnimation.setDuration((long) (duration * 0.2f));
        showAnimation.setInterpolator(new BounceInterpolator());

        // - Prepare translate animation.
        final TranslateAnimation translateAnimation = new TranslateAnimation(delta[0], 0, delta[1], 0);
        translateAnimation.setDuration(duration);
        showAnimation.setStartOffset((long) (duration * 0.1f));
        translateAnimation.setInterpolator(new DecelerateInterpolator());

        // - Prepare animations set.
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.setStartOffset(startDelay);
        animationSet.addAnimation(showAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                viewToTranslate.setVisibility(View.VISIBLE);
                animationEndCallBack.run();
            }
        });

        // - Let's move !
        viewToTranslate.startAnimation(animationSet);
    }

    /**
     * Helps to reveal a view.
     *
     * @param viewToReveal         View to reveal.
     * @param startDelay           Start delay.
     * @param duration             Duration.
     * @param animationEndCallBack Callback fired on animation end.
     */
    static void revealView(final View viewToReveal, final int startDelay, final int duration, final Runnable animationEndCallBack) {
        // - Determine circle location and size.
        int viewCenterX = (viewToReveal.getLeft() + viewToReveal.getRight()) / 2;
        int viewCenterY = (viewToReveal.getTop() + viewToReveal.getBottom()) / 2;
        int viewDiameterX = Math.max(viewCenterX, viewToReveal.getWidth() - viewCenterX);
        int viewDiameterY = Math.max(viewCenterY, viewToReveal.getHeight() - viewCenterY);
        float finalRadius = (float) Math.hypot(viewDiameterX, viewDiameterY);

        // - Prepare animation.
        final Animator circularRevealAnimator =
                ViewAnimationUtils.createCircularReveal(viewToReveal, viewCenterX, viewCenterY, 0, finalRadius);
        circularRevealAnimator.setInterpolator(new DecelerateInterpolator());
        circularRevealAnimator.setStartDelay(startDelay);
        circularRevealAnimator.setDuration(duration);
        circularRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewToReveal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationEndCallBack.run();
            }
        });

        // Let's reveal !
        circularRevealAnimator.start();
    }

    /**
     * Helps to unreveal a view.
     *
     * @param viewToUnreveal       View to unreveal.
     * @param duration             Duration.
     * @param animationEndCallBack Callback fired on animation end.
     */
    static void unrevealView(final View viewToUnreveal, final int duration, final Runnable animationEndCallBack) {
        // - Determine circle location and size.
        int viewCenterX = (viewToUnreveal.getLeft() + viewToUnreveal.getRight()) / 2;
        int viewCenterY = (viewToUnreveal.getTop() + viewToUnreveal.getBottom()) / 2;
        int viewDiameterX = Math.max(viewCenterX, viewToUnreveal.getWidth() - viewCenterX);
        int viewDiameterY = Math.max(viewCenterY, viewToUnreveal.getHeight() - viewCenterY);
        float finalRadius = (float) Math.hypot(viewDiameterX, viewDiameterY);

        // - Prepare animation.
        final Animator circularRevealAnimator =
                ViewAnimationUtils.createCircularReveal(viewToUnreveal, viewCenterX, viewCenterY, finalRadius, 0);
        circularRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        circularRevealAnimator.setDuration(duration);
        circularRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewToUnreveal.setVisibility(View.INVISIBLE);
                animationEndCallBack.run();
            }
        });

        // Let's reveal !
        circularRevealAnimator.start();
    }

    /**
     * Helps to find recursivly all visible childs in a view group.
     *
     * @param viewGroup     View group.
     * @param ordoredChilds Childs list where visible childs will be added.
     */
    static void findAllVisibleChilds(final ViewGroup viewGroup, final List<View> ordoredChilds) {
        for (int childViewIndex = 0; childViewIndex < viewGroup.getChildCount(); childViewIndex++) {
            final View childView = viewGroup.getChildAt(childViewIndex);
            if (childView instanceof ViewGroup) {
                findAllVisibleChilds((ViewGroup) childView, ordoredChilds);
                continue;
            }
            if (childView.getVisibility() == View.VISIBLE) {
                ordoredChilds.add(childView);
            }
        }
    }

    /**
     * Helps to show views.
     *
     * @param views             Views to show.
     * @param animationDuration Animation duration.
     * @param animationDelay    Animation delay.
     */
    static void orderedShowViews(final List<View> views, long animationDuration, int animationDelay) {
        if (views != null) {
            for (int viewIndex = 0; viewIndex < views.size(); viewIndex++) {
                final View childView = views.get(viewIndex);
                childView.setVisibility(View.VISIBLE);
                final ScaleAnimation scaleAnimation = new ScaleAnimation(0, childView.getScaleX(), 0, childView.getScaleY(), Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
                scaleAnimation.setInterpolator(new DecelerateInterpolator());
                scaleAnimation.setDuration(animationDuration);
                scaleAnimation.setStartOffset(viewIndex * animationDelay);
                childView.startAnimation(scaleAnimation);
            }
        }
    }

}
