package com.mongodb.internal.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.internal.binding.AsyncWriteBinding;
import com.mongodb.internal.binding.WriteBinding;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave(type=MatchType.BaseClass)
public abstract class BaseFindAndModifyOperation<T> {
	
	protected abstract String getDatabaseName();
	
	public abstract MongoNamespace getNamespace();

	public T execute(final WriteBinding binding) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		ServerAddress address = binding.getWriteConnectionSource().getServerDescription().getAddress();
		DatastoreParameters params = DatastoreParameters
				.product(DatastoreVendor.MongoDB.name())
				.collection(getNamespace().getCollectionName())
				.operation(MongoUtil.OP_FIND_AND_UPDATE)
				.instance(address.getHost(), address.getPort())
				.databaseName(getDatabaseName())
				.build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		return Weaver.callOriginal();
	}

	public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<T> callback) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		if(binding.operationName == null) {
			binding.operationName = MongoUtil.OP_FIND_AND_UPDATE;
		}
		if(binding.namespace == null) {
			MongoNamespace namespace = getNamespace();
			binding.namespace = namespace;
		}
		if(callback.token == null) {
			callback.token = NewRelic.getAgent().getTransaction().getToken();
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncWriteOperation",getClass().getSimpleName(),"executeAsync"});
		Weaver.callOriginal();
	}

}
