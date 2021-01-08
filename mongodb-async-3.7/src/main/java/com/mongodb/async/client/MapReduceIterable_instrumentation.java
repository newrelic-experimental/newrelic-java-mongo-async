package com.mongodb.async.client;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.operation.AsyncOperationExecutor;
import com.mongodb.session.ClientSession;
import com.newrelic.api.agent.weaver.Weave;
import com.nr.agent.mongo.MongoUtil;

@Weave(originalName="com.mongodb.async.client.MapReduceIterableImpl")
abstract class MapReduceIterable_instrumentation<TDocument, TResult> extends MongoIterable_instrumentation<TResult>  {

	MapReduceIterable_instrumentation(final ClientSession clientSession, final MongoNamespace namespace, final Class<TDocument> documentClass,
			final Class<TResult> resultClass, final CodecRegistry codecRegistry, final ReadPreference readPreference,
			final ReadConcern readConcern, final WriteConcern writeConcern, final AsyncOperationExecutor executor,
			final String mapFunction, final String reduceFunction) {
		super(clientSession, executor, readConcern, readPreference);
		super.collectionName = namespace.getCollectionName();
		super.databaseName = namespace.getDatabaseName();
		super.operationName = MongoUtil.OP_MAP_REDUCE;
	}
}
