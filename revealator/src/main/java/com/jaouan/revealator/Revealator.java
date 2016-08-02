package com.jaouan.revealator;

import android.support.annotation.NonNull;
import android.view.View;

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

}
