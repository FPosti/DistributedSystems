// 
// Decompiled by Procyon v0.5.36
// 

package se.miun.distsys.messages;

import java.io.ObjectInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;

public class MessageSerializer
{
    public byte[] serializeMessage(final Message message) {
        byte[] byteArray = null;
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gos = new GZIPOutputStream(bos);
            final ObjectOutputStream oos = new ObjectOutputStream(gos);
            oos.writeUnshared(message);
            oos.flush();
            oos.close();
            gos.close();
            bos.close();
            byteArray = bos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final ObjectOutputStream oos = null;
        final ByteArrayOutputStream bos = null;
        final GZIPOutputStream gos = null;
        return byteArray;
    }
    
    public Message deserializeMessage(final byte[] byteRepresentation) {
        if (byteRepresentation == null) {
            return null;
        }
        Message message;
        try {
            final ByteArrayInputStream bis = new ByteArrayInputStream(byteRepresentation);
            final GZIPInputStream gis = new GZIPInputStream(bis);
            final ObjectInputStream ois = new ObjectInputStream(gis);
            message = (Message)ois.readUnshared();
            ois.close();
            gis.close();
            bis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }
}
