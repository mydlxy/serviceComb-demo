package com.ca.mfd.prc.mq.rocketmq.handler;

import org.apache.rocketmq.common.message.MessageExt;

public interface IRocketMqConsumer {
  //  void onMessage(MessageExt message);
  void onMessage(String message);

}
