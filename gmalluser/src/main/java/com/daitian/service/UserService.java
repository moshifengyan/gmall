package com.daitian.service;

import com.daitian.bean.UmsMember;
import com.daitian.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * Created by 代天 on 2019/11/29.
 */
public interface UserService {
    List<UmsMember> getAllUser();

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    UmsMember isMemeber(UmsMember umsMember);

}
