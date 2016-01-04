package xk.xact.util;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CustomPacket {

	private ByteArrayOutputStream byteArray;
	private DataOutputStream dataStream;

	public CustomPacket(byte type) throws IOException {
		this.byteArray = new ByteArrayOutputStream();
		this.dataStream = new DataOutputStream( byteArray );

		dataStream.writeByte( type );
	}

	public CustomPacket add(Object... objects) throws IOException {
		for( Object o : objects ) {
			if( o == null || o instanceof ItemStack ) {
				addItemStack( (ItemStack) o );
			} else if( o instanceof Byte ) {
				dataStream.writeByte( (Byte) o );
			} else if( o instanceof Short ) {
				dataStream.writeShort( (Short) o );
			} else if( o instanceof Integer ) {
				dataStream.writeInt( (Integer) o );
			} else if( o instanceof Boolean ) {
				dataStream.writeBoolean( (Boolean) o );
			}
		}
		return this;
	}

	public Packet toPacket() {
		Packet packet = new Packet() {
			
			@Override
			public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void processPacket(INetHandler p_148833_1_) {
				// TODO Auto-generated method stub
				
			}
		};
		
//		packet.channel = "xact_channel";
//		packet.data = byteArray.toByteArray();
//		packet.length = packet.data.length;
		return packet;
	}

	private void addItemStack(ItemStack stack) throws IOException {
		/*
		ItemStack:
			(short) itemID
			(byte) stackSize
			(short) itemDamage
		NBT:
			(short) nbtLength
			(byte[]) compressedNBT
		 */
		if( stack == null ) {
			dataStream.writeShort( -1 );
			return;
		}

		//dataStream.writeShort( (short) stack.itemID );
		dataStream.writeByte( (byte) stack.stackSize );
		dataStream.writeShort( (short) stack.getItemDamage() );

		if( stack.hasTagCompound() ) {
			byte[] compressedNBT = CompressedStreamTools.compress( stack.getTagCompound() );
			dataStream.writeShort( (short) compressedNBT.length );
			dataStream.write( compressedNBT );
		} else {
			dataStream.writeShort( -1 );
		}

	}

	public static CustomPacket openGui(int guiID, int meta, int x, int y, int z) throws IOException {
		CustomPacket packet = new CustomPacket( (byte) 0x01 );
		packet.add( (byte) guiID );
		packet.add( (short) meta );
		packet.add( x );
		packet.add( y );
		packet.add( z );
		return packet;
	}

}
