package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单
     */
    @Scheduled(cron="0 * * * * ?")
//    @Scheduled(cron="1/5 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        //查询待付款订单并且下单时间已经超过15分钟的
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status = ? and order_time <(当前时间-15分钟)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if(ordersList!=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送中
     */
//    @Scheduled(cron="0/5 * * * * ?")
    @Scheduled(cron="0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单：{}",LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        //这个代码在每天的凌晨一点执行，然后处理上一天的还在派送中的订单为已完成
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList!=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(time);
                orderMapper.update(orders);
            }
        }
    }
}
