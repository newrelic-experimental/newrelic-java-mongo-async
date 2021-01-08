package com.mongodb.internal.operation;

import java.util.logging.Level;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.internal.binding.AsyncWriteBinding;
import com.mongodb.internal.binding.WriteBinding;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave(type=MatchType.BaseClass)
public abstract class BaseWriteOperation {
	
	public MongoNamespace getNamespace() {
		return Weaver.callOriginal();
	}

	@Trace
	public WriteConcernResult execute(final WriteBinding binding) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		ServerAddress address = binding.getWriteConnectionSource().getServerDescription().getAddress();
		DatastoreParameters params = DatastoreParameters
                .product(DatastoreVendor.MongoDB.name())
                .collection(getNamespace().getCollectionName())
                .operation(MongoUtil.getOperation(getClass().getSimpleName()))
                .instance(address.getHost(), address.getPort())
                .databaseName(getNamespace().getDatabaseName())
                .build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		return Weaver.callOriginal();
	}

	@Trace
	public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<WriteConcernResult> callback) { 
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		if(binding.operationName == null) {
			binding.operationName = MongoUtil.getOperation(getClass().getSimpleName());
		}
		if(binding.namespace == null) {
			binding.namespace = getNamespace();
			if(binding.namespace == null) {
				NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Garbage"), "Failed to retrieve namespace", new Object[0]);
			}
		}
		if(callback.token == null) {
			callback.token = NewRelic.getAgent().getTransaction().getToken();
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncWriteOperation",getClass().getSimpleName(),"executeAsync"});
		Weaver.callOriginal();
	}
}
