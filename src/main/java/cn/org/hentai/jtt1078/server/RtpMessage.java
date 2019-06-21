package cn.org.hentai.jtt1078.server;

public class RtpMessage {

	 /**
     * 标志位，确定是否是完整数据帧的边界，占 1 bit
     */
    private byte M;
    /**
     * 负载类型，占 7 bit
     */
    private byte PT;
    /**
     * 包序号
     */
    private short seq;
    /**
     * sim 卡号
     */
    private String simNum;
    /**
     * 逻辑通道号
     */
    private byte logicChnnel;
    /**
     * 数据类型
     * 0000：视频I帧
     * 0001：视频P帧
     * 0010：视频B帧
     * 0011：音频数据
     * 0100：透传数据
     */
    private byte dataType;
    /**
     * 分包处理标记
     * 0000：原子包，不可被拆分
     * 0001：分包处理时的第一个包
     * 0010：分包处理时的最后一个包
     * 0011：分包处理时的中间包
     */
    private byte flag;
    /**
     * 时间戳
     */
    private long timestamp;
    private short LIFI;
    private short LFI;
    /**
     * 数据体长度
     */
    private short length;
    /**
     * 数据体
     */
    private byte[] body;

    public byte getM() {
        return M;
    }

    public void setM(byte m) {
        M = m;
    }

    public byte getPT() {
        return PT;
    }

    public void setPT(byte PT) {
        this.PT = PT;
    }

    public short getSeq() {
        return seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public String getSimNum() {
        return simNum;
    }

    public void setSimNum(String simNum) {
        this.simNum = simNum;
    }

    public byte getLogicChnnel() {
        return logicChnnel;
    }

    public void setLogicChnnel(byte logicChnnel) {
        this.logicChnnel = logicChnnel;
    }

    public byte getDataType() {
        return dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public short getLIFI() {
        return LIFI;
    }

    public void setLIFI(short LIFI) {
        this.LIFI = LIFI;
    }

    public short getLFI() {
        return LFI;
    }

    public void setLFI(short LFI) {
        this.LFI = LFI;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RtpMessage{" +
                "M=" + M +
                ", PT=" + PT +
                ", seq=" + seq +
                ", simNum='" + simNum + '\'' +
                ", logicChnnel=" + logicChnnel +
                ", dataType=" + dataType +
                ", flag=" + flag +
                ", timestamp=" + timestamp +
                ", LIFI=" + LIFI +
                ", LFI=" + LFI +
                ", length=" + length +
                '}';
    }
}
