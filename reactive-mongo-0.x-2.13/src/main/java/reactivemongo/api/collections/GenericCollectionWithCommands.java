package reactivemongo.api.collections;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.api.Cursor;
import reactivemongo.api.ReadPreference;
import reactivemongo.api.commands.BoxedAnyVal;
import reactivemongo.api.commands.CollectionCommand;
import reactivemongo.api.commands.CommandWithResult;
import reactivemongo.api.commands.CursorFetcher;
import reactivemongo.api.commands.ResponseResult;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@SuppressWarnings("deprecation")
@Weave(type=MatchType.Interface)
public abstract class GenericCollectionWithCommands<P extends reactivemongo.api.SerializationPack> {
	

	@Trace
	public <R, C extends CollectionCommand & CommandWithResult<R>> Future<R> runCommand(C cmd,ReadPreference pref,Object obj1,Object obj2,ExecutionContext ec) {
		Future<R> result = Weaver.callOriginal();
		String opName = cmd.getClass().getSimpleName();
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Collection","runCommand",opName);
		return result;
	}

	@Trace
	public <R, C extends CollectionCommand & CommandWithResult<R>> Future<ResponseResult<R>> runWithResponse(C cmd, ReadPreference pref, Object obj1, Object obj2, ExecutionContext ec) {
		Future<ResponseResult<R>> result = Weaver.callOriginal();
		String opName = cmd.getClass().getSimpleName();
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Collection","runWithResponse",opName);
		return result;
	}

	@Trace
	public <C extends CollectionCommand> CursorFetcher<P, Cursor<?>> runCommand(C cmd, Object obj) {
		return Weaver.callOriginal();
	}

	@Trace
	public <A, R extends BoxedAnyVal<A>, C extends CollectionCommand & CommandWithResult<R>> Future<A> runValueCommand(C cmd, ReadPreference pref, Object obj1, Object obj2, ExecutionContext ec) {
		Future<A> result = Weaver.callOriginal();
		String opName = cmd.getClass().getSimpleName();
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Collection","runValueCommand",opName);
		
		return result;
	}

}
