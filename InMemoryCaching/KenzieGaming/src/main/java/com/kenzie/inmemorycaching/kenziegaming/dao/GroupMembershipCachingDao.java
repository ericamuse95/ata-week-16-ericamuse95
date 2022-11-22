package com.kenzie.inmemorycaching.kenziegaming.dao;

import javax.inject.Inject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.kenzie.inmemorycaching.kenziegaming.dao.models.GroupMembershipCacheKey;

import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class GroupMembershipCachingDao {
    // Create your cache here
    private LoadingCache<GroupMembershipCacheKey, Boolean> theCache;

    @Inject
    public GroupMembershipCachingDao(final GroupMembershipDao delegateDao) {
        // Initialize the cache
        this.theCache = CacheBuilder.newBuilder()
                .maximumSize(20000)
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build(CacheLoader.from(delegateDao::isUserInGroup));
    }

    public boolean isUserInGroup(final String userId, final String groupId) {
        return theCache.getUnchecked(new GroupMembershipCacheKey(userId, groupId));
    }
}
