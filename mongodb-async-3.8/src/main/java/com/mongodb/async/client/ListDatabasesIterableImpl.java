package com.mongodb.async.client;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.lang.Nullable;
import com.newrelic.api.agent.weaver.Weave;
import com.nr.agent.mongo.MongoUtil;

@Weave
abstract class ListDatabasesIterableImpl<TResult> extends MongoIterableImpl<TResult>  {

	ListDatabasesIterableImpl(@Nullable final ClientSession clientSession, final Class<TResult> resultClass,
            final CodecRegistry codecRegistry, final ReadPreference readPreference,
            final OperationExecutor executor) {
		super(clientSession, executor, ReadConcern.DEFAULT, readPreference);
		super.collectionName = "allDatabases";
		super.databaseName = null;
		super.operationName = MongoUtil.OP_LIST_DATABASES;
	}
}
