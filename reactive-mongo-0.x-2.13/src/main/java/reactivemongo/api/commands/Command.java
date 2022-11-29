package reactivemongo.api.commands;

import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.reactivemongo.instrumentation.NRPartialFunction;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@Weave
public abstract class Command {

	@Weave
	public static class CommandWithPackRunner<P extends reactivemongo.api.SerializationPack> {

		@Trace
		public <R, C extends reactivemongo.api.commands.CollectionCommand & reactivemongo.api.commands.CommandWithResult<R>> Future<R> apply(reactivemongo.api.Collection coll, C cmd, reactivemongo.api.ReadPreference pref, Object obj1, Object obj2, ExecutionContext ec) {
			Future<R> result = Weaver.callOriginal();
			String cmdName = cmd.getClass().getSimpleName();
			String collName = coll.name();
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","CommandWithPackRunner","apply",cmdName,collName);
			NRPartialFunction<R> pf = new NRPartialFunction<R>("CommandWithPackRunner/apply/"+cmdName+"/"+collName, NewRelic.getAgent().getTransaction().getToken());
			
			
			DatastoreParameters params = DatastoreParameters.product("MongoDB").collection(collName).operation(cmdName).noInstance().databaseName(coll.db().name()).build();
			Segment segment = NewRelic.getAgent().getTransaction().startSegment(cmdName);
			pf.setParams(params);
			pf.setSegment(segment);
			return result.andThen(pf, ec);
		}


		@Trace(dispatcher=true)
		public <C extends reactivemongo.api.commands.CollectionCommand> reactivemongo.api.commands.CursorFetcher<P, reactivemongo.api.Cursor<?>> apply(reactivemongo.api.Collection coll, C cmd, Object obj) {
			return Weaver.callOriginal();
		}
	}
}
