package com.mongodb.internal.async;

import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.agent.mongo.MongoUtil;

@Weave(type=MatchType.Interface)
public abstract class SingleResultCallback<T> {

	@NewField
	public Token token = null;
	
	public void onResult(T result, Throwable t) {
		if(!MongoUtil.initialized) {
			MongoUtil.init();
		}
		if(token != null) {
			token.expire();
			token = null;
		}
		Weaver.callOriginal();
	}
}