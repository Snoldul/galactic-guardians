package com.tdt4240gr18.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {
    private final Stack<State> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }
    public State popAndReturn(){
        State temp = states.peek();
        states.pop();
        return temp;
    }

    public void set(State state){
        states.pop();
        states.push(state);
    }

    public void printStatesStack() {
        for (State s : states) {
            Gdx.app.log("stackPrint", s.toString());
        }
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        if(!(states.peek() instanceof PauseState || states.peek() instanceof GameOverState)){
            states.peek().render(sb);
        } else {
            renderAll(sb);
        }
    }

    public Object peek() {
        return states.peek();
    }

    public State getStateAt(int index) {
        if (index < 0 || index >= states.size()) {
            return null;
        }
        return states.get(index);
    }

    public Stack<State> getStack() {
        return states;
    }

    public void pushToTop(State state) {
        states.remove(state);
        states.push(state);
    }

    public void renderAll(SpriteBatch sb) {
        for (int i = 1; i < states.size(); i++) {
            states.get(i).render(sb);
        }
    }


}
