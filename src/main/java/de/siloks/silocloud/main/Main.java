package de.siloks.silocloud.main;

import de.siloks.silocloud.main.server.Server;

/**
 * Erstellt von Lars am 18.08.2017.
 * Alle Rechte vorbehalten. Der Entwickler kann jederzeit
 * die Rechte an diesem Code entziehen!
 */
public class Main {

    public static void main(String[] args){
        try {
            new Server();
        } catch (Exception e) {}
    }

}
