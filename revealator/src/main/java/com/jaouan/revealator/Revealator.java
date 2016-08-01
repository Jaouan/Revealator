package com.jaouan.revealator;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Revealator utils.
 */
public class Revealator {

    /**
     * Reveal a view.
     *
     * @param viewToReveal View to reveal.
     * @return Revealator "reveal" builder.
     */
    public static RevealBuilder reveal(@NonNull final View viewToReveal) {
        return new RevealBuilder(viewToReveal);
    }

    /**
     * Reveal a view.
     *
     * @param viewToUnreveal View to reveal. Parent must be instance of RevealViewGroup.
     * @return Revealator "unreveal" builder.
     */
    public static UnrevealBuilder unreveal(@NonNull final View viewToUnreveal) {
        return new UnrevealBuilder(viewToUnreveal);
    }

    /**
     * Revealator "reveal" builder.
     */
    public static class RevealBuilder {

        private View viewToReveal;

        private View fromView;

        private int translateDuration = 250;

        private int revealDuration = 250;

        private boolean childsAnimation = false;

        private long childAnimationDuration = 500;

        private int delayBetweenChildAnimation = 50;

        private Runnable endAction;

        private RevealBuilder(@NonNull final View viewToReveal) {
            this.viewToReveal = viewToReveal;
        }

        /**
         * Defines reveal duration.
         *
         * @param revealDuration Reveal duration.
         * @return Builder.
         */
        public RevealBuilder withRevealDuration(final int revealDuration) {
            this.revealDuration = revealDuration;
            return this;
        }

        /**
         * Defines translate duration.
         *
         * @param translateDuration Translate duration.
         * @return Builder.
         */
        public RevealBuilder withTranslateDuration(final int translateDuration) {
            this.translateDuration = translateDuration;
            return this;
        }

        /**
         * Defines that childs should be animated after reveal.
         *
         * @return Builder.
         */
        public RevealBuilder withChildsAnimation() {
            this.childsAnimation = viewToReveal instanceof ViewGroup;
            return this;
        }

        /**
         * Defines by child animation duration.
         *
         * @param childAnimationDuration Child animation duration.
         * @return Builder.
         */
        public RevealBuilder withChildAnimationDuration(final int childAnimationDuration) {
            this.childAnimationDuration = childAnimationDuration;
            return this;
        }

        /**
         * Defines delay between child animation.
         *
         * @param delayBetweenChildAnimation Delay between child animation.
         * @return Builder.
         */
        public RevealBuilder withDelayBetweenChildAnimation(final int delayBetweenChildAnimation) {
            this.delayBetweenChildAnimation = delayBetweenChildAnimation;
            return this;
        }

        /**
         * Defines the view to translate to the view to reveal.
         *
         * @param fromView View to translate to the view to reveal.
         * @return Builder.
         */
        public RevealBuilder from(@NonNull final View fromView) {
            this.fromView = fromView;
            return this;
        }

        /**
         * Defines end action callback.
         *
         * @param endAction End action callback.
         * @return Builder.
         */
        public RevealBuilder withEndAction(@NonNull final Runnable endAction) {
            this.endAction = endAction;
            return this;
        }

        /**
         * Let's animate !
         */
        public void start() {
            // - Make view to reveal invisible.
            viewToReveal.setVisibility(View.INVISIBLE);

            // - If from view exists, translate and hide the "from view" and delay reveal animation.
            int revealStartDelay = 0;
            if (this.fromView != null) {
                RevealatorHelper.translateAndHideView(this.fromView, this.viewToReveal, this.translateDuration);
                revealStartDelay = (int) (this.translateDuration * 0.9f);
            }

            // - Find and hide all childs if necessary.
            final List<View> ordoredChildsViews = new ArrayList<>();
            if (this.childsAnimation) {
                RevealatorHelper.findAllVisibleChilds((ViewGroup) this.viewToReveal, ordoredChildsViews);
                for (final View childView : ordoredChildsViews) {
                    childView.setVisibility(View.INVISIBLE);
                }
            }

            // - Reveal the view !
            RevealatorHelper.revealView(viewToReveal, revealStartDelay, this.revealDuration, new Runnable() {
                        @Override
                        public void run() {
                            // - Show childs view if necessary.
                            RevealatorHelper.orderedShowViews(ordoredChildsViews, childAnimationDuration, delayBetweenChildAnimation);

                            // - Fire end action if necessary.
                            if (endAction != null) {
                                endAction.run();
                            }
                        }
                    }
            );
        }
    }

    /**
     * Revealator "unreveal" builder.
     */
    public static class UnrevealBuilder {

        private View viewToUnreveal;

        private int unrevealDuration = 250;

        private int translateDuration = 250;

        private Runnable endAction;

        private View toView;

        private UnrevealBuilder(@NonNull final View viewToUnreveal) {
            this.viewToUnreveal = viewToUnreveal;
        }

        /**
         * Defines the view toView translate after the "unrevealation".
         * @param toView View toView translate.
         * @return Builder.
         */
        public UnrevealBuilder to(final View toView) {
            this.toView = toView;
            return this;
        }

        /**
         * Defines the unreveal duration.
         *
         * @param unrevealDuration Unreveal duration.
         * @return Builder.
         */
        public UnrevealBuilder withUnrevealDuration(final int unrevealDuration) {
            this.unrevealDuration = unrevealDuration;
            return this;
        }

        /**
         * Defines the translate duration.
         *
         * @param translateDuration translate duration.
         * @return Builder.
         */
        public UnrevealBuilder withTranslateDuration(final int translateDuration) {
            this.translateDuration = translateDuration;
            return this;
        }

        /**
         * Defines end action callback.
         *
         * @param endAction End action callback.
         * @return Builder.
         */
        public UnrevealBuilder withEndAction(@NonNull final Runnable endAction) {
            this.endAction = endAction;
            return this;
        }

        /**
         * Let's animate !
         */
        public void start() {
            // - Reveal the view !
            RevealatorHelper.unrevealView(viewToUnreveal, this.unrevealDuration, new Runnable() {
                        @Override
                        public void run() {
                            // - If no to view, fire end action if necessary.
                            if (toView == null && endAction != null) {
                                endAction.run();
                            }
                        }
                    }
            );

            // - If to view exists, show and translate the "to view".
            if(toView != null) {
                RevealatorHelper.showAndTranslateView(toView, viewToUnreveal, (int)(this.unrevealDuration * 0.9f), this.translateDuration, new Runnable() {
                            @Override
                            public void run() {
                                // - Fire end action if necessary.
                                if (endAction != null) {
                                    endAction.run();
                                }
                            }
                        }
                );
            }
        }

    }

}
