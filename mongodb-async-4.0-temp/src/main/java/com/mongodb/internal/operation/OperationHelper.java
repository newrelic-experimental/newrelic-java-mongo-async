package com.mongodb.internal.operation;

import java.util.logging.Level;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.AsyncConnectionSource;
import com.mongodb.internal.binding.AsyncReadBinding;
import com.mongodb.internal.binding.AsyncWriteBinding;
import com.mongodb.internal.connection.AsyncConnection;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
abstract class OperationHelper {
	
	@Weave(type=MatchType.Interface)
	static abstract class AsyncCallableWithSource {
		@NewField
		public Token token = null;
		@NewField 
		public Segment segment = null;
		@NewField 
		public String opName = null;
		@NewField
		public MongoNamespace namespace = null;

		@Trace(async=true,excludeFromTransactionTrace=true)
		public void call(AsyncConnectionSource source, Throwable t) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncCallableWithConnection","call"});
			Logger logger = NewRelic.getAgent().getLogger();
			if(token != null) {
				token.linkAndExpire();
				token = null;
			}
        	if(segment != null) {
        		ServerDescription description = source.getServerDescription();
        		ServerAddress address = new ServerAddress("UnknownHost");
        		Exception e = new Exception("Garbage");
        		if(description != null) {
        			address = description.getAddress();
        		} else {
        			logger.log(Level.FINE, e, "description is null", new Object[0]);
        		}
        		if(opName == null) {
        			opName = "UnknownOperation";
        			logger.log(Level.FINE, e, "opName is null", new Object[0]);
        		}
        		String collectionName = "UnknownCollection";
        		String dbName = "UnknownDatabase";
        		if(namespace != null) {
        			if(namespace.getCollectionName() != null) {
        				collectionName = namespace.getCollectionName();
        			} else {
            			logger.log(Level.FINE, e, "namespace collection name is null", new Object[0]);
        			}
        			if(namespace.getDatabaseName() != null) {
        				dbName = namespace.getDatabaseName();
        			} else {
            			logger.log(Level.FINE, e, "namespace database name is null", new Object[0]);
        			}
        		} else {
        			logger.log(Level.FINE, e, "namespace is null", new Object[0]);
        		}
        		DatastoreParameters params = DatastoreParameters
                        .product(DatastoreVendor.MongoDB.name())
                        .collection(collectionName)
                        .operation(opName)
                        .instance(address.getHost(), address.getPort())
                        .databaseName(dbName)
                        .build();
        		segment.reportAsExternal(params);
        		
        	}
        	Weaver.callOriginal();
        	if(segment != null) {
        		segment.end();
        	}
		}
	}
	
	@Weave(type=MatchType.Interface)
	static abstract class AsyncCallableWithConnection {
		@NewField
		public Token token = null;
		@NewField 
		public Segment segment = null;
		@NewField 
		public String opName = null;
		@NewField
		public MongoNamespace namespace = null;

		AsyncCallableWithConnection() {
			token = NewRelic.getAgent().getTransaction().getToken();
		}
		
		@Trace(async=true,excludeFromTransactionTrace=true)
		public void call(AsyncConnection connection, Throwable t) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncCallableWithConnection","call"});
			Logger logger = NewRelic.getAgent().getLogger();
			if(token != null) {
				token.linkAndExpire();
				token = null;
			}
        	if(segment != null) {
        		ConnectionDescription description = connection.getDescription();
        		ServerAddress address = new ServerAddress("UnknownHost");
        		Exception e = new Exception("Garbage");
        		if(description != null) {
        			address = description.getServerAddress();
        		} else {
        			logger.log(Level.FINE, e, "description is null", new Object[0]);
        		}
        		if(opName == null) {
        			opName = "UnknownOperation";
        			logger.log(Level.FINE, e, "opName is null", new Object[0]);
        		}
        		String collectionName = "UnknownCollection";
        		String dbName = "UnknownDatabase";
        		if(namespace != null) {
        			if(namespace.getCollectionName() != null) {
        				collectionName = namespace.getCollectionName();
        			} else {
            			logger.log(Level.FINE, e, "namespace collection name is null", new Object[0]);
        			}
        			if(namespace.getDatabaseName() != null) {
        				dbName = namespace.getDatabaseName();
        			} else {
            			logger.log(Level.FINE, e, "namespace database name is null", new Object[0]);
        			}
        		} else {
        			logger.log(Level.FINE, e, "namespace is null", new Object[0]);
        		}
        		DatastoreParameters params = DatastoreParameters
                        .product(DatastoreVendor.MongoDB.name())
                        .collection(collectionName)
                        .operation(opName)
                        .instance(address.getHost(), address.getPort())
                        .databaseName(dbName)
                        .build();
        		segment.reportAsExternal(params);
        		
        	}
        	Weaver.callOriginal();
        	if(segment != null) {
        		segment.end();
        	}
        }
    }

	@Weave(type=MatchType.Interface)
	static abstract class AsyncCallableWithConnectionAndSource {
		@NewField
		public Token token = null;
		@NewField 
		public Segment segment = null;
		@NewField 
		public String opName = null;
		@NewField
		public MongoNamespace namespace = null;

		AsyncCallableWithConnectionAndSource() {
			token = NewRelic.getAgent().getTransaction().getToken();
		}
		
		@Trace(async=true,excludeFromTransactionTrace=true)
		public void call(AsyncConnectionSource source, AsyncConnection connection, Throwable t) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","AsyncCallableWithConnectionAndSource","call"});
			if(token != null) {
				token.linkAndExpire();
				token = null;
			}
        	if(segment != null) {
        		ConnectionDescription description = connection.getDescription();
        		ServerAddress address = description.getServerAddress();
        		DatastoreParameters params = DatastoreParameters
                        .product(DatastoreVendor.MongoDB.name())
                        .collection(namespace.getCollectionName())
                        .operation(opName)
                        .instance(address.getHost(), address.getPort())
                        .databaseName(namespace.getDatabaseName())
                        .build();
        		segment.reportAsExternal(params);
        	}
        	Weaver.callOriginal();
        	
        	if(segment != null) {
        		segment.end();
        	}
        }
    }

	
    static void withAsyncConnection(final AsyncWriteBinding binding, final AsyncCallableWithConnection callable) {
    	if(binding.operationName != null) {
    		callable.opName = binding.operationName;
    	}
    	if(binding.namespace != null) {
    		callable.namespace = binding.namespace;
    		binding.namespace = null;
    	}
    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
    	Weaver.callOriginal();
    }

    static void withAsyncConnection(final AsyncWriteBinding binding, final AsyncCallableWithConnectionAndSource callable) {
    	if(binding.operationName != null) {
    		callable.opName = binding.operationName;
    	}
    	if(binding.namespace != null) {
    		callable.namespace = binding.namespace;
    		binding.namespace = null;
    	}
    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
    	Weaver.callOriginal();
    }

    static void withAsyncReadConnection(final AsyncReadBinding binding, final AsyncCallableWithSource callable) {
    	if(binding.operationName != null) {
    		callable.opName = binding.operationName;
    	}
    	if(binding.namespace != null) {
    		callable.namespace = binding.namespace;
    		binding.namespace = null;
    	}
    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
    	Weaver.callOriginal();
    }

    static void withAsyncReadConnection(final AsyncReadBinding binding, final AsyncCallableWithConnectionAndSource callable) {
    	if(binding.operationName != null) {
    		callable.opName = binding.operationName;
    	}
    	if(binding.namespace != null) {
    		callable.namespace = binding.namespace;
    		binding.namespace = null;
    	}
    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
    	Weaver.callOriginal();
    }

//	@Trace
//    static void withAsyncConnection(final AsyncWriteBinding binding, final AsyncCallableWithConnection callable) {
//    	if(binding.operationName != null) {
//    		callable.opName = binding.operationName;
//    	}
//    	if(binding.namespace != null) {
//    		callable.namespace = binding.namespace;
//    		binding.namespace = null;
//    	}
//    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//    	Weaver.callOriginal();
//    }
//
//    static void withAsyncConnection(final AsyncWriteBinding binding, final AsyncCallableWithConnectionAndSource callable) {
//    	if(binding.operationName != null) {
//    		callable.opName = binding.operationName;
//    	}
//    	if(binding.namespace != null) {
//    		callable.namespace = binding.namespace;
//    		binding.namespace = null;
//    	}
//    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//    	Weaver.callOriginal();
//    }
//
//    static void withAsyncReadConnection(final AsyncReadBinding binding, final AsyncCallableWithSource callable) {
//    	if(binding.operationName != null) {
//    		callable.opName = binding.operationName;
//    	}
//    	if(binding.namespace != null) {
//    		callable.namespace = binding.namespace;
//    		binding.namespace = null;
//    	}
//    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//    	Weaver.callOriginal();
//    }
//
//    static void withAsyncReadConnection(final AsyncReadBinding binding, final AsyncCallableWithConnectionAndSource callable) {
//    	if(binding.operationName != null) {
//    		callable.opName = binding.operationName;
//    	}
//    	if(binding.namespace != null) {
//    		callable.namespace = binding.namespace;
//    		binding.namespace = null;
//    	}
//    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//    	Weaver.callOriginal();
//    }
//
//	@Trace
//    static void withAsyncConnection(final AsyncReadBinding binding, final AsyncCallableWithConnection callable) {
//    	if(binding.operationName != null) {
//    		callable.opName = binding.operationName;
//    	}
//    	if(binding.namespace != null) {
//    		callable.namespace = binding.namespace;
//    		binding.namespace = null;
//    	}
//    	callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//    	Weaver.callOriginal();
//    }
//
//	@Trace
//    static void withAsyncConnection(final AsyncReadBinding binding, final AsyncCallableWithConnectionAndSource callable) {
//    	if (callable != null) {
//			if (binding != null) {
//				if (binding != null && binding.operationName != null) {
//					callable.opName = binding.operationName;
//				}
//				if (binding.namespace != null) {
//					callable.namespace = binding.namespace;
//					binding.namespace = null;
//				}
//			} else {
//				NewRelic.getAgent().getLogger().log(Level.FINE, "binding is null in withConnection", new Object[0]);
//			}
//			callable.segment = NewRelic.getAgent().getTransaction().startSegment("MongoDB", "AsyncReadOperation");
//		} else {
//			NewRelic.getAgent().getLogger().log(Level.FINE, "callable is null in withConnection", new Object[0]);
//		}
//		Weaver.callOriginal();
//    }

}
