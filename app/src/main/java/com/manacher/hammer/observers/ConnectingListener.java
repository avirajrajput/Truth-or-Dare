package com.manacher.hammer.observers;

import com.manacher.hammer.models.User;

public interface ConnectingListener {

    public void onOtherUser(User other);
}
