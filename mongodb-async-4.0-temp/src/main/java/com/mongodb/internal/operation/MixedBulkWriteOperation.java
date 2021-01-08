package com.mongodb.internal.operation;

import java.util.List;
import java.util.logging.Level;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.internal.binding.AsyncWriteBinding;
import com.mongodb.internal.binding.WriteBinding;
import com.mongodb.internal.bulk.WriteRequest;
import com.mongodb.internal.bulk.WriteRequest.Type;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave
public abstract class MixedBulkWriteOperation {
	
	public MongoNamespace getNamespace() {
		return Weaver.callOriginal();
	}
	
	public abstract List<? extends WriteRequest> getWriteRequests();

	@Trace
	public BulkWriteResult execute(final WriteBinding binding) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		ServerAddress address = binding.getWriteConnectionSource().getServerDescription().getAddress();
		DatastoreParameters params = DatastoreParameters
                .product(DatastoreVendor.MongoDB.name())
                .collection(getNamespace().getCollectionName())
                .operation(MongoUtil.OP_INSERT)
                .instance(address.getHost(), address.getPort())
                .databaseName(getNamespace().getDatabaseName())
                .build();
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		return Weaver.callOriginal();
	}
	
	@Trace
	public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<BulkWriteResult> callback) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		
		List<? extends WriteRequest> writeRequests = getWriteRequests();
		int size = writeRequests.size();
		if(size > 0) {
			Type writeType = writeRequests.get(0).getType();
			if(writeType.equals(Type.INSERT)) {
				if(size == 1) {
					binding.operationName = MongoUtil.OP_INSERT;
				} else {
					binding.operationName = MongoUtil.OP_INSERT_MANY;
				}
			} else if(writeType.equals(Type.DELETE)) {
				if(size == 1) {
					binding.operationName = MongoUtil.OP_DELETE;
				} else {
					binding.operationName = MongoUtil.OP_DELETE;
				}
			} else if(writeType.equals(Type.REPLACE)) {
				if(size == 1) {
					binding.operationName = MongoUtil.OP_REPLACE;
				} else {
					binding.operationName = MongoUtil.OP_REPLACE;
				}
			} else if(writeType.equals(Type.UPDATE)) {
				if(size == 1) {
					binding.operationName = MongoUtil.OP_UPDATE;
				} else {
					binding.operationName = MongoUtil.OP_UPDATE_MANY;
				}
			}
 		}
		if(binding.operationName == null) {
			binding.operationName = MongoUtil.OP_INSERT;
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
