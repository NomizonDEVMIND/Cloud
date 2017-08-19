package de.siloks.silocloud.main.server.manager;

import io.netty.channel.epoll.Epoll;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

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

    public static boolean EPOLL = Epoll.isAvailable();
    public static Process process;

    public static void start(){
        System.out.println("Starting bungeecord!");
        long ram = 1024;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Starting proxy with _____MB ram");
        try {
            while ((line = reader.readLine()) != null){
                    if(isNumber(line)){
                        ram = Long.parseLong(line);
                    }
                    if(ram >= 1024) {
                        File dir = new File("bungee");
                        File direc = new File("bungee/plugins");
                        if(!dir.exists()){
                            dir.mkdirs();
                            direc.mkdirs();
                            try {
                                download(new URL("http://nomizon.de/download/silocloud/SiloCord-1.0-SNAPSHOT.jar"), "bungee/plugins/", "SiloCord.jar");
                                download(new URL("http://nomizon.de/download/silocloud/bungee.jar"), "bungee/", "BungeeCord.jar");
                            } catch (MalformedURLException e) {
                                System.out.println("Cannot download BungeeCord.jar!");
                            }
                        }
                        try {
                            String starter = "java -jar BungeeCord.jar -Xms 216 -Xmx "+ram;
                            if(EPOLL){
                                ProcessBuilder pb = new ProcessBuilder(new String[] {"bash", "-c", starter})
                                        .redirectErrorStream(true)
                                        .directory(new File("bungee/"));
                                process = pb.start();
                            }else{
                                ProcessBuilder pb = new ProcessBuilder(new String[] {"cmd.exe", "/c", starter})
                                        .redirectErrorStream(true)
                                        .directory(new File("bungee/"));
                                process = pb.start();
                            }

                        } catch (Exception ex){
                            System.err.println("Cannot start Bungeecord!");
                            System.err.println("BungeeCord.jar is missing!");
                            ex.printStackTrace();
                            try {
                                download(new URL("http://nomizon.de/download/silocloud/bungee.jar"), "bungee/", "BungeeCord.jar");
                            } catch (MalformedURLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }else{
                        System.err.println("Please use more than 1024MB!");
                    }
            }
        } catch (IOException e) {}
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

    public static boolean isNumber(String input){
        try {
            long tester = Long.parseLong(input);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }

}
