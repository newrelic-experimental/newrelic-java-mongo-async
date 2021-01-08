package com.mongodb.internal.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.internal.async.AsyncBatchCursor;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.internal.binding.AsyncReadBinding;
import com.mongodb.internal.binding.ReadBinding;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave
public abstract class ListIndexesOperation<T> {

	private final MongoNamespace namespace = Weaver.callOriginal();

	@Trace
	public BatchCursor<T> execute(final ReadBinding binding) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		ServerAddress address = binding.getReadConnectionSource().getServerDescription().getAddress();
		DatastoreParameters params = DatastoreParameters
				.product(DatastoreVendor.MongoDB.name())
				.collection(namespace.getCollectionName())
				.operation(MongoUtil.OP_LIST_INDEX)
				.instance(address.getHost(), address.getPort())
				.databaseName(namespace.getDatabaseName())
				.build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		return Weaver.callOriginal();
	}

	@Trace
	public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		if(binding.operationName == null) {
			binding.operationName = MongoUtil.OP_LIST_INDEX;
		}
		if(binding.namespace == null) {
			binding.namespace = namespace;
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncReadOperation",getClass().getSimpleName(),"executeAsync"});
		if(callback.token == null) {
			callback.token = NewRelic.getAgent().getTransaction().getToken();
		}
		Weaver.callOriginal();

	}

}
