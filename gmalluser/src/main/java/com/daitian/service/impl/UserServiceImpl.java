package com.daitian.service.impl;

import com.daitian.bean.UmsMember;
import com.daitian.bean.UmsMemberReceiveAddress;
import com.daitian.mapper.UmsMemberReceiveAddressMapper;
import com.daitian.mapper.UserMapper;
import com.daitian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 代天 on 2019/11/29.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMemberList = userMapper.selectAllUser();//userMapper.selectAllUser();

        return umsMemberList;
    }
    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(Long.valueOf(receiveAddressId));
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

    // 封装的参数对象
    UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
    umsMemberReceiveAddress.setMemberId(Long.valueOf(memberId));
    List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);


//        Example example = new Example(UmsMemberReceiveAddress.class);
//        example.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);

    return umsMemberReceiveAddresses;
}

    @Override
    public UmsMember isMemeber(UmsMember umsMember) {
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        if(umsMembers!=null){
            return umsMembers.get(0);
        }
        return null;
    }
}
