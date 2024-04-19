package com.tdt4240gr18.game.entity.components;
import com.badlogic.ashley.core.Component;

public class MovementStateComponent implements Component {
    public enum State {
        ENTERING, FLOATING, DIVING
    }

    public State state = State.ENTERING;
    public float elapsedTime = 0.0f;  // Tracks time for state-specific behaviors
}