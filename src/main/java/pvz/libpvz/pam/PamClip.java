package pvz.libpvz.pam;

/**
 * An optimized, cached handle for a specific PAM animation clip.
 * Using this eliminates the need for expensive String lookups during the render loop.
 */
public final class PamClip {
    final BakedAnimation ba;
    final int[] range;
    
    /** The duration of the clip in seconds. */
    public final float duration;

    PamClip(BakedAnimation ba, int[] range, float duration) {
        this.ba = ba;
        this.range = range;
        this.duration = duration;
    }
}
