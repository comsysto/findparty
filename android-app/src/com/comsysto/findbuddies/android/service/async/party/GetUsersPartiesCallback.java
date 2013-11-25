package com.comsysto.findbuddies.android.service.async.party;

import com.comsysto.findparty.Party;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: djurast
 * Date: 25.11.13
 * Time: 16:33
 *
 * @version $Id$
 */
public interface GetUsersPartiesCallback {

    public void successOnGetUsersParties(List<Party> parties);
    public void failureOnGetUsersParties();
}
