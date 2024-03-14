package com.tdt4240gr18.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public final Vector3 position = new Vector3();
    public final Vector3 scale = new Vector3(1, 1, 1);
    public final Vector3 rotation = new Vector3();
    public final Vector3 velocity = new Vector3();
}
