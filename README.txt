This is README for the P2P project program developed by Peerless team.
Program Authors:
 Hee Won Lee, Nachiketa V Gadiyar, Deepak Angathil, Hemant E. Imudianda

1) Development Environment
 - Language: Java
 - IDE: Eclipse

2) File: Peerless-final-v1.0.zip

3) How to run from Eclipse
 a) First, unzip Peerless-fnal-v1.0.zip file and import it into Eclipse.
 b) Configuration
  - In order to execute Centralized Version, you set systemVersion = CommonConfig.centralizedVersion in class CommonConfig.

  public class CommonConfig {
  	public static final int systemVersion = CommonConfig.centralizedVersion;
	//public static final int systemVersion = CommonConfig.distributedVersion;
  }

  - In order to execute Distributed Version, you set systemVersion = CommonConfig.distributedVersion in class CommonConfig.

  public class CommonConfig {
  	//public static final int systemVersion = CommonConfig.centralizedVersion;
	public static final int systemVersion = CommonConfig.distributedVersion;
  }

  - In a peer, you should configure the IP addresses of Centralized server and UDP Server in peerlessServerAddress.
  public class ClientConfig {
	public static String peerlessServerAddress = "152.14.240.102:59999";
  }

  public class CommonConfig {
        public static final String udpPeerlessServerAddress = "152.14.240.102";
  }


 c) Run
  - Server: Run PeerlessServerMain.java in the net.peerless.server packages.
  - Client: Run PeerlessClientMain.java in the net.peerless.client package.


4) How to make executables
 a) In eclipse, File > Export > Runnable JAR file
 b) In order to make a server version, select PeerlessServerMain in Lauch configuration and a excutable name in Export destination.
 b) In order to make a client version, select PeerlessClientMain in Lauch configuration and a excutable name in Export destination.

5) How to run the executables:
 b) For client, run "java -jar server.jar" if your executable name for a client is client.java.
 a) For server, run "java -jar client.jar" if your executable name for a server is server.java.


6) Source Code Organization: 
 - net.peerless.client: contains client specific code i.e. code to start the client, login, registering, publishes etc.
 - net.peerless.server: Contains server specific code i.e code to start the server, receiving the client request, client data management etc.
 - net.peerless.message: Describes various message formats used in the project.
 - net.peerless.db: Contains code for interaction with the Data Base
 - net.peerless.common: Contains declarations and global definitions
 - net.peerless.hashtable: Contains the code to generate file signature and abstract and to determine the hash space a particular file signature belongs to.

7) Packages that is used in this program.
 a) apache mina-2.0.0-RC1
 b) javadb-10_5_3_0
 c) ftpserver-1.0.3
 d) apache-log4j-1.2.15
 e) edtftpj-2.0.5

