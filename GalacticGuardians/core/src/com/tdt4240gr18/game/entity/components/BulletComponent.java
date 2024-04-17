package com.tdt4240gr18.game.entity.components;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component{
    // This component is used to identify the bullet entity
    public int damage = 1;
    public int speed =  400;
}
