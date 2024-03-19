package net.nls;

import java.nio.ByteBuffer;

public interface ConnectionListener {
    void onOpen();

    void onClose(int var1, String var2);

    void onMessage(String var1);

    void onMessage(ByteBuffer var1);
}
