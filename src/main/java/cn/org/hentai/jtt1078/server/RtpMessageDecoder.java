package cn.org.hentai.jtt1078.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/***
 * RTP ��Ϣ������
 *
 * @author ������
 * @date 2018/7/16 0016 18:08
 */
public class RtpMessageDecoder extends ByteToMessageDecoder {
	private Logger logger = LoggerFactory.getLogger(RtpMessageDecoder.class);
	// RTP 封包头部最大长度（可能某些字段没有，所以应该取最大的那个长度）
	private final int MIN_HEADER_LENGTH = 34;

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
		if (in == null || in.readableBytes() <= MIN_HEADER_LENGTH) // 最坏打算，至少34个字节时才能读到数据体长度
			return;

		in.markReaderIndex();
		// 跳过无关紧要的数据
		in.skipBytes(5);
		
		RtpMessage msg = new RtpMessage();
		// M（1 bit）、PT（7 bit） 共占用 1 个字节
		byte b = in.readByte();

		msg.setM((byte) ((b >> 7) & 0x1));
		msg.setPT((byte) (b & 0x7f));

		msg.setSeq(in.readShort());
		byte[] simNum = new byte[6];
		in.readBytes(simNum);
		msg.setSimNum(StringUtil.convertByteToHexStringWithoutSpace(simNum));

		msg.setLogicChnnel(in.readByte());

		// 数据类型（4 bit）、分包处理标记（4 bit）共占用一个字节
		b = in.readByte();

		msg.setDataType((byte) (b >> 4));
		msg.setFlag((byte) (b & 0x0f));
		if (msg.getDataType() != 4) { // 不为透传数据类型
			msg.setTimestamp(in.readLong());
		}

		if (msg.getDataType() != 3 && msg.getDataType() != 4) { // 视频数据类型才有以下字段
			msg.setLIFI(in.readShort());
			msg.setLFI(in.readShort());
		}

		// 数据体长度
		msg.setLength(in.readShort());

		if (in.readableBytes() < msg.getLength()) {
			in.resetReaderIndex();
			return;
		}
		// 数据体
		byte[] body = new byte[msg.getLength()];
		in.readBytes(body);
		msg.setBody(body);
		list.add(msg);
	}
}
