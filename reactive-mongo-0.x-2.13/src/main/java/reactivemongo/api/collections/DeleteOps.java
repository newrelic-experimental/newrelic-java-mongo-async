package reactivemongo.api.collections;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class DeleteOps {

	@Weave
	public static class DeleteBuilder {
		
		@Trace
		public <Q, U> scala.concurrent.Future<reactivemongo.api.commands.WriteResult> one(Q q, scala.Option<java.lang.Object> option1, scala.Option<reactivemongo.api.commands.Collation> option2, scala.concurrent.ExecutionContext ec, java.lang.Object obj) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Delete","one");
			return Weaver.callOriginal();
		}
		
		
		@SuppressWarnings("deprecation")
		@Trace
		public scala.concurrent.Future<reactivemongo.api.commands.MultiBulkWriteResult> many(scala.collection.Iterable<reactivemongo.api.commands.DeleteCommand.DeleteElement> seq, scala.concurrent.ExecutionContext ec) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Delete","many");
			return Weaver.callOriginal();
		}
	}
}
