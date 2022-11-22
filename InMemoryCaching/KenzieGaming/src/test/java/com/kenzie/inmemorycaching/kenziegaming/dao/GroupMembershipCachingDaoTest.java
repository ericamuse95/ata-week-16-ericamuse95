package com.kenzie.inmemorycaching.kenziegaming.dao;

import com.kenzie.inmemorycaching.kenziegaming.dao.models.GroupMembershipCacheKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class GroupMembershipCachingDaoTest {
    @Mock
    private GroupMembershipDao membershipDao;

    // The unit under test
    @InjectMocks
    private GroupMembershipCachingDao cachingMembershipDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    // Rename this method
    @Test
    public void isUserInGroup_userNotInCache_delegateDaoCalled() {
        // GIVEN

        String userId  = "knownUser";
        String groupId = "knownGroup";

        GroupMembershipCacheKey theKey = new GroupMembershipCacheKey(userId, groupId);
        when(membershipDao.isUserInGroup(theKey)).thenReturn(true);

        // WHEN

        boolean result = cachingMembershipDao.isUserInGroup(userId, groupId);

        // THEN
        assertTrue(result,"expected result to be true");
        verify(membershipDao).isUserInGroup(theKey);
        verifyNoMoreInteractions(membershipDao);
    }

    @Test
    public void isUserInGroup_userInCache_delegateDaoNotCalled() {
        // GIVEN
        String userId  = "userId";
        String groupId = "groupId";
        GroupMembershipCacheKey key = new GroupMembershipCacheKey(userId, groupId);

        when(membershipDao.isUserInGroup(eq(key))).thenReturn(true);

        cachingMembershipDao.isUserInGroup(userId, groupId);

        // WHEN
        boolean result = cachingMembershipDao.isUserInGroup(userId, groupId);

        // THEN
        assertTrue(result, "Expected result to be consistent with the cached GroupMembershipDao's response");
        verify(membershipDao, times(1)).isUserInGroup(eq(key));
        verifyNoMoreInteractions(membershipDao);
    }
}
