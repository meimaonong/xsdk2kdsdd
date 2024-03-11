package com.art.app.orm.service.impl;

import com.art.app.orm.dao.PaymentOrderInfoMapper;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderInfoServiceImpl extends ServiceImpl<PaymentOrderInfoMapper, PaymentOrderInfo> implements PaymentOrderInfoService {

}
