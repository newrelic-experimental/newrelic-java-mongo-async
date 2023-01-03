package reactivemongo.core.protocol;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.WeaveAllConstructors;

@Weave
public abstract class Request {

	@NewField
	public Token token = null;
	
	@WeaveAllConstructors
	public Request() {
		Token t = NewRelic.getAgent().getTransaction().getToken();
		if(t != null && t.isActive()) {
			token = t;
		}
	}
	
	public abstract reactivemongo.core.protocol.RequestOp op();
	
}
