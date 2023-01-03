package reactivemongo.io.netty.channel;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type = MatchType.Interface, originalName = "reactivemongo.io.netty.channel.ChannelHandlerContext")
public abstract class ChannelHandlerContext_Instrumentation {

    public abstract ChannelPipeline_Instrumentation pipeline();
    

}
