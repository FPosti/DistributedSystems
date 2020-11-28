import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.JoinResponse;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.ChatMessage;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;

// 
// Decompiled by Procyon v0.5.36
// 

public class Program implements ChatMessageListener
{
    boolean runProgram;
    GroupCommuncation gc;
    
    public static void main(final String[] args) {
        final Program program = new Program();
    }
    
    public Program() {
        this.runProgram = true;
        this.gc = null;
        (this.gc = new GroupCommuncation()).setChatMessageListener(this);
        System.out.println("Group Communcation Started");
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (this.runProgram) {
            try {
                System.out.println("Write message to send: ");
                final String chat = br.readLine();
                this.gc.sendChatMessage(chat);
                Thread.sleep(1000L);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.gc.shutdown();
    }
    
    @Override
    public void onIncomingChatMessage(final ChatMessage chatMessage) {
        System.out.println(chatMessage.chat);
    }
    
    @Override
    public void onIncomingJoinMessage(final JoinMessage joinMessage) {
    }
    
    @Override
    public void onIncomingJoinResponse(final JoinResponse joinResponse) {
    }
    
    @Override
    public void onIncomingLeaveMessage(final LeaveMessage leaveMessage) {
    }
}
