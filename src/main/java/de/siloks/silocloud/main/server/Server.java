package de.siloks.silocloud.main.server;

import de.siloks.silocloud.main.server.manager.StartBungee;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.*;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Erstellt von Lars am 18.08.2017.
 * Alle Rechte vorbehalten. Der Entwickler kann jederzeit
 * die Rechte an diesem Code entziehen!
 */
public class Server {

    public static final boolean EPOLL = Epoll.isAvailable();
    public boolean confirmExit = false;

    public static int cloudport;
    public static int wrapperport;
    public static String IPAdress;
    public static String cIP;

    public Server() throws Exception{
        for (int i = 0; i < 250; i++){
            System.out.println(" ");
        }
        System.out.println("Starting Cloud...");
        Properties prop = new Properties();
        OutputStream output = null;
        File dir = new File("data");
        if(!dir.exists()){
            dir.mkdirs();
        }
            try {
                output = new FileOutputStream("data/cloud.properties");

                Inet4Address inet4Address = (Inet4Address) Inet4Address.getLocalHost();
                    prop.setProperty("server-adress", inet4Address.toString());
                    prop.setProperty("wrapperport", "800");
                    prop.setProperty("cloudport", "801");

                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        cloudport = Integer.parseInt(prop.getProperty("cloudport"));
        wrapperport = Integer.parseInt(prop.getProperty("wrapperport"));
        IPAdress = prop.getProperty("server-adress");
        String[] ip = IPAdress.split("/");
        IPAdress = ip[1];
        cIP = ip[0];

        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try{
            confirmExit = false;
            System.out.println(" ");
            new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8)).addLast(new StringEncoder(StandardCharsets.UTF_8)).addLast(new NetworkHandler());
                            System.out.println("Channel connected -> "+channel);
                        }

                    }).bind(cloudport).sync().channel();
            System.out.println("==================================================");
            System.out.println("Collected this data:");
            System.out.println("IP-Adress: "+IPAdress);
            System.out.println("Cloudport: "+cloudport);
            System.out.println("Wrapperport: "+wrapperport);
            System.out.println("Server/Computer: "+cIP);
            System.out.println("==================================================");
            System.out.println("Cloud started!");
            System.out.println("Use 'bungee' to stop the bungee");
            System.out.println("Use 'exit' to stop the cloud");
            System.out.println("==================================================");
            StartBungee.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = reader.readLine()) != null){
                if(line.equalsIgnoreCase("exit")){
                    if(confirmExit == false){
                        for(int i = 0; i < 260; i++){
                            System.out.println(" ");
                        }
                        System.out.println("Do you really want to stop the Cloud?");
                        System.out.println("Please use 'exit' to confirm.");
                        confirmExit = true;
                    }else{
                        for(int i = 0; i < 260; i++){
                            System.out.println(" ");
                        }
                        System.out.println("Stopping Cloud...");
                        StartBungee.process.destroy();
                        System.exit(0);
                        System.out.println("Cloud stopped!");
                    }
                }else if(line.equalsIgnoreCase("bungee")){
                    StartBungee.process.destroy();
                    System.out.println("Proxy killed! ["+StartBungee.process.waitFor()+"]");
                }
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
