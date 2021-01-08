package reactivemongo.api;

import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.WeaveAllConstructors;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.reactivemongo.instrumentation.NRPartialFunction;
import com.nr.reactivemongo.instrumentation.ReactiveMongoUtils;

import reactivemongo.core.protocol.Response;
import scala.Function0;
import scala.Function2;
import scala.collection.generic.CanBuildFrom;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@Weave
public abstract class DefaultCursor$$anon$1<A>  {
	
	@NewField
	private Token token = null;
	
	public abstract java.lang.String fullCollectionName();
	
	@WeaveAllConstructors
	public DefaultCursor$$anon$1() {
		Token t = NewRelic.getAgent().getTransaction().getToken();
		if(t != null && t.isActive()) {
			token = t;
		}
	}

	@Trace(async=true)
	public <M> Future<M> collect(int i,Function2<M, java.lang.Throwable, reactivemongo.api.Cursor.State<M>> f1,CanBuildFrom<M, A, M> cbf,ExecutionContext ec) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","ReactiveMongo","Cursor","collect");
		Future<M> result = Weaver.callOriginal();
		return result;
	}

	@Trace(dispatcher=true)
	public Future<A> fold(Function0<A> f1, int i, Function2<A, A, A> f2, ExecutionContext ec) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","ReactiveMongo","Cursor","fold");
		Future<A> result = Weaver.callOriginal();
		return result;
	}

	@Trace(async=true)
	public Future<Response> makeRequest(int i, ExecutionContext ec) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		Future<Response> result = Weaver.callOriginal();
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","ReactiveMongo","Cursor","makeRequest");
		NRPartialFunction<Response> pf = new NRPartialFunction<Response>("Cursor/makeRequest",NewRelic.getAgent().getTransaction().getToken());
		String[] split = ReactiveMongoUtils.splitFullCollectionName(fullCollectionName());
		DatastoreParameters params = DatastoreParameters.product("MongoDB").collection(split[1]).operation("Find").noInstance().databaseName(split[0]).build();
		pf.setParams(params);
		Segment segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB-Find");
		pf.setSegment(segment);
		return result.andThen(pf, ec);
	}

}
