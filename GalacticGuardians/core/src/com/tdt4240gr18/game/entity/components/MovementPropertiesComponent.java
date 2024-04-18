package com.tdt4240gr18.game.entity.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementPropertiesComponent implements Component {
    public float amplitude;
    public float frequency;
    public float verticalSpeed;
    public float targetDiveY;
    public float oscillationPhase;
    public Vector2 diveDirection;
}