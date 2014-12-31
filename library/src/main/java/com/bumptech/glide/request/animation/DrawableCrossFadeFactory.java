package com.bumptech.glide.request.animation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * A factory class that produces a new {@link com.bumptech.glide.request.animation.GlideAnimation} that varies depending
 * on whether or not the drawable was loaded from the memory cache and whether or not the drawable is the first
 * image to be set on the target.
 *
 * <p>
 *     Resources are usually loaded from the memory cache just before the user can see the view,
 *     for example when the user changes screens or scrolls back and forth in a list. In those cases the user
 *     typically does not expect to see an animation. As a result, when the resource is loaded from the memory
 *     cache this factory produces an {@link com.bumptech.glide.request.animation.NoAnimation}.
 * </p>
 */
public class DrawableCrossFadeFactory implements GlideAnimationFactory<Drawable> {
    private static final int DEFAULT_DURATION_MS = 300;
    private final ViewAnimationFactory<Drawable> animationFactory;
    private final int duration;
    private DrawableCrossFadeViewAnimation animation;

    public DrawableCrossFadeFactory() {
        this(DEFAULT_DURATION_MS);
    }

    public DrawableCrossFadeFactory(int duration) {
        this(new ViewAnimationFactory<Drawable>(new DefaultAnimationFactory()), duration);
    }

    public DrawableCrossFadeFactory(int defaultAnimationId, int duration) {
        this(new ViewAnimationFactory<Drawable>(defaultAnimationId), duration);
    }

    public DrawableCrossFadeFactory(Animation defaultAnimation, int duration) {
        this(new ViewAnimationFactory<Drawable>(defaultAnimation), duration);
    }

    DrawableCrossFadeFactory(ViewAnimationFactory<Drawable> animationFactory, int duration) {
        this.animationFactory = animationFactory;
        this.duration = duration;
    }

    @Override
    public GlideAnimation<Drawable> build(boolean isFromMemoryCache, boolean isFirstResource) {
        if (isFromMemoryCache) {
            return NoAnimation.get();
        }

        if (animation == null) {
            GlideAnimation<Drawable> defaultAnimation = animationFactory.build(false, isFirstResource);
            animation = new DrawableCrossFadeViewAnimation(defaultAnimation, duration);
        }

        return animation;
    }

    private static class DefaultAnimationFactory implements ViewAnimation.AnimationFactory {

        @Override
        public Animation build(Context context) {
            AlphaAnimation animation = new AlphaAnimation(0f, 1f);
            animation.setDuration(DEFAULT_DURATION_MS / 2);
            return animation;
        }
    }
}
