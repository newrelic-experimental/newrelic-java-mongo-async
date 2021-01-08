package com.nr.reactivemongo.instrumentation;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import scala.PartialFunction;
import scala.util.Try;

public class NRPartialFunction<A> implements PartialFunction<Try<A>, Try<A>> {
	
	private Token token = null;
	private static boolean isTransformed = false;
	private String name = null;
	private Segment segment = null;
	private DatastoreParameters params = null;
	
	public NRPartialFunction(String n, Token t) {
		token = t;
		name = n;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	@Trace(async=true)
	public Try<A> apply(Try<A> v1) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			if(params != null) {
				segment.reportAsExternal(params);
				params = null;
			}
			segment.end();
			segment = null;
		} else {
			if(params != null) {
				NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
				params = null;
			}
		}
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","FutureCompleted",name});
		}
		return v1;
	}

	@Override
	public boolean isDefinedAt(Try<A> x) {
		return true;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	public void setParams(DatastoreParameters params) {
		this.params = params;
	}



}
