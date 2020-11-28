// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys.messages;

public class JoinMessage extends Message
{
    public String joinmsg;
    
    public JoinMessage(final String msg) {
        this.joinmsg = "";
        this.joinmsg = msg;
    }
}
