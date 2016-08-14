package com.jaouan.revealator.animations;

import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Arc translate animation.
 * Based on snippet https://gist.github.com/guohai/2293628.
 */
public class BezierTranslateAnimation extends Animation {

    private int mFromXType = ABSOLUTE;
    private int mToXType = ABSOLUTE;
    private int mFromYType = ABSOLUTE;
    private int mToYType = ABSOLUTE;

    private float mFromXValue;
    private float mToXValue;
    private float mFromYValue;
    private float mToYValue;

    private PointF mStart;
    private PointF mControl;
    private PointF mEnd;

    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param fromXDelta Change in X coordinate to apply at the start of the animation
     * @param toXDelta   Change in X coordinate to apply at the end of the animation
     * @param fromYDelta Change in Y coordinate to apply at the start of the animation
     * @param toYDelta   Change in Y coordinate to apply at the end of the animation
     */
    public BezierTranslateAnimation(float fromXDelta, float toXDelta,
                                    float fromYDelta, float toYDelta) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;
    }

    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param fromXType  Specifies how fromXValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param fromXValue Change in X coordinate to apply at the start of the animation.
     *                   This value can either be an absolute number if fromXType is
     *                   ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toXType    Specifies how toXValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param toXValue   Change in X coordinate to apply at the end of the animation.
     *                   This value can either be an absolute number if toXType is
     *                   ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param fromYType  Specifies how fromYValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param fromYValue Change in Y coordinate to apply at the start of the animation.
     *                   This value can either be an absolute number if fromYType is
     *                   ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toYType    Specifies how toYValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param toYValue   Change in Y coordinate to apply at the end of the animation.
     *                   This value can either be an absolute number if toYType is
     *                   ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public BezierTranslateAnimation(int fromXType, float fromXValue, int toXType,
                                    float toXValue, int fromYType, float fromYValue, int toYType,
                                    float toYValue) {
        mFromXValue = fromXValue;
        mToXValue = toXValue;
        mFromYValue = fromYValue;
        mToYValue = toYValue;
        mFromXType = fromXType;
        mToXType = toXType;
        mFromYType = fromYType;
        mToYType = toYType;
    }


    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param fromXDelta   Change in X coordinate to apply at the start of the animation
     * @param toXDelta     Change in X coordinate to apply at the end of the animation
     * @param fromYDelta   Change in Y coordinate to apply at the start of the animation
     * @param toYDelta     Change in Y coordinate to apply at the end of the animation
     * @param controlPoint Control point for Bezier algorithm.
     */
    public BezierTranslateAnimation(float fromXDelta, float toXDelta,
                                    float fromYDelta, float toYDelta,
                                    PointF controlPoint) {
        this(fromXDelta, toXDelta, fromYDelta, toYDelta);
        mControl = controlPoint;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float dx = calculateBezier(interpolatedTime, mStart.x, mControl.x, mEnd.x);
        final float dy = calculateBezier(interpolatedTime, mStart.y, mControl.y, mEnd.y);
        t.getMatrix().setTranslate(dx, dy);
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        final float mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
        final float mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
        final float mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
        final float mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);

        mStart = new PointF(mFromXDelta, mFromYDelta);
        mEnd = new PointF(mToXDelta, mToYDelta);
        // - Define the cross of the two tangents from point 0 and point 1 as control point if necessary.
        if (mControl == null) {
            mControl = new PointF(mFromXDelta, mToYDelta);
        }
    }

    /**
     * Calculate the position on a quadratic bezier curve by given three points
     * and the percentage of time passed.
     * <p/>
     * from http://en.wikipedia.org/wiki/B%C3%A9zier_curve
     *
     * @param interpolatedTime The fraction of the duration that has passed where 0 <= time
     *                         <= 1.
     * @param point0           A single dimension of the starting point.
     * @param point1           A single dimension of the control point.
     * @param point2           A single dimension of the ending point.
     */
    private long calculateBezier(float interpolatedTime, float point0, float point1, float point2) {
        return Math.round((Math.pow((1 - interpolatedTime), 2) * point0)
                + (2 * (1 - interpolatedTime) * interpolatedTime * point1)
                + (Math.pow(interpolatedTime, 2) * point2));
    }

}