package reactivemongo.api.collections;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import reactivemongo.api.ReadPreference;
import reactivemongo.api.SerializationPack;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

@Weave(type=MatchType.Interface)
public class GenericQueryBuilder<P extends SerializationPack> {

	@Trace
	public <T> Future<Option<T>> one(ReadPreference readPreference, Object reader,ExecutionContext ec) {
		Future<Option<T>> result = Weaver.callOriginal();
		return result;
	}
	
	@Trace
	public <T> Future<Option<T>> one(T t, ExecutionContext ec) {
		Future<Option<T>> result = Weaver.callOriginal();
		return result;
	}
	
	@Trace
	public <T> Future<T> requireOne(Object obj, ExecutionContext ec) {
		Future<T> result = Weaver.callOriginal();
		return result;
	}
	
	@Trace
	public <T> Future<T> requireOne(ReadPreference readPref, Object obj, ExecutionContext ec) {
		Future<T> result = Weaver.callOriginal();
		return result;
	}

}
