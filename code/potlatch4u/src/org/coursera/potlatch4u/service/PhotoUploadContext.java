package org.coursera.potlatch4u.service;

import org.coursera.potlatch4u.orm.GiftData;

public interface PhotoUploadContext {
    void signalUploadComplete(GiftData gift);
}
