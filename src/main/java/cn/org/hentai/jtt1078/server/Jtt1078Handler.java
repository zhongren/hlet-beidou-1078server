package cn.org.hentai.jtt1078.server;

import cn.org.hentai.jtt1078.util.ByteUtils;
import cn.org.hentai.jtt1078.util.Configs;
import cn.org.hentai.jtt1078.util.Packet;
import cn.org.hentai.jtt1078.video.PublisherManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by matrixy on 2019/4/9.
 */
public class Jtt1078Handler extends SimpleChannelInboundHandler<RtpMessage> {
    static Logger logger = LoggerFactory.getLogger(Jtt1078Handler.class);
    private static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session-key");
    private ChannelHandlerContext context;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RtpMessage rtpMessage) throws Exception {
        this.context = ctx;

        String sim = rtpMessage.getSimNum();
        int channel = rtpMessage.getLogicChnnel();
        String taskName = sim + "_" + channel;

        // 因为FFMPEG推送有缓冲，所以在停止后又立即发起视频推送是会出现推送通道冲突的情况
        // 所以最好能够每次都分配到新的rtmp通道上去
        //String rtmpURL = Configs.get("rtmp.format").replace("{sim}", sim).replace("{channel}", String.valueOf(channel));

        Session session = getSession();
        if (null == session) {
            setSession(session = new Session());
        }

        String channelKey = "publisher-" + taskName + "-" + "stream";
        Long publisherId = session.get(channelKey);
        if (publisherId == null || PublisherManager.getInstance().getPublishers().get(publisherId) == null) {
            //String rtmpURL = "rtmp://10.20.129.54:1935/live/stream/";
            String rtmpURL=Configs.get("rtmp.format");
            rtmpURL = rtmpURL + taskName;
            publisherId = PublisherManager.getInstance().request(rtmpURL, sim, channel);
            if (publisherId == -1) {throw new RuntimeException("exceed max concurrent stream pushing limitation");}
            session.set(channelKey, publisherId);

            logger.info("start streaming to {}", rtmpURL);
        }
        if (rtpMessage.getDataType() != 3 && rtpMessage.getDataType() != 4) {
            PublisherManager.getInstance().publish(publisherId, rtpMessage);
        }

    }

    public final Session getSession() {
        Attribute<Session> attr = context.channel().attr(SESSION_KEY);
        if (null == attr) {
            return null;
        } else {
            return attr.get();
        }
    }

    public final void setSession(Session session) {
        context.channel().attr(SESSION_KEY).set(session);
    }

    private static final String peer = "Connection reset by peer";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException && peer.equals(cause.getMessage())) {

        } else {
            //super.exceptionCaught(ctx, cause);
            logger.error("异常", cause);
            ctx.close();
        }

    }
}
