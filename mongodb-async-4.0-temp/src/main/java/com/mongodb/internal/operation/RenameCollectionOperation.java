package com.mongodb.internal.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.internal.binding.AsyncWriteBinding;
import com.mongodb.internal.binding.WriteBinding;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave
public abstract class RenameCollectionOperation {

	private final MongoNamespace originalNamespace = Weaver.callOriginal();
	
	@Trace
	public Void execute(final WriteBinding binding) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		ServerAddress address = binding.getWriteConnectionSource().getServerDescription().getAddress();
		DatastoreParameters params = DatastoreParameters.product(DatastoreVendor.MongoDB.name())
				.collection(originalNamespace.getCollectionName())
				.operation(MongoUtil.OP_RENAME_COLLECTION)
				.instance(address.getHost(), address.getPort())
				.databaseName(originalNamespace.getDatabaseName())
				.build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		return Weaver.callOriginal();
	}
	
	@Trace
	public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		if(binding.operationName == null) {
			binding.operationName = MongoUtil.OP_RENAME_COLLECTION;
		}
		if(binding.namespace == null) {
			binding.namespace = originalNamespace;
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncReadOperation",getClass().getSimpleName(),"executeAsync"});
		if(callback.token == null) {
			callback.token = NewRelic.getAgent().getTransaction().getToken();
		}
		Weaver.callOriginal();

	}
}
