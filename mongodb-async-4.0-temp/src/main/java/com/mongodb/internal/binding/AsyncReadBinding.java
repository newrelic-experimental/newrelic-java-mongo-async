package com.mongodb.internal.binding;

import com.mongodb.MongoNamespace;
import com.mongodb.internal.async.SingleResultCallback;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type=MatchType.Interface)
public abstract class AsyncReadBinding {

	@NewField
	public String operationName = null;
	@NewField 
	public MongoNamespace namespace = null;
	
	public abstract void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> callback);
	
}
