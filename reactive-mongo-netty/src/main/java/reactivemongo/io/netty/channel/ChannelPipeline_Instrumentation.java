package reactivemongo.io.netty.channel;

import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type = MatchType.BaseClass, originalName = "reactivemongo.io.netty.channel.ChannelPipeline")
public abstract class ChannelPipeline_Instrumentation {

	
    @NewField
    public Token reactiveLayerToken;
    
    @NewField
    public Segment segment;
    
    @NewField
    public DatastoreParameters params;

}
