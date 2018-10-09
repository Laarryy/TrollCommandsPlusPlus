package me.egg82.tcpp.reflection.entity;

public enum AnimationType {
    SWING_ARM(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL(4),
    MAGIC_CRITICAL(0);

    private int animationId = -1;

    AnimationType(int animationId) {
        this.animationId = animationId;
    }

    public int getAnimationId() {
        return animationId;
    }
}
