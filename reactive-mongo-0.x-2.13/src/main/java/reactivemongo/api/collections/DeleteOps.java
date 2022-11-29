package reactivemongo.api.collections;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.api.commands.Collation;
import reactivemongo.api.commands.WriteResult;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@SuppressWarnings("deprecation")
@Weave(type=MatchType.Interface)
public abstract class DeleteOps {

	@Weave
	public static class DeleteBuilder {
		
		@Trace
		public <Q,U> Future<WriteResult> one(Q q, Option<Object> option1, Option<Collation> option2, ExecutionContext ec, Object obj ) {
			String method = "one";
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Delete",method);
			return Weaver.callOriginal();
		}
		
		@Trace
		public scala.concurrent.Future<reactivemongo.api.commands.MultiBulkWriteResult> many(scala.collection.Iterable<reactivemongo.api.commands.DeleteCommand.DeleteElement> deletes, scala.concurrent.ExecutionContext ec) {
			String method = "many";
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Delete",method);
			return Weaver.callOriginal();
		}
		
	}
}
