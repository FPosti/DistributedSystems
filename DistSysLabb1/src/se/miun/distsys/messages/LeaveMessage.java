// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys.messages;

public class LeaveMessage extends Message
{
    public String leavemessage;
    
    public LeaveMessage(final String msg) {
        this.leavemessage = "";
        this.leavemessage = msg;
    }
}
