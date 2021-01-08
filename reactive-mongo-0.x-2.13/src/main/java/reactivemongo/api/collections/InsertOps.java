package reactivemongo.api.collections;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class InsertOps {
	
	@Weave(type=MatchType.Interface)
	public static class InsertBuilder<T> {
		
		
		public scala.concurrent.Future<reactivemongo.api.commands.WriteResult> one(T t, scala.concurrent.ExecutionContext ec, java.lang.Object obj) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Insert","one");
			return Weaver.callOriginal();
		}

		public scala.concurrent.Future<reactivemongo.api.commands.MultiBulkWriteResult> many(scala.collection.Iterable<T> seq, scala.concurrent.ExecutionContext ec, java.lang.Object obj) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Insert","many");
			return Weaver.callOriginal();
		}

	}


}
