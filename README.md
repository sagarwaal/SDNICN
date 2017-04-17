# SDNICN
This application simulate working of controller, switch and host for Software Defined Network over Information Centric Network.
The application can be run either in host, controller or switch mode.

When run in controller mode, a controller starts which listens incoming requests for INTEREST and PUBLISH packet. When a PUBLISH
packet arrives, it adds content and host information in hashMap. The hashMap maps content to a list containing information of nodes
that have published this packet.
On receiving an INTEREST packet, it checks for the available nodes for the matched content. And choses the best path from list of 
available paths ( currently it is choosing first path), the information of first node on the best path is returned to the switch. 
HashMap used here is synchronised for consistency issues.


For running in switch mode controller IP is provided, and a connection is established between switch and controller, 
a server thread starts to listen incoming requests from hosts for INTEREST and PUBLISH packets. Each request is handled by a 
separate thread,and the connection between switch and controller is used in synchronized fashion. On receiving PUBLISH packet,
switch simply forwards it to controller, on receiving INTEREST packet, switch communicates with controller to know about next 
hop, connects to that and sends INTEREST packet to that hop, data is traversed in reverse direction, from the node that provides 
file to the node that asked for it. If file is not found then a packet with NOTFOUND message type traverses back the path.


For running in host mode, IP of node where switch module is running is provided,host can publish local files on network or retrieve
files from network. When host publishes a file it makes a entry in sqlite database, mapping name by which content is published to 
the local path where content is stored. It then sends a PUBLISH with content name to the switch.
For retrieving a file, host starts a file receiver thread, which creates a connection with the switch module and sends INTEREST packet with content name to it. On receiving packet with message type as SUCCESS, it retreive file from network, else it aborts.
Host also starts a server thread to listen requests for the content published by this host.


MESSAGE TYPES are defined in java class util/MsgTyp.java.Following are types of messages:-
SUCCESS,
NOTFOUND,
PUBLISH,
INTEREST,
REQUEST(not used),
REPLY (not used).


Packet here is a java object with following attributes:
MSGTyp,
addr,
data,
Serial version UID

For running application, you can build a executable jar file. On running that file, application will ask you to choose mode. 

