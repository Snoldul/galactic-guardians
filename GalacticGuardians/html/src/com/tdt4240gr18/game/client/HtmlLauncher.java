package com.tdt4240gr18.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.GalacticGuardians;

public class HtmlLauncher extends GwtApplication {
        private final DatabaseInterface databaseInterface = new DatabaseInterface() {
            @Override
            public void fetchDataFromDatabase() {

            }

            @Override
            public void insertTestData() {

            }
        };

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new GalacticGuardians(databaseInterface);
        }
}