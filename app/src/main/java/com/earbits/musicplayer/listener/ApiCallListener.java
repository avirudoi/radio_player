package com.earbits.musicplayer.listener;

import com.earbits.musicplayer.model.SongObject;

/**
 * Created by avirudoi on 12/24/15.
 */
public interface ApiCallListener {

    void onSuccess (SongObject object);

    void onFailure(String messege);
}
