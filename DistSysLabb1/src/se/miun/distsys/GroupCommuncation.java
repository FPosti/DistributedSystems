// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys;

import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.JoinResponse;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.ChatMessage;
import java.net.DatagramPacket;
import se.miun.distsys.messages.Message;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.messages.MessageSerializer;
import java.net.DatagramSocket;

public class GroupCommuncation
{
    private int datagramSocketPort;
    DatagramSocket datagramSocket;
    boolean runGroupCommuncation;
    MessageSerializer messageSerializer;
    ChatMessageListener chatMessageListener;
    public ArrayList<String> friendList;
    
    public GroupCommuncation() {
        this.datagramSocketPort = 616;
        this.datagramSocket = null;
        this.runGroupCommuncation = true;
        this.messageSerializer = new MessageSerializer();
        this.chatMessageListener = null;
        this.friendList = null;
        try {
            this.runGroupCommuncation = true;
            this.datagramSocket = new MulticastSocket(this.datagramSocketPort);
            ((MulticastSocket)this.datagramSocket).joinGroup(InetAddress.getByName("230.255.255.255"));
            this.friendList = new ArrayList<String>();
            final ReceiveThread rt = new ReceiveThread();
            rt.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shutdown() {
        this.runGroupCommuncation = false;
    }
    
    public void sendMessage(final Message message) {
        try {
            final byte[] sendData = this.messageSerializer.serializeMessage(message);
            final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), this.datagramSocketPort);
            this.datagramSocket.send(sendPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendChatMessage(final String msg) {
        this.sendMessage(new ChatMessage(User.username + ": " + msg));
    }
    
    public void sendJoinMessage(final String msg) {
        this.sendMessage(new JoinMessage(msg));
    }
    
    public void sendJoinReponse(final String msg) {
        this.sendMessage(new JoinResponse(msg));
    }
    
    public void sendLeaveMessage(final String msg) {
        this.sendMessage(new LeaveMessage(msg));
    }
    
    public void setChatMessageListener(final ChatMessageListener listener) {
        this.chatMessageListener = listener;
    }
    
    class ReceiveThread extends Thread
    {
        @Override
        public void run() {
            final byte[] buffer = new byte[65536];
            final DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            while (GroupCommuncation.this.runGroupCommuncation) {
                try {
                    GroupCommuncation.this.datagramSocket.receive(datagramPacket);
                    final byte[] packetData = datagramPacket.getData();
                    final Message receivedMessage = GroupCommuncation.this.messageSerializer.deserializeMessage(packetData);
                    this.handleMessage(receivedMessage);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void handleMessage(final Message message) {
            if (message instanceof ChatMessage) {
                final ChatMessage chatMessage = (ChatMessage)message;
                if (GroupCommuncation.this.chatMessageListener != null) {
                    GroupCommuncation.this.chatMessageListener.onIncomingChatMessage(chatMessage);
                }
            }
            else if (message instanceof JoinMessage) {
                final JoinMessage joinMessage = (JoinMessage)message;
                if (GroupCommuncation.this.friendList.contains(joinMessage.joinmsg)) {
                    System.out.println("this person is allready online123321");
                }
                else {
                    GroupCommuncation.this.friendList.add(joinMessage.joinmsg);
                    GroupCommuncation.this.sendJoinReponse(User.username);
                }
                if (GroupCommuncation.this.chatMessageListener != null) {
                    GroupCommuncation.this.chatMessageListener.onIncomingJoinMessage(joinMessage);
                }
            }
            else if (message instanceof JoinResponse) {
                final JoinResponse joinResponse = (JoinResponse)message;
                if (GroupCommuncation.this.friendList.contains(joinResponse.joinresponse)) {
                    System.out.println("this person is allready online");
                }
                else {
                    GroupCommuncation.this.friendList.add(joinResponse.joinresponse);
                }
            }
            else if (message instanceof LeaveMessage) {
                final LeaveMessage leaveMessage = (LeaveMessage)message;
                GroupCommuncation.this.friendList.remove(leaveMessage.leavemessage);
                System.out.println(leaveMessage.leavemessage);
                if (GroupCommuncation.this.chatMessageListener != null) {
                    GroupCommuncation.this.chatMessageListener.onIncomingLeaveMessage(leaveMessage);
                }
            }
            else {
                System.out.println("Unknown message type");
            }
        }
    }
}
