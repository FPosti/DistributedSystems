

import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.JoinResponse;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.ChatMessage;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.User;

import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JFrame;
// 
// Decompiled by Procyon v0.5.36
// 

public class WindowProgram implements ChatMessageListener, ActionListener {
    JFrame frame;
    JTextPane txtpnChat;
    JTextPane txtpnMessage;
    JTextPane txtpnOnlineUsers;
    GroupCommuncation gc;

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final WindowProgram window = new WindowProgram();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public WindowProgram() {
        this.txtpnChat = new JTextPane();
        this.txtpnMessage = new JTextPane();
        this.txtpnOnlineUsers = new JTextPane();
        this.gc = null;
        this.initializeFrame();
        (this.gc = new GroupCommuncation()).setChatMessageListener(this);
        System.out.println("Group Communcation Started");
        this.gc.friendList.add(User.username);
        this.gc.sendJoinMessage(User.username);
    }

    private void initializeFrame() {
        (this.frame = new JFrame()).setBounds(100, 100, 650, 400);
        this.frame.setDefaultCloseOperation(3);
        this.frame.getContentPane().setLayout(new GridLayout(2, 3, 10, 11));
        final JScrollPane scrollPane = new JScrollPane();
        this.frame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(this.txtpnChat);
        this.txtpnOnlineUsers.setEditable(false);
        this.frame.getContentPane().add(this.txtpnOnlineUsers);
        this.txtpnChat.setEditable(false);
        this.txtpnChat.setText("--== Group Chat ==--");
        this.txtpnMessage.setText("Message");
        this.frame.getContentPane().add(this.txtpnMessage);
        final JButton btnSendChatMessage = new JButton("Send Chat Message");
        btnSendChatMessage.addActionListener(this);
        btnSendChatMessage.setActionCommand("send");
        this.frame.getContentPane().add(btnSendChatMessage);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent winEvt) {
                WindowProgram.this.gc.sendLeaveMessage(User.username);
                WindowProgram.this.gc.shutdown();
            }
        });
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event.getActionCommand().equalsIgnoreCase("send")) {
            this.gc.sendChatMessage(this.txtpnMessage.getText());
        }
    }

    @Override
    public void onIncomingChatMessage(final ChatMessage chatMessage) {
        this.txtpnChat.setText(this.txtpnChat.getText() + "\n" + chatMessage.chat);
    }

    @Override
    public void onIncomingJoinMessage(final JoinMessage joinMessage) {
        if (this.gc.friendList.indexOf(joinMessage.joinmsg) == -1) {
            this.txtpnChat.setText(joinMessage.joinmsg + this.txtpnChat.getText());
            this.gc.friendList.add(joinMessage.joinmsg);
        }
        this.gc.friendList.forEach(friend -> this.txtpnOnlineUsers.setText(friend+ "\n" + this.txtpnOnlineUsers.getText()));
        //this.txtpnOnlineUsers.setText(joinMessage.joinmsg + this.txtpnOnlineUsers.getText());
    }

    @Override
    public void onIncomingJoinResponse(final JoinResponse joinResponse) {

        if (this.gc.friendList.indexOf(joinResponse.joinresponse) == -1) {
            this.gc.friendList.add(joinResponse.joinresponse);
        }
        this.gc.friendList.forEach(friend -> this.txtpnOnlineUsers.setText(friend +"\n" + this.txtpnOnlineUsers.getText()));

    }

    @Override
    public void onIncomingLeaveMessage(final LeaveMessage leaveMessage) {
        this.txtpnChat.setText(leaveMessage.leavemessage + this.txtpnChat.getText());
        this.txtpnOnlineUsers.setText(leaveMessage.leavemessage + this.txtpnOnlineUsers.getText());
    }
}
