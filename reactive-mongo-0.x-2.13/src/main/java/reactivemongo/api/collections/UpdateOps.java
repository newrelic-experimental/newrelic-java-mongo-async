package reactivemongo.api.collections;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.api.commands.MultiBulkWriteResult;
import reactivemongo.api.commands.UpdateCommand;
import reactivemongo.api.commands.UpdateWriteResult;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@Weave(type=MatchType.Interface)
public abstract class UpdateOps<P> {

	@Weave
	public static class UpdateBuilder {
		
		@Trace
		public <Q, U> Future<UpdateWriteResult> one(Q q, U u, boolean b1, boolean b2, ExecutionContext ec, Object o1, Object o2) {
			Future<UpdateWriteResult> result = Weaver.callOriginal();
			return result;
		}
		
		@SuppressWarnings("rawtypes")
		@Trace
		public Future<MultiBulkWriteResult> many(scala.collection.Iterable<UpdateCommand.UpdateElement> it, ExecutionContext ec) {
			Future<MultiBulkWriteResult> result = Weaver.callOriginal();
			return result;
		}
	}


}
