package com.zw.avshome.alexa.util;

/**
 * 作者：RedKeyset on 2018/12/27 17:24
 * 邮箱：redkeyset@aliyun.com
 */
public class MediaPlayerStateMessage {
    private String MediaState;

    public String getMediaState() {
        return MediaState;
    }

    public void setMediaState(String mediaState) {
        MediaState = mediaState;
    }

    public MediaPlayerStateMessage(String mediaState) {

        MediaState = mediaState;
    }
}
