package com.mongodb.async.client;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.operation.AsyncOperationExecutor;
import com.mongodb.session.ClientSession;
import com.newrelic.api.agent.weaver.Weave;
import com.nr.agent.mongo.MongoUtil;

@Weave(originalName="com.mongodb.async.client.ListIndexesIterableImpl")
abstract class ListIndexesIterable_instrumentation<TResult> extends MongoIterable_instrumentation<TResult>  {

	ListIndexesIterable_instrumentation(final ClientSession clientSession, final MongoNamespace namespace, final Class<TResult> resultClass,
			final CodecRegistry codecRegistry, final ReadPreference readPreference, final AsyncOperationExecutor executor) {
		super(clientSession, executor, ReadConcern.DEFAULT, readPreference);
		super.collectionName = namespace.getCollectionName();
		super.databaseName = namespace.getDatabaseName();
		super.operationName = MongoUtil.OP_LIST_INDEX;
	}
}
