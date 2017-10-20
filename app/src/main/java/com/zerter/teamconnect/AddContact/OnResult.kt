package com.zerter.teamconnect.AddContact

import com.zerter.teamconnect.Models.Group

/**
 * Created by krystiankowalski on 16.10.2017.
 */
interface OnResult {
    fun result(groups: MutableList<Group>)
}