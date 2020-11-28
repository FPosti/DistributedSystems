// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys.messages;

public class JoinResponse extends Message
{
    public String joinresponse;
    
    public JoinResponse(final String joinmsg) {
        this.joinresponse = "";
        this.joinresponse = joinmsg;
    }
}
