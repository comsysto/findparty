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
public interface SearchPartiesCallback {

    public void successOnSearchParties(List<Party> parties);
    public void failureOnSearchParties();
}
