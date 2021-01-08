package com.mongodb.async.client;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.operation.AsyncOperationExecutor;
import com.mongodb.session.ClientSession;
import com.newrelic.api.agent.weaver.Weave;
import com.nr.agent.mongo.MongoUtil;

@Weave(originalName="com.mongodb.async.client.ListCollectionsIterableImpl")
abstract class ListCollectionsIterable_instrumentation<TResult> extends MongoIterable_instrumentation<TResult> {

	ListCollectionsIterable_instrumentation(final ClientSession clientSession, final String databaseName, final Class<TResult> resultClass,
			final CodecRegistry codecRegistry,
			final ReadPreference readPreference, final AsyncOperationExecutor executor) {
		super(clientSession, executor, ReadConcern.DEFAULT, readPreference); 
		super.collectionName = "collections";
		super.databaseName = databaseName;
		super.operationName = MongoUtil.OP_LIST_COLLECTIONS;
	}

}
