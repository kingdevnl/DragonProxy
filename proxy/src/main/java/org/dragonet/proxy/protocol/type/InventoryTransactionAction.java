package org.dragonet.proxy.protocol.type;

import org.dragonet.proxy.protocol.packets.InventoryTransactionPacket;
import org.dragonet.proxy.utilities.BinaryStream;

/**
 * Created on 2017/10/21.
 */
public class InventoryTransactionAction {

    public final static int SOURCE_CONTAINER = 0;
    public final static int SOURCE_WORLD = 2; //drop/pickup item entity
    public final static int SOURCE_CREATIVE = 3;
    public final static int SOURCE_TODO = 99999;

    /**
     * Fake window IDs for the SOURCE_TODO type (99999)
     *
     * These identifiers are used for inventory source types which are not currently implemented server-side in MCPE.
     * As a general rule of thumb, anything that doesn't have a permanent inventory is client-side. These types are
     * to allow servers to track what is going on in client-side windows.
     *
     * Expect these to change in the future.
     */
    public final static int SOURCE_TYPE_CRAFTING_ADD_INGREDIENT = -2;
    public final static int SOURCE_TYPE_CRAFTING_REMOVE_INGREDIENT = -3;
    public final static int SOURCE_TYPE_CRAFTING_RESULT = -4;
    public final static int SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5;

    public final static int SOURCE_TYPE_ANVIL_INPUT = -10;
    public final static int SOURCE_TYPE_ANVIL_MATERIAL = -11;
    public final static int SOURCE_TYPE_ANVIL_RESULT = -12;
    public final static int SOURCE_TYPE_ANVIL_OUTPUT = -13;

    public final static int SOURCE_TYPE_ENCHANT_INPUT = -15;
    public final static int SOURCE_TYPE_ENCHANT_MATERIAL = -16;
    public final static int SOURCE_TYPE_ENCHANT_OUTPUT = -17;

    public final static int SOURCE_TYPE_TRADING_INPUT_1 = -20;
    public final static int SOURCE_TYPE_TRADING_INPUT_2 = -21;
    public final static int SOURCE_TYPE_TRADING_USE_INPUTS = -22;
    public final static int SOURCE_TYPE_TRADING_OUTPUT = -23;

    public final static int SOURCE_TYPE_BEACON = -24;

    /** Any client-side window dropping its contents when the player closes it */
    public final static int SOURCE_TYPE_CONTAINER_DROP_CONTENTS = -100;

    public final static int ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM = 0;
    public final static int ACTION_MAGIC_SLOT_CREATIVE_CREATE_ITEM = 1;

    public final static int ACTION_MAGIC_SLOT_DROP_ITEM = 0;
    public final static int ACTION_MAGIC_SLOT_PICKUP_ITEM = 1;


    public int sourceType;
    public int containerId;
    public int unknown;

    public boolean craftingPart;

    public int slotId;
    public Slot oldItem;
    public Slot newItem;

    public static InventoryTransactionAction read(InventoryTransactionPacket packet) {
        InventoryTransactionAction action = new InventoryTransactionAction();
        action.sourceType = (int) packet.getUnsignedVarInt();

        switch(action.sourceType){
            case SOURCE_CONTAINER:
                action.containerId = packet.getVarInt();
                break;
            case SOURCE_WORLD:
                action.unknown = (int) packet.getUnsignedVarInt();
                break;
            case SOURCE_CREATIVE:
                break;
            case SOURCE_TODO:
                action.containerId = packet.getVarInt();
                switch(action.containerId){
                    case SOURCE_TYPE_CRAFTING_USE_INGREDIENT:
                    case SOURCE_TYPE_CRAFTING_RESULT:
                        packet.craftingPart = true;
                        break;
                }
                break;
        }

        action.slotId = (int) packet.getUnsignedVarInt();
        action.oldItem = packet.getSlot();
        action.newItem = packet.getSlot();
        return action;
    }

    public void write(BinaryStream packet) {
        packet.putUnsignedVarInt(sourceType);

        switch(sourceType){
            case SOURCE_CONTAINER:
                packet.putVarInt(containerId);
                break;
            case SOURCE_WORLD:
                packet.putUnsignedVarInt(unknown);
                break;
            case SOURCE_CREATIVE:
                break;
            case SOURCE_TODO:
                packet.putVarInt(containerId);
                break;
        }

        packet.putUnsignedVarInt(slotId);
        packet.putSlot(oldItem);
        packet.putSlot(newItem);
    }

}
