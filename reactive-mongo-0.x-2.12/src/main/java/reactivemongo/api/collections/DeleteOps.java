package reactivemongo.api.collections;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.api.commands.WriteResult;
import scala.collection.Seq;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@Weave(type=MatchType.Interface)
public abstract class DeleteOps {

	@Weave
	public static class DeleteBuilder {
		
		@Trace
		private Future<WriteResult> execute(Seq<Object> seq, ExecutionContext ec) {
			int size = seq.size();
			String method = "unknown";
			if(size == 1) {
				method = "one";
			} else if(size > 1) {
				method = "many";
			}
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Delete",method);
			Future<WriteResult> result = Weaver.callOriginal();
			return result;
		}
	}
}
