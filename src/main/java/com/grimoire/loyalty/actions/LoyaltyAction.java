package com.grimoire.loyalty.actions;

public enum LoyaltyAction {
    PURCHASE {
        @Override
        public int getPointsFor(Long amount) {
            return (int) (amount / 10);
        }
    },
    GEEK_DAY_PURCHASE {
        @Override
        public int getPointsFor(Long amount) {
            final var points = (int) (amount / 10);
            return points + 50;
        }
    };

    public abstract int getPointsFor(Long amount);
}
