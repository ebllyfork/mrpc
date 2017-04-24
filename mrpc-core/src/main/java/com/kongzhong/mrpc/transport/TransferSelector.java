package com.kongzhong.mrpc.transport;

import com.kongzhong.mrpc.enums.SerializeEnum;
import com.kongzhong.mrpc.enums.TransportEnum;
import com.kongzhong.mrpc.exception.InitializeException;
import com.kongzhong.mrpc.serialize.ProtostuffSerialize;
import com.kongzhong.mrpc.serialize.RpcSerialize;
import com.kongzhong.mrpc.transport.http.HttpServerChannelInitializer;
import com.kongzhong.mrpc.transport.tcp.TcpServerChannelInitializer;
import io.netty.channel.ChannelHandler;

/**
 * 传输协议选择器
 *
 * @author biezhi
 *         2017/4/20
 */
public class TransferSelector {

    private String serialize;
    private RpcSerialize rpcSerialize;

    public TransferSelector(String serialize) {
        this.serialize = serialize;
    }

    /**
     * 根据传输协议获取一个服务端处理handler
     *
     * @param transport
     * @return
     * @see TransportEnum
     */
    public ChannelHandler getServerChannelHandler(String transport) {

        SerializeEnum serializeEnum = SerializeEnum.valueOf(serialize.toUpperCase());
        if (null == serializeEnum) {
            throw new InitializeException("serialize type [" + serialize + "] error.");
        }

        if (serializeEnum.equals(SerializeEnum.PROTOSTUFF)) {
            rpcSerialize = new ProtostuffSerialize();
        }

        TransportEnum transportEnum = TransportEnum.valueOf(transport.toUpperCase());
        if (null == transportEnum) {
            throw new InitializeException("transfer type [" + transport + "] error.");
        }

        if (null == rpcSerialize) {
            throw new InitializeException("rpc server serialize is null.");
        }

        if (transportEnum.equals(TransportEnum.TCP)) {
            return new TcpServerChannelInitializer(rpcSerialize);
        }

        if (transportEnum.equals(TransportEnum.HTTP)) {
            return new HttpServerChannelInitializer();
        }

        throw new InitializeException("transfer type is null.");
    }

}
