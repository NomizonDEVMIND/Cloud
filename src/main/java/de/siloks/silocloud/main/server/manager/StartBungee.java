package de.siloks.silocloud.main.server.manager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Erstellt von Lars am 18.08.2017.
 * Alle Rechte vorbehalten. Der Entwickler kann jederzeit
 * die Rechte an diesem Code entziehen!
 */
public class StartBungee {

    public static void start(){
        System.out.println("Starting bungeecord!");
        File dir = new File("bungee");
        if(!dir.exists()){
            dir.mkdirs();
            try {
                download(new URL("http://nomizon.de/download/silocloud/bungee.jar"), "bungee/", "BungeeCord.jar");
            } catch (MalformedURLException e) {
                System.out.println("Cannot download BungeeCord.jar!");
            }
        }
        String cmd = "java -jar \"" + "bungee/BungeeCord.jar";
        try {
            Process p = null;
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(new File("bungee/"));
            p = pb.start();

        } catch (IOException e) {
            System.err.println("Cannot start Bungeecord!");
            System.err.println("BungeeCord.jar is missing!");
            try {
                download(new URL("http://nomizon.de/download/silocloud/bungee.jar"), "bungee/", "BungeeCord.jar");
            } catch (MalformedURLException e1) {}
        }
    }

    public static void download(URL url, String path, String name){
        try {
            System.out.println("Downloading "+name+"...");
            final URLConnection conn = url.openConnection();
            final InputStream is = new BufferedInputStream(conn.getInputStream());
            final OutputStream os = new BufferedOutputStream(new FileOutputStream(path+name));
            byte[] chunk = new byte[1024];
            int chunkSize;
            while ((chunkSize = is.read(chunk)) != -1) {
                os.write(chunk, 0, chunkSize);
            }
            os.flush();
            os.close();
            is.close();
            System.out.println("Download finished!");
        } catch (IOException e) {

        }
    }

}
