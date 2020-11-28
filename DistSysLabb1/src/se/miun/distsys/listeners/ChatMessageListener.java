// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys.listeners;

import se.miun.distsys.messages.JoinResponse;
import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.ChatMessage;

public interface ChatMessageListener
{
    void onIncomingChatMessage(final ChatMessage p0);
    
    void onIncomingJoinMessage(final JoinMessage p0);
    
    void onIncomingLeaveMessage(final LeaveMessage p0);
    
    void onIncomingJoinResponse(final JoinResponse p0);
}
