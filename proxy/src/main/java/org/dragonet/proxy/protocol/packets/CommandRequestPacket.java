package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/11/15.
 */
public class CommandRequestPacket extends PEPacket {

    public String command;

    @Override
    public int pid() {
        return ProtocolInfo.COMMAND_REQUEST_PACKET;
    }

    @Override
    public void encodePayload() {
        putString(command);
    }

    @Override
    public void decodePayload() {
        command = getString();
    }
}
