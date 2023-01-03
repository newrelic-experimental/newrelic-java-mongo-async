package reactivemongo.core.protocol;

import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.io.netty.channel.ChannelHandlerContext_Instrumentation;

@Weave
public abstract class MongoHandler {
	
	@Trace(async=true)
	public void channelRead(ChannelHandlerContext_Instrumentation ctx, java.lang.Object msg) {
		if(ctx.pipeline().reactiveLayerToken != null) {
			ctx.pipeline().reactiveLayerToken.linkAndExpire();
			ctx.pipeline().reactiveLayerToken = null;
		}
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","MongoHandler","channelRead",msg.getClass().getSimpleName());
		Weaver.callOriginal();
	}

	@Trace(async=true)
	public void write(ChannelHandlerContext_Instrumentation ctx, java.lang.Object msg, reactivemongo.io.netty.channel.ChannelPromise promise) {
		Token t = null;
		if(msg instanceof Request) {
			Request request = (Request)msg;
			if(request.token != null) {
				t = request.token;
				request.token = null;
			}
			RequestOp op = request.op();
			String collectionName = "Unknown";
			String dbName = "Unknown";
			if(op instanceof CollectionAwareRequestOp) {
				CollectionAwareRequestOp awareOp = (CollectionAwareRequestOp)op;
				collectionName = awareOp.collectionName();
			}
			DatastoreParameters params = DatastoreParameters.product("MongoDB").collection(collectionName).operation(op.getClass().getSimpleName()).noInstance().databaseName(dbName).build();
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, false, "MongoRequest", "MongoRequest",op.getClass().getSimpleName());
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","MongoHandler","write",op.getClass().getSimpleName());
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","MongoHandler","write",msg.getClass().getSimpleName());
		}
		if(t == null) {
			t = NewRelic.getAgent().getTransaction().getToken();
			if(t != null && !t.isActive()) {
				t.expire();
				t = null;
			}
		}
		if(t == null && ctx.pipeline().reactiveLayerToken != null) {
			t = ctx.pipeline().reactiveLayerToken;
		}
		if(ctx.pipeline().reactiveLayerToken == null) {
			if(t != null) {
				t.link();
			}
			ctx.pipeline().reactiveLayerToken = t;
		} else if(t != null) {
			t.linkAndExpire();
			t = null;
		}
		Weaver.callOriginal();
	}

	@Trace(dispatcher=true)
	public void exceptionCaught(ChannelHandlerContext_Instrumentation ctx, java.lang.Throwable t) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","MongoHandler","exceptionCaught");
		Weaver.callOriginal();
	}

}
