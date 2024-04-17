package com.tdt4240gr18.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Intersector;

public class CollisionComponent implements Component {
    public Shape2D bounds;

    public boolean overlaps(CollisionComponent other) {

        if (bounds instanceof Rectangle && other.bounds instanceof Rectangle) {
            Rectangle rectA = (Rectangle) bounds;
            Rectangle rectB = (Rectangle) other.bounds;

            return Intersector.overlaps(rectA, rectB);
        } else {
            // Handle other shape types or return false if types don't match
            return false;
        }
    }
}
